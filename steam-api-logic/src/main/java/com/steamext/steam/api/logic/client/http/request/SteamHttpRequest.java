package com.steamext.steam.api.logic.client.http.request;

import org.apache.http.HttpRequest;

public interface SteamHttpRequest {
    HttpRequest getRequest();

    Class<?> getValueType();
}
