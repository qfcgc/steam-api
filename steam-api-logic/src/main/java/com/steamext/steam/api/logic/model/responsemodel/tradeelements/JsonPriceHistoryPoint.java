package com.steamext.steam.api.logic.model.responsemodel.tradeelements;

import lombok.*;

import java.util.Calendar;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JsonPriceHistoryPoint {
    private Calendar date;
    private double price;
    private int amount;

    @Override
    public String toString() {
        return "JsonPriceHistoryPoint{" +
                "date=" + date.getTime() +
                ", price=" + price +
                ", amount=" + amount +
                '}';
    }
}
