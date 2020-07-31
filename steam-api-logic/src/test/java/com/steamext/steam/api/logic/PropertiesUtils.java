package com.steamext.steam.api.logic;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
@Data
@Slf4j
public class PropertiesUtils {
    private Properties data = new Properties();

    @PostConstruct
    protected void load() throws Exception {
        loadProperties(data, getClass().getResourceAsStream("/sensitive-data.properties"));
    }

    private void loadProperties(Properties properties,
                                InputStream steamApiLinksPropertiesFileInputStream) throws Exception {
        try {
            properties.load(steamApiLinksPropertiesFileInputStream);
        } catch (IOException e) {
            wrapCauseExceptionForLoadingProperties(e);
        }
    }

    private void wrapCauseExceptionForLoadingProperties(Throwable e) throws Exception {
        String errorMessage = "Loading integration test user credentials properties file failed";
        log.error(errorMessage, e);
        throw new Exception(errorMessage, e);
    }
}
