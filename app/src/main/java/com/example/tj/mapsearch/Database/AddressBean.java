package com.example.tj.mapsearch.Database;

/**
 * Created by TJ on 2016-07-19.
 */
public class AddressBean {

    String addressName;
    double latitude;
    double longitude;

    public AddressBean(String addressName, double latitude, double longitude) {
        this.addressName = addressName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
