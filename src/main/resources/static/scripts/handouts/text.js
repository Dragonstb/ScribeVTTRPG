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

builders.text = {

    /** Creates the UI of a text handout piece.
     * @author Dragonstb
     * @since 0.0.5;
     * @param {object} data The data describing the handout piece.
     * @param {function} actionCallback A function called when any edit action occured on this handout piece.
     * @param {string} appendix A strin added to the front of each element id in this handout piece.
     * @param {type} depth Depth level of the handout piece in the container structure of a handout.
     * @returns {HTMLElement} The DOM element representing the handout piece.
     */
    createNew: function( data, actionCallback, appendix, depth ) {

        let id = appendix + data.id;

        function createView( text='' ) {
            // <div id=id>text</div>

            let handout = document.createElement( 'div' );
            handout.setAttribute( 'id', id );
            handout.innerText = text;

            return handout;
        }

        // _____ create _____
        let view = createView( data.text );
        return view;
    }
};