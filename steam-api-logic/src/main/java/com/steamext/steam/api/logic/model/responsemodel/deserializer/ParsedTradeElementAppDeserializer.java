package com.steamext.steam.api.logic.model.responsemodel.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.steamext.steam.api.logic.model.responsemodel.tradeelements.ParsedTradeElementApp;
import com.steamext.steam.api.logic.model.responsemodel.tradeelements.ParsedTradeElementContext;

import java.io.IOException;

public class ParsedTradeElementAppDeserializer extends StdDeserializer<ParsedTradeElementApp> {
    public ParsedTradeElementAppDeserializer() {
        super(ParsedTradeElementApp.class);
    }

    @Override
    public ParsedTradeElementApp deserialize(JsonParser jsonParser,
                                             DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        ParsedTradeElementApp app = new ParsedTradeElementApp();

        String contextId;
        while ((contextId = jsonParser.nextFieldName()) != null) {
            jsonParser.nextToken();
            ParsedTradeElementContext context =
                    deserializationContext.readValue(jsonParser, ParsedTradeElementContext.class);
            context.setContextId(Integer.parseInt(contextId));

            app.getContexts().add(context);
        }

        return app;
    }
}
