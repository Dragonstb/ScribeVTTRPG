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

        String mainTitle = messageSource.getMessage("web.mainTitle", null, "<web.mainTitle>", loc);
        model.addAttribute("mainTitle", mainTitle);
        String toHall = messageSource.getMessage("web.toHall", null, "<web.toHall>", loc);
        model.addAttribute("toHall", toHall);

        return "index";
    }

}
