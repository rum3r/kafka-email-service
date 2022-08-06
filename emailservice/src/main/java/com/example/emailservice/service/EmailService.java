package com.example.emailservice.service;

import com.example.emailservice.model.EmailDetails;
import org.springframework.web.multipart.MultipartFile;

public interface EmailService {
    String sendMail(EmailDetails emailDetails);
    String sendMailWithAttachments(EmailDetails emailDetails, MultipartFile file);
    String sendEmailWithCsv(EmailDetails emailDetails, MultipartFile file);
}
