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
package dev.dragonstb.scribevttrpg.campaigns;

import dev.dragonstb.scribevttrpg.utils.Constants;
import org.springframework.lang.NonNull;

/** Collection of all the data belonging to a campaign.
 *
 * @author Dragonstb
 * @since 0.0.2;
 */
public final class Campaign {

    /** Name of the campaign. */
    @NonNull private String name;
    /** The rpg system played in this campaign. */
    @NonNull private String system;

    /** Generates.
     * @author Dragonstb
     * @since 0.0.2;
     * @param name Name of the campaign.
     * @param system The rpg system played in this campaign.
     */
    private Campaign(@NonNull String name, String system) {
        this.name = name;
        this.system = system != null ? system : Constants.EMPTY_STRING;
    }

    /** Creates a new instance of {@code Campaign}.
     * @author Dragonstb
     * @since 0.0.2;
     * @param name Name of the campaign. Must not be null.
     * @param system The rpg system played in this campaign. If {@code null} is passed here, the
     * @return A new instance of {@code Campaign}.
     */
    @NonNull
    public static Campaign create(String name, String system){
        if(name == null) {
            throw new IllegalArgumentException("name must not be null when creating a Campaign");
        }
        Campaign cam = new Campaign(name, system);
        return cam;
    }

    /** Gets the name of the campaign.
     * @author Dragonstb
     * @since 0.0.2;
     * @return The name of the campaign.
     */
    @NonNull
    public String getName() {
        return name;
    }

    /** Sets the name of the campaign. Must not be {@code null}.
     * @author Dragonstb
     * @since 0.0.2;
     * @param name The name of the campaign. Must not be {@code null}.
     */
    @NonNull
    public void setName(@NonNull String name) {
        this.name = name;
    }

    /** Gets the name of the system the campaign is played in. It could be the empty string.
     * @author Dragonstb
     * @since 0.0.2;
     * @return The name of the system the campaign is played in. It could be the empty string.
     */
    @NonNull
    public String getSystem() {
        return system;
    }

    /** Sets the System the campaign is played in. If {@code null} is passed here, the empty string is used.
     * @author Dragonstb
     * @since 0.0.2;
     * @param system The System the campaign is played in. If {@code null} is passed here, the empty string is used.
     */
    public void setSystem(String system) {
        this.system = system != null ? system : Constants.EMPTY_STRING;
    }


}
