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

import java.util.List;
import org.springframework.lang.NonNull;

/** Manages handouts.
 *
 * @author Dragonstb
 * @since 0.0.4;
 */
public interface HandoutManager {

    /** Adds the handout to the handout manager. The container and all of the content within can only be accessed by
     * the GM in the first place.
     * @author Dragonstb
     * @since 0.0.6
     * @param handout Handout that is added.
     * @throws IllegalArgumentException When an ID is already taken.
     */
    public void addHandout(@NonNull ContainerHandout handout);

    /** Adds the handouts to the handout manager. The container and all of the content within can only be accessed by
     * the GM in the first place.
     * @author Dragonstb
     * @since 0.0.6
     * @param handouts Handouts that are added.
     */
    public void addHandouts( @NonNull List<ContainerHandout> handouts );

    /** Gets the handouts.
     * @since 0.0.6;
     * @author Dragonstb
     * @return The handouts.
     */
    @NonNull
    public List<ContainerHandout> getHandouts();
}
