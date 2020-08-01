package com.steamext.steam.api.logic.client.http.utils;

import org.apache.http.cookie.ClientCookie;

public interface ClientCookieParser {
    ClientCookie parseCookieValue(String rawCookie) throws Exception;
}
