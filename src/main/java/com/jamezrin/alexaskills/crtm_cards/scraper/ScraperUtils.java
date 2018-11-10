package com.jamezrin.alexaskills.crtm_cards.scraper;

import com.amazonaws.util.Base64;
import com.jamezrin.alexaskills.crtm_cards.AppConsts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.jamezrin.alexaskills.crtm_cards.AppConsts.CRTM_BASE_URI;

public class ScraperUtils {
    private static final HttpClient httpClient = makeHttpClient();

    public static HttpClient makeHttpClient() {
        return HttpClients
                .custom()
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .setUserAgent(AppConsts.CRTM_USER_AGENT)
                .build();
    }

    public static String fetchViewState() throws IOException {
        HttpGet viewRequest = new HttpGet(AppConsts.CRTM_QUERY_URI);
        HttpResponse viewResponse = httpClient.execute(viewRequest);
        HttpEntity viewResponseEntity = viewResponse.getEntity();

        Document viewStateDocument = Jsoup.parse(viewResponseEntity.getContent(), StandardCharsets.UTF_8.name(), CRTM_BASE_URI);
        return viewStateDocument.getElementById("__VIEWSTATE").attr("value");
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
