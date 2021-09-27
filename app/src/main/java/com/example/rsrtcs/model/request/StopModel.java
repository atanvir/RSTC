package com.example.rsrtcs.model.request;

public class StopModel {
    private String stopName;

    public StopModel(String stopName) {
        this.stopName = stopName;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }
}
