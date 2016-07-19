package com.example.tj.mapsearch;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by TJ on 2016-07-19.
 */
public class AlertDialogClickListener implements DialogInterface.OnClickListener{

    GoogleMap googleMap;
    Context context;
    String addressName;
    double latitude;
    double longitude;

    public AlertDialogClickListener(Context context, GoogleMap googleMap) {
        this.context = context;
        this.googleMap = googleMap;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        switch (which){
            case -1: // 예
                addMakers();
                break;
            case -2:  // 아니오

                break;
        }
    }

    public void setPlaceInfomation(String addressName, double latitude, double longitude){
        this.addressName = addressName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void addMakers(){
        MarkerOptions maker = new MarkerOptions();
        // 아이콘을 이용해 원하는 위치를 포인트로 쉽게 표현하는 마커 설정

        maker.position(new LatLng(latitude, longitude));
        maker.title(" 장소명 : "+addressName);
        maker.draggable(true);
        Bitmap srcImg = BitmapFactory.decodeResource(context.getResources(),R.drawable.smallstar);

        maker.icon(BitmapDescriptorFactory.fromBitmap(srcImg));

        googleMap.addMarker(maker);
    }
}
