package com.steamext.steam.api.logic.client.http.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;

@Slf4j
@NoArgsConstructor
@Data
public class GetMarketPageSteamHttpRequest implements SteamHttpRequest {
    private final String MARKET_PATH = "/market";

    @Override
    public HttpRequest getRequest() {
        return new HttpGet(MARKET_PATH);
    }

    @Override
    public Class<?> getValueType() {
        return String.class;
    }
}
