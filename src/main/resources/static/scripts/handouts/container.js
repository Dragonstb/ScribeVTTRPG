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

game.handouts.builders.container = {

    /** Creates a container handout element for the DOM.
     * @author Dragonstb
     * @param {Object} Data of the handout
     * @returns {HTMLElement} The HTML element that represents the handout on the screen.
     */
    createNew: function(data) {

        // TODO: maybe validate input

        // TODO: think about a fieldset, on the other side, handouts are structured with h3 and such and appear linearly
        const handout = document.createElement( 'div' );

        // TODO: count depth on nested containers for choosing appropiate h
        const headline = document.createElement( 'h3' );
        headline.innerText = data.name;
        handout.appendChild( headline );

        const content = document.createElement( 'div' );
        const auxilliaryText = document.createElement( 'span' );
        auxilliaryText.innerHTML = 'some text about '+data.name;
        content.appendChild( auxilliaryText );
        handout.appendChild( content );
        // TODO: kick auxilliaryText and put the real content into 'content'

        return handout;
    }

};
