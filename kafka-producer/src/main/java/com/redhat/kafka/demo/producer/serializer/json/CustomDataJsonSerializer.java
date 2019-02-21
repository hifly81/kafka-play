package com.redhat.kafka.demo.producer.serializer.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.redhat.kafka.demo.producer.serializer.model.CustomData;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class CustomDataJsonSerializer implements Serializer<CustomData> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public byte[] serialize(String topic, CustomData data) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsString(data).getBytes();
        } catch (Exception exception) {
            System.out.println("Error in serializing object"+ data);
        }
        return retVal;

    }

    @Override
    public void close() {}

}