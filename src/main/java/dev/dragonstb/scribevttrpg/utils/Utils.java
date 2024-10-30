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
package dev.dragonstb.scribevttrpg.utils;

import java.util.Base64;
import java.util.UUID;
import org.springframework.lang.NonNull;

/** Some utility functions.
 *
 * @author Dragonstb
 * @since 0.0.2;
 */
public class Utils {

    /** Assembles the name of a single webpage and the name of the web service the page is part of to a document
     * title. This title can be used as inner text of the title tag.<br><br>
     * Purpose of this method is providing a simple way of consistently building page titles that follow the suggestions
     * of Technique G88 of the WCAG 2.2: <a href=https://www.w3.org/WAI/WCAG22/Techniques/general/G88>https://www.w3.org/WAI/WCAG22/Techniques/general/G88</a>.
     *
     * @author Dragonstb
     * @since 0.0.2;
     * @param page Name of webpage.
     * @param service Name of web sevice.
     * @return "${page} - ${name}"
     */
    @NonNull
    public static String composeDocumentTitle(@NonNull String page, @NonNull String service) {
        return page + " - " + service;
    }

    /** Returns a Base64 encoded version of the bits in the uuid.
     * @since 0.0.6;
     * @param uuid UUID of interest.
     * @return Base64 encoded representation of the uuid.
     */
    @NonNull
    public static String uuidToString( @NonNull UUID uuid ) {
        long most = uuid.getMostSignificantBits();
        long least = uuid.getLeastSignificantBits();
        byte[] arr = new byte[8];
        for ( byte idx = 0; idx < 4; idx++ ) {
            arr[idx] = (byte)(most >> idx*8);
            arr[idx+4] = (byte)(least >> idx*8);
        }
        return Base64.getEncoder().encodeToString( arr );
    }

}
