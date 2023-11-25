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
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Dragonstb
 */
public class ContainerHandoutTest {

    private ContainerHandout con;
    private static final String BASE_LABEL = "jenny";
    private static final String BASE_ID = "ho";
    private String label;
    private String id;
    private int counter = 0;

    @BeforeEach
    public void setupForTest() {
        ++counter;
        label = BASE_LABEL + counter;
        id = BASE_ID + counter;
        con = ContainerHandout.create( label, id );
    }

    @Test
    public void testCreate_allsWell_noChildren() {
        assertEquals( label, con.getLabel() );
        assertEquals( id, con.getId() );
        assertEquals( HandoutType.container, con.getType() );
        assertTrue( con.getPieces().isEmpty() );
    }

    @Test
    public void testCreate_nullName() {
        IllegalArgumentException iae = assertThrows(
                IllegalArgumentException.class,
                () -> ContainerHandout.create( null, id )
            );

        assertEquals( "Label cannot be null", iae.getMessage() );
    }

    @Test
    public void testCreate_nullId() {
        IllegalArgumentException iae = assertThrows(
                IllegalArgumentException.class,
                () -> ContainerHandout.create( label, null )
            );

        assertEquals( "Id cannot be null", iae.getMessage() );
    }

    public void testCreate_allsWell_someChildren() {
        // TODO
    }

    @Test
    public void testToJsonString_noChildren() {
        String json = con.toJsonString();
        JSONObject obj = new JSONObject( json );

        assertTrue( obj.has("label"), "no label in json" );
        assertEquals( label, obj.get("label"), "wrong label in json" );
        assertTrue( obj.has("id"), "no id in json" );
        assertEquals( id, obj.get("id"), "wrong id in json" );
        assertTrue( obj.has("type") );
        assertEquals( HandoutType.container.toString(), obj.get("type"), "wrong type in json");
        assertFalse( obj.has("pieces") );

        assertEquals( 3, obj.keySet().size(), "wrong number of keys in json" );
    }

    public void testToJsonString_someChildren() {
        // TODO
    }

    @Test
    public void testToJsonObject_noChildren() {
        JSONObject obj = con.toJsonObject();

        assertTrue( obj.has("label"), "no label in json" );
        assertEquals( label, obj.get("label"), "wrong label in json" );
        assertTrue( obj.has("id"), "no id in json" );
        assertEquals( id, obj.get("id"), "wrong id in json" );
        assertTrue( obj.has("type") );
        assertEquals( HandoutType.container.toString(), obj.get("type"), "wrong type in json");
        assertFalse( obj.has("pieces") );

        assertEquals( 3, obj.keySet().size(), "wrong number of keys in json" );
    }

    public void testToJsonObject_someChildren() {
        AbstractHandoutPiece[] children = new AbstractHandoutPiece[] {
            makePiece( "child1" ), makePiece( "child2" )
        };
        for ( AbstractHandoutPiece child : children) {
            con.addPiece( child, con.getId() );
        }

        JSONObject obj = con.toJsonObject();

        assertTrue( obj.has("label"), "no label in json" );
        assertEquals( label, obj.get("label"), "wrong label in json" );
        assertTrue( obj.has("id"), "no id in json" );
        assertEquals( id, obj.get("id"), "wrong id in json" );
        assertTrue( obj.has("type") );
        assertEquals( HandoutType.container.toString(), obj.get("type"), "wrong type in json");
        assertFalse( obj.has("pieces") );

        assertTrue( obj.has("pieces") );
        assertInstanceOf( JSONArray.class, obj.get("pieces") );
        JSONArray arr = obj.getJSONArray( "pieces" );
        assertEquals( 2, arr.length() );

        for ( int idx = 0; idx < arr.length(); idx++ ) {
            JSONObject json = arr.getJSONObject( idx );
            assertEquals( children[idx].toJsonObject(), json );
        }

        assertEquals( 4, obj.keySet().size(), "wrong number of keys in json" );
    }


    @Test
    public void testAddPiece_here_allIsWell() {
        AbstractHandoutPiece child = makePiece();
        con.addPiece( child, con.getId() );

        List<AbstractHandoutPiece> pieces = con.getPieces();
        assertEquals( 1, pieces.size() );
        assertTrue( pieces.get(0) == child );
    }

    @Test
    public void testAddPiece_here_duplicateId() {
        AbstractHandoutPiece childA = makePiece( "child" );
        AbstractHandoutPiece childB = makePiece( "child" );

        con.addPiece( childA, con.getId() );

        RuntimeException exc = assertThrows(
                RuntimeException.class,
                () -> con.addPiece( childB, con.getId() )
        );

        assertEquals( "Cannot add handout piece that is already there.", exc.getMessage() );
    }

    @Test
    public void testAddPiece_onDescendant_allIsWell() {
        ContainerHandout childA = ContainerHandout.create( "Wayne", "child" );
        AbstractHandoutPiece childB = makePiece( "grandChild" );

        String parentId = con.getId() + childA.getId();

        con.addPiece( childA, con.getId() );
        con.addPiece( childB, parentId );

        assertEquals( 1, childA.getPieces().size() );
        assertTrue( childA.getPieces().get(0) == childB );
    }

    @Test
    public void testAddPiece_onDescendant_noProperChild() {
        ContainerHandout childA = ContainerHandout.create( "Wayne", "child" );
        AbstractHandoutPiece childB = makePiece( "grandChild" );

        String parentId = con.getId() + "something";

        con.addPiece( childA, con.getId() );

        RuntimeException exc = assertThrows(
                RuntimeException.class,
                () -> con.addPiece( childB, parentId )
        );

        assertEquals( "No child handout piece of matching name.", exc.getMessage() );
    }

    @Test
    public void testAddPiece_onDescendant_thatisNotAContainer() {
        TextHandout childA = TextHandout.create( "a story", "child" );
        AbstractHandoutPiece childB = makePiece( "grandChild" );

        String parentId = con.getId() + childA.getId();

        con.addPiece( childA, con.getId() );
        RuntimeException exc = assertThrows(
                RuntimeException.class,
                () -> con.addPiece( childB, parentId )
        );

        assertEquals( "Cannot add handout pieces to non-container handouts", exc.getMessage() );
    }

//    @Test
//    public void testAddPiece_() {
//        // TODO
//    }
//
//    @Test
//    public void testAddPiece_() {
//        // TODO
//    }


    // ____________________  utilities  ____________________

    /** Returns a handout piece.
     * @author Dragonstb
     * @since 0.0.4;
     * @param id An id.
     * @return A a handout piece.
     */
    private AbstractHandoutPiece makePiece(String id) {
        AbstractHandoutPiece piece = new AbstractHandoutPiece(HandoutType.container, id != null ? id : "defaultId" ) {
            @Override
            public String toJsonString() {
                return "{}";
            }

            @Override
            public JSONObject toJsonObject() {
                return new JSONObject().put( "id", id );
            }
        };

        return piece;
    }

    /** The same as {@code makePiece(null)}. For the really lazy unit tester :).
     * @Dragonstb
     * @since 0.0.4;
     * @return A handout piece.
     */
    private AbstractHandoutPiece makePiece() {
        return makePiece( null );
    }

}
