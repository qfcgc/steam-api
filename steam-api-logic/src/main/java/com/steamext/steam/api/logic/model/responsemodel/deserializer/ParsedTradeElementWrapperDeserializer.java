package com.steamext.steam.api.logic.model.responsemodel.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.steamext.steam.api.logic.model.responsemodel.tradeelements.ParsedTradeElementApp;
import com.steamext.steam.api.logic.model.responsemodel.tradeelements.ParsedTradeElementContext;
import com.steamext.steam.api.logic.model.responsemodel.tradeelements.ParsedTradeElementWrapper;

import java.io.IOException;

public class ParsedTradeElementWrapperDeserializer extends StdDeserializer<ParsedTradeElementWrapper> {
    public ParsedTradeElementWrapperDeserializer() {
        super(ParsedTradeElementWrapper.class);
    }

    @Override
    public ParsedTradeElementWrapper deserialize(JsonParser jsonParser,
                                                 DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        ParsedTradeElementWrapper parsedTradeElementWrapper = new ParsedTradeElementWrapper();
        String appId;
        while ((appId = jsonParser.nextFieldName()) != null) {
            jsonParser.nextToken();
            ParsedTradeElementApp app =
                    deserializationContext.readValue(jsonParser, ParsedTradeElementApp.class);
            app.setAppId(Integer.parseInt(appId));

            parsedTradeElementWrapper.getApps().add(app);
        }
        return parsedTradeElementWrapper;
    }
}
