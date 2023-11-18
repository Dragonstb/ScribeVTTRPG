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

import dev.dragonstb.scribevttrpg.game.handouts.ContainerHandout;
import dev.dragonstb.scribevttrpg.game.handouts.HandoutType;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Dragonstb
 * @since 0.0.3;
 */
@RestController
public class GameRestController {

    @PostMapping("/creategame")
    public String createGame() {
        // TODO: validate favourite room name
        // TODO: validate campaign name; this also includes checking if it is one of the game master's campaign at all
        // TODO: check if room name is unused, respond creation failure if used
        // TODO: create room for the campaign


        String room = "youllalwaysenduphere"; // TODO: use room name here

        JSONObject json = new JSONObject();
        json.put("success", true);
        json.put("room", room);

        return json.toString();
//        return "{\"success\": true,\"room\": \""+room+"\"}";
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
