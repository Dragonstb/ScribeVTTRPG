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

import dev.dragonstb.scribevttrpg.game.participant.ParticipantRole;
import dev.dragonstb.scribevttrpg.game.participant.DefaultParticipant;
import dev.dragonstb.scribevttrpg.game.participant.Participant;
import dev.dragonstb.scribevttrpg.GameManager;
import dev.dragonstb.scribevttrpg.game.exceptions.IdentityNotUniqueException;
import dev.dragonstb.scribevttrpg.game.handouts.ContainerHandout;
import dev.dragonstb.scribevttrpg.game.handouts.HandoutManager;
import dev.dragonstb.scribevttrpg.utils.Constants;
import dev.dragonstb.scribevttrpg.utils.LocKeys;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.json.JSONArray;
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
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

/**
 *
 * @author Dragonstb
 * @since 0.0.6
 */
@WebMvcTest(GameRestController.class)
public class GameRestControllerTest {

    private static final Locale EN = Locale.ENGLISH;
    private static final String ROOM_NAME = "myroom";
    private static final String CAMPAIGN_NAME = "mycastle";
    private static final String USER_NAME = "Lenny";
    private JSONObject jsonBody;

    @MockBean
    private GameManager gameManager;

    @MockBean
    private HandoutManager handoutManager;

    @MockBean
    private GameService game;

    @MockBean
    private GameUtils gameUtils;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MessageSource messageSource;

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

    // _______________________  create game  _______________________

    @Test
    public void testCreateGame_ok() throws Exception {
        when( gameManager.createGame( ROOM_NAME ) ).thenReturn( game );
        when( game.getHandoutManager() ).thenReturn( handoutManager );
        when( game.createAndAddParticipant(any(), any()) )
                .thenReturn( DefaultParticipant.create("Ramirez", ParticipantRole.gm) );

        RequestBuilder request = post( "/creategame" )
                .content( jsonBody.toString() )
                .contentType("application/json")
                .locale(EN);

        MvcResult response = mockMvc.perform( request )
                .andExpect( status().isCreated() )
                .andReturn();

        String content = response.getResponse().getContentAsString();
        JSONObject jsonContent;
        try {
            jsonContent = new JSONObject( content );
        } catch ( Exception e ) {
            fail( "response is not jsonic");
            return;
        }

        JSONObject expect = new JSONObject();
        expect.put( "success", true );
        expect.put( "room", ROOM_NAME );
        assertTrue( jsonContent.similar(expect), "jsons are not similar: \"jsonRes"+jsonContent.toString()+"\" vs \""+ expect.toString()+"\"");
    }

    @Test
    public void testCreateGame_invalidName() throws Exception{
        when( gameManager.createGame( ROOM_NAME ) ).thenThrow( IllegalArgumentException.class );

        RequestBuilder request = post( "/creategame" )
                .content( jsonBody.toString() )
                .contentType("application/json")
                .locale(EN);

        MvcResult response = mockMvc.perform( request )
                .andExpect( status().is(400) )
                .andReturn();

        String content = response.getResponse().getContentAsString();
        JSONObject jsonContent;
        try {
            jsonContent = new JSONObject( content );
        } catch ( Exception e ) {
            fail( "response is not jsonic");
            return;
        }

        JSONObject expect = new JSONObject();
        expect.put( "success", false );
        expect.put( "message", messageSource.getMessage( LocKeys.CREATE_ROOM_NAME_INVALID, null, EN) );
        assertTrue( jsonContent.similar(expect), "jsons are not similar: \"jsonRes"+jsonContent.toString()+"\" vs \""+ expect.toString()+"\"");

    }

    @Test
    public void testCreateGame_occupiedName() throws Exception{
        when( gameManager.createGame(ROOM_NAME) ).thenThrow( RuntimeException.class );

        RequestBuilder request = post( "/creategame" )
                .content( jsonBody.toString() )
                .contentType("application/json")
                .locale(EN);

        MvcResult response = mockMvc.perform( request )
                .andExpect( status().is(400) )
                .andReturn();

        String content = response.getResponse().getContentAsString();
        JSONObject jsonContent;
        try {
            jsonContent = new JSONObject( content );
        } catch ( Exception e ) {
            fail( "response is not jsonic");
            return;
        }

        JSONObject expect = new JSONObject();
        expect.put( "success", false );
        expect.put( "message", messageSource.getMessage( LocKeys.CREATE_ROOM_NAME_TAKEN, null, EN) );
        assertTrue( jsonContent.similar(expect), "jsons are not similar: \"jsonRes"+jsonContent.toString()+"\" vs \""+ expect.toString()+"\"");

    }

