package com.example.kafkaservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RedditTopic {
    private String postId;
    private String commentId;
}
