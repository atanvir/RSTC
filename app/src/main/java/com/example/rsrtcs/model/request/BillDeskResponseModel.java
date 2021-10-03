package com.example.rsrtcs.model.request;

public class BillDeskResponseModel {
    private String Msg;


    public BillDeskResponseModel(String msg) {
        Msg = msg;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }
}
