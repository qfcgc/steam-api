package com.steamext.steam.api.logic.client;

import com.steamext.steam.api.logic.exceptions.SteamHttpClientException;

public interface SteamClient {
    void login() throws SteamHttpClientException;

    void logout();

    void loadUserInfo() throws SteamHttpClientException;
}
