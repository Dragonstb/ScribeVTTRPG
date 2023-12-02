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

const builders = {

    /** Creates the handouts as elements nodes and appends them to the given parent elemen.
     * @author Dragonstb
     * @param {HTMLElement} parent Element node the handouts become atatched to.
     * @param {Array[Object]} data Array of object containing the data of each handout.
     * @param {String} An appendix for the id of the element.
     * @returns {undefined} nothing
     */
    digestHandoutData: function( parent, data, appendix='', depth=0 ) {

        // TODO: validate input

        for(let handout of data) {
            if( this.hasOwnProperty( handout.type ) ) {
                let child = this[handout.type].createNew( handout, appendix, depth );
                if( child ) {
                    // _____ row of edit buttons _____
                    if( depth > 0 ) {
                        // because depth == 0 is the outer most container which is the root of a handout and, therefore,
                        // must exist and be singular on this uppermost level
                        let id = child.getAttribute( 'id' );
                        let editRowId = id+'-'+constants.EDIT_ROW;
                        let upId = id+'-'+constants.UP;
                        let downId = id+'-'+constants.DOWN;
                        let deleteId = id+'-'+constants.DELETE;

                        let editRow = document.createElement( 'div' );
                        editRow.setAttribute( 'id', editRowId );

                        let upButton = document.createElement( 'button' );
                        upButton.setAttribute( 'id', upId );
                        upButton.innerText = 'up'; // TODO: localize
                        editRow.appendChild( upButton );

                        let downButton = document.createElement( 'button' );
                        downButton.setAttribute( 'id', downId );
                        downButton.innerText = 'down'; // TODO: localize
                        editRow.appendChild( downButton );

                        let deleteButton = document.createElement( 'button' );
                        deleteButton.setAttribute( 'id', deleteId );
                        deleteButton.innerText = 'X'; // TODO: localize
                        editRow.appendChild( deleteButton );

                        child.appendChild( editRow );
                    }
                    
                    parent.appendChild( child );
                }
            }
            else {
                console.log('cannot create element for '+handout.type );
            }
        }
    }

};
