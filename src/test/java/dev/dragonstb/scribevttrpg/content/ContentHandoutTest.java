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

import dev.dragonstb.scribevttrpg.game.handouts.ContainerHandout;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Dragonstb
 */
public class ContentHandoutTest {

    private ContentHandout item;
    private final String BASE_NAME = "Robert_";
    private String name;
    private final String BASE_ID = "ho-";
    private String id;
    private final String BASE_DESC = "This item is a handout ";
    private String desc;
    private ContainerHandout handout;
    private int counter = 0;

    @BeforeEach
    public void setUp() {
        ++counter;
        name = BASE_NAME + String.valueOf( counter );
        desc = BASE_DESC + String.valueOf( counter );
        id = BASE_ID + String.valueOf( counter );

        handout = ContainerHandout.create( name, id );

        item = new ContentHandout( handout, desc );
    }

    @Test
    public void testToJson_noChildren() {
        JSONObject json = item.toJson();

        String[] keys = new String[]{ "name", "desc", "type" };
        for ( String key : keys ) {
            assertTrue( json.has( key ) );
        }

        assertEquals( name, json.get( "name" ) );
        assertEquals( desc, json.get( "desc" ) );
        assertEquals( ContentType.handout.toString(), json.get( "type" ) );

        assertTrue( json.has( "pieces" ) );
        assertTrue( json.get( "pieces" ) instanceof JSONArray );
        assertTrue( ((JSONArray)json.get( "pieces" )).isEmpty() );

        assertEquals( keys.length + 1, json.keySet().size() );
    }

    @Test
    public void testGetHandout() {
        ContainerHandout ho = item.getHandout();
        assertTrue( ho == handout ); // yes, it should be the very same instance
    }

    @Test
    public void testSetName_ok() {
        String newName = "Virginia_"+counter;
        item.setName( newName );
        assertEquals( newName, item.getName() );
        assertEquals( newName, handout.getLabel() );
    }

    @Test
    public void testSetName_null() {
        String newName = null;
        assertThrows( IllegalArgumentException.class, () -> item.setName( newName ) );
    }

}
