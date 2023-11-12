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
