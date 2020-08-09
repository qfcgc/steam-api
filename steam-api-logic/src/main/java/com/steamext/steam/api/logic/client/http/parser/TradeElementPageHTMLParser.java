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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * HTML parser implementation for parsing trade element market page html input stream.
 */
@Slf4j
public class TradeElementPageHTMLParser implements SteamHTMLParser<ParsedTradeElementPageResponse> {
    @Override
    public ParsedTradeElementPageResponse parseHTML(InputStream html) throws SteamHttpClientException {
        ObjectMapper mapper = new ObjectMapper();

        ParsedTradeElementPageResponse parsedTradeElementPageResponse = new ParsedTradeElementPageResponse();
        try {
            Document doc = Jsoup.parse(html, StandardCharsets.UTF_8.name(),
                    "steamcommunity.com");

            String json = extractTradeElementInfoJson(doc);
            ParsedTradeElementWrapper parsedTradeElementWrapper =
                    mapper.readValue(json, ParsedTradeElementWrapper.class);
            parsedTradeElementPageResponse.setTradeElementWrapper(parsedTradeElementWrapper);

            String history = extractPriceHistoryInfoJson(doc);
            List<List<String>> historyPointStrings =
                    mapper.readValue(history, new TypeReference<>() {
                    });
            List<JsonPriceHistoryPoint> historyPoints =
                    transformToHistoryPoints(historyPointStrings);
            parsedTradeElementPageResponse.setPriceHistoryPoints(historyPoints);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parsedTradeElementPageResponse;
    }

    /**
     * Maps price history points from [string, float, string] form to
     * {{@link Calendar}, {@link Double}, {@link Integer}} form.
     *
     * @param historyPointStrings list of string lists
     * @return list of price history point objects
     */
    private List<JsonPriceHistoryPoint> transformToHistoryPoints(List<List<String>> historyPointStrings) {
        return historyPointStrings
                .stream().map(x -> JsonPriceHistoryPoint.builder()
                        .date(parseHistoryPointsDateString(x.get(0)))
                        .price(Double.parseDouble(x.get(1)))
                        .amount(Integer.parseInt(x.get(2)))
                        .build())
                .collect(Collectors.toList());
    }

    /**
     * Parse date from "MMM dd yyyy hh"-pattern String to {@link Calendar} object.
     *
     * @param dateString String date
     * @return Calendar object
     */
    private Calendar parseHistoryPointsDateString(String dateString) {
        DateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy hh", Locale.ENGLISH);
        Date date;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            log.error("Trade element history point dare parsing error", e);
            throw new RuntimeException(e);
        }

        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTime(date);
        return calendar;
    }

    /**
     * Extracts JSON string from document.
     * String contains information about trade element.
     *
     * @param doc document
     * @return json string
     */
    //todo: error handling
    private String extractTradeElementInfoJson(Document doc) {
        int rgAccessStartIndex = doc.html().indexOf("g_rgAssets =");
        int jsonStartIndex = doc.html().indexOf('{', rgAccessStartIndex);
        int jsonEndIndex = doc.html().indexOf("};", jsonStartIndex) + 1;
        return doc.html().substring(jsonStartIndex, jsonEndIndex);
    }

    /**
     * Extracts JSON string from document.
     * String contains information about trade element price history elements.
     *
     * @param doc document
     * @return json string
     */
    //todo: error handling
    private String extractPriceHistoryInfoJson(Document doc) {
        int priceHistoryInfoStartIndex = doc.html().indexOf("var line1=[[");
        int jsonStartIndex = doc.html().indexOf('[', priceHistoryInfoStartIndex);
        int jsonEndIndex = doc.html().indexOf("];", jsonStartIndex) + 1;
        return doc.html().substring(jsonStartIndex, jsonEndIndex);
    }
}
