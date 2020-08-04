package com.steamext.steam.api.logic.client.http.request;

import com.steamext.steam.api.logic.model.responsemodel.tradeelements.JsonTradeElementsContainer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class GetPageWithNTradeElementsByAppIdSteamHttpRequest implements SteamHttpRequest {
    private final String TRADE_ELEMENTS_BY_APP_ID_PATH = "/market/search/render/?query=&start=0&count=354856&search_descriptions=0&sort_column=popular&sort_dir=desc&norender=1";

    private int pageNumber = 0;

    @NonNull
    private String appId;

    /**
     * Known column names:
     * <ul>
     *     <li>popular (by default)</li>
     *     <li>name</li>
     * </ul>
     */
    @NonNull
    private String sortColumn = "popular";

    /**
     * 100 - max
     */
    @NonNull
    private int blockSize = 100;

    @Override
    public HttpRequest getRequest() {
        URI uri = null;
        try {
            uri = buildURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return new HttpGet(uri);
    }

    private URI buildURI() throws URISyntaxException {
        return new URIBuilder(TRADE_ELEMENTS_BY_APP_ID_PATH)
                .addParameter("appid", appId)
                .addParameter("query", "")
                .addParameter("count", String.valueOf(blockSize))
                .addParameter("search_descriptions", "0") //todo: what does it?
                .addParameter("sort_column", sortColumn)
                .addParameter("sort_dir", "desc") //asc doesn't work
                .addParameter("start", String.valueOf(blockSize * pageNumber))
                .addParameter("norender", "1") //1 means we don't want to get rendered html
                .build();
    }

    @Override
    public Class<?> getValueType() {
        return JsonTradeElementsContainer.class;
    }
}
