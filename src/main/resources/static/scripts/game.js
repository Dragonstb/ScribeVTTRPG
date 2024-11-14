const game = {
    handouts: {},
    room: null,
    myId: null,
    myRole: null,
    fetchFromServer: false,
    wsWrapper: null,
    wsAdminGameEventsAllowed: new Set(['newProspect']),
    wsInternalEventsAllowed: new Set(['sendLetInAsPlayer']),

    handlePieceAction: function( actionData ) {
        console.log( 'Game: '+JSON.stringify(actionData) );
    },

    receiveMessage: function( topic, msgBody ) {
        switch( topic ) {
            case constants.TOPIC_ADMINGAME:
                this.receiveAdminGameMessage( msgBody );
                break;
            case constants.TOPIC_INTERNAL:
                this.receiveInternalMessage( msgBody );
        }
    },

    // _______________  handle game-administrative messanges  ____________________

    receiveAdminGameMessage: function( msgBody ) {
        // TODO: use Utils.isSafeString (tbi)
        if( !msgBody.hasOwnProperty(constants.MSG_EVENT) || !Utils.isNonemptyString(msgBody[constants.MSG_EVENT]) ) {
            return;
        }

        let eventType = msgBody[constants.MSG_EVENT];
        // message body enetrs code from the web and is, consequently, considered usnave. Continue only for certain
        // values of 'eventType'. Otherwise, a manipulated, injected message may invoke any method.
        if( !this.wsAdminGameEventsAllowed.has(eventType) ) {
            console.log(eventType+' is not allowed to be called by websocket input');
            return;
        }

        if( this.hasOwnProperty(eventType) && typeof(this[eventType])==='function' ) {
            this[eventType].call( this, msgBody );
        }
    },

    /** Adds a new prospective candidate to the list of guests waiting in front of the door.
     * @since 0.1.1;
     * @author Dragonstb
     * @param {Object} msgBody Websocket message body. Is expected to contain a string-property 'name'.
     */
    newProspect: function( msgBody ) {
        if( msgBody.hasOwnProperty('name') && Utils.isNonemptyString(msgBody.name) ) { // TODO: use Utils.isSafeString (tbi)
            MainMenu.addProspect( msgBody.name, this );
        }
    },

    // _______________ handle internal messages  ____________________

    receiveInternalMessage: function( msgBody ) {
        // TODO: use Utils.isSafeString (tbi)
        if( !msgBody.hasOwnProperty(constants.MSG_EVENT) || !Utils.isNonemptyString(msgBody[constants.MSG_EVENT]) ) {
            return;
        }

        let eventType = msgBody[constants.MSG_EVENT];
        if( !this.wsInternalEventsAllowed.has( eventType ) ) {
            console.log(eventType+' is not allowed to be called here');
            return;
        }

        if( this.hasOwnProperty(eventType) && typeof(this[eventType])==='function' ) {
            this[eventType].call( this, msgBody );
        }
    },


    sendLetInAsPlayer: function( msgBody ) {
        if( msgBody.hasOwnProperty('name') && Utils.isNonemptyString(msgBody.name) ) { // TODO: use Utils.isSafeString (tbi)
            /* TODO: must reappear after some time if no reaction arrives from the server. Because then it is unclear if
             * the request has been processed. Best to add an info text, so that the user is not confused why these
             * UI elements have reappeared.
             */
            MainMenu.hideProspect( msgBody.name );
            if( !this.wsWrapper ) {
                console.log('local mode: no websocket for letting '+msgBody.name+' in as a player');
                return;
            }

            let obj = {
                name: msgBody.name,
                event: constants.EVENTTYPE_LET_JOIN_AS_PLAYER
            };
            let channel = '/wschannel/admingame/'+this.room;
            this.wsWrapper.sendJson( channel, obj );
        }
    }
};

document.addEventListener('DOMContentLoaded', afterLoadingGame);


// class names
const NODISPLAY = constants.NODISPLAY;

function afterLoadingGame() {
    let roomInput = document.querySelector("#roomname");
    let userIdInput = document.querySelector("#myId");
    let userRoleInput = document.querySelector("#myRole");
    game.room = roomInput.value;
    game.myId = userIdInput.value;
    game.myRole = userRoleInput.value;
    game.fetchFromServer = !roomInput.hasAttribute('th:value');
    Messenger.subscribe( constants.TOPIC_ADMINGAME, game );
    Messenger.subscribe( constants.TOPIC_INTERNAL, game );

    addMenuActions();
    fetchHandouts();
    setupWebSocket();
}

function addMenuActions() {
    MainMenu.init();

    if( !game.fetchFromServer ) {
        MainMenu.addProspect('Sam2');
        MainMenu.addProspect('Alex2');
    }
}

function fetchHandouts() {

    if( game.fetchFromServer ) {
        let target = '/materials/'+game.room;
        requester.requestData( target, resolveHandoutSuccess, resolveHandoutFailure );
    }
    else {
        // for local browsing for local testing without server
        // TODO: do this in another way
        let data = [
                {
                    label:'Skull the Barbarien',
                    type:'container',
                    id:'ho-1',
                    pieces: [
                        {
                            label:'Skills',type:'container',id:'ho-3',
                            pieces: [
                                {label:'Generic skills',type:'container',id:'ho-7', pieces: [
                                    {id:'gs-1', type:'text', text:'Drink'},
                                    {id:'gs-2', type:'text', text:'Endurance'}
                                ]},
                                {label:'Combat skills',type:'container',id:'ho-5', pieces:[
                                    {id:'cs-1', type:'text', text:'Axes'},
                                    {id:'cs-2', type:'text', text:'Swords'},
                                    {id:'cs-3', type:'text', text:'Bare Knuckles'}
                                ]},
                                {label:'Magic skills',type:'container',id:'ho-6', pieces:[
                                    {id:'ms-1', type:'text', text:'Firebolt'},
                                    {id:'ms-1', type:'text', text:'Lighning Bolt'}
                                ]}
                            ]
                        },
                        {
                            label:'Traits',type:'container',id:'ho-4',
                            pieces: [
                                {id:'ho-8', type:'text', text:'very honest'},
                                {id:'ho-9', type:'text', text:'very strong'},
                                {id:'ho-10', type:'text', text:'not very patient'}
                            ]
                        }
                    ]
                },
                {label:'Quest leaflet',type:'container',id:'ho-2', pieces:[
                    {id:'ql-1', type:'text', text:'Sinister howls from the wrecked castle at night fill the peaceful '+
                            'villagers with fear. Coincidentally, there are also rumors about a great treasure in that'+
                            ' haunted ruin.'}
                ]}
            ];
        resolveHandoutSuccess(data);
    }

    function resolveHandoutSuccess( data ) {
        if( data ) {
            let anchor = document.querySelector('#handout-anchor');
            builders.digestHandoutData( anchor, data, game.handlePieceAction );
        }
    }

    function resolveHandoutFailure() {
        console.log("failed in fetching the handouts");
    }
}

function setupWebSocket() {
    if( !game.fetchFromServer ) {
        console.log('No WebSockets in local mode');
        return;
    }

    WSBuilder.build(game);
}