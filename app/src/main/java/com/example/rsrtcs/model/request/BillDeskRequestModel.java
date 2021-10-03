package com.example.rsrtcs.model.request;

import com.example.rsrtcs.repository.cache.PrefrenceKeyConstant;

public class BillDeskRequestModel {
    private String CardNo;
    private String UserId;
    private String Msg;
    private String RequestType= PrefrenceKeyConstant.REQUEST_TYPE_BILLDESK;


    public BillDeskRequestModel(String cardNo, String userId, String msg) {
        CardNo = cardNo;
        UserId = userId;
        Msg = msg;
    }

    public String getCardNo() {
        return CardNo;
    }

    public void setCardNo(String cardNo) {
        CardNo = cardNo;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
    }

    public String getRequestType() {
        return RequestType;
    }

    public void setRequestType(String requestType) {
        RequestType = requestType;
    }
}

