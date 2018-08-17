package com.redhat.kafka.demo.producer;

import org.apache.kafka.clients.producer.RecordMetadata;

public class RecordMetadataUtil {

    public static void prettyPrinter(RecordMetadata recordMetadata) {
        if(recordMetadata != null) {
            System.out.printf("Topic: %s - Partition: %d - Offset: %d",
                    recordMetadata.topic(),
                    recordMetadata.partition(),
                    recordMetadata.offset());
        }
    }
}
