/*
 * Copyright (c) 2024, Dragonstb
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
import dev.dragonstb.scribevttrpg.game.participant.ParticipantRole;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Dragonstb
 * @since 0.1.1;
 */
@Controller
public class GameWebSocketController {

    @Autowired
    private GameManager gameManager;

    @Autowired
    private SimpMessagingTemplate smt;

    /** Handles messages sent from the clients for administrating the room. Examples are decisions made on requests
     * of joining the game.
     * @since 0.1.1;
     * @author Dragonstb
     * @param roomName Name of the room we are talking about
     */
    @MessageMapping("/admingame/{roomName:.+}")
    public void administrateGame( @DestinationVariable String roomName ) {
        // TODO: validate room name

        Optional<GameService> opt = gameManager.getGame( roomName );
        if( opt.isEmpty() ) {
            return;
        }

        // TODO: is sender administrator in this room?

        GameService game = opt.get();

        /*
        TODO:
        1. extract message sent
        2. get event type
        3. react on event

        when event is letJoinAs...:
            A: get candidate participant's name
            B: get candidate participant's new role
            C: call game.joinProspect() fur ushering the candidate participant in... if possible
            D: if successfull, send notification to waiting prospect
            E: notify room admins anyway, they shall kick the user from their list of waiting people
        */
    }

    // TODO: listen to websocket lifecycle events.
}
