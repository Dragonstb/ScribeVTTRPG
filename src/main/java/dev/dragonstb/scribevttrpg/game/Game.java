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
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.NonNull;

/**
 *
 * @author Dragonstb
 * @since 0.0.4;
 */
public final class Game implements GameService {

    /** The application context of the game. */
    private final ApplicationContext appContext;

    /** The name of the room the session takes place. This is also part of the path of the URL. */
    @NonNull
    private final String roomName;

    /** List of participants. */
    @NonNull
    private final List<Participant> participants = new ArrayList<>(); // TODO: will surely turn to be a map identifier -> participant

    /** Handout manager. */
    @NonNull
    private final HandoutManager handoutManager;

    /** Generates.
     * @author Dragonstb
     * @since 0.0.4;
     * @param roomName Name of the room for the game.
     * @param manager A handout manager the game can use.
     */
    private Game( @NonNull String roomName ) {
        appContext = new AnnotationConfigApplicationContext( GameAppContextConfig.class );
        this.roomName = roomName;
        this.handoutManager = appContext.getBean( DefaultHandoutManager.class );
    }

    /** Creates a new instance.
     * @author Dragonstb
     * @since 0.0.4;
     * @param roomName Name of the room for the game.
     * @return A new game.
     */
    public static Game create( @NonNull String roomName ) {
        // TODO: move check for valid name from GameManager to here
        Game game = new Game(roomName);
        return game;
    }

    @Override
    public Participant createAndAddParticipant(@NonNull ParticipantRole role) {
        // TODO: check if game is "open", throw exception if not
        Participant part = DefaultParticipant.create(role);
        synchronized ( participants ) {
            // TODO: check if identity is free, throw exception if not
            participants.add( part );
        }
        return part;
    }

    @Override
    public HandoutManager getHandoutManager() {
        return handoutManager;
    }

    /** The name of the room this game takes place in.
     * @author Dragonstb
     * @return The name of the room this game takes place in.
     */
    @NonNull
    String getRoomName() {
        // TODO: Do we really need this outside of unit tests?
        return roomName;
    }

    /** The list of participants.<br>
     * <b>Caution:</b> It is the list itself, which can be manipulated.
     * @author Dragonstb
     * @return The list of participants.
     */
    @NonNull
    List<Participant> getParticipants() {
        // TODO: Do we really need this outside of unit tests?
        return participants;
    }

    @Override
    public boolean isParticipating( Participant participant ) {
        return participants.contains( participant );
    }


}
