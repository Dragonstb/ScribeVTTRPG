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

import org.springframework.lang.NonNull;

/** Representation of a participant.
 *
 * @author Dragonstb
 * @since 0.0.4;
 */
public class DefaultParticipant implements Participant{

    /** The participant's name. */
    @NonNull private final String name;

    /** The role the participant has in the game session. */
    @NonNull private final ParticipantRole role;

    /** Generates.
     * @author Dragonstb
     * @since 0.0.4;
     * @param role The role the participant occupies.
     */
    private DefaultParticipant(@NonNull ParticipantRole role) {
        this.name = "TODO: make name constructable";
        this.role = role;
    }

    /** Creates a new instance.
     * @author Dragonstb
     * @since 0.0.4;
     * @param role The role the participant occupies. Must be {@code non-null}.
     * @return A new instance.
     */
    public static DefaultParticipant create(@NonNull ParticipantRole role) {
        DefaultParticipant part = new DefaultParticipant(role);
        return part;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ParticipantRole getRole() {
        return role;
    }

}
