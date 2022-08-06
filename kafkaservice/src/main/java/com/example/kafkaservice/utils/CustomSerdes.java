package com.example.kafkaservice.utils;
import com.example.kafkaservice.dto.OutputTopic;
import com.example.kafkaservice.dto.RedditTopic;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public final class CustomSerdes {
    private CustomSerdes() {}
    public static Serde<RedditTopic> redditTopic() {
        JsonSerializer<RedditTopic> serializer = new JsonSerializer<>();
        JsonDeserializer<RedditTopic> deserializer = new JsonDeserializer<>(RedditTopic.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }

    public static Serde<OutputTopic> outputTopic() {
        JsonSerializer<OutputTopic> serializer = new JsonSerializer<>();
        JsonDeserializer<OutputTopic> deserializer = new JsonDeserializer<>(OutputTopic.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }
}
