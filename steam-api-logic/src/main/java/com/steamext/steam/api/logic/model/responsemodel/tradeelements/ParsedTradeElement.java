package com.steamext.steam.api.logic.model.responsemodel.tradeelements;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ParsedTradeElement {
    @JsonProperty("currency")
    private int currency;

    @JsonProperty("appid")
    private int appId;

    @JsonProperty("contextid")
    private int contextId;

    @JsonProperty("id")
    private int id;

    @JsonProperty("classid")
    private int classId;

    @JsonProperty("instanceid")
    private int instanceId;

    @JsonProperty("amountid")
    private int amountId;

    @JsonProperty("status")
    private int status;

    @JsonProperty("original_amount")
    private int originalAmount;

    @JsonProperty("unowned_id")
    private long unownedId;

    @JsonProperty("unowned_contextid")
    private long unownedContextId;

    @JsonProperty("background_color")
    private String backgroundColor;

    @JsonProperty("icon_url")
    private String iconUrl;

    @JsonProperty("icon_url_large")
    private String iconUrlLarge;

    @JsonProperty("tradable")
    private int tradable;
}
