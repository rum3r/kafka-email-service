package com.example.emailservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetails {
    private String recipient;
    private String msgBody;
    private String subject;
}
