package com.steamext.steam.api.logic.model.responsemodel.tradeelements;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.steamext.steam.api.logic.model.responsemodel.deserializer.ParsedTradeElementAppDeserializer;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonDeserialize(using = ParsedTradeElementAppDeserializer.class)
public class ParsedTradeElementApp {
    @NonNull
    private List<ParsedTradeElementContext> contexts = new ArrayList<>();

    private int appId;
}
