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

document.addEventListener('DOMContentLoaded', afterLoadingPage);

function afterLoadingPage() {

    let createBtn = document.querySelector('#create-session-btn');
    createBtn.addEventListener('click', createSession);

    function createSession() {
        let target = createBtn.getAttribute('x-target');
        if( target !== 'local') {
            // get room name
            let nameInput = document.querySelector('#roomName');
            let roomName = null;
            if(nameInput) {
                roomName = nameInput.value;
            }

            // return and warn if roomName is null, too short, or too long, or uses inappropiate characters
            // TODO: return and warn if roomName is null, too short, or too long, or uses inappropiate characters

            // get campaign name
            let campaign = document.querySelector('#campaignName').innerText;

            // assemble request body
            let body = {
                campaign: campaign,
                roomName: roomName
            };

            // hide previous error message
            document.querySelector('#errorDisplay').classList.add('nodisplay');

            // TODO: turn on the aria-live="polite" waiting overlay that states "please wait" or similar
            // send request
            sendCreateGameRequest(target, body)
                    .then(
                            (resp) => {resolveCreateResponse(resp);}
                    );

        }
        else {
            // this is for local browsing, when testing the pages without connecting to the server
            window.location.href="./game.html";
        }
    }

    /** Sends the request for creating a new room for a new session
     * @author Dragonstb
     * @since 0.0.3;
     * @param {string} url The URL the request is addressed to. Requires the campaign name and the room name as
     * parameters. A parameter password is optional.
     * @param {object} body The request body.
     * @returns {object} The response object.
     */
    async function sendCreateGameRequest(url, body) {
        const resp = await fetch(url, {
            method: "POST",
            cache: "no-cache",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body)
        });
        return resp.json();
    }

    function resolveCreateResponse(data) {
        // TODO: hide waiting overlay again
        if(!data) {
            console.log('no data');
            return;
        }

        let validRoom = data.hasOwnProperty('room') && data.room && typeof data.room === 'string'
                && data.room.length > 0;

        if(validRoom) {
            onCreateSuccess(data);
        }
        else {
            onCreateError(data);
        }
    }

    function onCreateError(data) {
        console.log('failed');
        errDisplay = document.querySelector('#errorDisplay');
        if( data ) {
            console.dir(data);
            if( data.hasOwnProperty('message') && data.message && typeof data.message === 'string') {
                errDisplay.innerText = data.message;
            }
            else{
            }
        }
        else{
            errDisplay.innerText = "A problem occured when creating the room. Your chosen name for hte room might"
                + " be taken, or the name does not fullfill the constraints. Please try again with another name.";
        }
        errDisplay.classList.remove('nodisplay');
    }

    function onCreateSuccess(data) {
        window.location.href="./game/"+data.room;
    }
}

