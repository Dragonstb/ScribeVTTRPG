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

import org.json.JSONObject;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/** A publisher used for feeding the server-sent events and informing a candidate participant about if she/he can
 * join the room or not.
 *
 * @author Dragonstb
 * @since 0.1.0;
 */
final class JoinDecisionFeed {

    /** Sink for feeding the sever-sent events. */
    private final Sinks.Many<String> sink = Sinks.many().multicast().directAllOrNothing();

    /** Emits an event containing the decission.
     * @since 0.1.0
     * @author Dragonstb
     * @param accepted User can enter the room?
     */
    void emitDecision(boolean accepted) {
        JSONObject json = new JSONObject();
        json.put( "accepted", accepted );
        Sinks.EmitResult res = sink.tryEmitNext( json.toString() );

        if(res.isFailure()) {
            // TODO: handle
        }
        // TODO: close connection on rejection of user
    }

    /** Returns the sink as Flux.
     * @since 0.1.0;
     * @return The sink as Flux.
     */
    Flux<String> getFlux() {
        return sink.asFlux();
    }
}
