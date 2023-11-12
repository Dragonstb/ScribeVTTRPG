package dev.dragonstb.scribevttrpg.storage;

import dev.dragonstb.scribevttrpg.campaigns.Campaign;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.NonNull;

/** An interface abstracting the database and file storage access.
 *
 * @author Dragonstb
 * @since 0.0.2;
 */
public interface StorageInterface {

    /** Gets the user id of the given user.
     * @since 0.0.2;
     * @param userName The user's username.
     * @return The user's uid.
     */
    public UUID getUidByUsername(@NonNull String userName);

    /** Returns a list of all of the user's campaigns.
     * @author Dragonstb
     * @since 0.0.2;
     * @param uid User's uid.
     * @return His/her campaigns,
     */
    public List<Campaign> getAllCampaigns(@NonNull UUID uid);


}
