package com.example.tj.mapsearch;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.tj.mapsearch.AddressList.AddressItem;
import com.example.tj.mapsearch.Database.DatabaseOpenHelper;
import com.example.tj.mapsearch.MakerList.AddMakerDialogView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by TJ on 2016-07-21.
 */
public class GoogleMapLongClickListener implements GoogleMap.OnMapLongClickListener {

    private int ALERTDIALOG_REQUEST_CODE = 3;
    AlertDialogClickListener alertDialogClickListener;
    AlertDialog.Builder aBuilder;
    AddMakerLongClickDialogView addMakerLongClickDialogView;
    DialogView dialogView;
    Context context;

    public GoogleMapLongClickListener(Context context, AlertDialogClickListener alertDialogClickListener, AlertDialog.Builder aBuilder, DialogView dialogView) {
        this.alertDialogClickListener = alertDialogClickListener;
        this.aBuilder = aBuilder;
        this.dialogView = dialogView;
        this.context = context;
        this.alertDialogClickListener.setALERTDIALOG_REQUEST_CODE(ALERTDIALOG_REQUEST_CODE);
    }

    public GoogleMapLongClickListener(Context context, AlertDialog.Builder aBuilder, GoogleMap googleMap, DatabaseOpenHelper databaseOpenHelper) {
        this.aBuilder = aBuilder;
        this.context = context;
        this.addMakerLongClickDialogView = new AddMakerLongClickDialogView(this.context);
        this.alertDialogClickListener = new AlertDialogClickListener(context, googleMap, this.addMakerLongClickDialogView, ALERTDIALOG_REQUEST_CODE, databaseOpenHelper);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        this.alertDialogClickListener.setALERTDIALOG_REQUEST_CODE(ALERTDIALOG_REQUEST_CODE);
        alertDialogClickListener.setLat(latLng.latitude);
        alertDialogClickListener.setLongi(latLng.longitude);
        createUpdateAddressNameAlertDialog();
        aBuilder.show();
    }

    public void createAlertDialog(){
        aBuilder.setTitle("Maker Insert");
        aBuilder.setIcon(R.drawable.smallstar);
        aBuilder.setView(dialogView.getDialogView());
        aBuilder.setPositiveButton("예", alertDialogClickListener);
        aBuilder.setNegativeButton("아니오", alertDialogClickListener);
    }

    public void createUpdateAddressNameAlertDialog(){
        aBuilder.setTitle("Maker Insert");
        aBuilder.setIcon(R.drawable.smallstar);
        aBuilder.setView(addMakerLongClickDialogView.getDialogView());
        aBuilder.setMessage("해당 지점에 마커를 등록하겠습니다. 변경하실 주소명을 입력하세요.");
        aBuilder.setPositiveButton("확인", alertDialogClickListener);
        aBuilder.setNegativeButton("취소", alertDialogClickListener);
    }


}
