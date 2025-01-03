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

/** Collection of constants.
 *
 * @type object
 */
const constants = {
    NODISPLAY: 'nodisplay', // name of css class for hidden elements
    HEAD: 'head', // some constant strings for ids of parts of complicated control elements
    BODY: 'body', // some constant strings for ids of parts of complicated control elements
    /** Row for the buttons for changing the order of handout pieces and deleting a handout piece. */
    EDIT_ROW: 'edit-row',
    /** For an "one up" element. */
    UP: 'up',
    /** For an "one down" element. */
    DOWN: 'down',
    /** For an element of deletion. */
    DELETE: 'delete',
    /** For an element of adding. */
    ADD: 'add',
    /** Message topic: administrate game. */
    TOPIC_ADMINGAME: 'adminGame',
    /** Message topic: frontend-internal communications. */
    TOPIC_INTERNAL: 'internal',

    /** Role: GM. */
    ROLE_GM: 'gm',

    /** Message property: event type */
    MSG_EVENT: 'event',

    /** Administarte game event: let waiting user join as player. */
    EVENTTYPE_LET_JOIN_AS_PLAYER: 'letJoinAsPlayer'
};

Utils = {

    /** Chechs if the argument is a string of non-vanishing length.
     * @author Dragonstb
     * @since 0.1.1;
     * @param {any} text The entity to be tested.
     * @returns {Boolean} True if and only if the argument is an actual string with a length greater than zero.
     */
    isNonemptyString: function(text) {
        return text && typeof(text) === 'string' && text.length > 0;
    }

//    isSafeString: function(text) {
//        if( !this.isNonemptyString(text) ) {
//            return false;
//        }
//
//        // TODO: check if text contains unsafe characters
//        return false;
//    }
};