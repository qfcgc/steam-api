package com.steamext.steam.api.logic.client.http.request;

import com.steamext.steam.api.logic.model.responsemodel.RsaDataContainer;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;

@Slf4j
@RequiredArgsConstructor
@Data
public class GetRsaKeySteamHttpRequest implements SteamHttpRequest {
    private final String GET_RSA_KEY_PATH = "/login/getrsakey";

    @NonNull
    private String username;

    @Override
    public HttpRequest getRequest() {
        HttpPost request = new HttpPost(GET_RSA_KEY_PATH);

        HttpEntity body = generateRequestBody();
        request.setEntity(body);

        return request;
    }

    @Override
    public Class<?> getValueType() {
        return RsaDataContainer.class;
    }

    private HttpEntity generateRequestBody() {
        return MultipartEntityBuilder.create()
                .addTextBody("username", username)
                .build();
    }
}
