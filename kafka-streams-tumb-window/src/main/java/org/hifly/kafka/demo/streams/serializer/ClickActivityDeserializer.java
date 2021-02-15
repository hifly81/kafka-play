package org.hifly.kafka.demo.streams.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hifly.kafka.demo.streams.ClickActivity;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class ClickActivityDeserializer implements Deserializer<ClickActivity> {

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {}

    @Override
    public ClickActivity deserialize(String s, byte[] bytes) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String clickactivity = new String(bytes, CHARSET);
            return objectMapper.readValue(clickactivity, ClickActivity.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error reading bytes!", e);
        }
    }

    @Override
    public void close() {}

}