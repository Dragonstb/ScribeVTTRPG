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

import java.util.Optional;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Dragonstb
 * @since 0.0.5;
 */
public class ContentItemTest {

    private ContentItem item;
    private final String BASE_NAME = "Sally_";
    private String name;
    private final String BASE_DESC = "A default implementation ";
    private String desc;
    private int counter = 0;
    private final ContentType type = ContentType.handout;

    public ContentItemTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
        ++counter;
        name = BASE_NAME + String.valueOf( counter );
        desc = BASE_DESC + String.valueOf( desc );
        item = makeItem( type, name, desc );
    }

    @Test
    public void testGetMinimalJson() {
        JSONObject obj = new JSONObject();
        obj.put( "name", name );
        obj.put( "desc", desc );
        obj.put( "type", type.toString() );

        JSONObject json = item.getMinimalJson();

        assertEquals( obj.keySet().size(), json.keySet().size(), "wrong number of keys" );

        for ( String key : obj.keySet()) {
            assertTrue( json.has( key), "missing key: "+key );
            assertEquals( obj.get( key ), json.get( key ), "differing value for key: "+key );
        }
    }

    @Test
    public void testGetType() {
        assertEquals( type, item.getType() );
    }

    @Test
    public void testGetName() {
        assertEquals( name, item.getName() );
    }

    @Test
    public void testSetName_allOk() {
        String newName = "Miguel_"+counter;
        item.setName( newName );
        assertEquals( newName, item.getName() );
    }

    @Test
    public void testSetName_nullArg() {
        String newName = null;
        assertThrows( IllegalArgumentException.class, () -> item.setName( newName ) );
    }

    @Test
    public void testGetDescription_desc_exists() {
        Optional<String> opt = item.getDescription();
        assertTrue( opt.isPresent() );
        assertEquals( desc, opt.get() );
    }

    @Test
    public void testGetDescription_desc_not_present() {
        item = makeItem( type, name, null );
        Optional<String> opt = item.getDescription();
        assertTrue( opt.isEmpty() );
    }

    // ----------------------------------------------------------------------------------------

    private ContentItem makeItem(ContentType type, String name, String desc) {
        return new ContentItem( type, name, desc ) {
            @Override
            public JSONObject toJson() {
                return super.getMinimalJson();
            }
        };
    }
}
