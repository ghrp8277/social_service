package com.example.socialservice.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class JsonUtil {

    private final ObjectMapper objectMapper;

    public JsonUtil() {
        this.objectMapper = new ObjectMapper();
    }

    public <T> T parseJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON format", e);
        }
    }

    public <T> List<T> parseJsonToList(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON format", e);
        }
    }

    public List<Map<String, String>> parseJsonToMapList(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<List<Map<String, String>>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }

    public Map<String, Object> parseJsonToMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON format", e);
        }
    }

    public String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert map to JSON", e);
        }
    }

    public String getValueByKey(String json, String key) {
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode valueNode = rootNode.path(key);
            if (valueNode.isMissingNode()) {
                throw new RuntimeException("Key not found in JSON: " + key);
            }
            return valueNode.asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON format", e);
        }
    }

    public Map<String, Object> getMapByKey(String json, String key) {
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode valueNode = rootNode.path(key);
            if (valueNode.isMissingNode()) {
                throw new RuntimeException("Key not found in JSON: " + key);
            }
            return objectMapper.convertValue(valueNode, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON format", e);
        }
    }

    public List<Long> getListByKey(String json, String key) {
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode valueNode = rootNode.path(key);
            if (valueNode.isMissingNode()) {
                throw new RuntimeException("Key not found in JSON: " + key);
            }
            return objectMapper.convertValue(valueNode, objectMapper.getTypeFactory().constructCollectionType(List.class, Long.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Invalid JSON format", e);
        }
    }

    public <T> T convertValue(Object fromValue, TypeReference<T> toValueType) {
        return objectMapper.convertValue(fromValue, toValueType);
    }
}
