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

import dev.dragonstb.scribevttrpg.utils.Constants;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

/** A handout that contains nothing else than text.
 *
 * @author Dragonstb
 * @since 0.0.4;
 */
public class TextHandout extends AbstractHandoutPiece {

    /** The text. */
    @NonNull private final String text;

    private TextHandout( String text, @NonNull String id ) {
        super( HandoutType.text, id );
        this.text = text != null ? text : Constants.EMPTY_STRING;
    }

    /** Creates a new instance.
     * @author Dragonstb
     * @since 0.0.4;
     * @param text The text which is to be displayed
     * @param id An id;
     * @return A new text handout piece.
     * @throws IllegalArgumentException
     */
    public static TextHandout create( String text, @NonNull String id ) {
        return new TextHandout(text, id );
    }

    String getText() {
        return text;
    }

    @Override
    public String toJsonString() {
        JSONObject obj = toJsonObject();
        return obj.toString();
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject json = new JSONObject();
        json.put( "id", getId() );
        json.put( "type", getType().toString() );
        json.put( "text", text );
        return json;
    }






}
