package dev.dragonstb.scribevttrpg.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/** Storage configuration properties.
 *
 * @author Dragonstb
 * @since 0.0.2;
 */
@Configuration
@ConfigurationProperties(prefix = "scribe.storage")
public class StorageConfig {

    private String fileStorageDir;

    /**
     * Get the value of fileStorageDir
     *
     * @return the value of fileStorageDir
     */
    public String getFileStorageDir() {
        return fileStorageDir;
    }

    public void setFileStorageDir(String fileStorageDir) {
        this.fileStorageDir = fileStorageDir;
    }


}
