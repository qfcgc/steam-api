package com.steamext.steam.api.logic.client.http.request;

import com.steamext.steam.api.model.requestmodel.UserPageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPost;
import org.springframework.util.MimeTypeUtils;

import java.util.Map;
import java.util.Objects;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetUserInfoSteamHttpRequest implements SteamHttpRequest {
    private final String GET_USER_INFO_PATH = "/profiles/%s";

    private String steamId;

    @Override
    public HttpRequest getRequest() {
        validateSteamId(steamId);

        HttpPost request = new HttpPost(String.format(GET_USER_INFO_PATH, steamId));
        request.setHeader(HttpHeaders.ACCEPT, MimeTypeUtils.TEXT_HTML_VALUE);

        return request;
    }

    @Override
    public Class<?> getValueType() {
        return UserPageInfo.class;
    }

    private void validateSteamId(String steamId) {
        Objects.requireNonNull(steamId);
        if (steamId.isEmpty()) {
            throw new IllegalArgumentException("Steam id is empty");
        }
    }
}
