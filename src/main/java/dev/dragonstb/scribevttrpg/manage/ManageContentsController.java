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

package dev.dragonstb.scribevttrpg.manage;

import dev.dragonstb.scribevttrpg.utils.Utils;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/** Handles requests from the manage content web page.
 *
 * @author Dragonstb
 * @since 0.0.5;
 */
@Controller
public class ManageContentsController {

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/manage")
    public String getManageContents(@RequestHeader("Accept-Language") Locale loc, Model model) {
        String pageTitle = messageSource.getMessage("web.manage.pageTitle", null, "<web.manage.pageTitle>", loc);
        String baseTitle = messageSource.getMessage("web.common.baseTitle", null, "<web.common.baseTitle>", loc);
        String docTitle = Utils.composeDocumentTitle( pageTitle, baseTitle );
        model.addAttribute("documentTitle", docTitle );
        model.addAttribute("pageTitle", pageTitle );

        addModelText( "web.manage.addContent", "addContentBtnLabel", model, loc);
        addModelText( "web.manage.copyContent", "copyContentBtnLabel", model, loc);
        addModelText( "web.manage.deleteContent", "deleteContentBtnLabel", model, loc);
        addModelText( "web.manage.backToHall", "backToHall", model, loc);

        model.addAttribute( "fetchFromServer", "const fetchFromServer = true;" );
        return "managecontents";
    }

    /** Sets a localized text as attribute of the model.
     * @author Dragonstb
     * @since 0.0.5;
     * @param messageKey The key used for locating the text in the message bundle.
     * @param modelKey The key of the attribute filled with the text.
     * @param model The model.
     * @param loc The localization used.
     */
    private void addModelText(String messageKey, String modelKey, Model model, Locale loc) {
        // TODO: the same method exists on other controllers
        String text = messageSource.getMessage(messageKey, null, "<"+messageKey+">", loc);
        model.addAttribute(modelKey, text);
    }

}
