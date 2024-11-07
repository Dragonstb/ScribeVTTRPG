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

const WSBuilder = {
    build: function(game) {
        wrapper = {
            ready: false,
            sock: null
        };

        let handshakeUrl = window.location.toString();
        idx = handshakeUrl.indexOf('/'); // first / in protocol://domain:port/path
        idx += 2;
        idx = handshakeUrl.indexOf('/', idx); // the / between protocol and path
        handshakeUrl = handshakeUrl.slice(0, idx) + "/wshandshake";

        let sock = new StompJs.Client({
            brokerURL: handshakeUrl
        });

        sock.onStompError = function(frame) {
            console.log('WebSocket error:');
            console.dir(frame);
        };

        console.log("subscribing to /topic/admingame/"+game.room);
        sock.onConnect = function(frame) {
            const adminGameSubscription = sock.subscribe('/topic/admingame/'+game.room,
                    function(msg){console.log(msg);});

            if( !wrapper.ready ) {
                // TODO: remove test message
                sock.publish({
                    destination: '/topic/admingame/'+game.room,
                    body: 'here we are!'
                });
                game.wsWrapper = wrapper;
                wrapper.ready = true;
                // TODO: if room creator, signal to server that room creator's UI is ready, opening the game for guests
            }
        };

        sock.activate();
        wrapper.sock = sock;

        return wrapper;
    }
};