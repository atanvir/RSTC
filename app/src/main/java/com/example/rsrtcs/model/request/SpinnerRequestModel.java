package com.example.rsrtcs.model.request;

public class SpinnerRequestModel {
    private String ConcessionCode;

    public SpinnerRequestModel(String concessionCode) {
        ConcessionCode = concessionCode;
    }

    public String getConcessionCode() {
        return ConcessionCode;
    }

    public void setConcessionCode(String concessionCode) {
        ConcessionCode = concessionCode;
    }
}
