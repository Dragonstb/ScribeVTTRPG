package dev.dragonstb.scribevttrpg.utils;

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

}
