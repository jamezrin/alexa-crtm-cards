package com.jamezrin.alexaskills.crtmcards.scraper;

import com.amazonaws.util.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.jamezrin.alexaskills.crtmcards.AppConsts.CRTM_BASE_URI;
import static com.jamezrin.alexaskills.crtmcards.AppConsts.CRTM_QUERY_URI;
import static com.jamezrin.alexaskills.crtmcards.AppConsts.CRTM_USER_AGENT;

public class ScraperUtils {
    private static final CloseableHttpClient httpClient = makeHttpClient(5000);

    public static CloseableHttpClient makeHttpClient(int timeout) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(timeout)
                .setSocketTimeout(timeout)
                .setConnectionRequestTimeout(timeout)
                .build();
        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .setUserAgent(CRTM_USER_AGENT)
                .build();
    }

    public static String fetchViewState() throws IOException {
        HttpGet viewRequest = new HttpGet(CRTM_QUERY_URI);
        HttpResponse viewResponse = httpClient.execute(viewRequest);
        HttpEntity viewResponseEntity = viewResponse.getEntity();

        Document viewStateDocument = Jsoup.parse(
                viewResponseEntity.getContent(),
                StandardCharsets.UTF_8.name(),
                CRTM_BASE_URI
        );

        Element viewStateEl = viewStateDocument.getElementById("__VIEWSTATE");
        return viewStateEl.attr("value");
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
