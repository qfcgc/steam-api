package com.steamext.steam.api.logic.model.responsemodel.tradeelements;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JsonPriceHistoryPoint {
    private String date;
    private double price;
    private String amount;
}
