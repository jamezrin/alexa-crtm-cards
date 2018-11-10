package com.jamezrin.alexaskills.crtmcards.scraper.exceptions;

public class InactiveCardNumberException extends NotExistentCardNumberException {
    public InactiveCardNumberException(String message) {
        super(message);
    }
}
