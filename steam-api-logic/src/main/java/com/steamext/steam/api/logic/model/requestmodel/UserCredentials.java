package com.steamext.steam.api.logic.model.requestmodel;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class UserCredentials implements Serializable {
    @NonNull
    private String username;
    @NonNull
    private String password;
    private String emailauth;
    @JsonProperty("emailsteamid")
    private String emailSteamId;
}
