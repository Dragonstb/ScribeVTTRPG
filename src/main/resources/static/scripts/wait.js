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

document.addEventListener('DOMContentLoaded', afterLoadingPage);

function afterLoadingPage() {

    const roomName = document.querySelector('#room-name').innerText;
    let abortBtn = document.querySelector('#abort-join-btn');
    abortBtn.addEventListener('click', abortJoinSession);

    let target = abortBtn.getAttribute('x-target');
    let eventSource;

    if( target !== 'local' ) {
//        eventSource = new EventSource('../joindecision/'+roomName);
//        eventSource.onevent = (event) => processEvent(event);
//        eventSource.onmessage = (event) => processMessage(event);
//        eventSource.onerror = (event) => processError(event);
        // TODO: activate event sourcing
    }
    else {
        console.log('No event sourcing in local mode');
    }

    function processEvent(event) {
        // TODO: method body
        console.log('event! '+event.event);
        console.dir(event.data);
    }

    function processMessage(event) {
        // TODO: method body
        console.log('message!');
        console.dir(event.data);
    }

    function processError(event) {
        // TODO: method body
        console.error('error!');
        console.dir(event);
    }

    function abortJoinSession() {
        console.log('not yet implemented');
        showErrorText('This functionality has not been implemented yet.');
    }

    function showErrorText(text) {
        let elem = document.querySelector('#errorDisplay');
        elem.innerText = text;
        elem.classList.remove('nodisplay');
    }

    function hideErrorText() {
        let elem = document.querySelector('#errorDisplay');
        elem.classList.add('nodisplay');
        elem.innerText = "";
    }

    async function sendAbortRequest(url) {
        const resp = await fetch(url, {
            method: "GET",
            cache: "no-cache"
        });
        return resp.json();
    }

    function resolveAbortResponse(data) {
        // TODO: method body
    }
}

