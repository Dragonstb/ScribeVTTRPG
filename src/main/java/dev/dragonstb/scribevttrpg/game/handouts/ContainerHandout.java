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
import java.util.Optional;
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

    /** A label of the container. This one is displayed in the corresponding GUI element. */
    @NonNull private final String label;

    /** List pf child pieces. */
    private final List<AbstractHandoutPiece> pieces = new ArrayList<>();

    /** Generates.
     * @author Dragonstb
     * @since 0.0.3;
     * @param label A label of the container. This one is displayed in the corresponding GUI element.
     */
    private ContainerHandout(@NonNull String label, @NonNull String id) {
        super( HandoutType.container, id );
        this.label = label;
    }

    /** Generates a new instance.
     * @author Dragonstb
     * @param label A name of the container.
     * @param id An id for the container.
     * @return New container piece.
     * @throws IllegalArgumentException When {@code label} is {@code null}.
     */
    public static ContainerHandout create(String label, String id) throws IllegalArgumentException{
        if( label == null) {
            throw new IllegalArgumentException("Label cannot be null");
        }
        else if( id == null ) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        ContainerHandout ch = new ContainerHandout( label, id );
        return ch;
    }

    /** Adds the given piece either to the list of child pieces of {@code this} or to the list of child pieces of
     * a descendant of {@code this}. For finding the right place in the tree structure, the {@code parentId} is the
     * concatenation of the ids of all supposed-to-be ancestors of {@code piece}. It starts with the id of {@code this}
     * and ends with the id of the actual parent.<br>
     * If {@code piece} is not a child of {@code this}, the id of {@code this} is stripped from {@code parentId}.
     * From this reduced parent id, the correct child in the heredity line is deduced. This method {@code addPiece(..)}
     * is then invoked on this child, with the reduced parent id as parameter.
     * @author Dragonstb
     * @since 0.0.4;
     * @param piece Piece to be added.
     * @param parentId Concatenated ids of all ancestors of {@code piece} up to <i>and including</i> {@code this}.
     * @throws RuntimeException There are various reasons:
     * <ul>
     *  <li>A handout piece with the same id is already listed to {@code this}.</li>
     *  <li>The piece is to be added to a descendant piece of {@code this}, but there is not even a direct child with a
     *  name that follows from the {@code parentId}. </li>
     *  <li></li>
     *  <li>The piece that is supposed to be the parent is not a container handout.</li>
     * </ul>
     */
    public void addPiece( @NonNull AbstractHandoutPiece piece, @NonNull String parentId ) throws RuntimeException {
        String myId = this.getId();
        if( myId.equals( parentId ) ) {
            addPieceHere( piece );
        }
        else {
            String reducedId = parentId.substring( myId.length() ); // all part of 'parentId' beyond 'myId'
            addPieceToChild( piece, reducedId );
        }
    }

    /** Adds the piece to the list of child pieces.
     * @author Dragonstb
     * @since 0.4;
     * @param piece Handout piece to be added.
     * @throws RuntimeException In case there is already a handout piece with the same id.
     */
    private void addPieceHere( @NonNull AbstractHandoutPiece piece ) throws RuntimeException {
        // TODO: could be private, for now package protected for simple unit testing
        boolean yetNotListed = pieces.stream().noneMatch( pc -> pc.getId().equals( piece.getId()) );
        if( yetNotListed ) {
            pieces.add( piece );
        }
        else {
            throw new RuntimeException( "Cannot add handout piece that is already there." );
        }
    }

    /** Finds the right child from {@code reducedParentId} for invoking {@code addPiece(..)}.
     * @author Dragonstb
     * @since 0.0.4;
     * @param piece Piece to be added.
     * @param reducedParentId Concatenated ids of all ancestors of {@code piece} up to <i>but excluding</i>
     * {@code this}.
     * @throws RuntimeException When no proper child can be found, or when the proper child is not a container handout.
     */
    private void addPieceToChild( @NonNull AbstractHandoutPiece piece, @NonNull String reducedParentId )
            throws RuntimeException{
        // TODO: could be private, for now package protected for simple unit testing
        Optional<AbstractHandoutPiece> opt = pieces.stream().filter( pc -> reducedParentId.startsWith( pc.getId() ) )
                .findFirst();

        if( opt.isEmpty() ) {
            throw new RuntimeException( "No child handout piece of matching name." );
        }

        AbstractHandoutPiece next = opt.get();
        if( next.getType() == HandoutType.container ) {
            ((ContainerHandout)next).addPiece( piece, reducedParentId );
        }
        else {
            throw new RuntimeException( "Cannot add handout pieces to non-container handouts" );
        }
    }

    @Override
    public int hashCode() {
        int hash = 7 + 53 * label.hashCode();
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
        if (!Objects.equals(this.label, other.label)) {
            return false;
        }
        return Objects.equals(this.pieces, other.pieces);
    }

    public String getLabel() {
        return label;
    }

    public List<AbstractHandoutPiece> getPieces() {
        return pieces;
    }

    @Override
    public String toJsonString() {
        JSONObject obj = this.toJsonObject();
        return obj.toString();
    }

    @Override
    public JSONObject toJsonObject() {
        JSONObject obj = new JSONObject();
        obj.put( "type", this.getType().toString() );
        obj.put( "label", label );
        obj.put( "id", getId() );
        if (!pieces.isEmpty() ) {
            List<JSONObject> jsonPcs = this.pieces.stream().map( piece -> piece.toJsonObject() )
                    .collect( Collectors.toUnmodifiableList() );
            JSONArray arr = new JSONArray();
            jsonPcs.forEach( piece -> arr.put(piece) );
            obj.put( "pieces", jsonPcs );
        }

        return obj;
    }

}
