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
//let game = {
//    handouts: {
//        builders: {}
//    }
//};

/** The content manager.
 *
 * @type object
 */
let cm = {
    LIST_ELEM: 'list-elem-',
    WORKBENCH_ELEM: 'workbench-elem-',
    contentList: null,
    contentMap: new Map(),
    workbenchNode: null,

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

            // create content object and add to map with unique id as key
            let contentObject = this.makeContentObject( this, item, idx );
            this.contentMap.set( idx, contentObject );

            // add to DOM
            this.contentList.appendChild( contentObject.listElem );
        }
    },

    /** Creates the content object for the content item. This object contains an HTML element for the list.
     * @author Dragonstb
     * @since 0.0.5;
     * @param {object} item The data of the content item.
     * @returns {object} The content object.
     */
    makeContentObject: function( cm, item, idNum ) {

        /** Creates the HTML element for the list of content items.
         * @author Dragonstb
         * @since 0.0.5;
         * @param {Object} item The item data as object.
         * @param {number} idNum An unique index number. No other content object can have this number.
         * @returns {HTMLElement} The div to be appended to the overwie list.
         */
        function makeListElement( item, idNum ) {
            // main panel of item
            let panel = document.createElement( 'div' );
            panel.setAttribute( 'id', cm.LIST_ELEM + idNum );

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

            return panel;
        }

        let workbenchElemBuilder = {

            // functions for creating workbench objects of handouts
            handout: function( item, idNum ) {
                let data = {
                    label: item.name,
                    type: 'container',
                    id: cm.WORKBENCH_ELEM + idNum,
                    pieces: item.pieces
                };

                return builders.container.createNew( data, cm.handlePieceAction, '', 0 );
            }
        };

        function makeWorkbenchElement( item, idNum ) {
            if( !workbenchElemBuilder.hasOwnProperty(item.type) ) {
                return null;
            }

            return workbenchElemBuilder[ item.type ]( item, idNum );
        }

        let listElem = makeListElement( item, idNum );
        // invoke content manager's function with personal idx number as argument
        listElem.addEventListener( 'click', function(){cm.clickListElem( idNum );} );

        let workbenchElem = makeWorkbenchElement( item, idNum );

        // build content object
        let contentObject = {
            listElem: listElem,
            workbenchElem: workbenchElem
        };

        return contentObject;
    },

    clickListElem: function( idNum ) {

        let contentObject = cm.contentMap.get( idNum );

        for( let child of cm.workbenchNode.children) {
            cm.workbenchNode.removeChild( child );
        }

        cm.workbenchNode.appendChild( contentObject.workbenchElem );
    },

    handlePieceAction: function( actionJson ) {
        console.log( 'Manage: ' + JSON.stringify(actionJson) );
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
            {name:'Blank character sheet',desc:"A template", type:"handout",
                pieces: [
                    {
                        label:'Skills',type:'container', id:'wb1',
                        pieces: [
                            {label:'Generic skills',type:'container', id:'wb2',pieces: []},
                            {label:'Combat skills',type:'container', id:'wb3',pieces:[]},
                            {label:'Magic skills',type:'container', id:'wb4',pieces:[]}
                        ]
                    },
                    {
                        label:'Traits',type:'container', id:'wb5',
                        pieces: []
                    }
                ]
            },
            {name:'Skull the Babarien',desc:"Character sheet", type:"handout",
                pieces: [
                    {
                        label:'Skills',type:'container', id:'wb6',
                        pieces: [
                            {label:'Generic skills',type:'container', id:'wb7', pieces: [
                                {type:'text', id:'wb8', text:'Drink'},
                                {type:'text', id:'wb9', text:'Endurance'}
                            ]},
                            {label:'Combat skills',type:'container', id:'wb10', pieces:[
                                {type:'text', id:'wb11', text:'Axes'},
                                {type:'text', id:'wb12', text:'Swords'},
                                {type:'text', id:'wb13', text:'Bare Knuckles'}
                            ]},
                            {label:'Magic skills',type:'container', id:'wb14', pieces:[
                                {type:'text', id:'wb15', text:'Firebolt'},
                                {type:'text', id:'wb16', text:'Lighning Bolt'}
                            ]}
                        ]
                    },
                    {
                        label:'Traits',type:'container', id:'wb17',
                        pieces: [
                            {type:'text', id:'wb18', text:'very honest'},
                            {type:'text', id:'wb19', text:'very strong'},
                            {type:'text', id:'wb20', text:'not very patient'}
                        ]
                    }
                ]
            },
            {name:'Thunder the Wizard',desc:"Character sheet", type:"handout",
                pieces: [
                    {
                        label:'Skills',type:'container', id:'wb21',
                        pieces: [
                            {label:'Generic skills',type:'container', id:'wb22',pieces: [
                                {type:'text', id:'wb23', text:'Intimidate'},
                                {type:'text', id:'wb24', text:'Dancing'}
                            ]},
                            {label:'Combat skills',type:'container', id:'wb25',pieces:[
                                {type:'text', id:'wb26', text:'Staffs'},
                                {type:'text', id:'wb27', text:'Bows'}
                            ]},
                            {label:'Magic skills',type:'container', id:'wb28',pieces:[
                                {type:'text', id:'wb29', text:'Fireball'},
                                {type:'text', id:'wb30', text:'Energy Shield'},
                                {type:'text', id:'wb31', text:'Storm'},
                                {type:'text', id:'wb32', text:'Thunderbolt'},
                                {type:'text', id:'wb33', text:'Meteor Strike'},
                                {type:'text', id:'wb34', text:'Healing'}
                            ]}
                        ]
                    },
                    {
                        label:'Traits',type:'container', id:'wb35',
                        pieces: [
                            {type:'text', id:'wb36', text:'capable runner'},
                            {type:'text', id:'wb37', text:'dabbling crafter'},
                            {type:'text', id:'wb38', text:'good observer'}
                        ]
                    }
                ]
            },
            {name:'Quest Leaflet',desc:"The quest for the first mission",type:"handout",
                pieces: [
                    {type:'text', id:'wb39', text:'Sinister howls from the wrecked castle at night fill the peaceful '+
                            'villagers with fear. Coincidentally, there are also rumors about a great treasure in that'+
                            ' haunted ruin.'}
                ]
            },
            {name:'A mysterious letter',desc:"Writing from the mayor about a mine near the village",type:"handout",
                pieces: [
                    {type:'text', id:'wb40', text:'Greeting Marty, I hope everything runs well in the mine? This is going to be '+
                            'great if we can find what we are looking for there. I remind you that it is of utmost '+
                            'importance that no one takes a notice of our actions! Remember what is at stake.'}
                ]
            }
        ];
        resolveContentsSuccess(items);
    }

    function resolveContentsSuccess( data ) {
        console.log("succceeded in fetching the contents");
        if( data ) {
            console.dir( data );
            let hook = document.querySelector('#content-list');
            cm.contentList = hook;
            let workbenchNode = document.querySelector('#workbench');
            cm.workbenchNode = workbenchNode;
            cm.addFullList( data );
        }
    }

    function resolveContentFailure() {
        console.log("failed in fetching the contents");
    }
}

