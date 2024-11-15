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

package dev.dragonstb.scribevttrpg.events;

import dev.dragonstb.scribevttrpg.game.participant.ParticipantRole;
import org.springframework.lang.NonNull;

/** Internal event fired by the {@link dev.dragonstb.scribevttrpg.game.GameWebSocketController GameWebSocketController}
 * when a new participant is let into the room, joining the game.
 *
 * @author Dragonstb
 * @since 0.1.1;
 */
public final class ParticipantJoinsEvent {

    /** Role the new participant has in the game. */
    @NonNull
    private final ParticipantRole role;
    /** The new participant's name. */
    @NonNull
    private final String name;

    /** The name of the room. */
    @NonNull
    private final String roomName;

    /** Generates
     * @since 0.1.1;
     * @param role Role the new participant has in the game.
     * @param name The new participant's name.
     * @param roomName The name of the room.
     */
    public ParticipantJoinsEvent( @NonNull ParticipantRole role, @NonNull String name, @NonNull String roomName ) {
        this.role = role;
        this.name = name;
        this.roomName = roomName;
    }

    /** Get the new participant's role in the game.
     * @since 0.1.1;
     * @return The new participant's role in the game.
     */
    @NonNull
    public ParticipantRole getRole() {
        return role;
    }

    /** Get the new participant's name.
     * @since 0.1.1;
     * @return The new participant's name.
     */
    @NonNull
    public String getName() {
        return name;
    }

    /** Get the name of the room.
     * @since 0.1.1;
     * @return The name of the room.
     */
    public String getRoomName() {
        return roomName;
    }

}
