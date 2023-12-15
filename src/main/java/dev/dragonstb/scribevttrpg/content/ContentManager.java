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

package dev.dragonstb.scribevttrpg.content;

import dev.dragonstb.scribevttrpg.game.handouts.AbstractHandoutPiece;
import java.util.List;
import org.json.JSONArray;

/** An instance of an implementation manages a registered user's private campaign contents, . All requests from clients on content items end up here, may it be from the game web page (via a
 * {@link dev.dragonstb.scribevttrpg.game.handouts.HandoutManager handout manager}) ot from the manage contents web
 * page.
 *
 * @author Dragonstb
 * @since 0.0.5;
 */
public interface ContentManager {

    /** Returns a list of either all content items or all content items belonging to the campaign.
     * @author Dragonstb
     * @since 0.0.5;
     * @param campaignName Name of the campaign you want the content items for. If passing {@code null} here, all
     * content items
     * are returned.
     * @return List of content items for specified campaign. If passing {@code null} here, all content items are
     * returned.
     */
    public List<ContentItem> getContents( String campaignName );

    /** Returns a list of either all content items or all content items belonging to the campaign, but as a
     * {@code JSONObject}.
     * @author Dragonstb
     * @since 0.0.5;
     * @param campaignName Name of the campaign you want the content items for. If passing {@code null} here, all
     * content items
     * are returned.
     * @return List of content items for specified campaign. If passing {@code null} here, all content items are
     * returned.
     */
    public JSONArray getContentsAsJson( String campaignName);

    /** Returns a list of either all handouts or all handouts belonging to the campaign.
     * @author Dragonstb
     * @since 0.0.5;
     * @param campaignName Name of the campaign you want the handouts for. If passing {@code null} here, all handouts
     * are returned.
     * @return List of handouts for specified campaign. If passing {@code null} here, all handouts are returned.
     */
    public List<AbstractHandoutPiece> getHandouts( String campaignName );


}
