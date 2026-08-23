package com.paraizo.mysocial.storage;

// client's fault: empty file, too large, or not a recognizable image. Distinct from
// StorageException (our fault / R2's fault) so a future controller can map this to 400 and
// StorageException to 500.
public class InvalidImageException extends RuntimeException {

    public InvalidImageException(String message) {
        super(message);
    }
}
