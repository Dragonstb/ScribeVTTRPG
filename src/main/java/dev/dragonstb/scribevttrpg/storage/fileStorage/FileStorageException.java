package dev.dragonstb.scribevttrpg.storage.fileStorage;

/**
 *
 * @author Dragonstb
 * @since ;
 */
public class FileStorageException extends RuntimeException {

    public FileStorageException(String message) {
		super(message);
	}

	public FileStorageException(String message, Throwable cause) {
		super(message, cause);
	}

}
