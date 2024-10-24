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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.NonNull;

/**
 *
 * @author Dragonstb
 * @since 0.0.4;
 */
public class DefaultHandoutManager implements HandoutManager {

    /** List of all handouts. This is the list the gm acts on. */
    private final List<ContainerHandout> allHandouts = new ArrayList<>();

    /** List of the handout gards that track the access rights. */
    private final Map<String, HandoutAccessGuard> guards = new HashMap<>();

    /** The list of handouts the spectators can sse. Entries of this list are a subset of the entries in
     * {@code allHandouts}.
     @deprecated since 0.0.6 Access now managed by a list of {@link HandoutAccessGuards}.*/
    @Deprecated
    private final List<ContainerHandout> spectatorHandouts = new ArrayList<>();
    /** For each player, a list of handouts the player can see. Each entry in any of these lists is also an entry in
     * {@code allHandouts}.
     @deprecated since 0.0.6 Access now managed by a list of {@link HandoutAccessGuards}.*/
    @Deprecated
    private final List<List<ContainerHandout>> playerLists = new ArrayList<>();

    @Override
    @Deprecated
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

    /** Gets the map of handout access guards.
     * @since 0.0.6
     * @author Dragonstb
     * @return The map of "id of guarded handout piece to guard"
     */
    @NonNull
    Map<String, HandoutAccessGuard> getGuards() {
        return guards;
    }

    /** The list of handouts the spectators can read.
     * @author Dragonstb
     * @since 0.0.4;
     * @return The list of handouts the spectators can read.
     * @deprecated since 0.0.6 Access to individual handout pieces is now managed by a list of {@link HandoutAccessGuards}.
     */
    @NonNull
    @Deprecated
    List<ContainerHandout> getSpectatorHandouts() {
        // TODO: do we need this method outside of unit tests?
        return spectatorHandouts;
    }

    /** The list of lists of handouts for players. Each player has his/her own list.
     * @author Dragonstb
     * @since 0.0.4;
     * @return The list of lists of handouts for players. Each player has his/her own list.
     * @deprecated since 0.0.6 Access to individual handout pieces is now managed by a list of {@link HandoutAccessGuards}.
     */
    @NonNull
    @Deprecated
    List<List<ContainerHandout>> getPlayerLists() {
        // TODO: do we need this method outside of unit tests?
        return playerLists;
    }

    @Override
    public void addHandout( @NonNull ContainerHandout handout ) {
        List<AbstractHandoutPiece> preleminaryList = new ArrayList<>();
        addPiece( handout, preleminaryList ); // populates preleminaryMap, may throw IllegalArgumentException
        preleminaryList.forEach( piece -> {
            HandoutAccessGuard guard = new HandoutAccessGuard( piece );
            guards.put( piece.getId(), guard );
        });
        allHandouts.add( handout );
    }

    /** Checks if the ID of any of the handout pieces in the argument ({@code piece} can be a container) is already
     * listet in {@code guards}. If the argument is indeed a container, this becomes flattened.
     * @author Dragonstb
     * @param piece The handout piece that becomes added eventually.
     * @param preleminaryList In the end, a flattened list of the handout pieces in case {@code piece} is a
     * {@link dev.dragonstb.scribevttrpg.game.handouts.ContainerHandout ContainerHandout}. Enter an empty list here. The
     * argument and all nested pieces are added to this list.
     * @throws IllegalArgumentException When an ID is already taken.
     */
    private void addPiece( @NonNull AbstractHandoutPiece piece, @NonNull List<AbstractHandoutPiece> preleminaryList) {
        String id = piece.getId();
        if( guards.containsKey( id ) && guards.get(id) != null ) {
            throw new IllegalArgumentException("Key already in use.");
        }
        preleminaryList.add( piece );

        if( piece instanceof ContainerHandout container) {
            List<AbstractHandoutPiece> children = container.getPieces();
            children.forEach(child -> addPiece(child, preleminaryList) );
        }
    }
}
