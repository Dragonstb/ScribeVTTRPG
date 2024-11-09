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

const Messenger = {

    topics: {},

    /** Subscribes the listener to the topic. Both topic and listener must be proper for this to work. The topic is
     * created if it has not existed yet.
     * @since 0.1.1;
     * @author Dragonstb
     * @param {object} listener An object with a method 'receiveMessage'.
     * @param {string} topic A non-blank string.
     */
    subscribe: function( topic, listener ) {
        if( !topic || typeof(topic) !== "string" || topic.length === 0 ) {
            console.error('cannot subscribe to topic: topic name must be a non-blank string.');
            console.dir(topic);
            return;
        }
        if( !listener || !listener.hasOwnProperty('receiveMessage') || typeof(listener.receiveMessage) !== 'function' ) {
            console.error('cannot subscribe to topic: listener must have function receiveListener.');
            console.dir(topic);
            return;
        }

        if( !this.topics.hasOwnProperty(topic) ) {
            this.topics[topic] = new Set();
        }
        this.topics[topic].add( listener );
    },

    /** Relays a message to all listeners of the topic by calling their 'receiveMessage' functions.
     * @since 0.1.1;
     * @author Dragonstb
     * @param {string} topic Name of the topic.
     * @param {object} message The ,essage object.
     */
    relayMessage: function( topic, message ) {
        if( !topic || typeof(topic) !== "string" || topic.length === 0 ) {
            console.error('cannot relay topic: topic name must be a non-blank string.');
            console.dir(topic);
            return;
        }
        if( !this.topics.hasOwnProperty(topic) ) {
            console.error('cannot relay topic: topic does not exist.');
            console.dir(topic);
            return;
        }

        for (const listener of this.topics[topic] ) {
            listener.receiveMessage(topic, message);
        }
    }
};
