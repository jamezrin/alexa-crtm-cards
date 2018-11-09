package com.jamezrin.alexaskills.crtm_cards.scraper.exceptions;

public class InactiveCardNumberException extends NotExistentCardNumberException {
    public InactiveCardNumberException(String message) {
        super(message);
    }
}
