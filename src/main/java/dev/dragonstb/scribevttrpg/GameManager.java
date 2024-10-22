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

package dev.dragonstb.scribevttrpg;

import dev.dragonstb.scribevttrpg.game.Game;
import dev.dragonstb.scribevttrpg.game.handouts.DefaultHandoutManager;
import dev.dragonstb.scribevttrpg.game.handouts.HandoutManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/** Manages the games and their existence.
 *
 * @author Dragonstb
 * @since 0.0.4;
 */
@Component
public class GameManager {

    @Autowired
    private SettingsConfig settings;

    /** Lists all ongoing games. The keys are the room names of the games. */
    @NonNull
    private final Map<String, Game> games = new HashMap<>();
    ApplicationContext appCtx;

    // TODO: delete stale games somehow

    /** Creates and adds the new game with a room of the given name. If the name is in use, the process fails.
     * @author Dragonstb
     * @since 0.0.4;
     * @param roomName The favourite room name.
     * @return A new instance.
     * @throws IllegalArgumentException Room name does not match requirements.
     * @throws RuntimeException Room name has been taken by someone else.
     */
    public Game createGame(String roomName) throws IllegalArgumentException, RuntimeException {
        // TODO: synchronize, as two GMs may want to open two rooms with the same name at the same time
        if( roomName == null || roomName.length() < settings.getMinRoomNameLength() ) {
            throw new IllegalArgumentException( "name of the room must have at least "
                    +settings.getMinRoomNameLength()+" characters." );
        }

        if( games.containsKey(roomName) ) {
            throw new RuntimeException( "This name is already taken." );
        }

        HandoutManager handoutManager = new DefaultHandoutManager();
        Game game = Game.create(roomName, handoutManager);
        games.put(roomName, game);

        return game;
    }

    /** Gets the game associated to the given room name.
     * @author Dragonstb
     * @param roomName Name of the room the game takes place in.
     * @return If existing, an optional containing the game associated with the room name. An empty optional otherwise.
     */
    public Optional<Game> getGame(@NonNull String roomName) {
        Optional<Game> opt = Optional.ofNullable( games.get(roomName) );
        return opt;
    }

}
