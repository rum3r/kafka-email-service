package com.example.emailservice.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface ConvertJsonToCsv {
    Boolean convertToJsonAndSave(File file);
    File saveToCsv(MultipartFile file) throws IOException;
}
