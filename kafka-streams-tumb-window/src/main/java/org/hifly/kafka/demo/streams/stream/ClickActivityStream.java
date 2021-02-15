package org.hifly.kafka.demo.streams.stream;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hifly.kafka.demo.streams.ClickActivity;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;

import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Properties;

public class ClickActivityStream {

    private Properties properties;

    private static final String BROKER_LIST =
            System.getenv("kafka.broker.list") != null ? System.getenv("kafka.broker.list") : "localhost:9092,localhost:9093,localhost:9094";

    public void configure(Properties properties) {
        if (properties.getProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG) == null)
            properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BROKER_LIST);
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "clickactivity_app_id");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        properties.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, ClickActivityTimestampExtractor.class.getName());
        //latency first
        properties.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        try {
            properties.put(StreamsConfig.STATE_DIR_CONFIG,
                      Files.createTempDirectory("clickactivity-tumbling-windows").toAbsolutePath().toString());
        }
        catch(IOException e) {}
    }

    public void startStream(String inputTopic,
                            String outputTopic) {

        KafkaStreams kafkaStreams = 
            new KafkaStreams(createTopology(inputTopic, outputTopic), properties);
        kafkaStreams.start();

        // SIGTERM
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                kafkaStreams.close();
            } catch (final Exception e) {
            }
        }));

    }

    public Topology createTopology(
            String inputTopic,
            String outputTopic)  {

        StreamsBuilder builder = new StreamsBuilder();

        ObjectMapper mapper = new ObjectMapper();

        builder.stream(
                inputTopic,
                Consumed.with(Serdes.String(), Serdes.String()))
                .map((url, clickactivity) -> {
                    try {
                        ClickActivity ca = mapper.readValue(clickactivity, ClickActivity.class);
                        return new KeyValue<>(url, ca);
                    } catch (Exception e) {
                        throw new RuntimeException("Can't generate the kstream" + e);
                    }
                })
                .groupByKey()
                .windowedBy(TimeWindows.of(Duration.ofMinutes(5)))
                .count()
                .toStream()
                .map((Windowed<String> key, Long count) -> new KeyValue<>(key.key(), count.toString()))
                .to(outputTopic, Produced.with(Serdes.String(), Serdes.String()));


        return builder.build();
    }

}
