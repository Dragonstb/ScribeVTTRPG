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

    idMap: new Map(),
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
        if( this.idMap.has(name) ) {
            console.log(name+' is already waiting');
            return;
        }

        let letInElem = makeLetInElem( name );

        this.letThemInPanel.appendChild( letInElem.elem );
        this.idMap.set( name, letInElem );
    },

    hideProspect: function( name ) {
        if( !this.idMap.has(name) || !this.letThemInPanel ) {
            return;
        }

        let obj = this.idMap.get(name);
        this.letThemInPanel.removeChild( obj.elem );
        // TODO: also disable the buttons for this potential participant
    },

    /** Removes a prospect participant from the UI.
     * @since 0.1.1;
     * @author Dragonstb
     * @param {String} name The user's name.
     */
    removeProspect: function( name ) {
        if( !this.idMap.has(name) || !this.letThemInPanel ) {
            return;
        }

        let obj = this.idMap.get( name );
        this.idMap.delete( name );
        if( obj.elem.parentElement && obj.elem.parentElement === this.letThemInPanel ) {
            this.letThemInPanel.removeChild( obj.elem );
        }
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
