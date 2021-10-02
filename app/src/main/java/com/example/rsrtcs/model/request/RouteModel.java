package com.example.rsrtcs.model.request;

import com.example.rsrtcs.repository.cache.PrefrenceKeyConstant;

public class RouteModel {
    private String fromStop;
    private String tillStop;
    private String busType= PrefrenceKeyConstant.BUS_TYPE;

    public RouteModel(String fromStop, String tillStop) {
        this.fromStop = fromStop;
        this.tillStop = tillStop;
    }

    public String getFromStop() {
        return fromStop;
    }

    public void setFromStop(String fromStop) {
        this.fromStop = fromStop;
    }

    public String getTillStop() {
        return tillStop;
    }

    public void setTillStop(String tillStop) {
        this.tillStop = tillStop;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }
}
