package com.example.rsrtcs;

import java.io.Serializable;

public class pojoClass implements Serializable {

    private String routename;
    private String routeno;
    private String depotcd;
    private String km;

    public String getRoutename() {
        return routename;
    }

    public void setRoutename(String routename) {
        this.routename = routename;
    }

    public String getRouteno() {
        return routeno;
    }

    public void setRouteno(String routeno) {
        this.routeno = routeno;
    }

    public String getDepotcd() {
        return depotcd;
    }

    public void setDepotcd(String depotcd) {
        this.depotcd = depotcd;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }
}
