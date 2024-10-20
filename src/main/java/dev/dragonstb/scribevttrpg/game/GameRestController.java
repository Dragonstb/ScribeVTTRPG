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

import dev.dragonstb.scribevttrpg.GameManager;
import dev.dragonstb.scribevttrpg.game.handouts.ContainerHandout;
import dev.dragonstb.scribevttrpg.game.handouts.TextHandout;
import dev.dragonstb.scribevttrpg.utils.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

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

    /** This method creates a new game. The game is created with the favourite room name, as long as this name has not
     * been taken already. If the creation fails, an error messsage is returned.
     *
     * @param request The underlying servlet request.
     * @param body The request body.
     * @return The response.
     */
    @PostMapping("/creategame")
    public String createGame( HttpServletRequest request, @RequestBody String body ) {
        // TODO: validate favourite room name
        // TODO: validate campaign name; this also includes checking if it is one of the game master's campaign at all
        // TODO: check if room name is unused, respond creation failure if used
        // TODO: create room for the campaign

        JSONObject jbod = new JSONObject(body); // TODO: use converter
        String roomName = jbod.getString("roomName");

        // TODO: access session variables with annotations
        HttpSession httpSession = request.getSession();
        Map<String, Participant> participations = getParticipationsAndCreateIfNeeded( httpSession );
        Game game;
        // TODO: enable catches once the catch bodies are filled
//        try {
            game = gameManager.createGame( roomName );
//        }
//        catch(IllegalArgumentException e) {
//            // TODO: name is null or not long enough -> report
//        }
//        catch(RuntimeException e) {
//            // TODO: name is in use already -> report
//        }

        // add yourself as gm
        Participant part = game.addParticipant( ParticipantRole.gm );
        participations.put( roomName, part );

        // build response
        JSONObject json = new JSONObject();
        json.put("success", true);
        json.put("room", roomName);

        return json.toString();
    }

    @GetMapping("/materials/{roomName:.+}")
    public String getMaterials( HttpServletRequest request, HttpServletResponse response, @PathVariable String roomName ) {
        // TODO: validate room name
        HttpSession httpSession = request.getSession();
        Map<String, Participant> participations = getParticipationsAndCreateIfNeeded( httpSession );
        if( !participations.containsKey( roomName ) ) {
            // TODO: check out for a nice status code
            return setErrorAndGetResponse( response, "Not participating in this room", 400);
        }
        Participant participant = participations.get( roomName );
        if( participant == null ) {
            // TODO: check out for a nice status code
            return setErrorAndGetResponse( response, "Not participating in this room", 400);
        }

        Optional<Game> opt = gameManager.getGame( roomName );
        if( opt.isEmpty() ) {
            // TODO: check out for a nice status code
            return setErrorAndGetResponse( response, "Could not find game", 500);
        }
        Game game = opt.get();
        

        // TODO: check if you are allowed to see the materials

        // TODO: take handouts from campaign
        List<ContainerHandout> handouts = new ArrayList<>();
        ContainerHandout skull = ContainerHandout.create( "Skull the Barbarian", "ho1" );
        ContainerHandout skills = ContainerHandout.create( "Skills", "ho2" );
        ContainerHandout genSkills = ContainerHandout.create( "Generic Skills", "ho3" );
        ContainerHandout comSkills = ContainerHandout.create( "Combat Skills", "ho4" );
        ContainerHandout magSkills = ContainerHandout.create( "Magic Skills", "ho5" );
        ContainerHandout traits = ContainerHandout.create( "Traits", "ho6" );
        TextHandout drink = TextHandout.create( "Drink", "ho7" );
        TextHandout endurance = TextHandout.create( "Endurance", "ho8" );
        TextHandout axes = TextHandout.create( "Axes", "ho9" );
        TextHandout swords = TextHandout.create( "Swords", "ho10" );
        TextHandout knuckles = TextHandout.create( "Bare Knuckles", "ho11" );
        TextHandout firebolt = TextHandout.create( "Firebolt", "ho12" );
        TextHandout lightningbolt = TextHandout.create( "Lightning Bolt", "ho13" );
        TextHandout honest = TextHandout.create( "Very honest", "ho14" );
        TextHandout strong = TextHandout.create( "Very strong", "ho15" );
        TextHandout impatient = TextHandout.create( "Not very patient", "ho16" );
        skull.addPiece( skills, "ho1" );
        skull.addPiece( genSkills, "ho1ho2" );
        skull.addPiece( comSkills, "ho1ho2" );
        skull.addPiece( magSkills, "ho1ho2" );
        skull.addPiece( drink, "ho1ho2ho3" );
        skull.addPiece( endurance, "ho1ho2ho3" );
        skull.addPiece( axes, "ho1ho2ho4" );
        skull.addPiece( swords, "ho1ho2ho4" );
        skull.addPiece( knuckles, "ho1ho2ho4" );
        skull.addPiece( firebolt, "ho1ho2ho5" );
        skull.addPiece( lightningbolt, "ho1ho2ho5" );
        skull.addPiece( traits, "ho1" );
        skull.addPiece( honest, "ho1ho6" );
        skull.addPiece( strong, "ho1ho6" );
        skull.addPiece( impatient, "ho1ho6" );

        ContainerHandout quest = ContainerHandout.create( "Quest Leaflet", "ho17" );
        TextHandout questText = TextHandout.create( "Sinister howls from the wrecked castle at night fill the peaceful"
                + " villagers with fear. Coincidentally, there are also rumors about a great treasure in that "
                + "haunted ruin.", "ho18" );
        quest.addPiece( questText, "ho17" );

        handouts.add( skull );
        handouts.add( quest );

        JSONArray hoArr = new JSONArray();
//        handouts.forEach( ho -> hoArr.put( new JSONObject(ho.toJsonString()) ) );
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

    /** Sets the status code of the response and returns a stringified Json containing the message.
     * @author Dragonstb
     * @param response Http Response. Must not be null.
     * @param message Error message added to the response. Must not be null.
     * @param httpStatus New http status code for the response. Please set something useful here.
     * @return Stringified json for the response body.
     */
    @NonNull
    final static String setErrorAndGetResponse( @NonNull HttpServletResponse response, @NonNull String message, int httpStatus ) {
        response.setStatus( httpStatus );
        JSONObject json = new JSONObject();
        json.put( "Error", message );
        return json.toString();
    }
}
