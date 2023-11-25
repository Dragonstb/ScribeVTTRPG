let game = {
    handouts: {},
    room: null,
    fetchFromServer: false
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
        getHandoutData(target).then(
                                (resp) => {resolveHandoutResponse(resp);}
                        ); // TODO: define error function
    }
    else {
        // for local browsing for local testing without server
        // TODO: do this in another way
        let data = [
                {
                    name:'Skull the Barbarien',
                    type:'container',
                    id:'ho-1',
                    pieces: [
                        {
                            name:'Skills',type:'container',id:'ho-3',
                            pieces: [
                                {name:'Generic skills',type:'container',id:'ho-7', pieces: [
                                    {id:'gs-1', type:'text', text:'Drink'},
                                    {id:'gs-2', type:'text', text:'Endurance'}
                                ]},
                                {name:'Combat skills',type:'container',id:'ho-5', pieces:[
                                    {id:'cs-1', type:'text', text:'Axes'},
                                    {id:'cs-2', type:'text', text:'Swords'},
                                    {id:'cs-3', type:'text', text:'Bare Knuckles'}
                                ]},
                                {name:'Magic skills',type:'container',id:'ho-6', pieces:[
                                    {id:'ms-1', type:'text', text:'Firebolt'},
                                    {id:'ms-1', type:'text', text:'Lighning Bolt'}
                                ]}
                            ]
                        },
                        {
                            name:'Traits',type:'container',id:'ho-4',
                            pieces: [
                                {id:'ho-8', type:'text', text:'very honest'},
                                {id:'ho-9', type:'text', text:'very strong'},
                                {id:'ho-10', type:'text', text:'not very patient'}
                            ]
                        }
                    ]
                },
                {name:'Quest leaflet',type:'container',id:'ho-2', pieces:[
                    {id:'ql-1', type:'text', text:'Sinister howls from the wrecked castle at night fill the peaceful '+
                            'villagers with fear. Coincidentally, there are also rumors about a great treasure in that'+
                            ' haunted ruin.'}
                ]}
            ];
        resolveHandoutSuccess(data);
    }

    async function getHandoutData(url) {
        const resp = await fetch(url, {
            method: "GET",
            cache: "no-cache",
            headers: {
                "Accept": "application/json"
            }
        });
        return resp.json();
    }

    function resolveHandoutResponse( data ) {
        if(data === null || data === undefined) {
            resolveHandoutFailure(null);
        }
        else {
            resolveHandoutSuccess( data );
        }
    }

    function resolveHandoutSuccess( data ) {
        if( data ) {
            let anchor = document.querySelector('#handout-anchor');
            game.handouts.builders.digestHandoutData( anchor, data );
        }
    }

    function resolveHandoutFailure( data ) {
        console.log("failed in fetching the handouts");
        if( data ) {
            console.dir( data );
        }
    }
}
