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

package dev.dragonstb.scribevttrpg.game;

import dev.dragonstb.scribevttrpg.GameManager;
import static dev.dragonstb.scribevttrpg.game.ParticipationStatus.none;
import static dev.dragonstb.scribevttrpg.game.ParticipationStatus.participating;
import static dev.dragonstb.scribevttrpg.game.ParticipationStatus.waiting;
import dev.dragonstb.scribevttrpg.game.exceptions.GameNotFoundException;
import dev.dragonstb.scribevttrpg.game.exceptions.NotInGameException;
import dev.dragonstb.scribevttrpg.game.participant.Participant;
import dev.dragonstb.scribevttrpg.utils.LocKeys;
import dev.dragonstb.scribevttrpg.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.server.ResponseStatusException;

/** The request controller for the game web page
 *
 * @author Dragonstb
 * @since 0.0.3;
 */
@Controller
public class GameController {

    @Autowired
    private GameUtils gameUtils;

    @Autowired
    private MessageSource messageSource;

    @GetMapping("/game/{roomName:.+}")
    public String getGamePage(HttpServletRequest request, @PathVariable String roomName,
            @RequestHeader("Accept-Language") Locale loc, Model model) {
        // TODO: validate room name
        HttpSession httpSession = request.getSession();
        Map<String, Participant> participations = GameUtils.getParticipationsAndCreateIfNeeded( httpSession );

        ParticipationStatus status;
        try {
            status = gameUtils.getUserParticipationStatus( participations, roomName );
        } catch ( GameNotFoundException gnfe ) {
            throw new ResponseStatusException( HttpStatus.NOT_FOUND );
        } catch ( NotInGameException gnfe ) {
            status = ParticipationStatus.none;
        }

        String result = switch( status ) {
            case participating -> serveGamePage( roomName, model, loc );
            case none -> "redirect:" + Utils.getJoinPath( roomName );
            case waiting -> "redirect:" + Utils.getWaitPath( roomName );
        };

        return result;
    }

    /** Returns the page where people who want to join the game can set their name and hand in their join request.
     * @since 0.1.0
     * @author Dragonstb
     * @param request The servlet request object.
     * @param roomName The name of the room the person wants to enter.
     * @param loc Localization to be used.
     * @param model The MVC object.
     * @return Name of the join page.
     */
    @GetMapping("/join/{roomName:.+}")
    public String getJoinPage(HttpServletRequest request, @PathVariable String roomName,
            @RequestHeader("Accept-Language") Locale loc, Model model ) {
        // TODO: validate room name
        HttpSession httpSession = request.getSession();
        Map<String, Participant> participations = GameUtils.getParticipationsAndCreateIfNeeded( httpSession );

        ParticipationStatus status;
        try {
            status = gameUtils.getUserParticipationStatus( participations, roomName );
        } catch ( GameNotFoundException gnfe ) {
            throw new ResponseStatusException( HttpStatus.NOT_FOUND );
        } catch ( NotInGameException gnfe ) {
            status = ParticipationStatus.none;
        }

        String result = switch( status ) {
            case none -> serveJoinPage( roomName, model, loc );
            case participating -> "redirect:" + Utils.getGamePath( roomName );
            case waiting -> "redirect:" + Utils.getWaitPath( roomName );
        };

        return result;
    }

    /** Returns the page on that people who want to join the game wait until their join request is accepted or
     * rejected.
     * @since 0.1.0
     * @author Dragonstb
     * @param request The servlet request object.
     * @param roomName The name of the room the person wants to enter.
     * @param loc Localization to be used.
     * @param model The MVC object.
     * @return Name of the join page.
     */
    @GetMapping("/wait/{roomName:.+}")
    public String getWaitPage(HttpServletRequest request, @PathVariable String roomName,
            @RequestHeader("Accept-Language") Locale loc, Model model) {
        // TODO: validate room name
        HttpSession httpSession = request.getSession();
        Map<String, Participant> participations = GameUtils.getParticipationsAndCreateIfNeeded( httpSession );

        ParticipationStatus status;
        try {
            status = gameUtils.getUserParticipationStatus( participations, roomName );
        } catch ( GameNotFoundException gnfe ) {
            throw new ResponseStatusException( HttpStatus.NOT_FOUND );
        } catch ( NotInGameException gnfe ) {
            status = ParticipationStatus.none;
        }

        String result = switch( status ) {
            case waiting -> serveWaitPage( roomName, model, loc );
            case participating -> "redirect:" + Utils.getGamePath( roomName );
            case none -> "redirect:" + Utils.getJoinPath( roomName );
        };

        return result;
    }

