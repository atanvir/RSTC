package com.example.rsrtcs.model.response;

import java.io.Serializable;

public class RouteModel implements Serializable {
    private String routeName;
    private String routeNo;
    private String depotCd;
    private String km;

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }

    public String getDepotCd() {
        return depotCd;
    }

    public void setDepotCd(String depotCd) {
        this.depotCd = depotCd;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }
}
