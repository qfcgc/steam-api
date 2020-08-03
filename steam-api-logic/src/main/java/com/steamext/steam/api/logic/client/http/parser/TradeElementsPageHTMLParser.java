package com.steamext.steam.api.logic.client.http.parser;

import com.steamext.steam.api.logic.exceptions.SteamHttpClientException;
import com.steamext.steam.api.logic.model.responsemodel.MarketRestrictions;
import com.steamext.steam.api.logic.model.responsemodel.StartMarketPageResponse;
import com.steamext.steam.api.logic.model.responsemodel.TradeElement;
import com.steamext.steam.api.logic.model.responsemodel.TradeElements;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.core.util.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public class TradeElementsPageHTMLParser implements SteamHTMLParser<TradeElements> {
    @Override
    public TradeElements parseHTML(InputStream html) throws SteamHttpClientException {
        TradeElements response = new TradeElements();
        try {
            Document doc = Jsoup.parse(html, StandardCharsets.UTF_8.name(),
                    "steamcommunity.com");

            response.setTradeElements(getTradeElements(doc));

            return response;
        } catch (IOException e) {
            throw new SteamHttpClientException(e);
        }
    }

    private List<TradeElement> getTradeElements(Document doc) {
        List<TradeElement> tradeElements = new ArrayList<>();
        Element searchResultsTable = doc.getElementById("searchResultsTable");
        if (searchResultsTable != null) {
            tradeElements = getTradeElementsFromContainer(searchResultsTable);
        }

        return tradeElements;
    }

    private List<TradeElement> getTradeElementsFromContainer(Element container) {
        List<TradeElement> tradeElements = new ArrayList<>();
        Elements tradeElementsContainers = container.select(".market_listing_row_link");
        for (Element tradeElementContainer : tradeElementsContainers) {
            TradeElement tradeElement = new TradeElement();
            tradeElement.setTradeElementHref(
                    tradeElementContainer.selectFirst("a.market_listing_row_link").attr("href"));
            tradeElement.setAppId(
                    tradeElementContainer.selectFirst("a div.market_listing_row").attr("data-appid"));
            tradeElement.setHashName(
                    tradeElementContainer.selectFirst("a div.market_listing_row").attr("data-hash-name"));
            tradeElement.setImageHref(
                    tradeElementContainer.selectFirst("a div img.market_listing_item_img").attr("src"));
            tradeElement.setName(
                    tradeElementContainer.selectFirst("div.market_listing_item_name_block span.market_listing_item_name").text());
            tradeElement.setGameName(
                    tradeElementContainer.selectFirst("div.market_listing_item_name_block span.market_listing_game_name").text());
            tradeElement.setQuantity(
                    tradeElementContainer.selectFirst("div.market_listing_num_listings span.market_listing_num_listings_qty").attr("data-qty"));
            //Todo: make price item, $0.75 USD -> data-price = 75
            //tradeElementContainer.selectFirst("div.market_listing_their_price span.normal_price span.normal_price").attr("data-price")
            //Todo: make price item, $0.75 USD -> data-currency = 1
            //tradeElementContainer.selectFirst("div.market_listing_their_price span.normal_price span.normal_price").attr("data-currency")
            tradeElement.setLowestPrice(
                    tradeElementContainer.selectFirst("div.market_listing_their_price span.normal_price span.normal_price").text());
            tradeElements.add(tradeElement);
        }

        return tradeElements;
    }
}
