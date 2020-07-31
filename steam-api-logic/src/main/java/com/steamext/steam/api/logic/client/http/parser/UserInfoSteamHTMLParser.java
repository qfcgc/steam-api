package com.steamext.steam.api.logic.client.http.parser;

import com.steamext.steam.api.logic.exceptions.SteamHttpClientException;
import com.steamext.steam.api.model.requestmodel.UserPageInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Data
@Slf4j
public class UserInfoSteamHTMLParser<T> implements SteamHTMLParser<T> {
    @Override
    public T parseHTML(InputStream htmlInputStream) throws SteamHttpClientException {
        UserPageInfo user = new UserPageInfo();
        try {
            Document doc = Jsoup.parse(htmlInputStream, StandardCharsets.UTF_8.name(),
                    "steamcommunity.com");

            user.setUsername(getUsername(doc));
            user.setRealname(getRealName(doc));
            user.setAddress(getAddress(doc));
            user.setLevel(getLevel(doc));

        } catch (IOException e) {
            throw new SteamHttpClientException(e);
        }

        return (T) user;
    }

    private String getUsername(Document doc) {
        String username = null;
        Element usernameSpan = doc.selectFirst("span.actual_persona_name");
        if (usernameSpan != null) {
            username = usernameSpan.text();
        }
        return username;
    }

    private String getRealName(Document doc) {
        String realName = null;
        Element realNameDiv = doc.selectFirst("div.header_real_name > bdi");
        if (realNameDiv != null) {
            realName = realNameDiv.text();
        }
        return realName;
    }

    private String getAddress(Document doc) {
        String address = null;
        Element addressDiv = doc.selectFirst("div.header_real_name");
        if (addressDiv != null) {
            String text = addressDiv.html();
            address = text.substring(text.lastIndexOf('>') + 1).trim();
        }
        return address;
    }

    private int getLevel(Document doc) {
        String level = "0";
        Element levelDiv = doc.selectFirst("div.friendPlayerLevel");
        if (levelDiv != null) {
            level = levelDiv.text();
        }
        int levelDefaultValue = 0;
        int levelInt = 0;
        try {
            levelInt = Integer.parseInt(level);
        } catch (NumberFormatException e) {
            log.warn(String.format("Error getting user level. " +
                    "Using default value: %d", levelDefaultValue), e);
        }

        return levelInt;
    }
}
