package com.example.kafkaservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OutputTopic {
    private String postId;
    private Long postCount;
}
