package org.hifly.kafka.demo.streams.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hifly.kafka.demo.streams.ClickActivity;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class ClickActivitySerializer implements Serializer<ClickActivity> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public byte[] serialize(String topic, ClickActivity data) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsString(data).getBytes();
        } catch (Exception exception) {
            System.out.println("Error in serializing object:"+ data);
        }
        return retVal;

    }

    @Override
    public void close() {}

}