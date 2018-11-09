package com.jamezrin.alexaskills.crtm_cards.scraper.types;

import java.util.Date;
import java.util.EnumMap;

public class Card {
    private final String fullNum;
    private final String title;
    private final CardType type;

    private final CardRenewal[] renewals;

    private final Date expiration;
    private final EnumMap<CardType, Date> profiles;

    public Card(String fullNum, String title, CardType type, CardRenewal[] renewals, Date expiration, EnumMap<CardType, Date> profiles) {
        this.fullNum = fullNum;
        this.title = title;
        this.type = type;
        this.renewals = renewals;
        this.expiration = expiration;
        this.profiles = profiles;
    }

    public String getFullNum() {
        return fullNum;
    }

    public String getTitle() {
        return title;
    }

    public CardType getType() {
        return type;
    }

    public CardRenewal[] getRenewals() {
        return renewals;
    }

    public Date getExpiration() {
        return expiration;
    }

    public EnumMap<CardType, Date> getProfiles() {
        return profiles;
    }
}
