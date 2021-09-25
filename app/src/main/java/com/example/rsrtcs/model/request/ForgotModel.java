package com.example.rsrtcs.model.request;

public class ForgotModel {
    private String EmailId;

    public ForgotModel(String emailId) {
        EmailId = emailId;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }
}
