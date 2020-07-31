package com.steamext.steam.api.model.responsemodel;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MarketRestriction {
    private String restrictionText;
    private String restrictionHelpLink;
}
