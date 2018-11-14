package com.jamezrin.alexaskills.crtmcards.scraper;

import com.jamezrin.alexaskills.crtmcards.scraper.exceptions.*;
import com.jamezrin.alexaskills.crtmcards.scraper.types.Card;
import com.jamezrin.alexaskills.crtmcards.scraper.types.CardRenewal;
import com.jamezrin.alexaskills.crtmcards.scraper.types.CardType;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.jamezrin.alexaskills.crtmcards.AppConsts.CRTM_BASE_URI;

public class ResponseParser {
    private final InputStream inputStream;
    private final Charset charset;
    private final Document document;

    public ResponseParser(InputStream inputStream, Charset charset) throws ScraperException {
        this.inputStream = inputStream;
        this.charset = charset;

        try {
            document = Jsoup.parse(
                    inputStream,
                    charset.name(),
                    CRTM_BASE_URI
            );

        } catch (IOException e) {
            throw new ScraperException("Could not parse the data returned by the endpoint", e);
        }
    }

    public ResponseParser(HttpResponse response) throws Exception {
        this(
                response.getEntity().getContent(),
                ContentType.getOrDefault(response.getEntity()).getCharset()
        );
    }

    public Card parse() throws ScraperException {
        checkForErrors(document);

        return processCard(document);
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public Charset getCharset() {
        return charset;
    }

    public Document getDocument() {
        return document;
    }

    public static final DateTimeFormatter CRTM_DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy").withLocale(Locale.forLanguageTag("es-ES"));
    public static final Pattern RENEWAL_DATE_PATTERN = Pattern.compile("^(?<type>[\\s\\S]+): (?<date>[0-9]{2}-[0-9]{2}-[0-9]{4})$");
    public static final Pattern PROFILE_DATE_PATTERN = Pattern.compile("^Perfil (?<type>[\\s\\S]+) caduca: (?<date>[0-9]{2}-[0-9]{2}-[0-9]{4})$");
    public static final Pattern FORMAT_ERROR_PATTERN = Pattern.compile("^javascript:alert\\(\'(?<message>Error. [\\s\\S]+)\'\\);$");
    public static final Pattern PROFILE_LINE_PATTERN = Pattern.compile("<br>(\\s+)?");

    public static void checkForErrors(Document document) throws ScraperException {
        checkForErrorLabel(document.getElementById("ctl00_cntPh_lblError"));
        checkForErrorScript(document.getElementsByTag("script"));
    }

    public static void checkForErrorLabel(Element element) throws ScraperException {
        if (element != null) {
            String content = element.text();
            if (!content.isEmpty()) {
                switch (content) {
                    case "POR FAVOR, INTRODUZCA DE NUEVO SU TARJETA": // get these strings in the exceptions
                        throw new NotExistentCardNumberException(content);
                    case "TARJETA NO ACTIVA":
                        throw new InactiveCardNumberException(content);
                    case "En estos momentos no es posible realizar la consulta. Disculpe las molestias.":
                        throw new NotAvailableException(content);
                    default:
                        throw new ScraperException(content);
                }
            }
        }
    }

    public static void checkForErrorScript(Elements elements) throws ScraperException {
        if (elements != null) {
            for (Element element : elements) {
                Matcher matcher = FORMAT_ERROR_PATTERN.matcher(element.html());

                if (!matcher.find()) {
                    continue;
                }

                String content = matcher.group("message");

                throw new InvalidCardNumberException(content);
            }
        }
    }

    public static Card processCard(Document document) {
        // Full card number
        Element fullNumEl = document.getElementById("ctl00_cntPh_lblNumeroTarjeta");

        // Dates of renewals
        Element resultsTableEl = document.getElementById("ctl00_cntPh_tableResultados");
        Element resultRowEl = resultsTableEl.getElementsByTag("td").get(1);
        Elements resultsEls = resultRowEl.getElementsByTag("span");

        // Card expiration date
        Element expirationEl = document.getElementById("ctl00_cntPh_lblFechaCaducidadTarjeta");

        // Card relevant profiles w/ exp. dates
        Element profilesEl = document.getElementById("ctl00_cntPh_lblInfoDatosDeLaTarjeta");

        return new Card(
                fullNumEl.text(),
                resultsEls.get(0).text(),
                CardType.fromId(resultsEls.get(1).text()),

                new CardRenewal[]{
                        extractRenewal(resultsEls.subList(2, 6)),
                        extractRenewal(resultsEls.subList(6, 10))
                },

                extractSimpleDate(expirationEl.text()),
                extractProfiles(profilesEl.html())
        );
    }

    public static CardRenewal extractRenewal(List<Element> list) {
        return new CardRenewal(
                extractSimpleDate(list.get(0).text()),
                extractSimpleDate(list.get(1).text()),
                extractSimpleDate(list.get(2).text()),
                extractSimpleDate(list.get(3).text())
        );
    }

    public static LocalDate extractSimpleDate(String string) {
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

    public static EnumMap<CardType, LocalDate> extractProfiles(String string) {
        EnumMap<CardType, LocalDate> map = new EnumMap<>(CardType.class);
        String[] parts = PROFILE_LINE_PATTERN.split(string);

        for (String part : parts) {
            Matcher matcher = PROFILE_DATE_PATTERN.matcher(part);

            if (!matcher.find()) {
                continue;
            }

            String typeString = matcher.group("type");
            String dateString = matcher.group("date");

            CardType type = CardType.fromId(typeString);
            LocalDate date = LocalDate.parse(dateString, CRTM_DATE_FORMAT);

            if (type == null) {
                continue;
            }

            map.put(type, date);
        }

        return map;
    }
}
