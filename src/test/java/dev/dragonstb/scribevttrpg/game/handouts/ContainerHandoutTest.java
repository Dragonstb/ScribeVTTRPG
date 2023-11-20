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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author dragon
 */
public class ContainerHandoutTest {

    private ContainerHandout con;
    private static final String BASE_NAME = "jenny";
    private String name;
    private int counter = 0;

    @BeforeEach
    public void setupForTest() {
        name = BASE_NAME + (++counter);
        con = ContainerHandout.create(name);
    }


    @Test
    public void testCreate_allsWell_noChildren() {
        assertEquals(name, con.getName());
        assertEquals( HandoutType.container, con.getType() );
        assertTrue( con.getPieces().isEmpty() );
    }

    @Test
    public void testCreate_nullName() {
        IllegalArgumentException iae = assertThrows(
                IllegalArgumentException.class,
                () -> ContainerHandout.create( null )
            );

        assertEquals( "Name cannot be null", iae.getMessage() );
    }

    public void testCreate_allsWell_someChildren() {
        // TODO
    }

    public void testCreate_someEqualChildren() {
        // TODO
    }

    public void testEquals() {
        // TODO
    }

    /**
     * Test of toJsonString method, of class ContainerHandout.
     */
    @Test
    public void testToJsonString_noChildren() {
        String json = con.toJsonString();
        JSONObject obj = new JSONObject( json );
        assertTrue( obj.has("name"), "no name in json" );
        assertEquals( name, obj.get("name"), "wrong name in json" );
        assertTrue( obj.has("type") );
        assertEquals( HandoutType.container.toString(), obj.get("type"), "wrong type in json");
        assertFalse( obj.has("pieces") );
    }

    public void testToJsonString_someChildren() {
        // TODO
    }

}