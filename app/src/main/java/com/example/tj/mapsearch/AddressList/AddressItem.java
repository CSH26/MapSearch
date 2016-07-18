package com.example.tj.mapsearch.AddressList;

public class AddressItem {

    private boolean mSelectable;
    private String addressName;
    private Double latitude;
    private Double longitude;

    public AddressItem(boolean mSelectable, String addressName, Double latitude, Double longitude) {
        this.mSelectable = mSelectable;
        this.addressName = addressName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public AddressItem(String addressName, Double latitude, Double longitude) {
        this.mSelectable = false;
        this.addressName = addressName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAddressName(){
        return addressName;
    }

    public boolean ismSelectable() {
        return mSelectable;
    }

    public void setmSelectable(boolean mSelectable) {
        this.mSelectable = mSelectable;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }


}
