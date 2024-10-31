/*
 * Copyright (c) 2023, Dragonstb
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package dev.dragonstb.scribevttrpg.game;

import dev.dragonstb.scribevttrpg.game.participant.Participant;
import dev.dragonstb.scribevttrpg.game.participant.ParticipantRole;
import dev.dragonstb.scribevttrpg.GameManager;
import dev.dragonstb.scribevttrpg.content.ContentManager;
import dev.dragonstb.scribevttrpg.content.DefaultContentManager;
import dev.dragonstb.scribevttrpg.game.handouts.ContainerHandout;
import dev.dragonstb.scribevttrpg.game.handouts.HandoutManager;
import dev.dragonstb.scribevttrpg.utils.Constants;
import dev.dragonstb.scribevttrpg.utils.LocKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author Dragonstb
 * @since 0.0.3;
 */
@RestController
@SessionAttributes("paticipationList")
public class GameRestController {

    @Autowired
    private GameManager gameManager;

    @Autowired
    private GameUtils gameUtils;

    @Autowired
    private MessageSource messageSource;

    /** This method creates a new game. The game is created with the favourite room name, as long as this name has not
     * been taken already. If the creation fails, an error messsage is returned.
     *
     * @param request The underlying servlet request.
     * @param response The underlying servlet response.
     * @param body The request body.
     * @param loc Locale of the header with the accepted languages.
     * @return The response.
     */
    @PostMapping("/creategame")
    public String createGame( HttpServletRequest request, HttpServletResponse response, @RequestBody String body,
            @RequestHeader("Accept-Language") Locale loc ) {
        // TODO: validate json body
        // TODO: validate favourite room name
        // TODO: validate campaign name; this also includes checking if it is one of the game master's campaign at all
        JSONObject jbod = new JSONObject(body); // TODO: use converter
        String roomName = jbod.getString("roomName");
        String campaignName = jbod.getString( "campaign" );

        // TODO: access session variables with annotations
        HttpSession httpSession = request.getSession();
        Map<String, Participant> participations = getParticipationsAndCreateIfNeeded( httpSession );

        GameService game;
        try {
            game = gameManager.createGame( roomName );
        }
        catch( IllegalArgumentException e ) {
            response.setStatus(400);
            JSONObject json = new JSONObject();
            String key = LocKeys.CREATE_ROOM_NAME_INVALID;
            json.put( "success", false );
            json.put( "message", messageSource.getMessage( key, null, "<"+key+">", loc) );
            return json.toString();

        }
        catch( RuntimeException e ) {
            response.setStatus(400);
            JSONObject json = new JSONObject();
            String key = LocKeys.CREATE_ROOM_NAME_TAKEN;
            json.put( "success", false );
            json.put( "message", messageSource.getMessage( key, null, "<"+key+">", loc) );
            return json.toString();
        }

        // fetch materials for campaign and add them to the game
        ContentManager cm = (ContentManager)request.getSession().getAttribute( Constants.KEY_CONTENT_MANAGER );
        if( cm == null ) {
            cm = new DefaultContentManager();
            request.getSession().setAttribute( Constants.KEY_CONTENT_MANAGER, cm );
        }
        List<ContainerHandout> handouts = cm.getHandouts( campaignName );
        HandoutManager handoutManager = game.getHandoutManager();
        handoutManager.addHandouts( handouts );

        // add yourself as gm
        Participant part = game.createAndAddParticipant( "TODO: pick user's name from Principal", ParticipantRole.gm );
        participations.put( roomName, part );

        // build response
        JSONObject json = new JSONObject();
        json.put("success", true);
        json.put("room", roomName);

        response.setStatus( HttpStatus.CREATED.value() );
        return json.toString();
    }

    /** Returns the materials used in the game. Such materials can be
     * <ul>
     *   <li>Handouts</li>
     *   <li>Dice</li>
     *   <li>...</li>
     * </ul>
     *
     * @author Dragonstb
     * @param request The servlet request.
     * @param roomName The name of the room where the game takes place.
     * @return The materials as json string.
     * @throws ResponseStatusException when the user's participation in the game that is specified by {@code roomName}
     * cannot be verified by the server.
     */
    @GetMapping("/materials/{roomName:.+}")
    public String getMaterials( HttpServletRequest request, @PathVariable String roomName ) {
        // TODO: validate room name
        HttpSession httpSession = request.getSession();
        Map<String, Participant> participations = getParticipationsAndCreateIfNeeded( httpSession );

        // check user's participation
        Optional<GameService> opt;
        try {
            opt = gameUtils.getGameUserIsParticipatingIn( participations, roomName );
        } catch ( Exception e ) {
            throw new ResponseStatusException( HttpStatus.BAD_REQUEST );
        }

        if( opt.isEmpty() ) {
            throw new ResponseStatusException( HttpStatus.BAD_REQUEST );
        }

        // assemble response
        GameService game = opt.get();
        HandoutManager handoutManager = game.getHandoutManager();
        List<ContainerHandout> handouts = handoutManager.getHandouts(); // TODO: user ID and role as arguments
        JSONArray hoArr = new JSONArray();
        handouts.forEach( ho -> hoArr.put( ho.toJsonObject() ) );

        return hoArr.toString();
    }

    /** Gets the attribute "participations" from the http session object. This is a map that maps room names to the
     * user participations in gaming sessions.<br><br>
     * If the attribute is not set, a new, empty map is added. Therefore, this method garuantees the return of a nonnull
     * map.
     * @param session The http session. Must be not-null
     * @return The map of romm names to participations. Never null.
     */
    @NonNull
    final static Map<String, Participant> getParticipationsAndCreateIfNeeded(@NonNull HttpSession session) {
        // TODO: do we really need this method beyond this calls and beyond of unit tests?
        Map<String, Participant> participations = (HashMap<String, Participant>)session.getAttribute( Constants.KEY_PARTICIPATIONS );
        if( participations == null ) {
            participations = new HashMap<>();
            session.setAttribute( Constants.KEY_PARTICIPATIONS, participations );
        }
        return participations;
    }

}
