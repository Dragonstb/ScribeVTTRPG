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

package dev.dragonstb.scribevttrpg.content;

import dev.dragonstb.scribevttrpg.game.handouts.AbstractHandoutPiece;
import dev.dragonstb.scribevttrpg.game.handouts.ContainerHandout;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

/** A content item of type handout.
 *
 * @author Dragonstb
 * @since 0.0.5;
 */
public class ContentHandout extends ContentItem{

    @NonNull
    private final ContainerHandout handout;

    ContentHandout( @NonNull ContainerHandout handout, String description ) {
        super(ContentType.handout, handout.getLabel(), description );
        this.handout = handout;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = getMinimalJson();
        JSONObject handoutPieces = this.handout.toJsonObject();

        JSONArray pieces;
        if( handoutPieces.has( "pieces" ) ) {
            pieces = handoutPieces.getJSONArray( "pieces" );
        }
        else {
            pieces = new JSONArray(); // use empty array
        }
        json.put( "pieces", pieces );

        return json;
    }

    /**
     * @since 0.0.5;
     * @return The handout.
     */
    @NonNull
    public ContainerHandout getHandout() {
        return handout;
    }

    @Override
    public void setName( String name ) {
        // use also as label of the handout
        handout.setLabel( name );
        super.setName( handout.getLabel() );
    }


}
