package com.steamext.steam.api.logic.model.responsemodel.tradeelements;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.util.List;

@Data
public class TradeElementAssertDescription {
    @JsonProperty("appid")
    private int appId;

    @JsonProperty("market_fee_app")
    private int marketFeeApp;

    @JsonProperty("classid")
    private String classId;

    @JsonProperty("instanceid")
    private String instanceId;

    @JsonProperty("contextid")
    private int contextId;

    @JsonProperty("id")
    private String id;

    @JsonProperty("unowned_id")
    private String unownedId;

    @JsonProperty("unowned_contextid")
    private String unownedContextId;

    @JsonProperty("amount")
    private String amount;

    @JsonProperty("original_amount")
    private String originalAmount;

    @JsonProperty("status")
    private String status;

    @JsonProperty("background_color")
    private String backgroundColor;

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

    @JsonProperty("owner")
    private int owner;

    //TODO: add decoder
    @JsonProperty("icon_url_large")
    private String iconUrlLarge;

    //TODO: add decoder
    @JsonProperty("icon_url")
    private String iconUrl;

    @JsonProperty("app_icon")
    private String appIcon;

    @JsonProperty("descriptions")
    private List<TradeElementDescriptionObject> descriptions;

    @JsonProperty("owner_descriptions")
    private List<TradeElementDescriptionObject> ownerDescriptions;

    @JsonProperty("market_tradable_restriction")
    private int marketTradableRestriction;

    @JsonProperty("market_marketable_restriction")
    private int marketMarketableRestriction;

    @JsonProperty("marketable")
    private int marketable;

    @JsonProperty("market_buy_country_restriction")
    private String marketBuyCountryRestriction;

    @JsonProperty("actions")
    private List<TradeElementAction> actions;

    @JsonProperty("market_actions")
    private List<TradeElementAction> marketActions;

    @JsonProperty("owner_actions")
    private List<TradeElementAction> ownerActions;

    @JsonProperty("fraudwarnings")
    private List<String> fraudWarnings;
}
