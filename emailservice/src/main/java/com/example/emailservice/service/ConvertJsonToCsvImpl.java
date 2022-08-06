package com.example.emailservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.dataformat.csv.CsvSchema.Builder;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ConvertJsonToCsvImpl implements ConvertJsonToCsv {
    @Autowired
    private Gson gson;
    public Boolean convertToJsonAndSave(File file) {
        try {
            String fileName = file.getName();
            JsonNode jsonTree = new ObjectMapper().readTree(file);

            Builder csvSchemaBuilder = CsvSchema.builder();
            JsonNode firstObject = jsonTree.elements().next();

            firstObject.fieldNames().forEachRemaining(fieldName -> {csvSchemaBuilder.addColumn(fieldName);} );
            CsvSchema csvSchema = csvSchemaBuilder.build().withHeader();

            CsvMapper csvMapper = new CsvMapper();
            csvMapper.writerFor(JsonNode.class)
                    .with(csvSchema)
                    .writeValue(new File("/home/kuljeetsingh/Desktop/file-upload-data/" + "CSV" +
                            fileName.substring(0, fileName.length() - 5) + ".csv"), jsonTree);
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public File saveToCsv(MultipartFile file) throws IOException {
        File convFile = new File("/home/kuljeetsingh/Desktop/file-upload-data/" + file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
//    public static void main(String[] args) {
//        ConvertJsonToCsvImpl csv = new ConvertJsonToCsvImpl();
//        csv.convert("kuljeet.json");
//    }
}
