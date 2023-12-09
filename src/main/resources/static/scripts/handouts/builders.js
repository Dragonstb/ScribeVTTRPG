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
    digestHandoutData: function( parent, data, actionCallback, appendix='', depth=0 ) {

        // TODO: validate input

        for(let handout of data) {
            if( this.hasOwnProperty( handout.type ) ) {
                let child = this[handout.type].createNew( handout, actionCallback, appendix, depth );
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
                        let upAction = builders.makeUpAction( id ).build();
                        upButton.addEventListener( 'click', event => actionCallback(upAction) );

                        let downButton = document.createElement( 'button' );
                        downButton.setAttribute( 'id', downId );
                        downButton.innerText = 'down'; // TODO: localize
                        editRow.appendChild( downButton );
                        let downAction = builders.makeDownAction( id ).build();
                        downButton.addEventListener( 'click', event => actionCallback(downAction) );

                        let deleteButton = document.createElement( 'button' );
                        deleteButton.setAttribute( 'id', deleteId );
                        deleteButton.innerText = 'X'; // TODO: localize
                        editRow.appendChild( deleteButton );
                        let deleteAction = builders.makeDeleteAction( id ).build();
                        deleteButton.addEventListener( 'click', event => actionCallback(deleteAction) );

                        child.appendChild( editRow );
                    }

                    parent.appendChild( child );
                }
            }
            else {
                console.log('cannot create element for '+handout.type );
            }
        }
    },

    /** Creates a builder object that can be used for easy and programmatic building of the action json.
     * @author Dragonstb
     * @since 0.0.5;
     * @param {type} forId Id of the html element the action is for.
     * @param {type} forAction Type of the action.
     * @returns {object} A builder that has a function build() for spawning the action json.
     */
    makeActionBuilder: function() {
        let builder = {
            /** Sets a property of the builder, if the arguments are correct.
             * @author Dragonstb
             * @since 0.0.5;
             * @param {string} prop The name of the property without the leading _.
             * @param {string} value The value of the property.
             * @returns {object} Simply 'this' for chaining function calls.
             */
            set: function( prop, value ) {
                if( typeof(prop) !== 'string' ) {
                    return this;
                }

                let ownAttr = '_'+prop;
                if( !this.hasOwnProperty(ownAttr) ) {
                    return this;
                }

                if( typeof(value) === 'string' ) {
                    this[ownAttr] = value;
                }
                return this;
            },

            /** Action desired. */
            _action: null,
            /** Sets the action in this builder.
             * @author Dragonstb
             * @since 0.0.5;
             * @param {string} forAction The desired action.
             * @returns {object} Simply 'this' for chaining function calls.
             */
            action: function( forAction ) {
                this.set( 'action', forAction);
                return this;
            },

            /** Element id th action is for. */
            _id: null,
            /** Sets the id in this builder.
             * @author Dragonstb
             * @since 0.0.5;
             * @param {string} forId The id of the HTML element the action is for.
             * @returns {object} Simply 'this' for chaining function calls.
             */
            id: function( forId ) {
                this.set( 'id', forId );
                return this;
            },

            /** Creates a json object with the same non-function attributes as this builder, all of them having the
             * same values as this builder.
             * @author Dragonstb
             * @since 0.0.5;
             * @returns {object} Objects describing a desired action
             */
            build: function() {
                let obj = {};
                for (let attr in this) {
                    if( typeof(this[attr]) === 'function' ) {
                        continue;
                    }
                    // slice(1) accounts for the leading _ of each attribute
                    obj[ attr.slice(1) ] = this[ attr ];
                }
                return obj;
            }
        };

        return builder;
    },

    /** Shorthand for making a builder of action 'up'.
     * @author Dragonstb
     * @since 0.0.5;
     * @param {type} id Id of the HTML element the action is for.
     * @returns {object} An appropiate action builder.
     */
    makeUpAction: function( id ) {
        return builders.makeActionBuilder().action( constants.UP ).id( id );
    },

    /** Shorthand for making a builder of action 'moveUp'.
     * @author Dragonstb
     * @since 0.0.5;
     * @param {type} id Id of the HTML element the action is for.
     * @returns {object} An appropiate action builder.
     */
    makeDownAction: function( id ) {
        return builders.makeActionBuilder().action( constants.DOWN ).id( id );
    },

    /** Shorthand for making a builder of action 'delete'.
     * @author Dragonstb
     * @since 0.0.5;
     * @param {type} id Id of the HTML element the action is for.
     * @returns {object} An appropiate action builder.
     */
    makeDeleteAction: function( id ) {
        return builders.makeActionBuilder().action( constants.DELETE ).id( id );
    },

    /** Shorthand for making a builder of action 'add'.
     * @author Dragonstb
     * @since 0.0.5;
     * @param {type} id Id of the HTML element the action is for.
     * @returns {object} An appropiate action builder.
     */
    makeAddAction: function( id ) {
        return builders.makeActionBuilder().action( constants.ADD ).id( id );
    }

};
