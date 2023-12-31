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

builders.container = {

    /** Creates a container handout element for the DOM.
     * @author Dragonstb
     * @param {Object} Data of the handout
     * @returns {HTMLElement} The HTML element that represents the handout on the screen.
     */
    createNew: function( data, actionCallback, appendix, depth ) {

        let id = appendix + data.id;
        let headId = id+'-'+constants.HEAD;
        let bodyId = id+'-'+constants.BODY;
        let addId = id+'-'+constants.ADD;

        function createView( data ) {
            // <div id=data.id>
            //   <h3 id=headId aria-controls=bodyId aria-expanded="false">data.name</h3>
            //   <div id=bodyId class="nodisplay">
            //     <span>some text</span>
            //   </div>
            //   <div>
            //      <button id=addId />
            //   </div>
            //   <div id=editRowId>          -+
            //      <button id=upId />        |
            //      <button id=downId />      +- default part, becomes added by builders.js
            //      <button id=deleteId />    |
            //   </div>                      -+
            // </div>

            // TODO: maybe validate input

            // _____ main div _____
            // TODO: think about a fieldset, on the other side, handouts are structured with h3 and such and appear linearly
            const handout = document.createElement( 'div' );
            handout.setAttribute( 'id', id );

            // _____ head _____
            let headline;
            switch( depth ) {
                case 0:
                    headline = document.createElement( 'h3' );
                    break;
                case 1:
                    headline = document.createElement( 'h4' );
                    break;
                case 2:
                    headline = document.createElement( 'h5' );
                    break;
                default:
                    headline = document.createElement( 'h6' );
            }
            headline.innerText = data.label;
            headline.setAttribute( 'id', headId );
            headline.setAttribute( 'aria-expanded', false ); // begins in collapsed state
            headline.setAttribute( 'aria-controls', bodyId );
            handout.appendChild( headline );

            // _____ content area _____
            const content = document.createElement( 'div' );
            content.setAttribute( 'id', bodyId );
            content.classList.add( constants.NODISPLAY ); // begins in collapsed state
            handout.appendChild( content );

            // _____ children _____
            if( data.hasOwnProperty('pieces') ) {
                let useDepth = depth < 3 ? depth+1 : 3;
                builders.digestHandoutData( content,  data.pieces, actionCallback, id, useDepth );
            }

            // _____ button for adding a new handout piece _____
            let addButton = document.createElement( 'button' );
            addButton.setAttribute( 'id', addId );
            addButton.innerText = '+'; // TODO: localize
            let addAction = builders.makeAddAction( addId ).build();
            addButton.addEventListener( 'click', event => actionCallback(addAction) );
            handout.appendChild( addButton );

            return handout;
        }

        function createController( view ) {
            let head = view.querySelector( '#'+headId );
            let body = view.querySelector( '#'+bodyId );

            head.addEventListener( 'click', toggleContainer );

            /** Collapses or expands the content.
             * @author Dragonstb
             * @since 0.0.4;
             * @returns nothing
             */
            function toggleContainer() {
                let collapsed = head.getAttribute( 'aria-expanded' ) === 'false';
                if( collapsed ) {
                    body.classList.remove( constants.NODISPLAY );
                    head.setAttribute( 'aria-expanded', 'true' );
                }
                else {
                    body.classList.add( constants.NODISPLAY );
                    head.setAttribute( 'aria-expanded', 'false' );
                }
            }
        }

        // __________________  create  __________________

        let view = createView( data );
        createController( view );
        return view;
    }

};
