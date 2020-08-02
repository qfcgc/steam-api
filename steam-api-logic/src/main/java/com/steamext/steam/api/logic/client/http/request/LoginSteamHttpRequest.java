package com.steamext.steam.api.logic.client.http.request;

import com.steamext.steam.api.logic.model.requestmodel.UserCredentials;
import com.steamext.steam.api.logic.model.responsemodel.RsaDataContainer;
import com.steamext.steam.api.logic.model.responsemodel.UserLoginInfo;
import com.steamext.steam.api.logic.utils.SteamPasswordRSAEncode;
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
public class LoginSteamHttpRequest implements SteamHttpRequest {
    private final String LOGIN_REQUEST_PATH = "/login/dologin";

    private UserCredentials credentials;
    private RsaDataContainer rsaDataContainer;

    @Override
    public HttpRequest getRequest() {
        validateRequiredFields();

        HttpPost request = new HttpPost(LOGIN_REQUEST_PATH);

        request.setEntity(generateRequestBody());

        return request;
    }

    @Override
    public Class<?> getValueType() {
        return UserLoginInfo.class;
    }

    protected HttpEntity generateRequestBody() {
        log.info("Generating request body for steam login request");
        MultipartEntityBuilder multipartBuilder = MultipartEntityBuilder.create()
                .addTextBody("username", credentials.getUsername())
                .addTextBody("password",
                        SteamPasswordRSAEncode.encodePassword(
                                rsaDataContainer.getPublicKeyMod(),
                                rsaDataContainer.getPublicKeyExp(),
                                credentials.getPassword()))
                .addTextBody("rsatimestamp", rsaDataContainer.getTimestamp())
                .addTextBody("remember_login", "false");
        if (credentials.getEmailauth() != null) {
            multipartBuilder.addTextBody("emailauth", credentials.getEmailauth());
        }
        if (credentials.getEmailSteamId() != null) {
            multipartBuilder.addTextBody("emailsteamid", credentials.getEmailSteamId());
        }
        return multipartBuilder.build();
    }

    private void validateRequiredFields() {
        validateCredentials();
        validateRsaDataContainer();
    }

    private void validateCredentials() {
        Objects.requireNonNull(credentials, "Credentials are required for login to steam");
        Objects.requireNonNull(credentials.getUsername(), "Username is required for login to steam");
        Objects.requireNonNull(credentials.getPassword(), "Password is required for login to steam");
    }

    private void validateRsaDataContainer() {
        Objects.requireNonNull(rsaDataContainer, "RSA data is required for login to steam");
        Objects.requireNonNull(rsaDataContainer.getPublicKeyExp(),
                "RSA public key is required for login to steam");
        Objects.requireNonNull(rsaDataContainer.getPublicKeyMod(),
                "RSA public mod is required for login to steam");
    }
}
