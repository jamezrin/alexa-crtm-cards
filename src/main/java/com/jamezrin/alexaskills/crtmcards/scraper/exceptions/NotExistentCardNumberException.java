package com.jamezrin.alexaskills.crtmcards.scraper.exceptions;

public class NotExistentCardNumberException extends InvalidCardNumberException {
    public NotExistentCardNumberException(String message) {
        super(message);
    }
}
