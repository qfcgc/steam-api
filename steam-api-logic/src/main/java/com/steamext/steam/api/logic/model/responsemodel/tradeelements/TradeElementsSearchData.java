package com.steamext.steam.api.logic.model.responsemodel.tradeelements;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TradeElementsSearchData {
    @JsonProperty("query")
    private String query;

    @JsonProperty("search_descriptions")
    private boolean searchDescriptions;

    @JsonProperty("total_count")
    private int totalCount;

    @JsonProperty("pagesize")
    private int pageSize;

    @JsonProperty("prefix")
    private String prefix;

    @JsonProperty("class_prefix")
    private String classPrefix;
}
