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
package dev.dragonstb.scribevttrpg.game;

import dev.dragonstb.scribevttrpg.game.handouts.DefaultHandoutManager;
import dev.dragonstb.scribevttrpg.game.handouts.HandoutManager;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Dragonstb
 * @since 0.0.4;
 */
public class GameTest {

    private static final String ROOM_NAME_BASE = "Bath Room ";
    private HandoutManager manager;
    private String roomName;
    private static int counter = 0;

    @BeforeEach
    public void setUp() {
        roomName = ROOM_NAME_BASE + (++counter);
        manager = new DefaultHandoutManager();
    }

    @Test
    public void testCreate() {
        Game game = Game.create(roomName);

        assertEquals( roomName, game.getRoomName() );
        List<Participant> participants = game.getParticipants();
        assertTrue( participants.isEmpty() );
    }

    @Test
    public void testAddParticipant() {
        String name = "Barbara";
        ParticipantRole role = ParticipantRole.player;
        Game game = Game.create(roomName);

        Participant part = game.createAndAddParticipant( name, role );

        assertEquals( role, part.getRole() );
        assertEquals( name, part.getName() );
        assertTrue( game.getParticipants().contains( part ) );
    }

}
