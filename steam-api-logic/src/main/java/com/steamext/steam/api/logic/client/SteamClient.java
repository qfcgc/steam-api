package com.steamext.steam.api.logic.client;

import com.steamext.steam.api.logic.entry.SteamGuardCodeProvider;
import com.steamext.steam.api.logic.exceptions.SteamHttpClientException;

/**
 * Client for executing requests in Steam.
 */
public interface SteamClient {
    /**
     * Login into steam account.
     * Uses {@link SteamGuardCodeProvider} if steam guard code is needed.
     *
     * @throws SteamHttpClientException in case of steam availability error
     */
    void login() throws SteamHttpClientException;

    void logout();

    void loadUserInfo() throws SteamHttpClientException;
}
