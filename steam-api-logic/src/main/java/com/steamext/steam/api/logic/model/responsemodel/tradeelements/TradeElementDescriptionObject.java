package com.steamext.steam.api.logic.model.responsemodel.tradeelements;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TradeElementDescriptionObject {
    @JsonProperty("type")
    private String type;

    @JsonProperty("value")
    private String value;

    @JsonProperty("color")
    private String color;
}
