package com.example.kafkaservice.controller;

import com.example.kafkaservice.dto.OutputTopic;
import com.example.kafkaservice.dto.RedditTopic;
import com.example.kafkaservice.service.KafkaProducerService;
import com.example.kafkaservice.utils.CustomSerdes;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;


@RestController
public class KafkaController {
    @Autowired
    private KafkaProducerService kafkaProducerService;
    @Autowired
    Environment env;
    private KafkaStreams kafkaOuterStreams;
    private final String LEFT_STREAM_TOPIC = "left-stream-topic";
    private final String RIGHT_STREAM_TOPIC = "right-stream-topic";
    private final String OUTER_JOIN_STREAM_OUT_TOPIC = "stream-stream-outerjoin";
    private final String PROCESSED_STREAM_OUT_TOPIC = "processed-topic";
    private final String KAFKA_APP_ID = "outerjoin";
    private final String KAFKA_SERVER_NAME = "localhost:9092";


    public void startStreamOuterJoin() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, KAFKA_APP_ID);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER_NAME);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        final StreamsBuilder streamsBuilder = new StreamsBuilder();

        KStream<String, String> leftStream = streamsBuilder.stream(LEFT_STREAM_TOPIC);
        KStream<String, String> rightStream = streamsBuilder.stream(RIGHT_STREAM_TOPIC);
        leftStream.foreach((k, v) -> System.out.println("key: " + k + " value: " + v));
        rightStream.foreach((k, v) -> System.out.println("key: " + k + " value: " + v));


        leftStream.outerJoin(rightStream
                        ,(leftValue, rightValue) -> "left=" + leftValue + ", right=" + rightValue,
                        JoinWindows.of(Duration.ofSeconds(1)))
                .groupByKey()
                .reduce(((key, value) -> value))
                .toStream()
                .to(OUTER_JOIN_STREAM_OUT_TOPIC);

        final Topology topology = streamsBuilder.build();
        kafkaOuterStreams = new KafkaStreams(topology, props);

        final CountDownLatch latch = new CountDownLatch(1);

        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                kafkaOuterStreams.close();
                latch.countDown();
            }
        });

        try {
            kafkaOuterStreams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }

    private String REDDIT_STORE_TOPIC = "reddit-store-topic";
    private String REDDIT_COUNT_STORE_TOPIC = "reddit-count-store-topic";
    //last 1 hour key -value pairs fetched

    @RequestMapping("/initializeTopic")
    public void initializetopic() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        Producer<String, String> producer = new KafkaProducer<>(props);

        for (int i = 0; i < 1000000; ++i) {
            producer.send(new ProducerRecord<String, String>(REDDIT_STORE_TOPIC, String.valueOf(i), "Hi, kuljeet"));
        }
        producer.send(new ProducerRecord<String, String>(REDDIT_STORE_TOPIC, "a", "kuljeet"));
        producer.send(new ProducerRecord<String, String>(REDDIT_STORE_TOPIC, "b", "sujit"));
        producer.send(new ProducerRecord<String, String>(REDDIT_STORE_TOPIC, "c", "kuljeet"));
        producer.send(new ProducerRecord<String, String>(REDDIT_STORE_TOPIC, "d", "pta"));
        producer.send(new ProducerRecord<String, String>(REDDIT_STORE_TOPIC, "e", "nhi"));
        producer.send(new ProducerRecord<String, String>(REDDIT_STORE_TOPIC, "a", "haan"));
        producer.close();
    }
    @GetMapping("/test")
    public void test() {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, KAFKA_APP_ID);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_SERVER_NAME);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        final StreamsBuilder builder = new StreamsBuilder();

        KStream<String, RedditTopic> eventStream = builder.stream(REDDIT_STORE_TOPIC,
                Consumed.with(Serdes.String(), CustomSerdes.redditTopic()));

        eventStream
                .groupBy((key, value) -> value.getPostId())
                .count()
                .mapValues
                .to(REDDIT_COUNT_STORE_TOPIC, Produced.with(Serdes.String(), CustomSerdes.outputTopic()));



        final Topology topology = builder.build();
        final KafkaStreams kafkaStreams = new KafkaStreams(topology, props);

        kafkaStreams.start();

    }

}
