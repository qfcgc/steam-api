package com.steamext.steam.api.logic.model.responsemodel;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketRestriction {
    private String restrictionText;
    private String restrictionHelpLink;
}
