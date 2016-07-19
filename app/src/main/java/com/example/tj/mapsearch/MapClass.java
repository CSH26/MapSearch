package com.example.tj.mapsearch;

import android.content.Context;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by TJ on 2016-07-14.
 */
public class MapClass {

    Context context;
    GoogleMap googleMap;

    public MapClass() {
    }

    public MapClass(Context context) {
        this.context = context;
    }

    public MapClass(GoogleMap googleMap, Context context) {
        this.context = context;
        this.googleMap = googleMap;
    }

    public void typeAndCameraSetting(LatLng latLng) {
        int scale = 15; // 축척 값
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, scale));
        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);  // 체크박스가 선택되지 않으면 디폴트로 노말타입
        // 현재 위치를 지도의 중심으로 표시 LatLng객체와 축척 값을 넘겨줌
        // 축척 값이 클수록 가깝게 보인다.
        // 맵 타입을 SATELLITE으로 하면 위성영상, TERRAIN은 지형도로 보여준다.

    }

}