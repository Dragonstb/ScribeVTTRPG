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
package dev.dragonstb.scribevttrpg;

import java.util.Locale;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/** Converts a text, assumed to be the value of the "Accept-Language" header of a http request,
 * into a locale with the language code given in the text.
 *
 * @author Dragonstb
 * @since 0.0.1;
 */
@Component
public class LocaleConverter implements Converter<String, Locale> {

    /** Converts the first available language found in the argument to a locale. The arg
     * {@code text} is assumed to be a comma separated list.
     * @since 0.0.1;
     * @author Dragonstb
     * @param text Text with the languages codes. usually the value of the "Accept-Language" header
     * of a http request.
     * @return The locale associated with the first language code found in text, but al least
     * {@code Locale.ENGLISH}.
     */
    @Override
    public Locale convert(String text) {
        Locale use = Locale.ENGLISH;
        if(text == null) {
            return use;
        }

        String[] locs = text.split(",");
        for (String loc : locs) {
            // trim to first semicolon
            int semicolonAt = loc.indexOf(";");
            if( semicolonAt > 0) {
                loc = loc.substring(0, semicolonAt);
            }
            loc = loc.toLowerCase().trim();

            // TODO: setup (probably autoconfigured) list of available locales
            // and compare against this list
            if( loc.equals("en") || loc.startsWith("en_") || loc.startsWith("en-")) {
                use = Locale.ENGLISH;
                break;
            }
            else if( loc.equals("de") || loc.startsWith("de_") || loc.startsWith("de-")) {
                use = Locale.GERMAN;
                break;
            }
        }

        return use;
    }

}
