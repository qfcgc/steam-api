package com.steamext.steam.api.logic.model.responsemodel.tradeelements;

import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParsedTradeElementPageResponse {
    private ParsedTradeElementWrapper tradeElementWrapper;
    @NonNull
    private List<JsonPriceHistoryPoint> priceHistoryPoints = new ArrayList<>();
}
