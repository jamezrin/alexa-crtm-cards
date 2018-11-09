package com.jamezrin.alexaskills.crtm_cards.scraper.backyard;

import com.jamezrin.alexaskills.crtm_cards.scraper.exceptions.InactiveCardNumberException;
import com.jamezrin.alexaskills.crtm_cards.scraper.exceptions.InvalidCardNumberException;
import com.jamezrin.alexaskills.crtm_cards.scraper.exceptions.NotExistentCardNumberException;
import com.jamezrin.alexaskills.crtm_cards.scraper.exceptions.ScraperException;
import com.jamezrin.alexaskills.crtm_cards.scraper.types.Card;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;

import static com.jamezrin.alexaskills.crtm_cards.AppConsts.*;

//TODO work in progress
public class OldResponseParser {
    public static final DocumentChecker[] DOCUMENT_CHECKERS = {
            new DocumentChecker() {
                @Override
                public void check(Document document) throws ScraperException {
                    Element element = document.getElementById("ctl00_cntPh_lblError"); // make it a constant
                    if (element != null) {
                        String text = element.wholeText();
                        if (text.equals("POR FAVOR, INTRODUZCA DE NUEVO SU TARJETA")) { // make it a constant
                            throw new NotExistentCardNumberException(text);
                        } else if (text.equals("TARJETA NO ACTIVA")) {
                            throw new InactiveCardNumberException(text);
                        } else {
                            throw new ScraperException(text);
                        }
                    }
                }
            },
            new DocumentChecker() {
                @Override
                public void check(Document document) throws ScraperException {
                    Elements elements = document.select("body script"); // make it a constant
                    for (Element element : elements) {
                        String text = element.wholeText();
                        if (text.equals("javascript:alert('Error. Debe introducir un número de 10 dígitos, tal y como se muestra en la imagen')")) {
                            throw new InvalidCardNumberException(text);
                        }
                    }
                }
            }
    };

    private final InputStream inputStream;
    private Document document;

    public OldResponseParser(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Card scrape() throws ScraperException {
        if (document == null) {
            try {
                document = Jsoup.parse(inputStream, "UTF-8", CRTM_BASE_URI);
            } catch (IOException e) {
                throw new ScraperException("Could not parse the data returned by the endpoint", e);
            }
        }

        for (DocumentChecker checker : DOCUMENT_CHECKERS) {
            checker.check(document);
        }

        // TODO use some kind of extractor structure with the document
        Element element = document.getElementById("ctl00_cntPh_tableResultados");

        // ctl00_cntPh_tableResultados
        // ctl00_cntPh_lblFechaCaducidadTarjeta
        // ctl00_cntPh_lblInfoDatosDeLaTarjeta
        return null;
    }

    public static abstract class DocumentChecker {
        public abstract void check(Document document) throws ScraperException;
    }
}
