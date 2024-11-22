package org.clx.library.exception;

public class UnauthorizedBookDeletionException extends RuntimeException {
    public UnauthorizedBookDeletionException(String message) {
        super(message);
    }

    public UnauthorizedBookDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
