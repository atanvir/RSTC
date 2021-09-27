package com.example.rsrtcs.model.request;

public class ForgotModel {
    private String MobileNo;

    public ForgotModel(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }
}
