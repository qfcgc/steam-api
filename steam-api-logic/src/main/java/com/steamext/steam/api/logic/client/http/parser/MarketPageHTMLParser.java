package com.steamext.steam.api.logic.client.http.parser;

import com.steamext.steam.api.logic.exceptions.SteamHttpClientException;
import com.steamext.steam.api.logic.model.responsemodel.MarketRestriction;
import com.steamext.steam.api.logic.model.responsemodel.MarketRestrictions;
import com.steamext.steam.api.logic.model.responsemodel.StartMarketPageResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


/**
 * HTML parser implementation for parsing start market page html input stream.
 */
@Data
@Slf4j
public class MarketPageHTMLParser implements SteamHTMLParser<StartMarketPageResponse> {
    @Override
    public StartMarketPageResponse parseHTML(InputStream html) throws SteamHttpClientException {
        StartMarketPageResponse response = new StartMarketPageResponse();
        try {
            Document doc = Jsoup.parse(html, StandardCharsets.UTF_8.name(),
                    "steamcommunity.com");

            MarketRestrictions restrictions = getAllRestrictions(doc);
            response.setRestrictions(restrictions);

            return response;
        } catch (IOException e) {
            throw new SteamHttpClientException(e);
        }
    }

    /**
     * Extract all restriction elements from html page.
     *
     * @param doc market html page
     * @return market restrictions element (not null, it can contain empty list of restrictions)
     */
    private MarketRestrictions getAllRestrictions(Document doc) {
        MarketRestrictions restrictions;
        Element marketRestrictionElements = doc.selectFirst("ul.market_restrictions");
        if (marketRestrictionElements != null) {
            restrictions = getAllRestrictionsFromRestrictionsContainer(marketRestrictionElements);
        } else {
            restrictions = new MarketRestrictions();
        }
        return restrictions;
    }

    /**
     * Extract all restriction elements from html element.
     *
     * @param restrictionsContainer html element which contains restriction elements
     * @return market restrictions element (not null, it can contain empty list of restrictions)
     */
    private MarketRestrictions getAllRestrictionsFromRestrictionsContainer(Element restrictionsContainer) {
        MarketRestrictions marketRestrictions = new MarketRestrictions();

        if (restrictionsContainer != null) {
            Elements restrictionItems = restrictionsContainer.select("li");
            if (restrictionItems != null) {
                for (Element restrictionItem : restrictionItems) {
                    MarketRestriction restriction = getRestrictionFromElement(restrictionItem);
                    marketRestrictions.getRestrictions().add(restriction);
                }
            }
        }

        return marketRestrictions;
    }

    /**
     * Extract restriction from restriction html element.
     *
     * @param restrictionElement html element containing restriction info
     * @return market restriction object or null if there are no fields to extract from html element
     */
    private MarketRestriction getRestrictionFromElement(Element restrictionElement) {
        MarketRestriction restriction = null;
        String restrictionHelpLink = getRestrictionHelpLink(restrictionElement);
        String restrictionText = restrictionElement.text();
        if (restrictionHelpLink != null || restrictionText != null) {
            restriction = new MarketRestriction();
            restriction.setRestrictionHelpLink(restrictionHelpLink);
            restriction.setRestrictionText(restrictionText);
        }
        return restriction;
    }

    /**
     * Extract link to steam help page for restriction.
     *
     * @param restrictionElement element containing link to help page
     * @return link to steam help page or null if {@code restrictionElement} doesn't contain it
     */
    private String getRestrictionHelpLink(Element restrictionElement) {
        String restrictionHelpLink = null;
        if (restrictionElement != null) {
            Element a = restrictionElement.selectFirst("a");
            if (a != null) {
                String href = a.attr("href");
                if (href != null) {
                    restrictionHelpLink = href;
                }
            }
        }
        return restrictionHelpLink;
    }
}
