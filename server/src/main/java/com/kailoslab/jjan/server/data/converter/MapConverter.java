package com.kailoslab.jjan.server.data.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.kailoslab.jjan.server.utils.Constants;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Converter
@Slf4j
public class MapConverter implements AttributeConverter<Map<String, Object>, String> {

    TypeReference<HashMap<String,Object>> typeRef
            = new TypeReference<HashMap<String,Object>>() {};

    @Override
    public String convertToDatabaseColumn(Map jsonData) {
        if(jsonData == null) {
            jsonData = Collections.emptyMap();
        }

        try {
            return Constants.OBJECT_MAPPER.writeValueAsString(jsonData);
        } catch (JsonProcessingException e) {
            log.error("Cannot convert a map to a string: " + jsonData);
            return "{}";
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String jsonString) {
        Map<String, Object> jsonData;
        try {
            jsonData = Constants.OBJECT_MAPPER.readValue(jsonString, typeRef);
        } catch (JsonProcessingException ex) {
            log.error("Cannot convert to JSONObject: " + jsonString, ex);
            jsonData = new HashMap<>();
        }
        return jsonData;
    }
}