package com.steamext.steam.api.logic.model.responsemodel.tradeelements;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.steamext.steam.api.logic.model.responsemodel.deserializer.ParsedTradeElementContextDeserializer;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonDeserialize(using = ParsedTradeElementContextDeserializer.class)
public class ParsedTradeElementContext {
    @NonNull
    private List<TradeElementAssertDescription> tradeElements = new ArrayList<>();

    private int contextId;
}
