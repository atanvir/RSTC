package com.example.rstc.model.request;

public class ReportModel {
    private String UserId;

    public ReportModel(String UserId) {
        this.UserId=UserId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }
}
