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

        String baseTitle = messageSource.getMessage("web.common.baseTitle", null, "<web.common.baseTitle>", loc);
        String pageTitle = messageSource.getMessage("web.create.pageTitle", null, "<web.create.pageTitle>", loc);
        String documentTitle = Utils.composeDocumentTitle(pageTitle, baseTitle);
        model.addAttribute("documentTitle", documentTitle);

        // TODO: check campaign name against list of available campaigns
        String name = campaignName != null ? campaignName :
                messageSource.getMessage("web.create.cantStartSession", null, "<web.create.cantStartSession>", loc);
        model.addAttribute("campaignName", name);

        addModelText("web.create.startNewSession", "startNewSession", model, loc);
        addModelText("web.create.forCampaign", "forCampaign", model, loc);
        addModelText("web.common.cancel", "cancel", model, loc);
        addModelText("web.create.roomNameLabel", "roomNameLabel", model, loc);
        addModelText("web.create.roomPasswordLabel", "roomPasswordLabel", model, loc);
        addModelText("web.create.newSessionLabel", "newSessionLabel", model, loc);

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
