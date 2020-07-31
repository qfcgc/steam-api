package com.steamext.steam.api.logic.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SteamHttpClientException extends Exception {
    public SteamHttpClientException() {
    }

    public SteamHttpClientException(String message) {
        super(message);
    }

    public SteamHttpClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public SteamHttpClientException(Throwable cause) {
        super(cause);
    }

    public SteamHttpClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
