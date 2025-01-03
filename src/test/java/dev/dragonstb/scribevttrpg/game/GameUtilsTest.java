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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import dev.dragonstb.scribevttrpg.GameManager;
import dev.dragonstb.scribevttrpg.game.exceptions.GameNotFoundException;
import dev.dragonstb.scribevttrpg.game.exceptions.NotInGameException;

/**
 *
 * @author Dragonstb
 */
@ContextConfiguration(classes = GameUtils.class)
@ExtendWith(SpringExtension.class)
public class GameUtilsTest {

    @Autowired
    private GameUtils utils;

    @MockBean
    private GameManager gameManager;

    @MockBean
    private GameService game;

    private Participant participant = DefaultParticipant.create( "Robert", ParticipantRole.gm );
    private final Map<String, Participant> participations = new HashMap<>();
    private final String roomName = "very nice kitchen";

    public GameUtilsTest() {
    }

    @BeforeEach
    public void setUp() {
        participations.clear();
    }

    @Test
    public void testGetUserParticipationStatus_not_in_users_participations() {
        when( gameManager.getGame(roomName) ).thenReturn( Optional.of(game) );

        ParticipationStatus status = utils.getUserParticipationStatus( participations, roomName );
        assertEquals( ParticipationStatus.none, status );
    }

    @Test
    public void testGetUserParticipationStatus_participation_is_null() {
        participations.put( roomName, null );
        when( gameManager.getGame(roomName) ).thenReturn( Optional.of(game) );

        ParticipationStatus status = utils.getUserParticipationStatus( participations, roomName );
        assertEquals( ParticipationStatus.none, status );
        assertFalse( participations.containsKey(roomName) );
    }

    @Test
    public void testGetUserParticipationStatus_unkown_roomname() {
        when( gameManager.getGame(roomName) ).thenReturn( Optional.empty() );

        GameNotFoundException exc = assertThrows( GameNotFoundException.class,
                () -> utils.getUserParticipationStatus(participations, roomName) );
        assertEquals( roomName, exc.getRoomName() );
    }

    @Test
    public void testGetUserParticipationStatus_not_listed_in_game() {
        participations.put( roomName, participant );
        when( gameManager.getGame(roomName) ).thenReturn( Optional.of(game) );
        when( game.getParticipationStatus(participant) ).thenReturn( ParticipationStatus.none );

        NotInGameException exc = assertThrows( NotInGameException.class,
                () -> utils.getUserParticipationStatus(participations, roomName) );
        assertEquals( roomName, exc.getRoomName() );
        assertFalse( participations.containsKey(roomName) );
    }

    @Test
    public void testGetUserParticipationStatus_waiting() {
        participant = DefaultParticipant.create( "Frederic", ParticipantRole.prospect );
        participations.put( roomName, participant );
        when( gameManager.getGame(roomName) ).thenReturn( Optional.of(game) );
        when( game.getParticipationStatus(participant) ).thenReturn( ParticipationStatus.waiting );

        ParticipationStatus status = utils.getUserParticipationStatus( participations, roomName );
        assertEquals( ParticipationStatus.waiting, status );
    }

    @Test
    public void testGetUserParticipationStatus_ok() {
        participations.put( roomName, participant );
        when( gameManager.getGame(roomName) ).thenReturn( Optional.of(game) );
        when( game.getParticipationStatus(participant) ).thenReturn( ParticipationStatus.participating );

        ParticipationStatus status = utils.getUserParticipationStatus( participations, roomName );
        assertEquals( ParticipationStatus.participating, status );
    }

}
