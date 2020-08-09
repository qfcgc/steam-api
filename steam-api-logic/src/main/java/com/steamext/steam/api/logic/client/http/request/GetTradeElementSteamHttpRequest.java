package com.steamext.steam.api.logic.client.http.request;

import com.steamext.steam.api.logic.model.responsemodel.tradeelements.ParsedTradeElementPageResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Slf4j
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class GetTradeElementSteamHttpRequest implements SteamHttpRequest {
    private final String TRADE_ELEMENT_PAGE_PATTERN = "/market/listings/%d/%s";

    @NonNull
    private String tradeElementMarketHashName;

    @NonNull
    private int gameId;

    @Override
    public HttpRequest getRequest() {
        log.info("Formatting trade element page URL pattern");
        String path = String.format(TRADE_ELEMENT_PAGE_PATTERN, gameId,
                encodeValue(tradeElementMarketHashName));
        log.info("URL path is formatted: {}", path);
        return new HttpGet(path);

    }

    private String encodeValue(String value) {
        String encodedValue = value;
        try {
            encodedValue = URLEncoder.encode(value, StandardCharsets.UTF_8.toString())
                    .replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            log.warn("Encoding is unsupported. Returning original value", e);
        }
        return encodedValue;
    }

    @Override
    public Class<?> getValueType() {
        return ParsedTradeElementPageResponse.class;
    }
}
