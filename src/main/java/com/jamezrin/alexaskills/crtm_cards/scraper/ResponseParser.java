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
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            //System.out.println(document.html());

            return processCard(document);
        } catch (IOException e) {
            throw new ScraperException("Could not parse the data returned by the endpoint", e);
        }
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public static final DateTimeFormatter CRTM_DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy").withLocale(Locale.getDefault());
    public static final Pattern RENEWAL_DATE_PATTERN = Pattern.compile("^(?<type>[\\s\\S]+): (?<date>[0-9]{2}-[0-9]{2}-[0-9]{4})$");
    public static final Pattern PROFILE_DATE_PATTERN = Pattern.compile("^Perfil (?<type>[\\s\\S]+) caduca: (?<date>[0-9]{2}-[0-9]{2}-[0-9]{4})$");

    public static Card processCard(Document document) {
        // Full card number
        Element fullNumEl = document.getElementById("ctl00_cntPh_lblNumeroTarjeta");

        // Dates of renewals
        Element resultsTableEl = document.getElementById("ctl00_cntPh_tableResultados");
        Element resultRowEl = resultsTableEl.getElementsByTag("td").get(1);
        Elements results = resultRowEl.getElementsByTag("span");

        // Card expiration date
        Element expirationEl = document.getElementById("ctl00_cntPh_lblFechaCaducidadTarjeta");

        // Card relevant profiles w/ exp. dates
        Element profilesEl = document.getElementById("ctl00_cntPh_lblInfoDatosDeLaTarjeta");

        return new Card(
                fullNumEl.text(),
                results.get(0).text(),
                processCardType(results.get(1).text()),

                new CardRenewal[] {
                        extractRenewal(results.subList(2, 6)),
                        extractRenewal(results.subList(6, 10))
                },

                extractDate(expirationEl.text()),
                extractProfiles(profilesEl)
        );
    }

    public static EnumMap<CardType, LocalDate> extractProfiles(Element string) {
        System.out.println(string);
        return null;
    }

    public static CardType processCardType(String string) {
        for (CardType type : CardType.values()) {
            if (type.getId().equals(string)) {
                return type;
            }
        }

        return null;
    }

    public static CardRenewal extractRenewal(List<Element> list) {
        return new CardRenewal(
                extractDate(list.get(0).text()),
                extractDate(list.get(1).text()),
                extractDate(list.get(2).text()),
                extractDate(list.get(3).text())
        );
    }

    public static LocalDate extractDate(String string) {
        if (string == null) {
            return null;
        }

        Matcher matcher = RENEWAL_DATE_PATTERN.matcher(string);

        if (!matcher.find()) {
            return null;
        }

        String dateString = matcher.group("date");

        return LocalDate.parse(dateString, CRTM_DATE_FORMAT);
    }
}
