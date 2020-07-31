package com.steamext.steam.api.logic.client.http.parser;

import com.steamext.steam.api.logic.exceptions.SteamHttpClientException;

import java.io.InputStream;

public interface SteamHTMLParser<T> {
    T parseHTML(InputStream html) throws SteamHttpClientException;
}
