package com.jamezrin.alexaskills.crtm_cards.scraper;

import com.jamezrin.alexaskills.crtm_cards.AppConsts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.jamezrin.alexaskills.crtm_cards.scraper.ScraperUtils.makeHttpClient;

public class EndpointConnector {
    private static final HttpClient httpClient = makeHttpClient();

    private final String cardPrefix;
    private final String cardNumber;

    public EndpointConnector(String cardPrefix, String cardNumber) {
        this.cardPrefix = cardPrefix;
        this.cardNumber = cardNumber;
    }

    public InputStream connect() throws Exception {
        /* Doesn't work for some reason
            ViewStateGenerator viewStateGenerator = new ViewStateGenerator();
            String viewState = viewStateGenerator.generate();
        */

        String viewState =
                "/wEPDwUJNzkyNzg1MDMyD2QWAmYPZBYCAgMPZBYEAgMPDxYCHgRUZXh0BSBTw6FiYWRvLCAxMCBkZSBub3ZpZW1icm" +
                "UgZGUgMjAxOGRkAgUPZBYGAgEPDxYCHwAFCjEwLzExLzIwMThkZAIDDw8WAh8ABQUxNDozMWRkAgUPZBYCAgMPZBYC" +
                "Zg9kFgICAQ8QZA8WBmYCAQICAgMCBAIFFgYQBQMtLS0FAy0tLWcQBQMwMDEFAzAwMWcQBQMwMDIFAzAwMmcQBQMwMD" +
                "MFAzAwM2cQBQMxNzUFAzE3NWcQBQMyNTEFAzI1MWdkZGQBRJ/qa2v0OAMMeRdkpd2XFiCltKiJjyXS6doF/w0EQg==";

        HttpPost queryRequest = buildQueryRequest(viewState, cardPrefix, cardNumber);
        HttpResponse queryResponse = httpClient.execute(queryRequest);
        HttpEntity queryResponseEntity = queryResponse.getEntity();

        return queryResponseEntity.getContent();
    }

    public static HttpPost buildQueryRequest(String viewState, String cardPrefix, String cardNumber) {
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
}
