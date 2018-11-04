package com.redhat.kafka.demo.producer.serializer.json;

import com.redhat.kafka.demo.producer.BaseProducerCallback;
import com.redhat.kafka.demo.producer.RecordMetadataUtil;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

public class Runner {

    public static void main (String [] args) {
        CustomDataProducer customDataProducer = new CustomDataProducer();
        customDataProducer.start();
        bunchOfMessages("test_custom_data", customDataProducer);
        bunchOfFFMessages("test_custom_data", customDataProducer);
        bunchOfAsynchMessages("test_custom_data", customDataProducer);
    }

    public static void bunchOfMessages(String topic, CustomDataProducer customDataProducer) {
        RecordMetadata lastRecord = null;
        for (int i= 10; i < 30000; i++ )
            lastRecord = customDataProducer.produceSync(new ProducerRecord<>(topic, new CustomData(i)));
        RecordMetadataUtil.prettyPrinter(lastRecord);

    }

    public static void bunchOfFFMessages(String topic, CustomDataProducer baseProducer) {
        for (int i= 10; i < 30000; i++ )
            baseProducer.produceFireAndForget(new ProducerRecord<>(topic, new CustomData(i)));
    }

    public static void bunchOfAsynchMessages(String topic, CustomDataProducer baseProducer) {
        for (int i= 10; i < 30000; i++ )
            baseProducer.produceAsync(new ProducerRecord<>(topic, new CustomData(i)), new BaseProducerCallback());
    }
}