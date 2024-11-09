/*
 * Copyright (c) 2024, Dragonstb
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

const MainMenu = {

    mainMenuBtn: null,
    mainMenuPanel: null,
    letThemInBtn: null,
    letThemInPanel: null,

    /** Makes the name appear in the list of people who want to join the room. Some buttons for answering this
     * request are added, too.
     * @since 0.1.1;
     * @author Dragonstb
     * @param {String} name User's name
     */
    addProspect: function( name ) {
        // TODO: make sure you do not produce duplicated items
        let letInDivId = 'let-'+name+'-in-div';
        let letInBtnId = 'let-'+name+'-in-btn';
        let letInPnlId = 'let-'+name+'-in-panel';
        let letInAsPlayerBtnId = 'let-'+name+'-in-btn-as-player';
        let letInAsSpecBtnId = 'let-'+name+'-in-btn-as-spectator';
        let rejectBtnId = 'reject-'+name;

        // -----  view  -----
        /*
        <div id=letInDivId>
            <button id=letInBtnId aria-controls=letInPnlId aria-owns=letInPnlId aria-expanded=false>name</button>
            <div id=letInPnlId class="nodisplay">
                <button id=letInAsPlayerBtnId>Let {name} join as player</button>
                <button letInAsSpecBtnId>Let {name} join as spectator</button>
                <button rejectBtnId>Reject {name} join as spectator</button>
            </div>
        </div>
        */

       const letInDiv = document.createElement('div');
       letInDiv.setAttribute( 'id', letInDivId );

       const letInBtn = document.createElement('button');
       letInBtn.setAttribute( 'id', letInBtnId );
       letInBtn.setAttribute( 'aria-controls', letInPnlId );
       letInBtn.setAttribute( 'aria-owns', letInPnlId );
       letInBtn.setAttribute( 'aria-expanded', false );
       letInBtn.innerText = name;
       letInDiv.appendChild( letInBtn );

       const letInPanel = document.createElement('div');
       letInPanel.setAttribute( 'id', letInPnlId );
       letInPanel.classList.add( constants.NODISPLAY );
       letInDiv.appendChild( letInPanel );

       const letInAsPlayerBtn = document.createElement('button');
       letInAsPlayerBtn.setAttribute( 'id', letInAsPlayerBtnId );
       letInAsPlayerBtn.innerText = 'Let '+name+' join as player'; // TODO: localize
       letInPanel.appendChild( letInAsPlayerBtn );

       const letInAsSpecBtn = document.createElement('button');
       letInAsSpecBtn.setAttribute( 'id', letInAsSpecBtnId );
       letInAsSpecBtn.innerText = 'Let '+name+' join as spectator'; // TODO: localize
       letInPanel.appendChild( letInAsSpecBtn );

       const rejectBtn = document.createElement('button');
       rejectBtn.setAttribute( 'id', rejectBtnId );
       rejectBtn.innerText = 'Reject '+name; // TODO: localize
       letInPanel.appendChild( rejectBtn );

       // -----  controller  -----

       function toggleButtonsVisibility() {
           letInPanel.classList.toggle( constants.NODISPLAY );
           let visible = !letInPanel.classList.contains( constants.NODISPLAY );
           letInBtn.setAttribute( 'aria-expanded', visible );
       }

       function letInAsPlayerAction() {
           // TODO: function content
           console.log('Letting '+name+' in as player'); // TODO: localize
       }

       function letInAsSpectatorAction() {
           // TODO: function content
           console.log('Letting '+name+' in as spectator'); // TODO: localize
       }

       function rejectAction() {
           console.log('Rejecting '+name); // TODO: localize
       }

       letInBtn.addEventListener( 'click', toggleButtonsVisibility );
       letInAsPlayerBtn.addEventListener( 'click', letInAsPlayerAction );
       letInAsSpecBtn.addEventListener( 'click', letInAsSpectatorAction );
       rejectBtn.addEventListener( 'click', rejectAction );

       this.letThemInPanel.appendChild( letInDiv );
    },

    /** Initializes the main menu with actions. Do not call until the dom content is loaded.
     * @since 0.1.1;
     * @author Dragonstb
     */
    init: function() {
        const mainMenuBtn = document.getElementById('main-menu-btn');
        const mainMenuPanel = document.getElementById('main-menu-panel');
        const letThemInBtn = document.getElementById('let-them-in-btn');
        const letThemInPanel = document.getElementById('let-them-in-panel');
        this.mainMenuBtn = mainMenuBtn;
        this.mainMenuPanel = mainMenuPanel;
        this.letThemInBtn = letThemInBtn;
        this.letThemInPanel = letThemInPanel;

        function toggleMainMenuVisibility() {
            let wasVisible = !mainMenuPanel.classList.contains( constants.NODISPLAY );
            mainMenuPanel.classList.toggle( constants.NODISPLAY );

            if( wasVisible ) {
                hideLetThemInPanel();
            }
        }

        function toggleLetThemInVisibility() {
            letThemInPanel.classList.toggle( constants.NODISPLAY );
        }

        function hideLetThemInPanel() {
            letThemInPanel.classList.add( constants.NODISPLAY );
        }

        mainMenuBtn.addEventListener('click', toggleMainMenuVisibility);
        letThemInBtn.addEventListener('click', toggleLetThemInVisibility);
    }
};
