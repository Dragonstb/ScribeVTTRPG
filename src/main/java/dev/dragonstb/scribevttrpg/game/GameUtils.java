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

    /** Text for exception thrown when the room name leads to {@code null} in the user's map of participations. */
    static final String EXC_NULL_PARTICIPATION = "Room name is mapped to null, not to an instance of Participation.";
    /** Text for exception thrown when a user's participation relates to a nonexisting game. */
    static final String EXC_GAME_DOES_NOT_EXIST = "No game is registered under this room name.";
    /** Text for exception throwen when the user's participation is not listed in the related game. */
    static final String EXC_NOT_LISTED_IN_GAME = "User\'s Partcipation is not listed in the game.";

    static enum ParticipationStatus {
        /** A user is participating. */
        participating,
        /** A user is waiting for being let in into the room. */
        waiting,
        /** The user is not participating. */
        none
    }


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
     * @deprecated since 0.1.0: Use {@code getUserParticipationStatus(...)} for checking the user's participation
     * status.
     */
    @NonNull
    @Deprecated
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

        if( !game.hasJoinedAlready(participant) ) {
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

            boolean related = game.isRelated( participant );
            if( !related ) {
                participations.remove( roomName );
                throw new NotInGameException( roomName );
            }

            return participant.hasJoinedAlready() ? ParticipationStatus.participating : ParticipationStatus.waiting;
        }
    }

}