    /** Assembles the model for the game page.
     * @author Dragonstb;
     * @since 0.1.0;
     * @param roomName Name of the room.
     * @param model Page model.
     * @param loc Locale used for the localization.
     * @return Name of the game page html file.
     */
    private String serveGamePage( String roomName, Model model, Locale loc ) {
        String baseTitle = messageSource.getMessage("web.common.baseTitle", null, "<web.common.baseTitle>", loc);
        String campaignName="hello RPG world"; // TODO: fetch campaign title from the room associated with the name
        String documentTitle = Utils.composeDocumentTitle(campaignName, baseTitle != null ? baseTitle : "ScribeVTTRPG");

        model.addAttribute("pageTitle", campaignName);
        model.addAttribute("documentTitle", documentTitle);
        model.addAttribute("roomName", roomName);

        return "game";
    }

    /** Assembles the model for the start-join-process page.
     * @since 0.1.0
     * @author Dragonstb
     * @param roomName Name of the room.
     * @param model Page model.
     * @param loc Locale used for the localization.
     * @return Name of the join page html file.
     */
    private String serveJoinPage( @NonNull String roomName, @NonNull Model model, @NonNull Locale loc) {
        String baseTitle = getMessage( LocKeys.BASE_TITLE, loc);
        String docTitlePart = getMessage( LocKeys.JOIN_PAGE_TITLE, loc );
        String documentTitle = Utils.composeDocumentTitle(docTitlePart, baseTitle != null ? baseTitle : "ScribeVTTRPG");
        String pageTitle = getMessage( LocKeys.JOIN_PAGE_TITLE, loc );
        String welcomeText = getMessage( LocKeys.JOIN_WELCOME_TEXT, loc );
        String nameLabel = getMessage( LocKeys.JOIN_NAME_LABEL, loc );
        String pwLabel = getMessage( LocKeys.JOIN_PW_LABEL, loc );
        String btnLabel = getMessage( LocKeys.JOIN_JOIN_BUTTON_LABEL, loc );

        // TODO: weave room name into the welcome text, or the page title
        // TODO: prefill name field with user name from Principal, if the user is logged in
        model.addAttribute( "documentTitle", documentTitle );
        model.addAttribute( "pageTitle", pageTitle );
        model.addAttribute( "welcomeText", welcomeText );
        model.addAttribute( "nameFieldLabel", nameLabel );
        model.addAttribute( "roomPasswordLabel", pwLabel );
        model.addAttribute( "roomName", roomName );
        model.addAttribute( "requestJoinLabel", btnLabel );

        return "joinsession";
    }

    /** Assembles the model for the wait page.
     * @since 0.1.0
     * @author Dragonstb
     * @param roomName Name of the room.
     * @param model Page model.
     * @param loc Locale used for the localization.
     * @return Name of the join page html file.
     */
    private String serveWaitPage( @NonNull String roomName, @NonNull Model model, @NonNull Locale loc) {
        String baseTitle = getMessage( LocKeys.BASE_TITLE, loc);
        String docTitlePart = getMessage( LocKeys.WAIT_DOC_TITLE, loc );
        String documentTitle = Utils.composeDocumentTitle(docTitlePart, baseTitle != null ? baseTitle : "ScribeVTTRPG");
        String pageTitle = getMessage( LocKeys.WAIT_PAGE_TITLE, loc );
        String waitText = getMessage( LocKeys.WAIT_WAIT_TEXT, loc );
        String btnLabel = getMessage( LocKeys.WAIT_ABORT_LABEL, loc );

        // TODO: weave room name into the welcome text, or the page title
        // TODO: prefill name field with user name from Principal, if the user is logged in
        model.addAttribute( "documentTitle", documentTitle );
        model.addAttribute( "pageTitle", pageTitle );
        model.addAttribute( "waitText", waitText );
        model.addAttribute( "abortJoinLabel", btnLabel );
        model.addAttribute( "roomName", roomName );

        return "wait";
    }

    /** Gets the localized string from the message source.
     * @since 0.1.0;
     * @author Dragonstb
     * @param key Localization key.
     * @param locale Locale used.
     * @return Localized string.
     */
    private String getMessage( @NonNull String key, @NonNull Locale locale ) {
        return messageSource.getMessage( key, null, "<"+key+">", locale );
    }
}
