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

package dev.dragonstb.scribevttrpg.game.handouts;

import dev.dragonstb.scribevttrpg.game.ParticipantRole;
import java.util.ArrayList;
import java.util.List;
import org.springframework.lang.NonNull;

/**
 *
 * @author Dragonstb
 * @since 0.0.4;
 */
public class DefaultHandoutManager implements HandoutManager {

    /** List of all handouts. This is the list the gm acts on. */
    private final List<ContainerHandout> allHandouts = new ArrayList<>();
    /** The list of handouts the spectators can sse. Entries of this list are a subset of the entries in
     * {@code allHandouts}. */
    private final List<ContainerHandout> spectatorHandouts = new ArrayList<>();
    /** For each player, a list of handouts the player can see. Each entry in any of these lists is also an entry in
     * {@code allHandouts}. */
    private final List<List<ContainerHandout>> playerLists = new ArrayList<>();

    @Override
    public List<ContainerHandout> getListForRole(ParticipantRole role) {
        List<ContainerHandout> list = switch( role ) {
            case gm -> {
                yield allHandouts;
            }
            case player -> {
                List<ContainerHandout> newList = new ArrayList<>();
                playerLists.add( newList );
                yield newList;
            }
            case spectator -> {
                yield spectatorHandouts;
            }
        };

        return list;
    }

    /** The list of all handouts.
     * @author Dragonstb
     * @since 0.0.4;
     * @return The list of all handouts.
     */
    @NonNull
    List<ContainerHandout> getAllHandouts() {
        // TODO: do we need this method outside of unit tests?
        return allHandouts;
    }

    /** The list of handouts the spectators can read.
     * @author Dragonstb
     * @since 0.0.4;
     * @return The list of handouts the spectators can read.
     */
    @NonNull
    List<ContainerHandout> getSpectatorHandouts() {
        // TODO: do we need this method outside of unit tests?
        return spectatorHandouts;
    }

    /** The list of lists of handouts for players. Each player has his/her own list.
     * @author Dragonstb
     * @since 0.0.4;
     * @return The list of lists of handouts for players. Each player has his/her own list.
     */
    @NonNull
    List<List<ContainerHandout>> getPlayerLists() {
        // TODO: do we need this method outside of unit tests?
        return playerLists;
    }

}
