package com.steamext.steam.api.logic.model.responsemodel.tradeelements;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TradeElementAssertDescription {
    @JsonProperty("appid")
    private int appId;

    @JsonProperty("classid")
    private String classId;

    @JsonProperty("instanceid")
    private String instanceId;

    @JsonProperty("background_color")
    private String backgroundColor;

    @JsonProperty("icon_url")
    private String iconUrl;

    @JsonProperty("tradable")
    private int tradable;

    @JsonProperty("name")
    private String name;

    @JsonProperty("name_color")
    private String nameColor;

    @JsonProperty("type")
    private String type;

    @JsonProperty("market_name")
    private String marketName;

    @JsonProperty("market_hash_name")
    private String marketHashName;

    @JsonProperty("commodity")
    private int commodity;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("icon_url_large")
    private String iconUrlLarge;

    @JsonProperty("descriptions")
    private List<TradeElementDescriptionObject> descriptions;

    @JsonProperty("owner_descriptions")
    private List<TradeElementDescriptionObject> ownerDescriptions;

    @JsonProperty("market_tradable_restriction")
    private int marketTradableRestriction;

    @JsonProperty("marketable")
    private int marketable;

    @JsonProperty("market_buy_country_restriction")
    private String marketBuyCountryRestriction;

    @JsonProperty("actions")
    private List<TradeElementAction> actions;

    @JsonProperty("market_actions")
    private List<TradeElementAction> marketActions;

    @JsonProperty("fraudwarnings")
    private List<String> fraudWarnings;
}