package com.jamezrin.alexaskills.crtm_cards.scraper;

import com.jamezrin.alexaskills.crtm_cards.scraper.exceptions.ScraperException;
import com.jamezrin.alexaskills.crtm_cards.scraper.types.Card;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;

import static com.jamezrin.alexaskills.crtm_cards.AppConsts.CRTM_BASE_URI;

public class ResponseParser {
    private final InputStream inputStream;

    public ResponseParser(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Card parse() throws ScraperException {
        try {
            Document document = Jsoup.parse(inputStream, "UTF-8", CRTM_BASE_URI);

            System.out.println(document.getElementById("ctl00_cntPh_tableResultados").html());
        } catch (IOException e) {
            throw new ScraperException("Could not parse the data returned by the endpoint", e);
        }

        return null;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
