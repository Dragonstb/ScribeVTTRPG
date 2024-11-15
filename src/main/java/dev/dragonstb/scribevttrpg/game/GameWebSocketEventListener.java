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

import dev.dragonstb.scribevttrpg.events.ParticipantJoinsEvent;
import dev.dragonstb.scribevttrpg.utils.Constants;
import dev.dragonstb.scribevttrpg.utils.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/** Reacts on events that are related to message flow over web sockets somehow.
 *
 * @author Dragonstb
 * @since 0.1.1;
 */
@Component
public final class GameWebSocketEventListener {

    @Autowired
    private SimpMessagingTemplate smt;

    /** Reacts when a join process for a new participant ends with the decision that the user can join the game.
     * @since 0.1.1;
     * @author Dragonstb
     * @param event Event fired
     */
    @EventListener(ParticipantJoinsEvent.class)
    public void onParticipantJoinsEvent( ParticipantJoinsEvent event ) {
        JSONObject json = new JSONObject();
        json.put( "event", Constants.EVT_GM_ADM_JOIN_DECIDED );
        json.put( "name", event.getName() );
        smt.convertAndSend( Utils.getAdminGamePath(event.getRoomName()), json.toString() );
    }

}
