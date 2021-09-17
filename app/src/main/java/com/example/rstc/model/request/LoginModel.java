package com.example.rstc.model.request;

public class LoginModel {
    private String MobileNo;
    private String password;

    public LoginModel(String mobileNo,String password) {
        MobileNo = mobileNo;
        this.password=password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }
}
