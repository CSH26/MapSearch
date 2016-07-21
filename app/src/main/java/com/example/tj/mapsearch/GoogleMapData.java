package com.example.tj.mapsearch;

import android.os.Parcel;
import android.os.Parcelable;


import com.google.android.gms.maps.GoogleMap;

public class GoogleMapData implements Parcelable{

    static private GoogleMap googleMap;

    public GoogleMapData(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    protected GoogleMapData(Parcel in) {
    }

    public static final Creator<GoogleMapData> CREATOR = new Creator<GoogleMapData>() {
        @Override
        public GoogleMapData createFromParcel(Parcel in) {
            return new GoogleMapData(in);
        }

        @Override
        public GoogleMapData[] newArray(int size) {
            return new GoogleMapData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }
}
