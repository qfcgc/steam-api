package com.steamext.steam.api.logic.client.http.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.steamext.steam.api.logic.exceptions.SteamHttpClientException;
import com.steamext.steam.api.logic.model.responsemodel.tradeelements.JsonPriceHistoryPoint;
import com.steamext.steam.api.logic.model.responsemodel.tradeelements.ParsedTradeElementPageResponse;
import com.steamext.steam.api.logic.model.responsemodel.tradeelements.ParsedTradeElementWrapper;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class TradeElementPageHTMLParser implements SteamHTMLParser<ParsedTradeElementPageResponse> {
    @Override
    public ParsedTradeElementPageResponse parseHTML(InputStream html) throws SteamHttpClientException {
        ObjectMapper mapper = new ObjectMapper();

        ParsedTradeElementPageResponse parsedTradeElementPageResponse = new ParsedTradeElementPageResponse();
        try {
            Document doc = Jsoup.parse(html, StandardCharsets.UTF_8.name(),
                    "steamcommunity.com");

            ParsedTradeElementWrapper parsedTradeElementWrapper = new ParsedTradeElementWrapper();

            String json = extractTradeElementInfoJson(doc);
            parsedTradeElementWrapper = mapper.readValue(json, ParsedTradeElementWrapper.class);
            parsedTradeElementPageResponse.setTradeElementWrapper(parsedTradeElementWrapper);

            String history = extractPriceHistoryInfoJson(doc);
            List<List<String>> historyPointStrings =
                    mapper.readValue(history, new TypeReference<>() {
                    });
            List<JsonPriceHistoryPoint> historyPoints = historyPointStrings
                    .stream().map(x -> JsonPriceHistoryPoint.builder()
                            .date(x.get(0))
                            .price(Double.parseDouble(x.get(1)))
                            .amount(x.get(2)).build()).collect(Collectors.toList());
            parsedTradeElementPageResponse.setPriceHistoryPoints(historyPoints);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parsedTradeElementPageResponse;
    }

    //todo: error handling
    private String extractTradeElementInfoJson(Document doc) {
        int rgAccessStartIndex = doc.html().indexOf("g_rgAssets =");
        int jsonStartIndex = doc.html().indexOf('{', rgAccessStartIndex);
        int jsonEndIndex = doc.html().indexOf("};", jsonStartIndex) + 1;
        return doc.html().substring(jsonStartIndex, jsonEndIndex);
    }

    private String extractPriceHistoryInfoJson(Document doc) {
        //var line1=[[
        int priceHistoryInfoStartIndex = doc.html().indexOf("var line1=[[");
        int jsonStartIndex = doc.html().indexOf('[', priceHistoryInfoStartIndex);
        int jsonEndIndex = doc.html().indexOf("];", jsonStartIndex) + 1;
        return doc.html().substring(jsonStartIndex, jsonEndIndex);
    }
}
