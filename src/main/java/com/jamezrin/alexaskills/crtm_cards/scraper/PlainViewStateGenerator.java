package com.jamezrin.alexaskills.crtm_cards.scraper;

import java.time.Instant;

public class PlainViewStateGenerator {
    private final Instant instant;

    public PlainViewStateGenerator() {
        this.instant = Instant.now();
    }

    public PlainViewStateGenerator(Instant instant) {
        this.instant = instant;
    }

    public Instant getInstant() {
        return instant;
    }

    public String generate() {
        return null;
    }
}
