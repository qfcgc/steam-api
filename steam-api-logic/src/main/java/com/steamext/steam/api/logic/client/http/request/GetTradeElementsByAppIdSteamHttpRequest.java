package com.steamext.steam.api.logic.client.http.request;

import com.steamext.steam.api.logic.model.responsemodel.TradeElements;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RequiredArgsConstructor
@Data
public class GetTradeElementsByAppIdSteamHttpRequest implements SteamHttpRequest {
    private final String TRADE_ELEMENTS_BY_APP_ID_PATH = "/market/search";

    @NonNull
    private String appId;

    @Override
    public HttpRequest getRequest() {
        URI uri = null;
        try {
            uri = new URIBuilder(TRADE_ELEMENTS_BY_APP_ID_PATH).addParameter("appid", appId).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return new HttpGet(uri);
    }

    @Override
    public Class<?> getValueType() {
        return TradeElements.class;
    }
}