    // ____________________________  request join process  ____________________________

    @Test
    public void testStartJoinProcess_ok() throws Exception {
        jsonBody = new JSONObject();
        jsonBody.put( "roomName", ROOM_NAME );
        jsonBody.put( "name", USER_NAME );
        Participant participant = DefaultParticipant.create( USER_NAME, ParticipantRole.prospect );
        Map<String, Participant> participations = new HashMap<>();

        when( gameManager.getGame(ROOM_NAME) ).thenReturn( Optional.of(game) );
        when( game.createAndAddParticipant(USER_NAME, ParticipantRole.prospect) ).thenReturn( participant );

        RequestBuilder request = post( "/joingame" )
                .content( jsonBody.toString() )
                .contentType("application/json")
                .sessionAttr( Constants.KEY_PARTICIPATIONS, participations )
                .locale(EN);

        MvcResult response = mockMvc.perform( request )
                .andExpect( status().isCreated())
                .andExpect( header().string("content-type", "application/json") )
                .andReturn();

        String content = response.getResponse().getContentAsString();
        JSONObject jsonContent;
        try {
            jsonContent = new JSONObject( content );
        } catch ( Exception e ) {
            fail( "response is not jsonic");
            return;
        }

        JSONObject expect = new JSONObject();
        expect.put( "accepted", true );
        expect.put( "room", ROOM_NAME );
        assertTrue( jsonContent.similar(expect), "jsons are not similar: \"jsonRes"+jsonContent.toString()+"\" vs \""+ expect.toString()+"\"");

        assertTrue( participations.containsKey(ROOM_NAME), "room name is not a key in participations" );
        assertEquals( participant, participations.get(ROOM_NAME), "participant has not been added" );
    }

    @Test
    public void testStartJoinProcess_no_such_room() throws Exception {
        jsonBody = new JSONObject();
        jsonBody.put( "roomName", ROOM_NAME );
        jsonBody.put( "name", USER_NAME );
        Map<String, Participant> participations = new HashMap<>();

        when( gameManager.getGame(ROOM_NAME) ).thenReturn( Optional.empty() );

        RequestBuilder request = post( "/joingame" )
                .content( jsonBody.toString() )
                .contentType("application/json")
                .sessionAttr( Constants.KEY_PARTICIPATIONS, participations )
                .locale(EN);

        MvcResult response = mockMvc.perform( request )
                .andExpect( status().isNotFound() )
                .andExpect( header().string("content-type", "application/json") )
                .andReturn();

        String content = response.getResponse().getContentAsString();
        JSONObject jsonContent;
        try {
            jsonContent = new JSONObject( content );
        } catch ( Exception e ) {
            fail( "response is not jsonic");
            return;
        }

        JSONObject expect = new JSONObject();
        expect.put( "accepted", false );
        expect.put( "message", "room not found." ); // HINT: value of "message" yet not finalized, is subject to change
        assertTrue( jsonContent.similar(expect), "jsons are not similar: \"jsonRes"+jsonContent.toString()+"\" vs \""+ expect.toString()+"\"");
    }

    @Test
    public void testStartJoinProcess_identity_occupied() throws Exception {
        jsonBody = new JSONObject();
        jsonBody.put( "roomName", ROOM_NAME );
        jsonBody.put( "name", USER_NAME );
        Map<String, Participant> participations = new HashMap<>();

        when( gameManager.getGame(ROOM_NAME) ).thenReturn( Optional.of(game) );
        when( game.createAndAddParticipant(USER_NAME, ParticipantRole.prospect) )
                .thenThrow( IdentityNotUniqueException.class );
        String key = LocKeys.JOIN_NAME_OCCUPIED;
        String msg = messageSource.getMessage( key, null, "<"+key+">", EN);

        RequestBuilder request = post( "/joingame" )
                .content( jsonBody.toString() )
                .contentType("application/json")
                .sessionAttr( Constants.KEY_PARTICIPATIONS, participations )
                .locale(EN);

        MvcResult response = mockMvc.perform( request )
                .andExpect( status().isOk() )
                .andExpect( header().string("content-type", "application/json") )
                .andReturn();

        String content = response.getResponse().getContentAsString();
        JSONObject jsonContent;
        try {
            jsonContent = new JSONObject( content );
        } catch ( Exception e ) {
            fail( "response is not jsonic");
            return;
        }

        JSONObject expect = new JSONObject();
        expect.put( "accepted", false );
        expect.put( "message", msg );
        assertTrue( jsonContent.similar(expect), "jsons are not similar: \"jsonRes"+jsonContent.toString()+"\" vs \""+ expect.toString()+"\"");

        assertFalse( participations.containsKey(ROOM_NAME), "room name is a key" );
    }

