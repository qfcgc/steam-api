package com.steamext.steam.api.logic.model.responsemodel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Collection;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserLoginInfo {
    @JsonProperty("success")
    private boolean success;

    @JsonProperty("requires_twofactor")
    private boolean requiredTwoFactor;

    @JsonProperty("login_complete")
    private boolean loginComplete;

    @JsonProperty("transfer_urls")
    private Collection<String> transferURLs;

    @JsonProperty("transfer_parameters")
    private TransferParameters transferParameters;

    @JsonProperty("emailauth_needed")
    private boolean emailAuthNeeded;

    @JsonProperty("emailsteamid")
    private String emailSteamId;

    @JsonProperty("emaildomain")
    private String emailDomain;

    @JsonProperty("message")
    private String message;

    @Data
    public static class TransferParameters {
        @JsonProperty("steamid")
        private String steamId;

        @JsonProperty("token_secure")
        private String tokenSecure;

        @JsonProperty("auth")
        private String auth;

        @JsonProperty("remember_login")
        private boolean rememberLogin;

        @JsonProperty("webcookie")
        private String webCookie;
    }
}
