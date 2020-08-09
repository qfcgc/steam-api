package com.steamext.steam.api.logic.model.responsemodel.tradeelements;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.steamext.steam.api.logic.model.responsemodel.deserializer.ParsedTradeElementWrapperDeserializer;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonDeserialize(using = ParsedTradeElementWrapperDeserializer.class)
public class ParsedTradeElementWrapper {
    @NonNull
    private List<ParsedTradeElementApp> apps = new ArrayList<>();
}
