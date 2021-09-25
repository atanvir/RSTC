package com.example.rsrtcs.model.request;

public class SMSModel {
    private String msg;
    private String mobile;
    private String SenderId="RSRTCR";

    public SMSModel(String msg, String mobile) {
        this.msg = msg;
        this.mobile = mobile;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }
}
