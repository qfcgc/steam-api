package com.steamext.steam.api.logic.client.http.request;

import com.steamext.steam.api.logic.model.responsemodel.tradeelements.ParsedTradeElement;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;


@Slf4j
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class GetTradeElementSteamHttpRequest implements SteamHttpRequest {
    private final String TRADE_ELEMENT_PAGE_PATTERN = "/market/listings/%d/%s";

    @NonNull
    private String tradeElementName;

    @NonNull
    private int gameId;

    @Override
    public HttpRequest getRequest() {
        log.info("Formatting trade element page URL pattern");
        String path = String.format(TRADE_ELEMENT_PAGE_PATTERN, gameId, tradeElementName);
        log.info("URL path is formatted: {}", path);
        return new HttpGet(path);
    }

    @Override
    public Class<?> getValueType() {
        return ParsedTradeElement.class;
    }
}
