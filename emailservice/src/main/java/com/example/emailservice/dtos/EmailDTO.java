package com.example.emailservice.dtos;

import java.util.ArrayList;
import java.util.List;

public class EmailDTO {
    private String apiToHit;
    private String subject;
    private List<String> ccList = new ArrayList<>();
    private List<String> bccList;
    private String body;
    private List<String> requiredHeaders;

    public String getApiToHit() {
        return apiToHit;
    }

    public void setApiToHit(String apiToHit) {
        this.apiToHit = apiToHit;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<String> getCcList() {
        return ccList;
    }

    public void setCcList(List<String> ccList) {
        this.ccList = ccList;
    }

    public List<String> getBccList() {
        return bccList;
    }

    public void setBccList(List<String> bccList) {
        this.bccList = bccList;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getRequiredHeaders() {
        return requiredHeaders;
    }

    public void setRequiredHeaders(List<String> requiredHeaders) {
        this.requiredHeaders = requiredHeaders;
    }
}
