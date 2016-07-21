package com.example.tj.mapsearch;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.tj.mapsearch.AddressList.AddressItem;
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
    DialogView dialogView;
    Context context;

    public GoogleMapLongClickListener(Context context, AlertDialogClickListener alertDialogClickListener, AlertDialog.Builder aBuilder, DialogView dialogView) {
        this.alertDialogClickListener = alertDialogClickListener;
        this.aBuilder = aBuilder;
        this.dialogView = dialogView;
        this.context = context;
        this.alertDialogClickListener.setALERTDIALOG_REQUEST_CODE(ALERTDIALOG_REQUEST_CODE);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        alertDialogClickListener.setLat(latLng.latitude);
        alertDialogClickListener.setLongi(latLng.longitude);
        createAlertDialog();
        aBuilder.show();
    }

    public void createAlertDialog(){
        aBuilder.setTitle("Maker Insert");
        aBuilder.setIcon(R.drawable.smallstar);
        aBuilder.setView(dialogView.getDialogView());
        aBuilder.setPositiveButton("예", alertDialogClickListener);
        aBuilder.setNegativeButton("아니오", alertDialogClickListener);
    }


}
