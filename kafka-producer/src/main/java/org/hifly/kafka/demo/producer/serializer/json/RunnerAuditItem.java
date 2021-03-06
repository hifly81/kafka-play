package org.hifly.kafka.demo.producer.serializer.json;

import org.hifly.kafka.demo.producer.RecordMetadataUtil;
import org.hifly.kafka.demo.producer.serializer.model.AuditItem;
import org.hifly.kafka.demo.producer.serializer.model.CustomData;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;


public class RunnerAuditItem {

    public static void main (String [] args) {
        JsonProducer<CustomData> jsonProducer = new JsonProducer<>("org.hifly.kafka.demo.producer.serializer.json.AuditItemJsonSerializer");
        jsonProducer.start();
        bunchOfMessages("audit", jsonProducer);
    }

    public static void bunchOfMessages(String topic, JsonProducer jsonProducer) {
        RecordMetadata lastRecord = null;
        for (int i= 0; i < 2; i++ ) {
            AuditItem auditItem = new AuditItem();
            auditItem.setMethod("test");
            lastRecord = jsonProducer.produceSync(new ProducerRecord<>(topic, auditItem));
        }
        RecordMetadataUtil.prettyPrinter(lastRecord);

    }

}
