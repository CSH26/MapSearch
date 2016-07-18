package com.example.tj.mapsearch;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by TJ on 2016-07-14.
 */
public class GPSListener implements LocationListener {

    private final String TAG = "GPSListener";
    private int ACCESS_COUNT = 0;
    MapClass mapClass;

    public GPSListener(MapClass mapClass) {
        this.mapClass = mapClass;
    }

    // 위치정보가 전달될때 자동 호출되는 메소드
    @Override
    public void onLocationChanged(Location location) {
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();

        String msg = "Last location : "+"Latitude "+latitude+"\n Longitude "+longitude;
        Log.d(TAG, msg);
        showCurrentLocation(latitude,longitude);
    }

    private void showCurrentLocation(Double latitude, Double longitude){

        LatLng curPoint = new LatLng(latitude,longitude); // 현재 위치로 객체 생성

        if(ACCESS_COUNT == 0){
            this.mapClass.typeAndCameraSetting(curPoint);
            ACCESS_COUNT = 1;
        }


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}