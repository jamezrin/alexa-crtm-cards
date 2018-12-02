package com.github.jamezrin.alexacrtmcards.handlers.data;

public class CardInfo {
    private final String cardPrefix;
    private final String cardNumber;

    public CardInfo(String cardPrefix, String cardNumber) {
        this.cardPrefix = cardPrefix;
        this.cardNumber = cardNumber;
    }

    public String getCardPrefix() {
        return cardPrefix;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    @Override
    public String toString() {
        return "CardInfo{" +
                "cardPrefix='" + cardPrefix + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                '}';
    }
}
