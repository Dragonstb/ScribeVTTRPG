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

package dev.dragonstb.scribevttrpg.game.handouts;

import dev.dragonstb.scribevttrpg.game.ParticipantRole;
import java.util.List;
import org.springframework.lang.NonNull;

/** Manages handouts.
 *
 * @author Dragonstb
 * @since 0.0.4;
 */
public interface HandoutManager {

    /** Depending on the role specified, this method returns either the gm's list of handouts, the spectators'
     * list of handouts, or a new, empty list for a player.
     * @author Dragonstb
     * @since 0.0.4;
     * @param role The role for which a list is returned.
     * @return Either
     * <ul>
     *  <li> for {@code gm}, the list of all handouts. This list is shared among all gms (as soon as multiple gms become
     * allowed.)</li>
     *  <li> for {@code player}, a new ans empty instance of a list.</li>
     *  <li> or for {@code spectator}, the list of handouts seen by the spectators. This list is shared among all
     * spectators, too.</li>
     * </ul>
     */
    @NonNull
    public List<ContainerHandout> getListForRole(@NonNull ParticipantRole role);
}
