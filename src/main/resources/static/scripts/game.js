let game = {
    handouts: {},
    room: null,
    fetchFromServer: false,

    handlePieceAction: function( actionData ) {
        console.log( 'Game: '+JSON.stringify(actionData) );
    }
};

document.addEventListener('DOMContentLoaded', afterLoadingGame);


// class names
const NODISPLAY = "nodisplay";

function afterLoadingGame() {
    let roomInput = document.querySelector("#roomname");
    game.room = roomInput.value;
    game.fetchFromServer = !roomInput.hasAttribute('th:value');

    addMenuActions();
    fetchHandouts();
}

function addMenuActions() {
    let mainMenuBtn = document.getElementById('main-menu-btn');
    let mainMenuPanel = document.getElementById('main-menu-panel');
    let letThemInBtn = document.getElementById('let-them-in-btn');
    let letThemInPanel = document.getElementById('let-them-in-panel');

    function toggleMainMenuVisibility() {
        let wasVisible = !mainMenuPanel.classList.contains(NODISPLAY);
        mainMenuPanel.classList.toggle(NODISPLAY);

        if( wasVisible ) {
            hideLetThemInPanel();
        }
    }

    function toggleLetThemInVisibility() {
        letThemInPanel.classList.toggle(NODISPLAY);
    }

    function hideLetThemInPanel() {
        letThemInPanel.classList.add(NODISPLAY);
    }

    mainMenuBtn.addEventListener('click', toggleMainMenuVisibility);
    letThemInBtn.addEventListener('click', toggleLetThemInVisibility);
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
