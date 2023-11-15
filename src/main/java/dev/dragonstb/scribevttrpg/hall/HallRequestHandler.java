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
package dev.dragonstb.scribevttrpg.hall;

import dev.dragonstb.scribevttrpg.campaigns.Campaign;
import dev.dragonstb.scribevttrpg.storage.StorageInterface;
import dev.dragonstb.scribevttrpg.utils.Utils;
import java.util.List;
import java.util.Locale;
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

        String pageTitle = messageSource.getMessage("web.hall.pageTitle", null, "<web.hall.pageTitle>", loc);
        model.addAttribute("personalHall", pageTitle);

        String baseTitle = messageSource.getMessage("web.common.baseTitle", null, "<web.common.baseTitle>", loc);
        String documentTitle = Utils.composeDocumentTitle(pageTitle, baseTitle);
        model.addAttribute("documentTitle", documentTitle);

        addModelText("web.hall.accountSettings", "accountSettings", model, loc);
        addModelText("web.hall.campaigns", "campaignsLabel", model, loc);
        addModelText("web.hall.newCampaign", "newCampaign", model, loc);
        addModelText("web.hall.manageCampaignContent", "manageCampaignContent", model, loc);
        addModelText("web.hall.startSession", "startSession", model, loc);
        addModelText("web.common.logout", "logout", model, loc);

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
