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

// TODO: don't reuse object 'game' wwhich is used by game.js, but use a different, specialized object of other name.
// Both object may make use of the same builder then => cut builder from game
let game = {
    handouts: {
        builders: {}
    }
};

/** The content manager.
 *
 * @type object
 */
let cm = {
    contentList: null,

    /** Builds the full list of all valid content items given in 'data'. A content item in 'data' qualifies as 'valid'
     * if it has a not-zero-length string property name and a string property 'desc'. Html elements are created for each
     * contenmt item and added to the DOM.
     * @author Dragonstb
     * @since 0.0.5;
     * @param {Array} data An array of objects with string properties 'name' and 'desc'.
     */
    addFullList: function( data ) {
        if( this.contentList === null ) {
            console.log( 'cannot build content list: root element has not been initialized yet' );
            return;
        }
        if( !data || !Array.isArray( data ) ) {
            console.log( 'cannot build content list: invalid data' );
            return;
        }

        for (var idx = 0; idx < data.length; idx++) {
            let item = data[idx];
            if( !item.hasOwnProperty('name') || typeof item.name !== 'string' || item.length === 0) {
                console.log( 'cannot add content item: invalid data' );
                continue;
            }
            if( !item.hasOwnProperty('desc') || typeof item.desc !== 'string') {
                console.log( 'cannot add content item: invalid data' );
                continue;
            }

            // main panel of item
            let panel = document.createElement( 'div' );

            // field for name
            let nameField = document.createElement( 'div' );
            let nameContent = document.createElement( 'strong' );
            nameContent.innerText = item.name;
            nameField.appendChild( nameContent );
            panel.appendChild( nameField );

            // field for description
            let descField = document.createElement( 'div' );
            descField.innerHTML = item.desc;
            panel.appendChild( descField );

            // add to DOM
            this.contentList.appendChild( panel );
        }
    }
};

document.addEventListener('DOMContentLoaded', afterLoadingContentManager);

function afterLoadingContentManager() {
    console.log( 'fetching from server? '+fetchFromServer );

    addButtonActions();
    fetchContents();
}

/** Grants the actions to the buttons
 * @author Dragonstb
 * @since 0.0.5;
 */
function addButtonActions() {
    let addContentBtn = document.querySelector("#add-content-btn");
    let copyContentBtn = document.querySelector("#copy-content-btn");
    let deleteContentBtn = document.querySelector("#delete-content-btn");

    function addContent() {
        console.log( 'adding content' );
    }

    function copyContent() {
        console.log( 'copying content' );
    }

    function deleteContent() {
        console.log( 'deleting content' );
    }

    addContentBtn.addEventListener('click', addContent);
    copyContentBtn.addEventListener('click', copyContent);
    deleteContentBtn.addEventListener('click', deleteContent);
}

/** Fetches the data of the content items from the server.
 * @author Dragonstb
 * @since 0.0.5;
 */
function fetchContents() {

    if( fetchFromServer ) {
        let target = '/manage/contentlist';
        requester.requestData(target, resolveContentsSuccess, resolveContentFailure);
    }
    else {
        // for local browsing and for local testing without server
        // TODO: do this in another way
        let items = [
            {name:'Blank character sheet',desc:"A template"},
            {name:'Skull the Babarien',desc:"Character sheet"},
            {name:'Thunder the Wizard',desc:"Character sheet"},
            {name:'Quest Leaflet',desc:"The quest for the first mission"},
            {name:'A mysterious letter',desc:"Writing from the mayor about a mine near the village"}
        ];
        resolveContentsSuccess(items);
    }

    function resolveContentsSuccess( data ) {
        console.log("succceeded in fetching the contents");
        if( data ) {
            console.dir( data );
            let hook = document.querySelector('#content-list');
            cm.contentList = hook;
            cm.addFullList( data );
        }
    }

    function resolveContentFailure() {
        console.log("failed in fetching the contents");
    }
}

