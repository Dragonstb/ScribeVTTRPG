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

package dev.dragonstb.scribevttrpg.game.participant;

import org.springframework.lang.NonNull;

/** Some data about the participant.
 *
 * @author Dragonstb
 * @since 0.0.4;
 */
public interface Participant {

    /** Returns the participant's name.
     * @author Dragonstb
     * @since 0.0.6;
     * @return The participant's name. This is never {@code null}.
     */
    @NonNull
    public String getName();

    /** The participant's role in the game.
     * @author Dragonstb
     * @since 0.0.4;
     * @return The participant's role in the game.
     */
    @NonNull
    public ParticipantRole getRole();

    /** Says if the participant's role indicates that she/he has completed the process of joining and is actually
     * participating in the game already.
     * @since 0.1.0;
     * @return Is actually participating?
     */
    public boolean hasJoinedAlready();

    /** Sets the role to a role that indicates an actual participation in this game. The method does not change the
     * participant's actual role if {@code newRole} is a waiting role (i.e. <i>prospect</i>) or if the participant is
     * not in a waiting role.
     * @since 0.1.1;
     * @param newRole The new role.
     * @return Indicates if the change of the role was successfull. The change can fail when the new role is not a
     * participating role, or when the participant is not in a waiting role.
     */
    public boolean promoteToParticipatingRole( @NonNull ParticipantRole newRole );

    /** Tells if this participant is an administrator of the game. Administrators of a game can, for example, usher
     * waiting users in into the room.
     * @since 0.1.1;
     * @return {@code True} if and only if this participant is an administrator of the game.
     */
    public boolean isAdministratingGame();
}
