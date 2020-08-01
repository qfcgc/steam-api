package com.steamext.steam.api.logic.model.responsemodel;

import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class MarketRestrictions {
    @NonNull
    private Collection<MarketRestriction> restrictions = new ArrayList<>();
}
