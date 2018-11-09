package com.jamezrin.alexaskills.crtm_cards.scraper.types;

import java.util.Date;
import java.util.EnumMap;

public class Card {
    private final String ttpCode;
    private final String ttpNum;
    private final String fullNum;

    private final String title;
    private final Profile type;

    private final Recharge[] recharges;

    private final Date expiration;
    private final EnumMap<Profile, Date> profiles;

    public Card(String ttpCode, String ttpNum, String fullNum, String title, Profile type, Recharge[] recharges, Date expiration, EnumMap<Profile, Date> profiles) {
        this.ttpCode = ttpCode;
        this.ttpNum = ttpNum;
        this.fullNum = fullNum;
        this.title = title;
        this.type = type;
        this.recharges = recharges;
        this.expiration = expiration;
        this.profiles = profiles;
    }

    public String getTtpCode() {
        return ttpCode;
    }

    public String getTtpNum() {
        return ttpNum;
    }

    public String getFullNum() {
        return fullNum;
    }

    public String getTitle() {
        return title;
    }

    public Profile getType() {
        return type;
    }

    public Recharge[] getRecharges() {
        return recharges;
    }

    public Date getExpiration() {
        return expiration;
    }

    public EnumMap<Profile, Date> getProfiles() {
        return profiles;
    }
}
