package com.example.rsrtcs.model.request;

public class SpinnerDataModel {

    // Depot
    private String depotNum;
    private String depotName;

    // Proof
    private String proofId;
    private String proofName;

    private String concessionName;
    private String concessionCode;

    // Stop Name
    private String busStopName;
    private String busStopCode;

    public String getBusStopCode() {
        return busStopCode;
    }

    public void setBusStopCode(String busStopCode) {
        this.busStopCode = busStopCode;
    }

    public String getDepotNum() {
        return depotNum;
    }

    public void setDepotNum(String depotNum) {
        this.depotNum = depotNum;
    }

    public String getProofId() {
        return proofId;
    }

    public void setProofId(String proofId) {
        this.proofId = proofId;
    }

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
