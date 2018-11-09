package com.jamezrin.alexaskills.crtm_cards.scraper.exceptions;

public class NotExistentCardNumberException extends InvalidCardNumberException {
    public NotExistentCardNumberException(String message) {
        super(message);
    }
}
