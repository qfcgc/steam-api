package com.steamext.steam.api.logic.model.responsemodel.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.steamext.steam.api.logic.model.responsemodel.tradeelements.ParsedTradeElement;
import com.steamext.steam.api.logic.model.responsemodel.tradeelements.ParsedTradeElementApp;
import com.steamext.steam.api.logic.model.responsemodel.tradeelements.ParsedTradeElementContext;
import com.steamext.steam.api.logic.model.responsemodel.tradeelements.TradeElementAssertDescription;

import java.io.IOException;

public class ParsedTradeElementContextDeserializer extends StdDeserializer<ParsedTradeElementContext> {
    public ParsedTradeElementContextDeserializer() {
        super(ParsedTradeElementContext.class);
    }

    @Override
    public ParsedTradeElementContext deserialize(JsonParser jsonParser,
                                             DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        ParsedTradeElementContext context = new ParsedTradeElementContext();

        String tradeElementId;
        while ((tradeElementId = jsonParser.nextFieldName()) != null) {
            jsonParser.nextToken();
            TradeElementAssertDescription tradeElement =
                    deserializationContext.readValue(jsonParser, TradeElementAssertDescription.class);

            context.getTradeElements().add(tradeElement);
        }

        return context;
    }
}
