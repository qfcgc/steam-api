package com.steamext.steam.api.logic.client;

import com.steamext.steam.api.logic.client.http.SteamHttpClient;
import com.steamext.steam.api.logic.client.http.parser.UserInfoSteamHTMLParser;
import com.steamext.steam.api.logic.client.http.request.GetRsaKeySteamHttpRequest;
import com.steamext.steam.api.logic.client.http.request.GetUserInfoSteamHttpRequest;
import com.steamext.steam.api.logic.client.http.request.LoginSteamHttpRequest;
import com.steamext.steam.api.logic.entry.SteamGuardCodeProvider;
import com.steamext.steam.api.logic.exceptions.SteamHttpClientException;
import com.steamext.steam.api.logic.model.requestmodel.UserCredentials;
import com.steamext.steam.api.logic.model.responsemodel.RsaDataContainer;
import com.steamext.steam.api.logic.model.responsemodel.UserLoginInfo;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Data
@NoArgsConstructor
public class SteamClientImpl implements SteamClient {
    private SteamHttpClient client;

    private UserCredentials credentials;
    private RsaDataContainer rsaDataContainer;
    private UserLoginInfo userLoginInfo;
    private SteamGuardCodeProvider steamGuardCodeProvider;

    public SteamClientImpl(UserCredentials credentials) {
        this.credentials = credentials;
    }

    @Override
    public void login() throws SteamHttpClientException {
        try {
            rsaDataContainer = client.execute(new GetRsaKeySteamHttpRequest(credentials.getUsername()));
            userLoginInfo = client.execute(new LoginSteamHttpRequest(credentials, rsaDataContainer));

            if (!userLoginInfo.isSuccess()) {
                Objects.requireNonNull(steamGuardCodeProvider);
                String code = steamGuardCodeProvider.getSteamGuardCode();
                credentials = new UserCredentials(
                        credentials.getUsername(),
                        credentials.getPassword(),
                        code,
                        userLoginInfo.getEmailSteamId());

                rsaDataContainer =
                        client.execute(new GetRsaKeySteamHttpRequest(credentials.getUsername()));
                userLoginInfo = client.execute(new LoginSteamHttpRequest(credentials, rsaDataContainer));
            }
        } catch (SteamHttpClientException e) {
            log.error("Login failed", e);
            throw e;
        }
    }

    @Override
    public void logout() {

    }

    @Override
    public void loadUserInfo() throws SteamHttpClientException {
        try {
            Map<String, String> map =
                    client.execute(new GetUserInfoSteamHttpRequest(), new UserInfoSteamHTMLParser<>());
        } catch (SteamHttpClientException e) {
            log.error("Getting user info failed", e);
            throw e;
        }
    }
}
