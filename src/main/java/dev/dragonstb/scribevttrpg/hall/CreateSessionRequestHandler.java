package dev.dragonstb.scribevttrpg.hall;

import dev.dragonstb.scribevttrpg.utils.Utils;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Dragonstb
 * @since 0.0.2;
 */
@Controller
public class CreateSessionRequestHandler {

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/createsession")
    public String serveCreateSessionPage(@RequestParam(name = "campaign") String campaignName,
            @RequestHeader("Accept-Language") Locale loc, Model model) {

        String baseTitle = messageSource.getMessage("web.baseTitle", null, "<web.baseTitle>", loc);
        String pageTitle = messageSource.getMessage("web.createSessionTitle", null, "<web.baseTitle>", loc);
        String documentTitle = Utils.composeDocumentTitle(pageTitle, baseTitle);
        model.addAttribute("pageTitle", documentTitle);

        // TODO: check campaign name against list of available campaigns
        String name = campaignName != null ? campaignName : messageSource.getMessage("web.cantStartSession", null, "<web.cantStartSession>", loc);
        model.addAttribute("campaignName", name);

        addModelText("web.startNewSession", "createSession", model, loc);
        addModelText("web.forCampaign", "forCampaign", model, loc);
        addModelText("web.cancel", "cancel", model, loc);
        addModelText("web.roomNameLabel", "roomNameLabel", model, loc);
        addModelText("web.roomPasswordLabel", "roomPasswordLabel", model, loc);
        addModelText("web.newSessionLabel", "newSessionLabel", model, loc);

        return "createsession";
    }

    // TODO: this method also exists in HallrequestHandler, refactor
    /** Sets a localized text as attribute of the model.
     * @author Dragonstb
     * @since 0.0.2;
     * @param messageKey The key used for locating the text in the message bundle.
     * @param modelKey The key of the attribute filled with the text.
     * @param model The model.
     * @param loc The localization used.
     */
    private void addModelText(String messageKey, String modelKey, Model model, Locale loc) {
        String text = messageSource.getMessage(messageKey, null, "<"+messageKey+">", loc);
        model.addAttribute(modelKey, text);
    }

}
