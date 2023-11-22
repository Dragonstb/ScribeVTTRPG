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

import dev.dragonstb.scribevttrpg.game.handouts.ContainerHandout;
import dev.dragonstb.scribevttrpg.game.handouts.HandoutManager;
import java.util.ArrayList;
import java.util.List;
import org.springframework.lang.NonNull;

/**
 *
 * @author Dragonstb
 * @since 0.0.4;
 */
public class Game implements GameService {

    /** The name of the room the session takes place. This is also part of the path of the URL. */
    @NonNull
    private final String roomName;

    /** List of participants. */
    @NonNull
    private final List<Participant> participant = new ArrayList<>(); // TODO: will surely turn to be a map identifier -> participant

    /** Handout manager. */
    @NonNull
    private final HandoutManager handoutManager;

    /** Generates.
     * @author Dragonstb
     * @since 0.0.4;
     * @param roomName Name of the room for the game.
     * @param manager A handout manager the game can use.
     */
    private Game(@NonNull String roomName, @NonNull HandoutManager manager) {
        this.roomName = roomName;
        this.handoutManager = manager;
    }

    /** Creates a new instance.
     * @author Dragonstb
     * @since 0.0.4;
     * @param roomName Name of the room for the game.
     * @param manager A handout manager the game can use.
     * @return A new game.
     */
    public static Game create(@NonNull String roomName, @NonNull HandoutManager manager) {
        Game game = new Game(roomName, manager);
        return game;
    }

    @Override
    public Participant addParticipant(@NonNull ParticipantRole role) {
        List<ContainerHandout> handouts = handoutManager.getListForRole(role);
        DefaultParticipant part = DefaultParticipant.create(role, handouts);
        participant.add( part );
        return part;
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
    List<Participant> getParticipant() {
        // TODO: Do we really need this outside of unit tests?
        return participant;
    }

    /** The handout manager used by this game
     * @author Dragonstb
     * @return The handout manager used by this game
     */
    @NonNull
    HandoutManager getHandoutManager() {
        // TODO: Do we really need this outside of unit tests?
        return handoutManager;
    }


}
