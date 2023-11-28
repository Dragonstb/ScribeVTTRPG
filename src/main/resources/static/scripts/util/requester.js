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

const requester = {

    /** Fetches data from a remote location asyncly and calls one of the two callbacks, depending on if some data has
     * been returned.
     * @author Dragonstb
     * @since 0.0.5;
     * @param {string} url The url where the data of interest is located.
     * @param {function} resolveSuccess Callback function invoked in case some data comes back. The data returned is
     * used as argument for this function.
     * @param {function} resolveFail Callback invoked if the server does not return data. This function is not
     * expected to have an argument.
     * @returns {undefined}
     */
    requestData: function(url, resolveSuccess, resolveFail) {

        /** Async call to remote url.
         * @author Dragonstb
         * @since 0.0.5;
         * @param {String} url The URL to be called.
         */
        async function getContentData( url ) {
            console.log('making async call');
            const resp = await fetch(url, {
                method: "GET",
                cache: "no-cache",
                headers: {
                    "Accept": "application/json"
                }
            });
            return resp.json();
        }

        /** Makes a raw validation of the data and calls either a success or a fail function once the promise is
         * resolved.
         * @author Dragonstb
         * @since 0.0.5;
         * @param {Object} data Response data as json object.
         */
        function resolveContentResponse( data ) {
            console.log('resolving async call');
            if( data === null || data === undefined ) {
                resolveFail( null );
            }
            else {
                resolveSuccess( data );
            }
        }

        // --- start ---

        getContentData( url ).then(
                                (resp) => {resolveContentResponse(resp);}
                        ); // TODO: define error function

    }
};
