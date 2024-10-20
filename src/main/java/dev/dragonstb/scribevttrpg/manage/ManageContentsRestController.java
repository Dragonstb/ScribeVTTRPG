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

import dev.dragonstb.scribevttrpg.content.ContentManager;
import dev.dragonstb.scribevttrpg.content.DefaultContentManager;
import dev.dragonstb.scribevttrpg.utils.Constants;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** Handles the machine communication when managing the personal game contents/
 *
 * @author Dragonstb
 * @since 0.0.5;
 */
@RestController
public class ManageContentsRestController {

    @GetMapping("/manage/contentlist")
    public String getContentList( HttpServletRequest request ) {



        ContentManager cm = (ContentManager)request.getSession().getAttribute( Constants.KEY_CONTENT_MANAGER );
        if( cm == null ) {
            cm = new DefaultContentManager();
            request.getSession().setAttribute( Constants.KEY_CONTENT_MANAGER, cm );
        }

        JSONArray contents = cm.getContentsAsJson( null );
        return contents.toString();
    }

}
