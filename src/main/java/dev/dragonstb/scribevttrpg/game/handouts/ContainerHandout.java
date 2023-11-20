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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.lang.NonNull;

/** A handout piece that simply holds some other handout pieces together. The GUI contains of an element showing the
 * name and an expandable field displaying the children.
 *
 * @author Dragonstb
 * @since 0.0.3;
 */
public final class ContainerHandout extends AbstractHandoutPiece{

    public static enum DEPTH {
        /** Top level piece. Every handout is, fundamentally, a top level ContainerHandout. Displayes as h3 on the web
         page */
        top,
        /** One level below the top level. For containers that are direct child of a top level container. Displayed as
         h4 on the web page. */
        section,
        /** Two levels below the top level. For containers that are direct child of a top level container. Displayed as
         h5 on the web page. */
        subsection,
        /** Three levels below the top level. For containers that are direct child of a top level container. Displayed
         as h6 on the web page. Containers on this level may not have further containers as children. */
        paragraph
    };

    /** A name of the container. This one is displayed in the expandable field of the corresponding GUI element. */
    @NonNull private final String name;

    /** List pf child pieces. */
    private final List<AbstractHandoutPiece> pieces = new ArrayList<>();

    /** Generates.
     * @author Dragonstb
     * @since 0.0.3;
     * @param name A name of the container. This one is displayed in the expandable field of the corresponding GUI
     * element.
     */
    private ContainerHandout(@NonNull String name) {
        super( HandoutType.container );
        this.name = name;
    }

    /** Generates a new instance.
     * @author Dragonstb
     * @param name A name of the container.
     * @return New container piece.
     * @throws IllegalArgumentException When {@code name} is {@code null}.
     */
    public static ContainerHandout create(String name) throws IllegalArgumentException{
        if( name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }
        ContainerHandout ch = new ContainerHandout(name);
        return ch;
    }

    @Override
    public int hashCode() {
        int hash = 7 + 53 * name.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ContainerHandout other = (ContainerHandout) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return Objects.equals(this.pieces, other.pieces);
    }

    public String getName() {
        return name;
    }

    public List<AbstractHandoutPiece> getPieces() {
        return pieces;
    }

    @Override
    public String toJsonString() {
        JSONObject obj = new JSONObject();
        obj.putOpt("type", this.getType().toString());
        obj.put("name", name);
        if (!pieces.isEmpty() ) {
            List<String> jsonPcs = this.pieces.stream().map( piece -> piece.toJsonString() )
                    .collect( Collectors.toUnmodifiableList() );
            JSONArray arr = new JSONArray();
            jsonPcs.forEach( piece -> arr.put(piece) );
            obj.put( "pieces", jsonPcs );
        }
        return obj.toString();
    }


}
