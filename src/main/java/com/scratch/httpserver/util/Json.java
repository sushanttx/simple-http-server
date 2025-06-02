package com.scratch.httpserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

public class Json {
    // ObjectMapper is used to convert JSON to Java objects and vice versa
    private  static final ObjectMapper myObjectMapper = defaultObjectMapper();

    private static ObjectMapper defaultObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        // if the json has unknown properties, ignore them. Does not crash if a property is not found
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    // parse the json string and return a JsonNode
    public static JsonNode parse(String jsonSrc) throws JsonProcessingException {
        return myObjectMapper.readTree(jsonSrc);
    }

    // convert the JsonNode to an object of the specified class
    public static <A> A fromJson(JsonNode node, Class<A> clazz) throws JsonProcessingException {
        return myObjectMapper.treeToValue(node, clazz);
    }

    // convert the object to a JsonNode
    public static JsonNode toJson(Object obj){
        return myObjectMapper.valueToTree(obj);
    }

    // convert the JsonNode to a string
    public static String stringify(JsonNode node) throws JsonProcessingException {
        return generateJson(node, false);
    }

    // convert the JsonNode to a pretty string
    public static String stringifyPretty(JsonNode node) throws JsonProcessingException {
        return generateJson(node, true);
    }

    // generate the json string
    private static String generateJson(Object obj, boolean pretty) throws JsonProcessingException {
        ObjectWriter objectWriter = myObjectMapper.writer();
        if(pretty){
            objectWriter = objectWriter.with(SerializationFeature.INDENT_OUTPUT);
        }
        return objectWriter.writeValueAsString(obj);
    }







}
