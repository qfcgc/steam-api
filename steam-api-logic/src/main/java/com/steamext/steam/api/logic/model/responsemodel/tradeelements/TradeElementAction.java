package com.steamext.steam.api.logic.model.responsemodel.tradeelements;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TradeElementAction {
    @JsonProperty("link")
    private String link;

    @JsonProperty("name")
    private String name;
}