    @Test
    public void testStartJoinProcess_already_waiting() throws Exception {
        jsonBody = new JSONObject();
        jsonBody.put( "roomName", ROOM_NAME );
        jsonBody.put( "name", USER_NAME );
        Participant participant = DefaultParticipant.create( USER_NAME, ParticipantRole.prospect );
        Map<String, Participant> participations = new HashMap<>();
        participations.put( ROOM_NAME, participant );

        when( gameManager.getGame(ROOM_NAME) ).thenReturn( Optional.of(game) );
        when( game.hasJoinedAlready(participant) ).thenReturn( false );
        when( game.isRelated(participant) ).thenReturn( true );

        RequestBuilder request = post( "/joingame" )
                .content( jsonBody.toString() )
                .contentType("application/json")
                .sessionAttr( Constants.KEY_PARTICIPATIONS, participations )
                .locale(EN);

        MvcResult response = mockMvc.perform( request )
                .andExpect( status().isOk() )
                .andExpect( header().string("content-type", "application/json") )
                .andReturn();

        String content = response.getResponse().getContentAsString();
        JSONObject jsonContent;
        try {
            jsonContent = new JSONObject( content );
        } catch ( Exception e ) {
            fail( "response is not jsonic");
            return;
        }

        // HINT: response behavior is subject to change: is going to switch to redirecting the user
        JSONObject expect = new JSONObject();
        expect.put( "accepted", false );
        expect.put( "message", "You are already waiting here." );
        assertTrue( jsonContent.similar(expect), "jsons are not similar: \"jsonRes"+jsonContent.toString()+"\" vs \""+ expect.toString()+"\"");
    }

    @Test
    public void testStartJoinProcess_already_participating() throws Exception {
        jsonBody = new JSONObject();
        jsonBody.put( "roomName", ROOM_NAME );
        jsonBody.put( "name", USER_NAME );
        Participant participant = DefaultParticipant.create( USER_NAME, ParticipantRole.prospect );
        Map<String, Participant> participations = new HashMap<>();
        participations.put( ROOM_NAME, participant );

        when( gameManager.getGame(ROOM_NAME) ).thenReturn( Optional.of(game) );
        when( game.hasJoinedAlready(participant) ).thenReturn( true );
        when( game.isRelated(participant) ).thenReturn( true );

        RequestBuilder request = post( "/joingame" )
                .content( jsonBody.toString() )
                .contentType("application/json")
                .sessionAttr( Constants.KEY_PARTICIPATIONS, participations )
                .locale(EN);

        MvcResult response = mockMvc.perform( request )
                .andExpect( status().isOk() )
                .andExpect( header().string("content-type", "application/json") )
                .andReturn();

        String content = response.getResponse().getContentAsString();
        JSONObject jsonContent;
        try {
            jsonContent = new JSONObject( content );
        } catch ( Exception e ) {
            fail( "response is not jsonic");
            return;
        }

        // HINT: response behavior is subject to change: is going to switch to redirecting the user
        JSONObject expect = new JSONObject();
        expect.put( "accepted", false );
        expect.put( "message", "You are already participating in this room" );
        assertTrue( jsonContent.similar(expect), "jsons are not similar: \"jsonRes"+jsonContent.toString()+"\" vs \""+ expect.toString()+"\"");
    }

