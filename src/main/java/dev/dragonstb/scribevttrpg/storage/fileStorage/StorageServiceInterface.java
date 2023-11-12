package dev.dragonstb.scribevttrpg.storage.fileStorage;

import java.nio.file.Path;
import java.util.stream.Stream;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/** Interface for file storage service.
 *
 * @author Dragonstb
 * @since 0.0.2;
 */
public interface StorageServiceInterface {

    void init();

	void store(MultipartFile file);

	Stream<Path> loadAll();

	Path load(String filename);

	Resource loadAsResource(String filename);

	void deleteAll();

}
