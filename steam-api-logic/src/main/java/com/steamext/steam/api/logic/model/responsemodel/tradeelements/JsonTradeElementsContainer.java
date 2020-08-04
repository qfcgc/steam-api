package com.steamext.steam.api.logic.model.responsemodel.tradeelements;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class JsonTradeElementsContainer {
    @JsonProperty("success")
    private boolean success;

    @JsonProperty("start")
    private int start;

    @JsonProperty("pagesize")
    private int pageSize;

    @JsonProperty("total_count")
    private int totalCount;

    @JsonProperty("searchdata")
    private TradeElementsSearchData searchData;

    @JsonProperty("results")
    private List<JsonTradeElement> tradeElements;
}
