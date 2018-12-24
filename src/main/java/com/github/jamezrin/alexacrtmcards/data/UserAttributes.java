package com.github.jamezrin.alexacrtmcards.data;

import com.github.jamezrin.crtmcards.types.CrtmCard;

public final class UserAttributes {
    private UserCardInfo savedCardInfo;
    private CrtmCard lastCardState;

    public UserAttributes() {
        this.savedCardInfo = null;
        this.lastCardState = null;
    }

    public UserAttributes(UserCardInfo savedCardInfo, CrtmCard lastCardState, boolean firstUse) {
        this.savedCardInfo = savedCardInfo;
        this.lastCardState = lastCardState;
    }

    public UserCardInfo getSavedCardInfo() {
        return savedCardInfo;
    }

    public void setSavedCardInfo(UserCardInfo savedCardInfo) {
        this.savedCardInfo = savedCardInfo;
    }

    public CrtmCard getLastCardState() {
        return lastCardState;
    }

    public void setLastCardState(CrtmCard lastCardState) {
        this.lastCardState = lastCardState;
    }

    @Override
    public String toString() {
        return "UserAttributes{" +
                "savedCardInfo=" + savedCardInfo +
                ", lastCardState=" + lastCardState +
                '}';
    }
}
