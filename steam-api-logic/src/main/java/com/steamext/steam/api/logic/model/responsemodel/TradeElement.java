package com.steamext.steam.api.logic.model.responsemodel;

import lombok.Data;

@Data
public class TradeElement {
    private String tradeElementHref;
    private String imageHref;
    private String name;
    private String hashName;
    private String gameName;
    private String appId;
    private String quantity;
    private String lowestPrice;
}
