package dev.dragonstb.scribevttrpg;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 *
 * @author Dragonstb
 * @since 0.0.1;
 */
@Controller
public class RequestHandler {

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/")
    public String handleHome(@RequestHeader("Accept-Language") Locale loc, Model model) {
//        // convert accepted languages to locale in a complicated way for now
//        Locale loc = Locale.ENGLISH;
//        if(langs != null) {
//            String[] locCodes = langs.split(",");
//            for (String locCode : locCodes) {
//                // trim to first semicolon
//                int semicolonAt = locCode.indexOf(";");
//                if( semicolonAt > 0) {
//                    locCode = locCode.substring(0, semicolonAt);
//                }
//                // get locale
//                if( locCode.equals("de") || locCode.startsWith("de_") || locCode.startsWith("de-")) {
//                    loc = Locale.GERMAN;
//                }
//            }
//        }

        String mainTitle = messageSource.getMessage("web.mainTitle", null, "<web.mainTitle>", loc);
        model.addAttribute("mainTitle", mainTitle);
        String toHall = messageSource.getMessage("web.toHall", null, "<web.toHall>", loc);
        model.addAttribute("toHall", toHall);

        return "index";
    }

}
