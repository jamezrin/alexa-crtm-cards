package com.github.jamezrin.alexacrtmcards.data;

import com.github.jamezrin.crtmcards.types.CrtmCard;

public final class UserAttributes {
    private UserCardInfo savedCardInfo;
    private CrtmCard lastCardState;
    private boolean firstUse;

    public UserAttributes() {
        this.savedCardInfo = null;
        this.lastCardState = null;
        this.firstUse = true;
    }

    public UserAttributes(UserCardInfo savedCardInfo, CrtmCard lastCardState, boolean firstUse) {
        this.savedCardInfo = savedCardInfo;
        this.lastCardState = lastCardState;
        this.firstUse = firstUse;
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

    public boolean isFirstUse() {
        return firstUse;
    }

    public void setFirstUse(boolean firstUse) {
        this.firstUse = firstUse;
    }

    @Override
    public String toString() {
        return "UserAttributes{" +
                "savedCardInfo=" + savedCardInfo +
                ", lastCardState=" + lastCardState +
                ", firstUse=" + firstUse +
                '}';
    }
}
