package com.steamext.steam.api.model.requestmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentials implements Serializable {
    private String username;
    private String password;
}
