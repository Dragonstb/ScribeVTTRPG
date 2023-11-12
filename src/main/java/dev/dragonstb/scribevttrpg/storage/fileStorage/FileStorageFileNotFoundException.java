package dev.dragonstb.scribevttrpg.storage.fileStorage;

/**
 *
 * @author Dragonstb
 * @since ;
 */
public class FileStorageFileNotFoundException extends FileStorageException {
    
    public FileStorageFileNotFoundException(String message) {
		super(message);
	}

	public FileStorageFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
