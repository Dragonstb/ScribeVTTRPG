/*
 * Copyright (c) 2024, Dragonstb
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
package dev.dragonstb.scribevttrpg.game;

import dev.dragonstb.scribevttrpg.utils.Constants;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 *
 * @author Dragonstb
 * @since 0.0.6
 */
public class GameRestControllerTest {

    @Test
    public void testGetParticipationsAndCreateIfNeeded_attribute_set() {
        HttpSession session = new MockHttpSession();
        Map<String, Participant> map = new HashMap();

        Map result = GameRestController.getParticipationsAndCreateIfNeeded( session );
        assertNotNull( result );
        assertEquals( map, result );
    }

    @Test
    public void testGetParticipationsAndCreateIfNeeded_attribute_empty() {
        HttpSession session = new MockHttpSession();

        assertNull( session.getAttribute( Constants.KEY_PARTICIPATIONS) );
        Map result = GameRestController.getParticipationsAndCreateIfNeeded( session );
        assertNotNull( result );
    }

    @Test
    public void testSetErrorAndGetResponse() {
        HttpServletResponse response = new MockHttpServletResponse();
        String msg = "Hello bad request";
        int status = 444;

        String result = GameRestController.setErrorAndGetResponse( response, msg, status );
        assertNotNull( result );
        JSONObject json = new JSONObject( result );
        assertTrue( json.has( "Error" ) );
        assertEquals( msg, json.getString("Error") );
        assertEquals( 1, json.length() );
    }

}
