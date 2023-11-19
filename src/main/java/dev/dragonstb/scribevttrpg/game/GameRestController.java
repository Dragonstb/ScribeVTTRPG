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
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
        Map<String, Participant> participations = (HashMap<String, Participant>)request.getSession().getAttribute("participations");
        if( participations == null ) {
            participations = new HashMap<>();
            request.getSession().setAttribute("participations", participations);
        }

        Game game;
        // TODO: enable catches once the catch bodies are filled
//        try {
            game = gameManager.addGame( roomName );
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

    @GetMapping("/materials/{*}")
    public String getMaterials() {
        // TODO: check if you are allowed to see the materials
        // TODO: validate room name
        // TODO: check if you are participant in the given room
        // TODO: take handouts from campaign
        List<ContainerHandout> handouts = new ArrayList<>();
        handouts.add( ContainerHandout.create("rose") );
        handouts.add( ContainerHandout.create("tyler") );

        JSONArray hoArr = new JSONArray();
        handouts.forEach( ho -> hoArr.put( new JSONObject(ho.toJsonString()) ) );

        return hoArr.toString();
    }

}
