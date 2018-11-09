package com.jamezrin.alexaskills.crtm_cards.scraper;

import com.jamezrin.alexaskills.crtm_cards.scraper.exceptions.ScraperException;
import com.jamezrin.alexaskills.crtm_cards.scraper.types.Card;
import com.jamezrin.alexaskills.crtm_cards.scraper.types.CardType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

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

            // TODO Check for errors before parsing

            // TODO CALL FUNCTIONS GETTING THESE FIELDS
            Elements elements = document.select("#ctl00_cntPh_tableResultados td:nth-child(1) span");

            return new Card(
                    null,
                    elements.get(0).text(),
                    null,
                    null, // special method
                    null, // from the extra info (diff id)
                    null // from the extra info (diff id)
            );

            //System.out.println(document.getElementById("ctl00_cntPh_tableResultados").html());
        } catch (IOException e) {
            throw new ScraperException("Could not parse the data returned by the endpoint", e);
        }
    }

    // private:
    //  processCard

    // private:
    //  processCardType
    //  processRenewals()
    //  processProfileDates()
    //  processExpiration()

    public InputStream getInputStream() {
        return inputStream;
    }
}
