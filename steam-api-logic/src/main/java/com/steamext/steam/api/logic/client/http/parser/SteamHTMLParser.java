package com.steamext.steam.api.logic.client.http.parser;

import com.steamext.steam.api.logic.exceptions.SteamHttpClientException;

import java.io.InputStream;

/**
 * HTML parser for parsing html input stream.
 * Implementations are to extract info from page and fill object with generic type.
 *
 * @param <T> type of object that will be filled with info from html input stream
 */
public interface SteamHTMLParser<T> {
    /**
     * Extract info from start market page to fill {@code <T>} object.
     *
     * @param html html page input stream
     * @return info from page
     * @throws SteamHttpClientException in case of problems with reading html page
     */
    T parseHTML(InputStream html) throws SteamHttpClientException;
}
