package com.bsoft.commonlib.util.location;


import com.bsoft.baselib.core.CoreVo;

public class LocationConfig extends CoreVo {
    public String name;
    public double latitude;
    public double longitude;

    public LocationConfig() {
    }

    public LocationConfig(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationConfig(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
