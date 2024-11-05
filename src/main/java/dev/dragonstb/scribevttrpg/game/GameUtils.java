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

import dev.dragonstb.scribevttrpg.game.participant.Participant;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import dev.dragonstb.scribevttrpg.GameManager;
import dev.dragonstb.scribevttrpg.game.exceptions.GameNotFoundException;
import dev.dragonstb.scribevttrpg.game.exceptions.NotInGameException;
import static dev.dragonstb.scribevttrpg.game.participant.ParticipantRole.prospect;
import dev.dragonstb.scribevttrpg.utils.Constants;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/** Provides some methods that are broadly used in various places.
 *
 * @author Dragonstb
 * @since 0.1.0;
 */
@Component
public class GameUtils {

    public static enum ParticipationStatus {
        /** A user is participating. */
        participating,
        /** A user is waiting for being let in into the room. */
        waiting,
        /** The user is not participating. */
        none
    }


    @Autowired
    private GameManager gameManager;

    /** Gets the attribute "participations" from the http session object. This is a map that maps room names to the
     * user participations in gaming sessions.<br><br>
     * If the attribute is not set, a new, empty map is added. Therefore, this method garuantees the return of a nonnull
     * map.
     * @since 0.1.0
     * @author Dragonstb
     * @param session The http session. Must be not-null
     * @return The map of romm names to participations. Never null.
     */
    @NonNull
    final static Map<String, Participant> getParticipationsAndCreateIfNeeded(@NonNull HttpSession session) {
        // TODO: when user is logged in, link the data to the Principal rather than to the session. So it does got get
        //       lost when the user loses his/her session.
        Map<String, Participant> participations = (HashMap<String, Participant>)session.getAttribute( Constants.KEY_PARTICIPATIONS );
        if( participations == null ) {
            participations = new HashMap<>();
            session.setAttribute( Constants.KEY_PARTICIPATIONS, participations );
        }
        return participations;
    }

    /** Gets the participation status of a user in a game registered in room {@code roomName}. This status is
     * exactly one of the following:
     * <ul>
     *   <li>none: the user is not related to the game in any way</li>
     *   <li>waiting: the user has already asked if she/he cn join the game, bat is still waiting for the response</li>
     *   <li>participating: the user has completed the process of joining the game and is already in the room</li>
     * </ul>
     * <br><br>
     * Side effect: if an inconsistent state for {@code participations} is detected, this becomes cleaned and the key
     * {@code roomname} with the associated instance of Participant is removed. This is done in a threadsafe way.
     * <br><br>
     * In part of its code, this method locks the {@code participations}.
     * @param participations User's participations as stored with the principal/session
     * @param roomName Name of the room where the game is supposed to take place.
     * @return Particiaption status
     * @throws GameNotFoundException The game manager has no game registered for the provided {@code roomName}.
     * @throws NotInGameException The user has a Participation for the game. This is, however, not listed in the game
     * itself.
     */
    @NonNull
    ParticipationStatus getUserParticipationStatus( @NonNull Map<String, Participant> participations,
            @NonNull String roomName ) throws GameNotFoundException, NotInGameException {
        Optional<GameService> opt;
        opt = gameManager.getGame( roomName );
        if( opt.isEmpty() ) {
            // TODO: serve a nice page which states that the room does not exists (404 not found) or, if the user
            // played before in this room, that the room has been closed in the meantime (410 gone). Note: as we cannot
            // decide on the server if the latter one is true (we don't want to put the effort for this), we send 404
            // anyway
            throw new GameNotFoundException( roomName );
        }

        GameService game = opt.get();

        synchronized ( participations ) {
            Participant participant = participations.get( roomName );
            if( participant == null ) {
                if( participations.containsKey(roomName) ) {
                    // key leading to nothing => remove key
                    participations.remove( roomName );
                }
                return ParticipationStatus.none;
            }

            ParticipationStatus status = game.getParticipationStatus( participant );
            if( status == ParticipationStatus.none ) {
                participations.remove( roomName );
                throw new NotInGameException( roomName );
            }

            return status;
        }
    }

}
