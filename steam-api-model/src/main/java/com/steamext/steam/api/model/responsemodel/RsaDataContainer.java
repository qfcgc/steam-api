package com.steamext.steam.api.model.responsemodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RsaDataContainer {
    private boolean success;

    @JsonProperty("publickey_mod")
    private String publicKeyMod;

    @JsonProperty("publickey_exp")
    private String publicKeyExp;

    private String timestamp;

    @JsonProperty("token_gid")
    private String tokenGId;
}
