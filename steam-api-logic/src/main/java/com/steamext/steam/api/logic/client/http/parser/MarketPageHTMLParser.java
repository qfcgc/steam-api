package com.steamext.steam.api.logic.client.http.parser;

import com.steamext.steam.api.logic.exceptions.SteamHttpClientException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Data
@Slf4j
public class MarketPageHTMLParser implements SteamHTMLParser<String> {
    @Override
    public String parseHTML(InputStream html) throws SteamHttpClientException {
        try {
            Document doc = Jsoup.parse(html, StandardCharsets.UTF_8.name(),
                    "steamcommunity.com");

//            user.setUsername(getUsername(doc));
//            user.setRealname(getRealName(doc));
//            user.setAddress(getAddress(doc));
//            user.setLevel(getLevel(doc));

            return doc.html();

        } catch (IOException e) {
            throw new SteamHttpClientException(e);
        }

//        return (T) user;
//        StringWriter sw = new StringWriter();
//        try {
//            IOUtils.copy(html, sw);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
