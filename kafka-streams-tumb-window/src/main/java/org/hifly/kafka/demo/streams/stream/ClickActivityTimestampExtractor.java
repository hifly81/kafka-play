package org.hifly.kafka.demo.streams.stream;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.streams.processor.TimestampExtractor;
import org.hifly.kafka.demo.streams.ClickActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ClickActivityTimestampExtractor implements TimestampExtractor {
    @Override
    public long extract(ConsumerRecord<Object, Object> record, long previousTimestamp) {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

        String eventTime = ((ClickActivity)record.value()).getTimestamp();

        try {
            return sdf.parse(eventTime).getTime();
        } catch(ParseException e) {
            return 0;
        }
    }
}

