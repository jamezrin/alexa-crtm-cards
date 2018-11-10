package com.jamezrin.alexaskills.crtm_cards.scraper;

import com.amazonaws.util.Base64;
import com.jamezrin.alexaskills.crtm_cards.AppConsts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static com.jamezrin.alexaskills.crtm_cards.AppConsts.CRTM_BASE_URI;

public class ScraperUtils {
    /*
        /wEPDwUJNzkyNzg1MDMyD2QWAmYPZBYCAgMPZBYEAgMPDxYCHgRUZXh0BSBTw6FiYWRvLCAxMCBkZSBub3ZpZW1icmUgZGUgMjAxOGRkAgUPZBYGAgEPDxYCHwAFCjEwLzExLzIwMThkZAIDDw8WAh8ABQUxNjo0MmRkAgUPZBYCAgMPZBYCZg9kFgICAQ8QZA8WBmYCAQICAgMCBAIFFgYQBQMtLS0FAy0tLWcQBQMwMDEFAzAwMWcQBQMwMDIFAzAwMmcQBQMwMDMFAzAwM2cQBQMxNzUFAzE3NWcQBQMyNTEFAzI1MWdkZGR9dqfr2AC/Nlee9Z0aGTuibu8nBNYADuaEARGNQOraSA==
        /wEPDwUJNzkyNzg1MDMyD2QWAmYPZBYCAgMPZBYEAgMPDxYCHgRUZXh0BSBTw6FiYWRvLCAxMCBkZSBub3ZpZW1icmUgZGUgMjAxOGRkAgUPZBYOAgEPDxYCHwAFCjEwLzExLzIwMThkZAIDDw8WAh8ABQUxNjo0M2RkAgUPZBYEAgMPZBYCZg9kFgICAQ8QZA8WBmYCAQICAgMCBAIFFgYQBQMtLS0FAy0tLWcQBQMwMDEFAzAwMWcQBQMwMDIFAzAwMmcQBQMwMDMFAzAwM2cQBQMxNzUFAzE3NWcQBQMyNTEFAzI1MWdkZAIFD2QWBGYPZBYCAgEPFgIeB1Zpc2libGVnZAIBD2QWAgIBDw8WAh8ABU1FbiBlc3RvcyBtb21lbnRvcyBubyBlcyBwb3NpYmxlIHJlYWxpemFyIGxhIGNvbnN1bHRhLiBEaXNjdWxwZSBsYXMgbW9sZXN0aWFzLmRkAgcPZBYCZg9kFgICAQ9kFgICAQ8PFgIfAGVkZAIJDxYCHglpbm5lcmh0bWxlZAIPD2QWAgIBD2QWAgIBDw8WAh8AZWRkAhEPZBYCAgEPZBYCAgEPDxYCHwBlZGRkE2sDoEP1A7yxET4YrfiBHvG3UTFwTw2Ls5TqtEbf9O4=
    */
    private static final HttpClient httpClient = makeHttpClient();

    public static HttpClient makeHttpClient() {
        int timeout = 5 * 1000;
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(timeout)
                .setSocketTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .build();
        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .setUserAgent(AppConsts.CRTM_USER_AGENT)
                .build();
    }

    public static String makeDefaultViewState() {
        return
                "/wEPDwUJNzkyNzg1MDMyD2QWAmYPZBYCAgMPZBYEAgMPDxYCHgRUZXh0BSBTw6FiYWRvLCAxMCBkZSBub3ZpZW1icm" +
                        "UgZGUgMjAxOGRkAgUPZBYGAgEPDxYCHwAFCjEwLzExLzIwMThkZAIDDw8WAh8ABQUxNDozMWRkAgUPZBYCAgMPZBYC" +
                        "Zg9kFgICAQ8QZA8WBmYCAQICAgMCBAIFFgYQBQMtLS0FAy0tLWcQBQMwMDEFAzAwMWcQBQMwMDIFAzAwMmcQBQMwMD" +
                        "MFAzAwM2cQBQMxNzUFAzE3NWcQBQMyNTEFAzI1MWdkZGQBRJ/qa2v0OAMMeRdkpd2XFiCltKiJjyXS6doF/w0EQg==";
    }

    public static class ViewStateInfo {
        private final String viewStateGenerator;
        private final String viewState;

        public ViewStateInfo(String viewStateGenerator, String viewState) {
            this.viewStateGenerator = viewStateGenerator;
            this.viewState = viewState;
        }

        public String getViewStateGenerator() {
            return viewStateGenerator;
        }

        public String getViewState() {
            return viewState;
        }

        public byte[] decodeViewState() {
            return Base64.decode(viewState);
        }
    }

    public static ViewStateInfo fetchViewState() throws IOException {
        HttpGet viewRequest = new HttpGet(AppConsts.CRTM_QUERY_URI);
        HttpResponse viewResponse = httpClient.execute(viewRequest);
        HttpEntity viewResponseEntity = viewResponse.getEntity();

        Document viewStateDocument = Jsoup.parse(viewResponseEntity.getContent(), StandardCharsets.UTF_8.name(), CRTM_BASE_URI);

        return new ViewStateInfo(
                viewStateDocument.getElementById("__VIEWSTATEGENERATOR").attr("value"),
                viewStateDocument.getElementById("__VIEWSTATE").attr("value")
        );
    }

    public static void printStringBytes(String string) {
        byte[] arr = string.getBytes(StandardCharsets.UTF_8);
        printByteArray(arr);
        System.out.println();
    }

    public static void printViewState(String viewState) {
        byte[] arr = Base64.decode(viewState);
        printByteArray(arr);
        System.out.println();
    }

    public static void printByteArray(byte[] arr) {
        for (byte x : arr) {
            System.out.printf("0x%02x ", x);
        }
    }
}
