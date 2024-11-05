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
import dev.dragonstb.scribevttrpg.game.exceptions.IdentityNotUniqueException;
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
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;

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
        Map<String, Participant> participations = GameUtils.getParticipationsAndCreateIfNeeded( httpSession );

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

    @PostMapping("/joingame")
    public String startJoinProcess( @RequestBody String body, HttpServletRequest request, HttpServletResponse response,
            @RequestHeader(name="Accept-Language", defaultValue="en") Locale loc ) {
        // TODO: validate json body
        // TODO: validate name
        // TODO: validate room name
        JSONObject jbod;
        String roomName;
        String name;
        try {
            jbod = new JSONObject( body ); // TODO: use converter
            roomName = jbod.getString( "roomName" );
            name = jbod.getString( "name" );
        } catch ( Exception e ) {
            // TODO: have a look if there is a more appropiate status code
            throw new ResponseStatusException( HttpStatus.BAD_REQUEST );
        }
        // TODO: validate name and roomName

        Optional<GameService> opt;
        opt = gameManager.getGame( roomName );
        if( opt.isEmpty() ) {
            // TODO: serve a nice page which states that the room does not exists (404 not found) or, if the user
            // played before in this room, that the room has been closed in the meantime (410 gone). Note: as we cannot
            // decide on the server if the latter one is true (we don't want to put the effort for this), we send 404
            // anyway
            response.setStatus( 404 );
            response.setContentType( MediaType.APPLICATION_JSON_VALUE );
            JSONObject json = new JSONObject();
            json.put( "accepted", false );
            json.put( "message", getMessage(LocKeys.JOIN_NO_SUCH_ROOM, loc) );
            return json.toString();
        }

        HttpSession httpSession = request.getSession();
        Map<String, Participant> participations = GameUtils.getParticipationsAndCreateIfNeeded( httpSession );

        JSONObject json = new JSONObject();
        GameService game = opt.get();
        synchronized( participations ) {
            Participant participant = participations.get( roomName );
            if( participant == null ) {
                try {
                    participant = game.createAndAddParticipant( name, ParticipantRole.prospect );
                    participations.put( roomName, participant );
                    json.put( "accepted", true );
                    json.put( "room", roomName );
                    response.setStatus( HttpStatus.CREATED.value() );
                } catch ( IdentityNotUniqueException inue ) {
                    json.put( "accepted", false );
                    json.put( "message", getMessage(LocKeys.JOIN_NAME_OCCUPIED, loc) );
                }
            }
            else {
                json.put( "accepted", false );
                GameUtils.ParticipationStatus status = game.getParticipationStatus( participant );
                switch( status ) {
                    case participating -> {
                        // TODO: provide link to /game/{roomName}
                        json.put( "message", getMessage( LocKeys.JOIN_ALREADY_PARTICIPATING, loc) );
                    }
                    case waiting -> {
                        // TODO: provide to /wait/{roomName}
                        json.put( "message", getMessage( LocKeys.JOIN_ALREADY_WAITING, loc) );
                    }
                    case none -> {
                        // inconsistent state, existing participant which is not part of the game. The game is assumed to be
                        // right in this point, so delete participation
                        participations.remove( roomName );
                        // TODO: automatically go back to the beginning of the synchronized block and start the join
                        //       process. For now, the user has to this for us
                        json.put( "message", "A problem occured, but we cleaned it up. Please simply try again." );
                    }
                }
            }
        }

        response.setContentType( MediaType.APPLICATION_JSON_VALUE );
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
        Map<String, Participant> participations = GameUtils.getParticipationsAndCreateIfNeeded( httpSession );

        Optional<GameService> opt = gameManager.getGame( roomName );
        if( opt.isEmpty() ) {
            throw new ResponseStatusException( HttpStatus.NOT_FOUND );
        }

        Participant participation;
        GameService game = opt.get();
        GameUtils.ParticipationStatus status;

        synchronized ( participations ) {
            participation = participations.get( roomName );
            if( participation == null ) {
                if( participations.containsKey(roomName) ) {
                    participations.remove( roomName );
                }
                throw new ResponseStatusException( HttpStatus.BAD_REQUEST );
            }

            status = game.getParticipationStatus( participation );
            if( status != GameUtils.ParticipationStatus.participating ) {
                if( status == GameUtils.ParticipationStatus.none ) {
                    // inconsistent state between 'participations' and 'game.participants', fix it assuming that the game
                    // is correct in this point
                    participations.remove( roomName );
                }
                throw new ResponseStatusException( HttpStatus.UNAUTHORIZED );
            }
        }

        // assemble response
        HandoutManager handoutManager = game.getHandoutManager();
        List<ContainerHandout> handouts = handoutManager.getHandouts(); // TODO: user ID and role as arguments
        JSONArray hoArr = new JSONArray();
        handouts.forEach( ho -> hoArr.put( ho.toJsonObject() ) );

        return hoArr.toString();
    }

    /** Listens for clients in need of a server-sent event that eventually tells them if they can join the game
     * session or not.
     * @author Dragonstb
     * @since 0.1.0
     * @param request The servlet request object.
     * @param roomName The name of the room.
     * @return A Flux feeding the server-sent events.
     */
    @GetMapping( path = "/joindecision/{roomName:.+}", produces = MediaType.TEXT_EVENT_STREAM_VALUE )
    public Flux<String> getJoinDecisionSSE( HttpServletRequest request, @PathVariable String roomName) {
            // TODO: validate room name
            // TODO: check if user is waiting for or participating in this room, react properly
            // TODO: check user is not already connected
            HttpSession httpSession = request.getSession();
            Map<String, JoinDecisionFeed> joinDecisions = (Map<String, JoinDecisionFeed>)httpSession.getAttribute( "joinDecisions" );
            if( joinDecisions == null ) {
                joinDecisions = new HashMap<>();
                httpSession.setAttribute( "joinDecisions", joinDecisions );
            }

            JoinDecisionFeed dec = joinDecisions.get( roomName );
            if( dec == null ) {
                dec = new JoinDecisionFeed();
                joinDecisions.put( roomName, dec );
            }

            return dec.getFlux();
    }

    /**
     * @since 0.1.0
     * @param request
     * @param roomName
     * @return
     */
    @GetMapping("/abortjoin/{roomName:.+}")
    public String abortJoin( HttpServletRequest request, @PathVariable String roomName) {
        // TODO: validate room name
        // TODO: check if waiting
        // TODO: remove from waiting list
        // TODO: notify room creator about user-sided abortion of join process
        HttpSession httpSession = request.getSession();
        Map<String, JoinDecisionFeed> joinDecisions = (Map<String, JoinDecisionFeed>)httpSession.getAttribute( "joinDecisions" );
        if( joinDecisions == null ) {
            joinDecisions = new HashMap<>();
            httpSession.setAttribute( "joinDecisions", joinDecisions );
        }

        boolean ok = false;
        JoinDecisionFeed dec = joinDecisions.get( roomName );

        if( dec != null ) {
            // TODO: synchronize, as the room creater may let the user in at the same time
            ok = true;
            joinDecisions.remove( roomName );
        }

        // TODO: proper response
        // TODO: close connection
        JSONObject json = new JSONObject();
        json.put( "ok?", ok);

        return json.toString();
    }

    /**
     * @since 0.1.0
     * @author Dragonstb
     * @param key Localization key
     * @param locale Localization used
     * @return Localized message.
     */
    private String getMessage( @NonNull String key, @NonNull Locale locale ) {
        return messageSource.getMessage( key, null, "<"+key+">", locale);
    }
}
