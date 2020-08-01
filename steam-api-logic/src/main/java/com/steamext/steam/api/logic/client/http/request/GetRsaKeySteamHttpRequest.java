package com.steamext.steam.api.logic.client.http.request;

import com.steamext.steam.api.logic.model.responsemodel.RsaDataContainer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;

import java.util.Objects;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GetRsaKeySteamHttpRequest implements SteamHttpRequest {
    private final String GET_RSA_KEY_PATH = "/login/getrsakey";

    private String username;

    @Override
    public HttpRequest getRequest() {
        validateRequiredFields();

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

    private void validateRequiredFields() {
        Objects.requireNonNull(username, "Username is required for getting steam RSA key");
    }
}
