package com.steamext.steam.api.logic.client.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.steamext.steam.api.logic.client.http.parser.SteamHTMLParser;
import com.steamext.steam.api.logic.client.http.request.SteamHttpRequest;
import com.steamext.steam.api.logic.client.http.utils.BasicClientCookieParser;
import com.steamext.steam.api.logic.client.http.utils.ClientCookieParser;
import com.steamext.steam.api.logic.exceptions.SteamHttpClientException;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Getter
public class SteamHttpClient {
    private final HttpHost defaultSteamHost =
            new HttpHost("steamcommunity.com", 443, "https");
    @Builder.Default
    private final CookieStore cookieStore = new BasicCookieStore();
    @Builder.Default
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Builder.Default
    private final ClientCookieParser cookieParser = new BasicClientCookieParser();
    private HttpClient client;

    @Builder
    public SteamHttpClient(ClientCookieParser clientCookieParser,
                           CookieStore cookieStore,
                           ObjectMapper objectMapper) {
        init();
    }

    private void init() {
        client = getConfiguredClient();
    }

    private HttpClient getConfiguredClient() {
        return HttpClients.custom().setDefaultCookieStore(cookieStore).build();
    }

    public <T> T execute(SteamHttpRequest request) throws SteamHttpClientException {
        return (T) execute(request.getRequest(), request.getValueType());
    }

    public <T> T execute(SteamHttpRequest request, Class<T> valueType) throws SteamHttpClientException {
        return execute(request.getRequest(), valueType);
    }

    public <T> T execute(HttpRequest request, Class<T> valueType) throws SteamHttpClientException {
        T resultObject;
        try {
            resultObject = client.execute(defaultSteamHost, request, response -> {
                extractAndSetCookies(response);

                //todo: handle if not 200 OK
                return objectMapper.readValue(response.getEntity().getContent(), valueType);
            });
        } catch (IOException e) {
            throw new SteamHttpClientException(e);
        }

        return resultObject;
    }

    public <T> T execute(SteamHttpRequest request, SteamHTMLParser<T> parser) throws SteamHttpClientException {
        return execute(request.getRequest(), parser);
    }

    public <T> T execute(HttpRequest request, SteamHTMLParser<T> parser) throws SteamHttpClientException {
        T resultObject;
        try {
            resultObject = client.execute(defaultSteamHost, request, response -> {
                extractAndSetCookies(response);

                //todo: handle if not 200 OK
                try {
                    return parser.parseHTML(response.getEntity().getContent());
                } catch (SteamHttpClientException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            throw new SteamHttpClientException(e);
        }

        return resultObject;
    }

    private void extractAndSetCookies(HttpResponse response) {
        Header[] setCookies = response.getHeaders("Set-Cookie");
        if (setCookies != null) {
            Arrays.stream(setCookies).forEach(this::extractAndSetCookie);
        }
    }

    private void extractAndSetCookie(Header cookieHeader) {
        String cookieLine = cookieHeader.getValue();
        try {
            parseCookieLineAndSetCookie(cookieLine);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseCookieLineAndSetCookie(String cookieLine) throws Exception {
        ClientCookie basicClientCookie = parseRawCookie(cookieLine);
        cookieStore.addCookie(basicClientCookie);
    }

    private ClientCookie parseRawCookie(String rawCookie) throws Exception {
        return cookieParser.parseCookieValue(rawCookie);
    }
}
