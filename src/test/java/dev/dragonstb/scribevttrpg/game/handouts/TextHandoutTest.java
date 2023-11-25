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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Dragonstb
 */
public class TextHandoutTest {

    private TextHandout handout;
    private static final String BASE_TEXT = "words";
    private static final String BASE_ID = "ho";
    private String text;
    private String id;
    private int counter = 0;

    public TextHandoutTest() {
    }

    @BeforeEach
    public void setUp() {
        ++counter;
        text = BASE_TEXT + counter;
        id = BASE_ID + counter;
        handout = TextHandout.create( text, id );
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testCreate() {
        assertEquals( id, handout.getId() );
        assertEquals( HandoutType.text, handout.getType() );
        assertEquals( text, handout.getText() );
    }

    @Test
    public void testToJsonString() {
        String string = handout.toJsonString();
        JSONObject json = new JSONObject(string);

        assertTrue( json.has("id") );
        assertEquals( id, json.get("id") );

        assertTrue( json.has("type") );
        assertEquals( handout.getType().toString(), json.get("type") );

        assertTrue( json.has("text") );
        assertEquals( text, json.get("text") );

        assertEquals( 3, json.keySet().size() );
    }

    @Test
    public void testToJsonObject() {
        JSONObject json = handout.toJsonObject();

        assertTrue( json.has("id") );
        assertEquals( id, json.get("id") );

        assertTrue( json.has("type") );
        assertEquals( handout.getType().toString(), json.get("type") );

        assertTrue( json.has("text") );
        assertEquals( text, json.get("text") );

        assertEquals( 3, json.keySet().size() );
    }

}
