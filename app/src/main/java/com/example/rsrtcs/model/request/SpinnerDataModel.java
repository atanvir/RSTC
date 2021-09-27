package com.example.rsrtcs.model.request;

public class SpinnerDataModel {
    private String depotName;
    private String proofName;
    private String concessionName;
    private String concessionCode;
    private String busStopName;

    public String getConcessionName() {
        return concessionName;
    }

    public void setConcessionName(String concessionName) {
        this.concessionName = concessionName;
    }

    public String getConcessionCode() {
        return concessionCode;
    }

    public void setConcessionCode(String concessionCode) {
        this.concessionCode = concessionCode;
    }

    public String getProofName() {
        return proofName;
    }

    public void setProofName(String proofName) {
        this.proofName = proofName;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getBusStopName() {
        return busStopName;
    }

    public void setBusStopName(String busStopName) {
        this.busStopName = busStopName;
    }
}
