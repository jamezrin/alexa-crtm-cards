package com.jamezrin.alexaskills.crtm_cards.scraper.types;

import java.util.Date;

public class CardRenewal {
    private final Date rechargeDate;
    private final Date validityStartDate;
    private final Date firstUseDate;
    private final Date expirationDate;

    public CardRenewal(Date rechargeDate, Date validityStartDate, Date firstUseDate, Date expirationDate) {
        this.rechargeDate = rechargeDate;
        this.validityStartDate = validityStartDate;
        this.firstUseDate = firstUseDate;
        this.expirationDate = expirationDate;
    }

    public Date getRechargeDate() {
        return rechargeDate;
    }

    public Date getValidityStartDate() {
        return validityStartDate;
    }

    public Date getFirstUseDate() {
        return firstUseDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }
}
