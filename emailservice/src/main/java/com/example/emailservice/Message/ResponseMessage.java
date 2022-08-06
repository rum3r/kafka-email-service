package com.example.emailservice.Message;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
public class ResponseMessage {
    private String message;
    private String dateTime;

    public ResponseMessage(String message) {
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.message = message;
        this.dateTime = dateTime.format(myFormatObj);
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String setMessage() {
        return message;
    }
    public String getDateTime() {
        return dateTime;
    }
}
