/*
 * Copyright (c) 2024, Dragonstb
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

package dev.dragonstb.scribevttrpg.utils;

/** Localization keys for the message sources.
 *
 * @author Dragonstb
 * @since 0.0.6;
 */
public interface LocKeys {

    /** Base title that appears in the browser tabs. */
    public static final String BASE_TITLE = "web.common.baseTitle";

    /** When creating a room: the chosen name for the room is occupied. */
    public static final String CREATE_ROOM_NAME_TAKEN = "web.create.roomNameTaken";
    /** When creating a room: the chosen name has an invalid format (length, characters used). */
    public static final String CREATE_ROOM_NAME_INVALID = "web.create.roomNameInvalid";

    /** When starting the join process: document title in the tab. */
    public static final String JOIN_DOC_TITLE = "web.join.documentTitle";
    /** When starting the join process: page title in the h1 tag. */
    public static final String JOIN_PAGE_TITLE = "web.join.pageTitle";
    /** When starting the join process: a welcome text. */
    public static final String JOIN_WELCOME_TEXT = "web.join.welcomeText";
    /** When starting the join process: label for the name field. */
    public static final String JOIN_NAME_LABEL = "web.join.nameFieldLabel";
    /** When starting the join process: label for the password field. */
    public static final String JOIN_PW_LABEL = "web.join.roomPasswordLabel";
    /** When starting the join process: label for the join button. */
    public static final String JOIN_JOIN_BUTTON_LABEL = "web.join.joinButtonLabel";

}
