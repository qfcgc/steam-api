package com.steamext.steam.api.logic.model.responsemodel.tradeelements;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JsonTradeElement {
    @JsonProperty("name")
    private String name;

    @JsonProperty("hash_name")
    private String hashName;

    @JsonProperty("sell_listings")
    private String sellListings;

    @JsonProperty("sell_price")
    private String sellPrice;

    @JsonProperty("sell_price_text")
    private String sellPriceText;

    @JsonProperty("app_icon")
    private String appIcon;

    @JsonProperty("app_name")
    private String appName;

    @JsonProperty("sale_price_text")
    private String salePriceText;

    @JsonProperty("asset_description")
    private TradeElementAssertDescription assertDescription;
}
