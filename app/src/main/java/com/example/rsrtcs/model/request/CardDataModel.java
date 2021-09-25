package com.example.rsrtcs.model.request;

public class CardDataModel {

    private String CardNo;
    public CardDataModel(String CardNo){
        this.CardNo=CardNo;
    }


    public String getCardNo() {
        return CardNo;
    }

    public void setCardNo(String cardNo) {
        CardNo = cardNo;
    }
}

