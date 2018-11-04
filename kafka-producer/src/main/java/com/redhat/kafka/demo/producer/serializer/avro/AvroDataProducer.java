package com.redhat.kafka.demo.producer.serializer.avro;

import com.redhat.kafka.demo.producer.AbstractKafkaProducer;
import com.redhat.kafka.demo.producer.BaseProducerCallback;
import com.redhat.kafka.demo.producer.KafkaConfig;
import com.redhat.kafka.demo.producer.BaseKafkaProducer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AvroDataProducer extends AbstractKafkaProducer<String, GenericRecord> implements BaseKafkaProducer<String, GenericRecord> {

    private Schema schema;
    private GenericRecord car;


    public void start() {
        producer = new org.apache.kafka.clients.producer.KafkaProducer(KafkaConfig.avroProducer());
        Schema.Parser parser = new Schema.Parser();
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            schema = parser.parse(new File(classLoader.getResource("car.avsc").getFile()));
            car = new GenericData.Record(schema);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(KafkaProducer<String, GenericRecord> kafkaProducer) {
        producer = kafkaProducer;
    }

    public void stop() {
        producer.close();
    }

    public GenericRecord getGenericRecord() {
        if(car != null)
            return new GenericData.Record(schema);
        return car;
    }

    @Override
    public Future<RecordMetadata> produceFireAndForget(ProducerRecord<String, GenericRecord> producerRecord) {
        if(producer == null)
            start();

        return producer.send(producerRecord);
    }

    @Override
    public RecordMetadata produceSync(ProducerRecord<String, GenericRecord> producerRecord) {
        if(producer == null)
            start();

        RecordMetadata recordMetadata = null;
        try {
            recordMetadata = producer.send(producerRecord).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return recordMetadata;
    }

    @Override
    public void produceAsync(ProducerRecord<String, GenericRecord> producerRecord, Callback callback) {
        if(producer == null)
            start();

        producer.send(producerRecord, new BaseProducerCallback());
    }

    public Schema getSchema() {
        return schema;
    }

    public GenericRecord getCar() {
        return car;
    }
}

