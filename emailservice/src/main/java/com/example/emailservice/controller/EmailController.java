package com.example.emailservice.controller;

import com.example.emailservice.Message.ResponseMessage;
import com.example.emailservice.model.EmailDetails;
import com.example.emailservice.service.EmailService;
import com.example.emailservice.service.EmailServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.util.List;

@RestController
public class EmailController {
    @Autowired
    private EmailServiceImpl emailService;

    private static Logger logger = LoggerFactory.getLogger(EmailController.class);

    @GetMapping("/test")
    public String test() {
        return "This is a testing api";
    }
    @PostMapping(value="/sendEmail", consumes= {"multipart/form-data"})
    public ResponseEntity<ResponseMessage> sendEmail(@RequestPart("body") EmailDetails emailDetails, @RequestPart("file") MultipartFile file) {
        String msg = "";
        try {
            logger.info("Sending request to email service");
            System.out.println("Sending request to email service");
            if (file == null) {
                msg = emailService.sendMail(emailDetails);
            }
            msg = emailService.sendMailWithAttachments(emailDetails, file);

            return ResponseEntity.ok().header("my-header", "my-value").body(new ResponseMessage(msg));
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("something bad happened"));
        }
    }

    @PostMapping(value="/sendEmailWithCsv", consumes= {"multipart/form-data"})
    public ResponseEntity<ResponseMessage> sendEmailWithCsv(@RequestPart("emaildetails") EmailDetails emailDetails,
                                                            @RequestPart("payload") MultipartFile file) {
        String msg = "";
        try {
            logger.info("Sending request to email service to send email with csv attachment");
            msg = emailService.sendEmailWithCsv(emailDetails, file);

            return ResponseEntity.ok().header("my-header", "my-value").body(new ResponseMessage(msg));
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("something bad happened"));
        }
    }
}
