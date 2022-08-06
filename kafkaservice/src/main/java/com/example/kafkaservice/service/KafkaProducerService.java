package com.example.kafkaservice.service;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class KafkaProducerService {
    @Autowired
    Environment env;

    private final String LEFT_STREAM_TOPIC = "left-stream-topic";
    private final String RIGHT_STREAM_TOPIC = "right-stream-topic";
    private final String OUTER_JOIN_STREAM_OUT_TOPIC = "stream-stream-outerjoin";
    private final String PROCESSED_STREAM_OUT_TOPIC = "processed-topic";
    private final String KAFKA_APP_ID = "outerjoin";
    private final String KAFKA_SERVER_NAME = "localhost:9092";

    public String kafkaProducer() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, env.getProperty("spring.kafka.application.id"));
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, env.getProperty("spring.kafka.bootstrap-servers"));
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);

        for (int i = 0; i < 100; ++i) {
            producer.send(new ProducerRecord<>("test-topic", String.valueOf(i), "Hi kuljeet"));
        }
        producer.close();
        return "Successfully produced to test topic";
    }
}
