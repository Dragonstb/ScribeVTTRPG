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

import dev.dragonstb.scribevttrpg.GameManager;
import dev.dragonstb.scribevttrpg.game.handouts.ContainerHandout;
import dev.dragonstb.scribevttrpg.game.handouts.HandoutManager;
import dev.dragonstb.scribevttrpg.utils.Constants;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * @author Dragonstb
 * @since 0.0.6
 */
@WebMvcTest(GameRestController.class)
public class GameRestControllerTest {

    private static final String ROOM_NAME = "myroom";
    private static final String CAMPAIGN_NAME = "mycastle";
    private JSONObject jsonBody;

    @MockBean
    private GameManager gameManager;

    @MockBean
    private HandoutManager handoutManager;

    @MockBean
    private GameService game;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        jsonBody = new JSONObject();
        jsonBody.put( "roomName", ROOM_NAME );
        jsonBody.put( "campaign", CAMPAIGN_NAME );
    }

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
    public void testCreateGame_ok() throws Exception {
        when( gameManager.createGame( ROOM_NAME ) ).thenReturn( game );
        when( game.getHandoutManager() ).thenReturn( handoutManager );
        when( game.createAndAddParticipant(ParticipantRole.gm) ).thenReturn( DefaultParticipant.create(ParticipantRole.gm) );

        RequestBuilder request = post( "/creategame" )
                .content( jsonBody.toString() )
                .contentType("application/json")
                .locale(Locale.ENGLISH);

        mockMvc.perform( request )
                .andExpect( status().isCreated() );

    }

    @Test
    public void testCreateGame_invalidName() throws Exception{
        when( gameManager.createGame( ROOM_NAME ) ).thenThrow( IllegalArgumentException.class );

        RequestBuilder request = post( "/creategame" )
                .content( jsonBody.toString() )
                .contentType("application/json")
                .locale(Locale.ENGLISH);

        mockMvc.perform( request )
                .andExpect( status().is(400) );
    }

    @Test
    public void testCreateGame_occupiedName() throws Exception{
        when( gameManager.createGame(ROOM_NAME) ).thenThrow( RuntimeException.class );

        RequestBuilder request = post( "/creategame" )
                .content( jsonBody.toString() )
                .contentType("application/json")
                .locale(Locale.ENGLISH);

        mockMvc.perform( request )
                .andExpect( status().is(400) );
    }


    @Test
    public void testGetMaterials_ok() throws Exception {
        List<ContainerHandout> handouts = new ArrayList<>();
        Map<String, Participant> participations = new HashMap<>();
        participations.put( ROOM_NAME, DefaultParticipant.create(ParticipantRole.gm) );

        when( gameManager.getGame(ROOM_NAME) ).thenReturn( Optional.of(game) );
        when( game.getHandoutManager() ).thenReturn( handoutManager );
        when( game.isParticipating(any()) ).thenReturn( true );
        when( handoutManager.getHandouts() ).thenReturn( handouts );


        RequestBuilder request = get( "/materials/"+ROOM_NAME )
                .content( jsonBody.toString() )
                .contentType("application/json")
                .locale(Locale.ENGLISH)
                .sessionAttr( Constants.KEY_PARTICIPATIONS, participations );

        mockMvc.perform( request )
                .andExpect( status().is(200) );
    }

    @Test
    public void testGetMaterials_roomNotInParticipations() throws Exception {
        List<ContainerHandout> handouts = new ArrayList<>();
        Map<String, Participant> participations = new HashMap<>();

        when( gameManager.getGame(ROOM_NAME) ).thenReturn( Optional.of(game) );
        when( game.getHandoutManager() ).thenReturn( handoutManager );
        when( game.isParticipating(any()) ).thenReturn( true );
        when( handoutManager.getHandouts() ).thenReturn( handouts );

        RequestBuilder request = get( "/materials/"+ROOM_NAME )
                .content( jsonBody.toString() )
                .contentType("application/json")
                .locale(Locale.ENGLISH)
                .sessionAttr( Constants.KEY_PARTICIPATIONS, participations );

        mockMvc.perform( request )
                .andExpect( status().is(400) );
    }

    @Test
    public void testGetMaterials_participationForThisRoomIsNull() throws Exception {
        List<ContainerHandout> handouts = new ArrayList<>();
        Map<String, Participant> participations = new HashMap<>();
        participations.put( ROOM_NAME, null );

        when( gameManager.getGame(ROOM_NAME) ).thenReturn( Optional.of(game) );
        when( game.getHandoutManager() ).thenReturn( handoutManager );
        when( game.isParticipating(any()) ).thenReturn( true );
        when( handoutManager.getHandouts() ).thenReturn( handouts );


        RequestBuilder request = get( "/materials/"+ROOM_NAME )
                .content( jsonBody.toString() )
                .contentType("application/json")
                .locale(Locale.ENGLISH)
                .sessionAttr( Constants.KEY_PARTICIPATIONS, participations );

        mockMvc.perform( request )
                .andExpect( status().is(400) );
    }

    @Test
    public void testGetMaterials_noGameUnderThisRoomName() throws Exception {
        List<ContainerHandout> handouts = new ArrayList<>();
        Map<String, Participant> participations = new HashMap<>();
        participations.put( ROOM_NAME, DefaultParticipant.create(ParticipantRole.gm) );

        when( gameManager.getGame(ROOM_NAME) ).thenReturn( Optional.empty() );
        when( game.getHandoutManager() ).thenReturn( handoutManager );
        when( game.isParticipating(any()) ).thenReturn( true );
        when( handoutManager.getHandouts() ).thenReturn( handouts );


        RequestBuilder request = get( "/materials/"+ROOM_NAME )
                .content( jsonBody.toString() )
                .contentType("application/json")
                .locale(Locale.ENGLISH)
                .sessionAttr( Constants.KEY_PARTICIPATIONS, participations );

        mockMvc.perform( request )
                .andExpect( status().is(500) );
    }

    @Test
    public void testGetMaterials_notParticipantInThisGame() throws Exception {
        List<ContainerHandout> handouts = new ArrayList<>();
        Map<String, Participant> participations = new HashMap<>();
        participations.put( ROOM_NAME, DefaultParticipant.create(ParticipantRole.gm) );

        when( gameManager.getGame(ROOM_NAME) ).thenReturn( Optional.of(game) );
        when( game.getHandoutManager() ).thenReturn( handoutManager );
        when( game.isParticipating(any()) ).thenReturn( false );
        when( handoutManager.getHandouts() ).thenReturn( handouts );


        RequestBuilder request = get( "/materials/"+ROOM_NAME )
                .content( jsonBody.toString() )
                .contentType("application/json")
                .locale(Locale.ENGLISH)
                .sessionAttr( Constants.KEY_PARTICIPATIONS, participations );

        mockMvc.perform( request )
                .andExpect( status().is(500) );
    }


}
