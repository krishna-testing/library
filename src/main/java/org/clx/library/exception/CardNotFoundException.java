package org.clx.library.exception;

public class CardNotFoundException extends RuntimeException{

    public CardNotFoundException(String message) {
        super(message);
    }
}
