package com.io.realworld.config;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class TagListDeserializer extends JsonDeserializer<List<String>> {
    @Override
    public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonToken jt = p.getCurrentToken();
        if(jt == JsonToken.START_ARRAY){
            return p.readValueAs(List.class);
        }else if(jt == JsonToken.VALUE_STRING){
            return Arrays.asList(p.getValueAsString());
        }
        throw new UnsupportedOperationException("Cannot update object");
    }
}
