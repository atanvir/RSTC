package com.example.rsrtcs.model.request;

public class SignupModel {
    private String UserName;
    private String EmailId;
    private String MobileNo;
    private String Password;
    private String otp;

    public SignupModel(String userName, String emailId, String mobileNo, String password, String otp) {
        UserName = userName;
        EmailId = emailId;
        MobileNo = mobileNo;
        Password = password;
        this.otp = otp;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