    @Test
    public void testStartJoinProcess_inconsisten_state() throws Exception {
        jsonBody = new JSONObject();
        jsonBody.put( "roomName", ROOM_NAME );
        jsonBody.put( "name", USER_NAME );
        Participant participant = DefaultParticipant.create( USER_NAME, ParticipantRole.prospect );
        Map<String, Participant> participations = new HashMap<>();
        participations.put( ROOM_NAME, participant );

        when( gameManager.getGame(ROOM_NAME) ).thenReturn( Optional.of(game) );
        when( game.hasJoinedAlready(participant) ).thenReturn( false );
        when( game.isRelated(participant) ).thenReturn( false );

        RequestBuilder request = post( "/joingame" )
                .content( jsonBody.toString() )
                .contentType("application/json")
                .sessionAttr( Constants.KEY_PARTICIPATIONS, participations )
                .locale(EN);

        MvcResult response = mockMvc.perform( request )
                .andExpect( status().isOk() )
                .andExpect( header().string("content-type", "application/json") )
                .andReturn();

        String content = response.getResponse().getContentAsString();
        JSONObject jsonContent;
        try {
            jsonContent = new JSONObject( content );
        } catch ( Exception e ) {
            fail( "response is not jsonic");
            return;
        }

        // HINT: response behavior is subject to change: is going to fix the problem and add the user
        JSONObject expect = new JSONObject();
        expect.put( "accepted", false );
        expect.put( "message", "A problem occured, but we cleaned it up. Please simply try again." );
        assertTrue( jsonContent.similar(expect), "jsons are not similar: \"jsonRes"+jsonContent.toString()+"\" vs \""+ expect.toString()+"\"");

        assertFalse( participations.containsKey(ROOM_NAME), "room name is still a key" );
    }



    // ____________________________  get materials  ____________________________

    @Test
    public void testGetMaterials_ok() throws Exception {
        List<ContainerHandout> handouts = new ArrayList<>();
        Map<String, Participant> participations = new HashMap<>();
        participations.put( ROOM_NAME, DefaultParticipant.create("Randy", ParticipantRole.gm) );

        when( gameUtils.getGameUserIsParticipatingIn( participations, ROOM_NAME ) ).thenReturn( Optional.of(game) );
        when( game.getHandoutManager() ).thenReturn( handoutManager );
        when( game.hasJoinedAlready(any()) ).thenReturn( true );
        when( handoutManager.getHandouts() ).thenReturn( handouts );


        RequestBuilder request = get( "/materials/"+ROOM_NAME )
                .content( jsonBody.toString() )
                .contentType("application/json")
                .locale(EN)
                .sessionAttr( Constants.KEY_PARTICIPATIONS, participations );

        MvcResult response = mockMvc.perform( request )
                .andExpect( status().is(200) )
                .andReturn();

        String content = response.getResponse().getContentAsString();
        JSONArray jsonContent;
        try {
            jsonContent = new JSONArray( content );
        } catch ( Exception e ) {
            fail( "response is not jsonic: "+content);
            return;
        }

        JSONArray expect = new JSONArray("[]");
        assertTrue( jsonContent.similar(expect), "jsons are not similar: \"jsonRes"+jsonContent.toString()+"\" vs \""+ expect.toString()+"\"");

    }

    @Test
    public void testGetMaterials_notParticipating() throws Exception {
        Map<String, Participant> participations = new HashMap<>();

        when( gameUtils.getGameUserIsParticipatingIn( any(), any() ) ).thenReturn( Optional.empty() );

        RequestBuilder request = get( "/materials/"+ROOM_NAME )
                .content( jsonBody.toString() )
                .contentType("application/json")
                .locale(EN)
                .sessionAttr( Constants.KEY_PARTICIPATIONS, participations );

        mockMvc.perform( request )
                .andExpect( status().is(400) );
    }

    @Test
    public void testGetMaterials_inconsistentParticipationRegistrations() throws Exception {
        Map<String, Participant> participations = new HashMap<>();

        when( gameUtils.getGameUserIsParticipatingIn( any(), any() ) ).thenThrow( RuntimeException.class );

        RequestBuilder request = get( "/materials/"+ROOM_NAME )
                .content( jsonBody.toString() )
                .contentType("application/json")
                .locale(EN)
                .sessionAttr( Constants.KEY_PARTICIPATIONS, participations );

        mockMvc.perform( request )
                .andExpect( status().is(400) );
    }

}
