package com.github.jamezrin.alexacrtmcards.data;

public class CardInfo {
    private String cardPrefix;
    private String cardNumber;

    public CardInfo(String cardPrefix, String cardNumber) {
        this.cardPrefix = cardPrefix;
        this.cardNumber = cardNumber;
    }

    public String getCardPrefix() {
        return cardPrefix;
    }

    public void setCardPrefix(String cardPrefix) {
        this.cardPrefix = cardPrefix;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public String toString() {
        return "CardInfo{" +
                "cardPrefix='" + cardPrefix + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                '}';
    }
}
