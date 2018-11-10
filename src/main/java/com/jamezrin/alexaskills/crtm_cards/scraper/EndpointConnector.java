package com.jamezrin.alexaskills.crtm_cards.scraper;

import com.jamezrin.alexaskills.crtm_cards.AppConsts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.jamezrin.alexaskills.crtm_cards.AppConsts.CRTM_BASE_URI;

public class EndpointConnector {
    private static final HttpClient httpClient = makeHttpClient();

    private final String cardPrefix;
    private final String cardNumber;

    public EndpointConnector(String cardPrefix, String cardNumber) {
        this.cardPrefix = cardPrefix;
        this.cardNumber = cardNumber;
    }

    public InputStream connect() throws Exception {
        String viewState = "/wEPDwUJNzkyNzg1MDMyD2QWAmYPZBYCAgMPZBYEAgMPDxYCHgRUZXh0BSBWaWVybmVzLCAwOSBkZSBub3ZpZW1icmUgZGUgMjAxOGRkAgUPZBYGAgEPDxYCHwAFCjA5LzExLzIwMThkZAIDDw8WAh8ABQUwMToyOGRkAgUPZBYCAgMPZBYCZg9kFgICAQ8QZA8WBmYCAQICAgMCBAIFFgYQBQMtLS0FAy0tLWcQBQMwMDEFAzAwMWcQBQMwMDIFAzAwMmcQBQMwMDMFAzAwM2cQBQMxNzUFAzE3NWcQBQMyNTEFAzI1MWdkZGToxCNvBRzJegbOeKpmT39Wqme8cu0qpIFR9XJrvmmEcw==";

        HttpPost queryRequest = buildQueryRequest(viewState, cardPrefix, cardNumber);
        HttpResponse queryResponse = httpClient.execute(queryRequest);
        HttpEntity queryResponseEntity = queryResponse.getEntity();

        return queryResponseEntity.getContent();
    }

    public static HttpClient makeHttpClient() {
        return HttpClients
                .custom()
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .setUserAgent(AppConsts.CRTM_USER_AGENT)
                .build();
    }

    public static HttpPost buildQueryRequest(String viewState, String cardPrefix, String cardNumber) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(AppConsts.CRTM_QUERY_URI);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("__SCROLLPOSITIONY", "530"));
        params.add(new BasicNameValuePair("__SCROLLPOSITIONX", "0"));
        params.add(new BasicNameValuePair("__VIEWSTATEGENERATOR", "761C03E9")); //todo make it a constant
        params.add(new BasicNameValuePair("__VIEWSTATE", viewState));
        params.add(new BasicNameValuePair("__EVENTTARGET", ""));
        params.add(new BasicNameValuePair("__EVENTARGUMENT", ""));

        params.add(new BasicNameValuePair("ctl00$cntPh$dpdCodigoTTP", cardPrefix));
        params.add(new BasicNameValuePair("ctl00$cntPh$txtNumTTP", cardNumber));
        params.add(new BasicNameValuePair("ctl00$cntPh$btnConsultar", "Continuar"));
        httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));

        return httpPost;
    }

    public static String fetchViewState() throws IOException {
        HttpGet viewRequest = new HttpGet(AppConsts.CRTM_QUERY_URI);
        HttpResponse viewResponse = httpClient.execute(viewRequest);
        HttpEntity viewResponseEntity = viewResponse.getEntity();

        Document viewStateDocument = Jsoup.parse(viewResponseEntity.getContent(), StandardCharsets.UTF_8.name(), CRTM_BASE_URI);
        return viewStateDocument.getElementById("__VIEWSTATE").attr("value");
    }
}
