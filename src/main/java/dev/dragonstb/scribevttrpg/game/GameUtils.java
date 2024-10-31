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
import dev.dragonstb.scribevttrpg.utils.Constants;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;

/** Provides some methods that are broadly used in various places.
 *
 * @author Dragonstb
 * @since 0.1.0;
 */
@Component
public class GameUtils {

    /** Text for exception thrown when the room name leads to {@code null} in the user's map of participations. */
    static final String EXC_NULL_PARTICIPATION = "Room name is mapped to null, not to an instance of Participation.";
    /** Text for exception thrown when a user's participation relates to a nonexisting game. */
    static final String EXC_GAME_DOES_NOT_EXIST = "No game is registered under this room name.";
    /** Text for exception throwen when the user's participation is not listed in the related game. */
    static final String EXC_NOT_LISTED_IN_GAME = "User\'s Partcipation is not listed in the game.";

    @Autowired
    private GameManager gameManager;

    /** Returns an optional either containing the game registered under {@code roomName} if and only if the user is
     * participating in this game, or an empty optional. Note that the user's instance of
     * {@link dev.dragonstb.scribevttrpg.game.participant.Participant Participant} is listed in two places: the user's
     * list of participations and the list of participants in the game object. If an inconsisted state is detected by
     * this method, an exception is thrown.
     * @author Dragonstb
     * @since 0.1.0;
     * @param participations Collection of user's participations.
     * @param roomName The room name where the game should be located.
     * @return Optional with the game, if the user is participating, or an empty optional if the user is not
     * participating.
     * @throws RuntimeException When an inconsistent state is detected. This could be:
     * <ul>
     *   <li>The {@code roomName} exists as key in the user's participations, but leads to {@code null}</li>
     *   <li>There is no game registered with {@code roomName}</li>
     *   <li>The user's participation exists in the user's collection of participations, but not in the list of participants in the game</li>
     * </ul>
     */
    @NonNull
    Optional<GameService> getGameUserIsParticipatingIn( @NonNull Map<String, Participant> participations, @NonNull String roomName) {
        if( !participations.containsKey( roomName ) ) {
            return Optional.empty();
        }

        // TODO: clean up detected inconsistent states
        Participant participant = participations.get( roomName );
        if( participant == null ) {
            throw new RuntimeException( EXC_NULL_PARTICIPATION );
        }

        Optional<GameService> opt = gameManager.getGame( roomName );
        if( opt.isEmpty() ) {
            throw new RuntimeException( EXC_GAME_DOES_NOT_EXIST );
        }
        GameService game = opt.get();

        if( !game.isParticipating(participant) ) {
            throw new RuntimeException( EXC_NOT_LISTED_IN_GAME );
        }

        return opt;
    }

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
        // TODO: do we really need this method beyond this calls and beyond of unit tests?
        Map<String, Participant> participations = (HashMap<String, Participant>)session.getAttribute( Constants.KEY_PARTICIPATIONS );
        if( participations == null ) {
            participations = new HashMap<>();
            session.setAttribute( Constants.KEY_PARTICIPATIONS, participations );
        }
        return participations;
    }
}
