package com.steamext.steam.api.logic.model.responsemodel.tradeelements;

import lombok.Data;
import lombok.NonNull;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParsedTradeElementPageResponse {
    private ParsedTradeElementWrapper tradeElementWrapper;

    @ToString.Exclude
    @NonNull
    private List<JsonPriceHistoryPoint> priceHistoryPoints = new ArrayList<>();
}
