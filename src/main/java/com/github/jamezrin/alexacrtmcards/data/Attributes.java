package com.github.jamezrin.alexacrtmcards.data;

import com.github.jamezrin.crtmcards.types.CardRenewal;
import com.github.jamezrin.crtmcards.types.CardType;
import com.github.jamezrin.crtmcards.types.CrtmCard;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Attributes {
    private CardInfo savedCardInfo;
    private CrtmCard lastCardState;

    public Attributes() {}

    public Attributes(CardInfo savedCardInfo, CrtmCard lastCardState) {
        this.savedCardInfo = savedCardInfo;
        this.lastCardState = lastCardState;
    }

    public CardInfo getSavedCardInfo() {
        return savedCardInfo;
    }

    public CrtmCard getLastCardState() {
        return lastCardState;
    }

    public void setSavedCardInfo(CardInfo savedCardInfo) {
        this.savedCardInfo = savedCardInfo;
    }

    public void setLastCardState(CrtmCard lastCardState) {
        this.lastCardState = lastCardState;
    }

    @Override
    public String toString() {
        return "Attributes{" +
                "savedCardInfo=" + savedCardInfo +
                ", lastCardState=" + lastCardState +
                '}';
    }

    public static Map<String, Object> toMap(Attributes attributes) {
        Map<String, Object> res = new HashMap<>();

        if (attributes.getSavedCardInfo() != null) {
            Map<String, Object> savedCardInfoMap = new HashMap<>();
            savedCardInfoMap.put("ttp_prefix", attributes.getSavedCardInfo().getCardPrefix());
            savedCardInfoMap.put("ttp_number", attributes.getSavedCardInfo().getCardNumber());
            res.put("savedCardInfo", savedCardInfoMap);
        }

        if (attributes.getLastCardState() != null) {
            Map<String, Object> lastCardStateMap = new HashMap<>();
            lastCardStateMap.put("fullNum", attributes.getLastCardState().getFullNum());
            lastCardStateMap.put("title", attributes.getLastCardState().getTitle());
            lastCardStateMap.put("type", attributes.getLastCardState().getType().name());

            List<Map<String, Object>> cardRenewalsList = new ArrayList<>();
            for (CardRenewal renewal : attributes.getLastCardState().getRenewals()) {
                Map<String, Object> renewalMap = new HashMap<>();
                renewalMap.put("rechargeDate", renewal.getRechargeDate().toString());
                renewalMap.put("validityStartDate", renewal.getValidityStartDate().toString());
                renewalMap.put("firstUseDate", renewal.getFirstUseDate().toString());
                renewalMap.put("expirationDate", renewal.getExpirationDate().toString());
                cardRenewalsList.add(renewalMap);
            }

            lastCardStateMap.put("renewals", cardRenewalsList);
            lastCardStateMap.put("expirationDate", attributes.getLastCardState().getExpirationDate().toString());
            lastCardStateMap.put("profiles", attributes.getLastCardState().getProfiles().entrySet().stream()
                    .collect(Collectors.toMap(k -> k.getKey().name(), v -> v.getValue().toString())));
            res.put("lastCardState", lastCardStateMap);
        }

        return res;
    }

    public static Attributes fromMap(Map<String, Object> map) {
        Attributes attributes = new Attributes();

        Map savedCardInfoMap = (Map) map.get("savedCardInfo");
        if (savedCardInfoMap != null) {
            attributes.setSavedCardInfo(new CardInfo(
                    (String) savedCardInfoMap.get("ttp_prefix"),
                    (String) savedCardInfoMap.get("ttp_number")
            ));
        }

        Map lastCardStateMap = (Map) map.get("lastCardState");
        if (lastCardStateMap != null) {
            List<CardRenewal> cardRenewalsList = new ArrayList<>();

            EnumMap<CardType, LocalDate> profilesMap = new EnumMap<>(CardType.class);

            attributes.setLastCardState(new CrtmCard(
                    (String) lastCardStateMap.get("fullNum"),
                    (String) lastCardStateMap.get("title"),
                    CardType.valueOf((String) lastCardStateMap.get("type")),
                    cardRenewalsList.toArray(new CardRenewal[cardRenewalsList.size()]),
                    LocalDate.parse((String) lastCardStateMap.get("expirationDate")),
                    profilesMap
            ));
        }

        return attributes;
    }
}
