package com.kailoslab.jjan.server.data.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.kailoslab.jjan.server.utils.Constants;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.*;

@Converter
@Slf4j
public class ListConverter implements AttributeConverter<List<Object>, String> {

    @Override
    public String convertToDatabaseColumn(List jsonData) {
        if(jsonData == null) {
            jsonData = Collections.emptyList();
        }

        try {
            return Constants.OBJECT_MAPPER.writeValueAsString(jsonData);
        } catch (JsonProcessingException e) {
            log.error("Cannot convert to a String: " + jsonData);
            return "[]";
        }
    }

    @Override
    public List<Object> convertToEntityAttribute(String jsonString) {
        List<Object> jsonData;
        try {
            jsonData = Constants.OBJECT_MAPPER.readValue(jsonString, new TypeReference<>() {});
        } catch (JsonProcessingException ex) {
            log.error("Cannot convert to a List: " + jsonString, ex);
            jsonData = new ArrayList<>();
        }
        return jsonData;
    }
}