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
package dev.dragonstb.scribevttrpg;

import dev.dragonstb.scribevttrpg.game.GameService;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author Dragonstb
 */
@ContextConfiguration
@ExtendWith(SpringExtension.class)
public class DefaultGameManagerTest {

    private DefaultGameManager manager;

    @MockBean
    private SettingsConfig settings;

    @BeforeEach
    public void setUp() {
        ApplicationContext appCon = new AnnotationConfigApplicationContext(Config.class);
        manager = appCon.getBean( DefaultGameManager.class );
    }

//    @Test
    public void testCreateGame() {
        // TODO: content
    }

    @Test
    public void testGetGame_existing_game() {
        String roomName = "abcdefghijkl";
        when( settings.getMinRoomNameLength() ).thenReturn( 2/*roomName.length()-1*/ );
        System.out.println( ">>>>>>> "+settings.getMinRoomNameLength() );
        GameService game = manager.createGame( roomName );
        assertNotNull( game, "Game is null" );

        Optional<GameService> opt = manager.getGame( roomName );
        assertNotNull( opt, "Optional is null" );
        assertTrue( opt.isPresent(), "Optional is empty" );
        assertEquals( game, opt.get() );
    }

    @Test
    public void testGetGame_nonexisting_game() {
        String roomName = "abcdefghijkl";
        GameService game = manager.createGame( roomName );
        assertNotNull( game, "Game is null" );

        Optional<GameService> opt = manager.getGame( roomName+roomName );
        assertNotNull( opt, "Optional is null" );
        assertTrue( opt.isEmpty(), "Optional is present" );
    }

    // ______________________________________________________________________________________________________

    @Configuration
    static class Config {
        @Bean
        SettingsConfig getConfig() {
            return new SettingsConfig();
        }

        @Bean
        DefaultGameManager getManager() {
            return new DefaultGameManager();
        }
    }
}
