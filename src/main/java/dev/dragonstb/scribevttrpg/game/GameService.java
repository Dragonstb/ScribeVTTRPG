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

import dev.dragonstb.scribevttrpg.game.exceptions.IdentityNotUniqueException;
import dev.dragonstb.scribevttrpg.game.handouts.HandoutManager;
import dev.dragonstb.scribevttrpg.game.participant.Participant;
import dev.dragonstb.scribevttrpg.game.participant.ParticipantRole;
import org.springframework.lang.NonNull;

/** The services a game offers as part of a public API. Other classes may mainly use this interface.
 *
 * @author Dragonstb
 * @since 0.0.4;
 */
public interface GameService {

    /** Adds a participant to the game session. The participant will already have a list of handouts she/he has
     * access to (this list might be empty, though, but not-null).
     * @author Dragonstb
     * @since 0.0.4;
     * @param name The participant's name.
     * @param role The role the participant has in the game session.
     * @return A new instance.
     * @throws IdentityNotUniqueException When there is already a participant in the game with the same identity.
     */
    @NonNull
    public Participant createAndAddParticipant(@NonNull String name, @NonNull ParticipantRole role) throws IdentityNotUniqueException;

    /** Returns the handout manager of the game.
     * @since 0.0.6;
     * @return The handout manager of the game.
     */
    @NonNull
    HandoutManager getHandoutManager();

    /** Returns the current participation status of the given participant in the game. This is exactly one of the
     * three values <i>participating</i>, <i>waiting</i>, or <i>none</i>.
     * @since 0.1.0
     * @param participant User of interest.
     * @return How is this user related to the game?
     */
    public ParticipationStatus getParticipationStatus( @NonNull Participant participant );

    public boolean joinProspect( @NonNull String name, @NonNull ParticipantRole newRole );
}
