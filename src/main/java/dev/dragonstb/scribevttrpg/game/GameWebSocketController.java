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
import dev.dragonstb.scribevttrpg.game.participant.Participant;
import dev.dragonstb.scribevttrpg.game.participant.ParticipantRole;
import dev.dragonstb.scribevttrpg.utils.Constants;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
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
     * @param msg The message body sent from an admin of the game.
     * @param accessor Header accessor for accessing the session attributes.
     */
    @MessageMapping("/admingame/{roomName:.+}")
    public void administrateGame( @DestinationVariable String roomName, Message<String> msg, SimpMessageHeaderAccessor accessor ) {
        /* Message payload:
        {
            event = hopefully a value of WSAdminGameEvent
            name = supposed to be the name of user of interest
        }
        */
        // TODO: make sure that the WebSocket request causes the http session to stay alive (Spring Session does this
        //       automatcally, but would cause further programming overhead we do not really need *nowadays*)
        // TODO: validate room name
        // TODO: validate message
        // TODO: use converter for accessing the message

        JSONObject json;
        try {
            json = new JSONObject( msg.getPayload() );
        } catch ( Exception e ) {
            // TODO: notify admins, so they may roll back some actions
            return;
        }

        // check existence of room
        Optional<GameService> opt = gameManager.getGame( roomName );
        if( opt.isEmpty() ) {
            // TODO: notify admins, so they may roll back some actions
            return;
        }

        // check if sender is admin of this game
        Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
        if( sessionAttributes == null ) {
            // TODO: handle
            return;
        }
        Object obj = sessionAttributes.get( Constants.KEY_PARTICIPATIONS );
        Map<String, Participant> participations;
        if( obj != null && obj instanceof Map ) {
            participations = (Map<String, Participant>)obj;
        }
        else{
            // TODO: handle - not part of this game
            return;
        }

        Participant part = participations.get( roomName );
        if( part == null || !part.isAdministratingGame() ) {
            // TODO: handle - either sender does not exists or does not has the rights to post here
            return;
        }

        GameService game = opt.get();
        if( opt.isEmpty() ) {
            // TODO: notify admins, so they may roll back some actions
            return;
        }

        String event =json.getString( "event" );
        if( event.equals( WSGameAdminEvents.letJoinAsPlayer.name() ) ) {
            //letJoin( game, json.getString("name"), ParticipantRole.player);
        }
        else {
            // TODO: notify admins, so they may roll back some actions
        }

    }

    /**
     * @since 0.1.1;
     * @author Dragonstb
     * @param game The game we are talking about.
     * @param name ID of the user who is ushered into the room.
     * @param newRole The new role for this user.
     */
    private void letJoin( @NonNull GameService game, @NonNull String name, @NonNull ParticipantRole newRole ) {
        boolean success = game.joinProspect( name, newRole );
        if( success ) {
            // TODO: notify game admins: they may permanently delete the let-in-UI for this user
            // TODO: send Server-Sent-Event to user and initialize auto-forwarding to the game page
        }
        else {
            // TODO: notify admins, so they may roll back some actions
            // TODO: notify waiting user that he/she could not be promoted to be a participant (a reason might be that
            //       the user has become a participant since a few milliseconds, due to the reacion of another game
            //       administrator. Consequently, this notification to the user needs to behave idempotently.)
        }
    }

    // TODO: listen to websocket lifecycle events.
}
