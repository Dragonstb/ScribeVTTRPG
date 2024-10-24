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

import dev.dragonstb.scribevttrpg.game.handouts.ContainerHandout;
import java.util.List;
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

    /** The handouts the participant can see.
     * @author Dragonstb
     * @since 0.0.4;
     * @return The handouts the participant can see.
     * @deprecated since 0.0.6: Request to handouts are forwarded from the API controller to the handout manager
     * directly, not anymore via this detour. The Participant is a pure informative class now.
     */
    @NonNull
    public List<ContainerHandout> getHandouts();
}
