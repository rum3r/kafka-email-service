package com.example.emailservice.service;

import com.example.emailservice.model.EmailDetails;
import org.apache.kafka.common.protocol.types.Field;
import org.rocksdb.util.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ConvertJsonToCsvImpl convertJsonToCsv;

    @Value("{spring.mail.username}")
    private String sender;

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    public String sendMail(EmailDetails emailDetails) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(sender);
            mailMessage.setTo(emailDetails.getRecipient());
            mailMessage.setText(emailDetails.getMsgBody());
            mailMessage.setSubject(emailDetails.getSubject());

            javaMailSender.send(mailMessage);
            return "Email sent successfully...";
        }
        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }

    public String sendMailWithAttachments(EmailDetails emailDetails, MultipartFile file) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(emailDetails.getRecipient());
            mimeMessageHelper.setText(emailDetails.getMsgBody());
            mimeMessageHelper.setSubject(emailDetails.getSubject());

            mimeMessageHelper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);

            javaMailSender.send(mimeMessage);
            return "Email sent successfully...";
        }
        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }

    public String sendEmailWithCsv(EmailDetails emailDetails, MultipartFile file) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;
        String msg = "";
        try {
            //save multipart file to json
            File file1 = convertJsonToCsv.saveToCsv(file);
            Boolean gotRes = convertJsonToCsv.convertToJsonAndSave(file1);

            if (!gotRes)
                throw new Exception();

            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(emailDetails.getRecipient());
            mimeMessageHelper.setText(emailDetails.getMsgBody());
            mimeMessageHelper.setSubject(emailDetails.getSubject());

            String oldfileName = file1.getName();
            File attachFile = new File("/home/kuljeetsingh/Desktop/file-upload-data/" + "CSV" +
                    oldfileName.substring(0, oldfileName.length() - 5) + ".csv");

            mimeMessageHelper.addAttachment(attachFile.getName(), attachFile);

            javaMailSender.send(mimeMessage);
            return "Email sent successfully...";
        }
        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }

    @Scheduled(cron = "*/60 * * * *")
    public void testMethod() {
        log.info("The time is now {}", dateFormat.format(new Date()));
    }
}
