package dev.dragonstb.scribevttrpg.hall;

import dev.dragonstb.scribevttrpg.campaigns.Campaign;
import dev.dragonstb.scribevttrpg.storage.StorageInterface;
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

    private StorageInterface storage;
    @Autowired
    private MessageSource messageSource;

    @GetMapping("/hall")
    public String getHallPage(@RequestHeader("Accept-Language") Locale loc, Model model) {

        String accountSettings = messageSource.getMessage("web.accountSettings", null, "<web.accountSettings>", loc);
        model.addAttribute("accountSettings", accountSettings);
        String campaignsLabel = messageSource.getMessage("web.campaigns", null, "<web.campaigns>", loc);
        model.addAttribute("campaignsLabel", campaignsLabel);

        try {
            // TODO: fill campaign list from persistent data
//            UUID uid = UUID.fromString("123456789");
//            List<Campaign> campaigns = storage.getAllCampaigns(uid);
            List<Campaign> campaigns = new ArrayList<>();
            for (int cnt = 0; cnt < 2; cnt++) {
                campaigns.add( Campaign.create("Title "+cnt, "system "+cnt) );
            }
            model.addAttribute("campaigns", campaigns);
        } catch (Exception e) {
            // TODO: switch to logback
            Logger log = Logger.getLogger("HallRequestHandler");
            log.warning(e.getLocalizedMessage());
        }

        return "hall";
    }

}
