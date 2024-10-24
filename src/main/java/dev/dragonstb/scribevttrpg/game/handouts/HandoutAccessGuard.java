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

package dev.dragonstb.scribevttrpg.game.handouts;

import java.util.ArrayList;
import java.util.List;
import org.springframework.lang.NonNull;

/** This class references a {@link dev.dragonstb.scribevttrpg.game.handouts.AbstractHandoutPiece handout piece} and
 * tracks who can access the handout piece in which way. An implementation of {@link dev.dragonstb.scribevttrpg.game.handouts.HandoutManager HandoutManager}
 * can use a map <i>id of handout piece to access guard<i> for building user specific views and for referencing the correct
 * guard when addressing a certain piece.
 *
 * @author Dragonstb
 * @since 0.0.6;
 */
final class HandoutAccessGuard {

    /** The handout piece this guard tracks the access rights for. */
    @NonNull
    private final AbstractHandoutPiece piece;

    /** IDs of players who can read the handout piece. */
    @NonNull
    private final List<String> playerCanRead = new ArrayList<>();

    /** IDs of players who can edit the handout piece. */
    @NonNull
    private final List<String> playerCanEdit = new ArrayList<>();

    /** Can spectators see this handout piece? */
    private boolean spectatorsCanRead = false;

    /** Generates.
     * @since 0.0.6
     * @param piece The handout piece guarded by the new instance.
     */
    HandoutAccessGuard( @NonNull AbstractHandoutPiece piece ) {
        this.piece = piece;
    }

    /** Gets the handout piece guarded by {@code this}.
     * @author Dragonstb
     * @since 0.0.6
     * @return The guarded handout piece.
     */
    @NonNull
    AbstractHandoutPiece getPiece() {
        // TODO: do we need this method outside of this class beyond automated testing?
        return piece;
    }
}
