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

import org.json.JSONObject;
import org.springframework.lang.NonNull;

/** Handouts a built up from one to many pieces. A little note found by the players may be just a single text piece.
 * A character sheet may consists of many different pieces organized in sections and subsections using container pieces.
 *
 * @author Dragonstb
 * @since 0.0.3;
 */
public abstract class AbstractHandoutPiece {

    @NonNull
    private final HandoutType type;

    @NonNull
    private final String id;

    /** Generates.
     * @author Dragonstb
     * @since 0.0.3;
     * @param type The type of the handout. Must be non-null.
     * @param id An id of the handout. Must be non-null and unique.
     */
    public AbstractHandoutPiece(@NonNull HandoutType type, @NonNull String id) {
        this.type = type;
        this.id = id;
    }

    /** Gets the type of the handout.
     * @suthor Dragonstb
     * @since 0.0.3;
     * @return Gets the type of the handout.
     */
    public final HandoutType getType() {
        return type;
    }

    /** Gets the id of this handout.
     * @author Dragonstb
     * @since 0.0.4;
     * @return Gets the id of this handout.
     */
    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public abstract String toJsonString();

    @NonNull
    public abstract JSONObject toJsonObject();
    
}
