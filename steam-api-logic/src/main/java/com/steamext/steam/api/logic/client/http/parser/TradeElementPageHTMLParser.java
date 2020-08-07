package com.steamext.steam.api.logic.client.http.parser;

import com.steamext.steam.api.logic.exceptions.SteamHttpClientException;
import com.steamext.steam.api.logic.model.responsemodel.tradeelements.ParsedTradeElement;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
public class TradeElementPageHTMLParser implements SteamHTMLParser<ParsedTradeElement> {
    @Override
    public ParsedTradeElement parseHTML(InputStream html) throws SteamHttpClientException {
        ParsedTradeElement tradeElement = new ParsedTradeElement();
        try {
            Document doc = Jsoup.parse(html, StandardCharsets.UTF_8.name(),
                    "steamcommunity.com");

            tradeElement.setItemName(extractName(doc));

            System.out.println("mock");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tradeElement;
    }

    private String extractName(Document doc) {
        String json = extractTradeElementInfoJson(doc);
        int firstIndexOf = json.indexOf("\"market_name\":\"") + "\"market_name\":\"".length();
        return json.substring(firstIndexOf, firstIndexOf + json.substring(firstIndexOf).indexOf('\"'));
    }

    //todo: error handling
    private String extractTradeElementInfoJson(Document doc) {
        int rgAccessStartIndex = doc.html().indexOf("g_rgAssets =");
        int jsonStartIndex = doc.html().indexOf('{', rgAccessStartIndex);
        int jsonEndIndex = doc.html().indexOf("};", jsonStartIndex) + 1;
        return doc.html().substring(jsonStartIndex, jsonEndIndex);
    }
}
