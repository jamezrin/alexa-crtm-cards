package com.jamezrin.alexaskills.crtm_cards.scraper;

import com.jamezrin.alexaskills.crtm_cards.scraper.exceptions.ScraperException;
import com.jamezrin.alexaskills.crtm_cards.scraper.types.Card;
import com.jamezrin.alexaskills.crtm_cards.scraper.types.CardRenewal;
import com.jamezrin.alexaskills.crtm_cards.scraper.types.CardType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import static com.jamezrin.alexaskills.crtm_cards.AppConsts.CRTM_BASE_URI;

public class ResponseParser {
    private final InputStream inputStream;

    public ResponseParser(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Card parse() throws ScraperException {
        try {
            Document document = Jsoup.parse(inputStream, "UTF-8", CRTM_BASE_URI);

            // TODO Check for errors before processing

            return processCard(document);
        } catch (IOException e) {
            throw new ScraperException("Could not parse the data returned by the endpoint", e);
        }
    }

    private Card processCard(Document document) {
        Element fullNumEl = document.getElementById("ctl00_cntPh_lblNumeroTarjeta");

        Element resultsTableEl = document.getElementById("ctl00_cntPh_tableResultados");
        Element resultRowEl = resultsTableEl.getElementsByTag("td").get(1);
        Elements results = resultRowEl.getElementsByTag("span");

        Element expirationEl = document.getElementById("ctl00_cntPh_lblFechaCaducidadTarjeta");
        Element profilesEl = document.getElementById("ctl00_cntPh_lblInfoDatosDeLaTarjeta");

        return new Card(
                fullNumEl.text(),
                results.get(0).text(),
                processCardType(results.get(1)),

                new CardRenewal[] {
                        processRenewals(results.subList(2, 5)),
                        processRenewals(results.subList(6, 9))
                },

                null, // from the extra info (diff id)
                null // from the extra info (diff id)
        );
    }


    // private:
    //  processCard

    // private:
    //  processCardType
    //  processRenewals()
    //  processProfileDates()
    //  processExpiration()

    private CardType processCardType(Element element) {
        for (CardType type : CardType.values()) {
            if (type.getId().equals(element.text())) {
                return type;
            }
        }

        return null;
    }

    private CardRenewal processRenewals(List<Element> list) {
        return null;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
