package com.steamext.steam.api.logic.client.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.steamext.steam.api.logic.client.http.parser.SteamHTMLParser;
import com.steamext.steam.api.logic.client.http.request.SteamHttpRequest;
import com.steamext.steam.api.logic.exceptions.SteamHttpClientException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

@Slf4j
public class SteamHttpClient {
    private HttpClient client;
    private CookieStore cookieStore;

    private HttpHost defaultSteamHost;

    private ObjectMapper objectMapper;

    public SteamHttpClient() {
        init();
    }

    private void init() {
        defaultSteamHost = new HttpHost("steamcommunity.com", 443, "https");
        objectMapper = new ObjectMapper();
        this.cookieStore = new BasicCookieStore();
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
                Header[] setCookies = response.getHeaders("Set-Cookie");
                if (setCookies != null) {
                    for (Header setCookie : setCookies) {
                        String cookieLine = setCookie.getValue();
                        try {
                            BasicClientCookie basicClientCookie = parseRawCookie(cookieLine);
                            cookieStore.addCookie(basicClientCookie);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
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
//                String cookieLine = response.getHeaders("Set-Cookie")[0].getValue();
//                String name = cookieLine.substring(cookieLine.indexOf('='));
//                String value = cookieLine.substring(cookieLine.indexOf('=')+1);
                Header[] setCookies = response.getHeaders("Set-Cookie");
                if (setCookies != null) {
                    for (Header setCookie : setCookies) {
                        String cookieLine = setCookie.getValue();
                        try {
                            BasicClientCookie basicClientCookie = parseRawCookie(cookieLine);
                            cookieStore.addCookie(basicClientCookie);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

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

    private BasicClientCookie parseRawCookie(String rawCookie) throws Exception {
        String[] rawCookieParams = rawCookie.split(";");

        String[] rawCookieNameAndValue = rawCookieParams[0].split("=");
        if (rawCookieNameAndValue.length != 2) {
            throw new Exception("Invalid cookie: missing name and value.");
        }

        String cookieName = rawCookieNameAndValue[0].trim();
        String cookieValue = rawCookieNameAndValue[1].trim();
        BasicClientCookie cookie = new BasicClientCookie(cookieName, cookieValue);
        for (int i = 1; i < rawCookieParams.length; i++) {
            String rawCookieParamNameAndValue[] = rawCookieParams[i].trim().split("=");

            String paramName = rawCookieParamNameAndValue[0].trim();

            if (paramName.equalsIgnoreCase("secure")) {
                cookie.setSecure(true);
            } else {
                if (rawCookieParamNameAndValue.length != 2) {
                    throw new Exception("Invalid cookie: attribute not a flag or missing value.");
                }

                String paramValue = rawCookieParamNameAndValue[1].trim();

                if (paramName.equalsIgnoreCase("expires")) {
                    Date expiryDate = DateFormat.getDateTimeInstance(DateFormat.FULL,
                            DateFormat.HOUR_OF_DAY1_FIELD)
                            .parse(paramValue);
                    cookie.setExpiryDate(expiryDate);
                } else if (paramName.equalsIgnoreCase("max-age")) {
                    long maxAge = Long.parseLong(paramValue);
                    Date expiryDate = new Date(System.currentTimeMillis() + maxAge);
                    cookie.setExpiryDate(expiryDate);
                } else if (paramName.equalsIgnoreCase("domain")) {
                    cookie.setDomain(paramValue);
                } else if (paramName.equalsIgnoreCase("path")) {
                    cookie.setPath(paramValue);
                } else if (paramName.equalsIgnoreCase("comment")) {
                    cookie.setPath(paramValue);
                } else {
                    throw new Exception("Invalid cookie: invalid attribute name.");
                }
            }
        }

        return cookie;
    }
}
