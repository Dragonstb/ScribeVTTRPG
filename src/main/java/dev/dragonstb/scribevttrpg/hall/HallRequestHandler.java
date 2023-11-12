package dev.dragonstb.scribevttrpg.hall;

import dev.dragonstb.scribevttrpg.campaigns.Campaign;
import dev.dragonstb.scribevttrpg.storage.StorageInterface;
import dev.dragonstb.scribevttrpg.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/** Controller for the personal hall webpage.
 *
 * @author Dragonstb
 * @since 0.0.2;
 */
@Controller
public class HallRequestHandler {

    @Autowired
    private StorageInterface storage;
    @Autowired
    private MessageSource messageSource;

    @GetMapping("/hall")
    public String getHallPage(@RequestHeader("Accept-Language") Locale loc, Model model) {

        String hallLabel = messageSource.getMessage("web.personalHall", null, "<web.personalHall>", loc);
        model.addAttribute("personalHall", hallLabel);

        String baseTitle = messageSource.getMessage("web.baseTitle", null, "<web.baseTitle>", loc);
        String pageTitle = Utils.composeDocumentTitle(hallLabel, baseTitle);
        model.addAttribute("pageTitle", pageTitle);

//        String accountSettings = messageSource.getMessage("web.accountSettings", null, "<web.accountSettings>", loc);
//        model.addAttribute("accountSettings", accountSettings);
        addModelText("web.accountSettings", "accountSettings", model, loc);

//        String campaignsLabel = messageSource.getMessage("web.campaigns", null, "<web.campaigns>", loc);
//        model.addAttribute("campaignsLabel", campaignsLabel);
        addModelText("web.campaigns", "campaignsLabel", model, loc);

//        String newCampaignLabel = messageSource.getMessage("web.newCampaign", null, "<web.newCampaign>", loc);
//        model.addAttribute("newCampaign", newCampaignLabel);
        addModelText("web.newCampaign", "newCampaign", model, loc);

        String manageContentLabel = messageSource.getMessage("web.manageCampaignContent", null, "<web.manageCampaignContent>", loc);
        model.addAttribute("manageContent", manageContentLabel);

        String startSessionLabel = messageSource.getMessage("web.startSession", null, "<web.startSession>", loc);
        model.addAttribute("startSession", startSessionLabel);

        String logoutLabel = messageSource.getMessage("web.logout", null, "<web.logout>", loc);
        model.addAttribute("logout", logoutLabel);

        try {
            // TODO: derive uid from username once spring security has become activated
            String uid = "123456789";
            List<Campaign> campaigns = storage.getAllCampaigns(uid);
            model.addAttribute("campaigns", campaigns);
        } catch (Exception e) {
            // TODO: switch to logback
            Logger log = Logger.getLogger("HallRequestHandler");
            log.warning(e.getLocalizedMessage());
        }

        return "hall";
    }

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
