package com.steamext.steam.api.logic.client.http.utils;

import org.apache.http.cookie.ClientCookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class BasicClientCookieParser implements ClientCookieParser {
    @Override
    public ClientCookie parseCookieValue(String rawCookie) throws Exception {
        String[] rawCookieParams = rawCookie.split(";");

        String[] rawCookieNameAndValue = rawCookieParams[0].split("=");
        if (rawCookieNameAndValue.length != 2) {
            throw new Exception("Invalid cookie: missing name and value.");
        }

        String cookieName = rawCookieNameAndValue[0].trim();
        String cookieValue = rawCookieNameAndValue[1].trim();
        BasicClientCookie cookie = new BasicClientCookie(cookieName, cookieValue);
        for (int i = 1; i < rawCookieParams.length; i++) {
            String[] rawCookieParamNameAndValue = rawCookieParams[i].trim().split("=");

            String paramName = rawCookieParamNameAndValue[0].trim();

            if (paramName.equalsIgnoreCase("HttpOnly")) {
                continue;
            }
            if (paramName.equalsIgnoreCase("secure")) {
                cookie.setSecure(true);
            } else {
                if (rawCookieParamNameAndValue.length != 2) {
                    throw new Exception("Invalid cookie: attribute not a flag or missing value.");
                }

                String paramValue = rawCookieParamNameAndValue[1].trim();

                if (paramName.equalsIgnoreCase("expires")) {
                    DateFormat osLocalizedDateFormat =
                            new SimpleDateFormat("EEE, dd-MMM-yyyy hh:mm:ss", Locale.ENGLISH);
                    osLocalizedDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                    Date expiryDate = osLocalizedDateFormat.parse("Sun, 01-Aug-2021 13:01:37 GMT");
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
