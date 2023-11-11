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
