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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 *
 * @author Dragonstb
 * @since 0.0.4;
 */
@Component
public class DefaultHandoutManager implements HandoutManager {

    /** List of all handouts. This is the list the gm acts on. */
    private final List<ContainerHandout> allHandouts = new ArrayList<>();

    /** List of the handout gards that track the access rights. */
    private final Map<String, HandoutAccessGuard> guards = new HashMap<>();

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

    @Override
    public void addHandouts( List<ContainerHandout> handouts ) {
        if( handouts == null) {
            return;
        }
        
        handouts.forEach( handout -> {
            if( handout != null ) {
                try {
                    addHandout( handout );
                }
                catch (IllegalArgumentException iae) {
                    // TODO: think about what to do here (for now: nothing, the handout is not added to the game session)
                }
            }
        });
    }

    @Override
    public void addHandout( @NonNull ContainerHandout handout ) {
        List<AbstractHandoutPiece> preleminaryList = new ArrayList<>();
        addPiece( handout, preleminaryList ); // populates preleminaryMap, may throw IllegalArgumentException
        // you are here means: no conflicts in ID
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

    @Override
    public List<ContainerHandout> getHandouts() {
        // TODO: pass participant ID and role and filter returned handouts for those that are visible to the user
        return allHandouts;
    }


}
