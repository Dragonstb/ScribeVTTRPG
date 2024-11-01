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

import dev.dragonstb.scribevttrpg.game.participant.DefaultParticipant;
import dev.dragonstb.scribevttrpg.game.participant.Participant;
import dev.dragonstb.scribevttrpg.game.participant.ParticipantRole;
import dev.dragonstb.scribevttrpg.utils.Constants;
import dev.dragonstb.scribevttrpg.utils.Utils;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.mockito.Mockito.when;

/**
 *
 * @author Dragonstb
 */
@WebMvcTest(GameController.class)
public class GameControllerTest {

    private final Locale EN = Locale.ENGLISH;
    private final String roomName = "swimmingpool";

    @MockBean
    private GameUtils gameUtils;

    @MockBean
    private MessageSource messageSource;

    @MockBean
    private GameService game;

    @Autowired
    private MockMvc mockMvc;

    Map<String, Participant> participations = new HashMap<>();

    @BeforeEach
    public void setUp() {
        participations.clear();
    }

    @Test
    public void testGetGamePage_not_participating() throws Exception {
        String expect = Utils.getJoinPath( roomName );

        RequestBuilder request = get( "/game/"+roomName )
                .locale(EN)
                .sessionAttr( Constants.KEY_PARTICIPATIONS, participations );

        MvcResult response = mockMvc.perform( request )
                .andExpect( status().is(302) )
                .andExpect( redirectedUrl(expect) )
                .andReturn();

    }

    @Test
    public void testGetGamePage_is_participating() throws Exception {
        Participant part = DefaultParticipant.create( "Vera", ParticipantRole.gm );
        participations.put( roomName, part );

        when( gameUtils.getGameUserIsParticipatingIn( participations, roomName ) ).thenReturn(  Optional.of(game) );

        RequestBuilder request = get( "/game/"+roomName )
                .locale(EN)
                .sessionAttr( Constants.KEY_PARTICIPATIONS, participations );

        MvcResult response = mockMvc.perform( request )
                .andExpect( status().isOk() )
                .andExpect( header().string( "content-type", "text/html;charset=UTF-8") )
                .andReturn();
    }

//    @Test
    public void testGetJoinPage() {
        // TODO: content
    }

}
