package com.example.tj.mapsearch;

import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tj.mapsearch.AddressList.AddressItem;
import com.example.tj.mapsearch.Database.DatabaseOpenHelper;
import com.example.tj.mapsearch.MakerList.AddMakerDialogView;
import com.example.tj.mapsearch.MakerList.MakerListAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by TJ on 2016-07-19.
 */
public class AlertDialogClickListener implements DialogInterface.OnClickListener{

    private final static String TAG = "AlertDialogClickListener";
    private int ALERTDIALOG_REQUEST_CODE;
    private int ALERTDIALOG_REQUEST_CODE_ADDRESSCONVERT_ACTIVITY = 1;
    private int ALERTDIALOG_REQUEST_CODE_MAKERLIST_ACTIVITY = 2;
    private int ALERTDIALOG_REQUEST_CODE_ON_MAP_LONGCLICK_MAIN_ACTIVITY = 3;

    GoogleMap googleMap;
    Context context;
    AddressItem addressItem;
    SQLiteDatabase sqLiteDatabase;
    DatabaseOpenHelper databaseOpenHelper;
    boolean isRecordInserted;
    DialogView dialogView;
    AddMakerDialogView addMakerDialogView;
    MakerListAdapter makerListAdapter;
    Geocoder gc;
    Address outAddr;
    double lat, longi;
    int addrCount;
    StringBuffer outAddrStr;
    String tempAddressName;
    boolean notFoundAddress;

    public AlertDialogClickListener(Context context, GoogleMap googleMap, DatabaseOpenHelper databaseOpenHelper, DialogView dialogView, int ALERTDIALOG_REQUEST_CODE) {
        this.context = context;
        this.googleMap = googleMap;
        this.databaseOpenHelper = databaseOpenHelper;
        this.sqLiteDatabase = this.databaseOpenHelper.getSqLiteDatabase();
        isRecordInserted = false;
        this.dialogView = dialogView;
        this.ALERTDIALOG_REQUEST_CODE = ALERTDIALOG_REQUEST_CODE;
    }

    public AlertDialogClickListener(Context context, AddMakerDialogView addMakerDialogView, int ALERTDIALOG_REQUEST_CODE,  MakerListAdapter makerListAdapter, DatabaseOpenHelper databaseOpenHelper) {
        this.context = context;
        isRecordInserted = false;
        this.addMakerDialogView = addMakerDialogView;
        this.ALERTDIALOG_REQUEST_CODE = ALERTDIALOG_REQUEST_CODE;
        this.makerListAdapter = makerListAdapter;
        gc = new Geocoder(context, Locale.KOREA);
        this.databaseOpenHelper = databaseOpenHelper;
        this.sqLiteDatabase = this.databaseOpenHelper.getSqLiteDatabase();
        this.googleMap = makerListAdapter.getGoogleMap();
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {

        switch (which){
            case -1: // 예
                if(ALERTDIALOG_REQUEST_CODE == ALERTDIALOG_REQUEST_CODE_ADDRESSCONVERT_ACTIVITY){
                    addMakers();
                    isRecordInserted = insertingRecords();
                    flagCheckShowToast();
                    break;
                }else if(ALERTDIALOG_REQUEST_CODE == ALERTDIALOG_REQUEST_CODE_MAKERLIST_ACTIVITY){
                    findLatlng(addMakerDialogView.getAddMakerDialogViewText());
                    if(notFoundAddress) {
                        if (!addMakerDialogView.getUpdateMakerNameText().toString().equals("")) {
                            setPlaceInfomation(addMakerDialogView.getUpdateMakerNameText(), getLat(), getLongi());
                        } else if (addMakerDialogView.getUpdateMakerNameText().toString().equals("")) {
                            setPlaceInfomation(addMakerDialogView.getAddMakerDialogViewText(), getLat(), getLongi());
                        }
                        addMakers();
                        isRecordInserted = insertingRecords();
                        if(isRecordInserted){
                            makerListAdapter.addItem(new AddressItem(addressItem.getAddressName(), addressItem.getLatitude(), addressItem.getLongitude()));
                            makerListAdapter.notifyDataSetChanged();
                        }
                        flagCheckShowToast();
                    }else {
                        Toast.makeText(context,addressItem.getAddressName()+"로 검색된 주소가 없습니다. ",Toast.LENGTH_SHORT);
                        return;
                    }
                    break;
                }else if(ALERTDIALOG_REQUEST_CODE == ALERTDIALOG_REQUEST_CODE_ON_MAP_LONGCLICK_MAIN_ACTIVITY){
                    gc = new Geocoder(context, Locale.KOREA);
                    searchLocation();
                    if(notFoundAddress) {
                        setPlaceInfomation(getTempAddressName(),getLat(),getLongi());
                        addMakers(getTempAddressName(),getLat(),getLongi());
                        isRecordInserted = insertingRecords();
                        flagCheckShowToast();
                    }else {
                        Toast.makeText(context,addressItem.getAddressName()+"로 검색된 주소가 없습니다. ",Toast.LENGTH_SHORT);
                        return;
                    }
                    break;

                }
            case -2:  // 아니오
                break;
        }

        if((ALERTDIALOG_REQUEST_CODE == ALERTDIALOG_REQUEST_CODE_ADDRESSCONVERT_ACTIVITY) ||
                (ALERTDIALOG_REQUEST_CODE == ALERTDIALOG_REQUEST_CODE_ON_MAP_LONGCLICK_MAIN_ACTIVITY)){
            if (dialogView != null)
            {
                ViewGroup parent = (ViewGroup) dialogView.getParent();
                if (parent != null)
                {
                    parent.removeView(dialogView);
                }
            }
        }else if(ALERTDIALOG_REQUEST_CODE == ALERTDIALOG_REQUEST_CODE_MAKERLIST_ACTIVITY){
            if (addMakerDialogView != null) // 중복창 띄우기
            {
                ViewGroup parent = (ViewGroup) addMakerDialogView.getParent();
                if (parent != null) {
                    parent.removeView(addMakerDialogView);
                }
            }
        }
    }

    public void setPlaceInfomation(String addressName, double latitude, double longitude){
        addressItem = new AddressItem(addressName,latitude,longitude);
        //adressBean = new AddressBean(addressName,latitude,longitude);
    }

    public void addMakers(){
        MarkerOptions maker = new MarkerOptions();
        // 아이콘을 이용해 원하는 위치를 포인트로 쉽게 표현하는 마커 설정

        maker.position(new LatLng(addressItem.getLatitude(), addressItem.getLongitude()));
        maker.title(" 주소명 : "+addressItem.getAddressName());
        maker.draggable(true);
        Bitmap srcImg = BitmapFactory.decodeResource(context.getResources(),R.drawable.smallstar);

        maker.icon(BitmapDescriptorFactory.fromBitmap(srcImg));

        this.googleMap.addMarker(maker);
    }

    public void addMakers(String addressName, double latitude, double longitude){
        MarkerOptions maker = new MarkerOptions();
        // 아이콘을 이용해 원하는 위치를 포인트로 쉽게 표현하는 마커 설정

        maker.position(new LatLng(latitude, longitude));
        maker.title(" 주소명 : "+addressName);
        maker.draggable(true);
        Bitmap srcImg = BitmapFactory.decodeResource(context.getResources(),R.drawable.smallstar);

        maker.icon(BitmapDescriptorFactory.fromBitmap(srcImg));

        googleMap.addMarker(maker);
    }

    public boolean insertingRecords(){
        Log.d(TAG," 주소명 "+addressItem.getAddressName());
        Log.d(TAG," 위도 "+Double.toString(addressItem.getLatitude()));
        Log.d(TAG," 경도 "+Double.toString(addressItem.getLongitude()));

        String INSERT_RECORD_SQL = "insert into "+databaseOpenHelper.getTableName()+"("
                +"address_name, latitude, longitude) values ("
                +"\""+addressItem.getAddressName()+"\", "
                +"\""+Double.toString(addressItem.getLatitude())+"\", "
                +"\""+Double.toString(addressItem.getLongitude())+"\");";

        try {
            sqLiteDatabase.execSQL(INSERT_RECORD_SQL);
            return true;
        }catch (Exception e){
            Log.d(TAG,"EXCEPTION IN INSERT_RECORD_SQL.",e);
            return false;
        }
    }

    public void findLatlng(String searchStr){
        List<Address> addressList = null;

        try{
            addressList = gc.getFromLocationName(searchStr,1);  // 매치되는 주소 최대 1개만 가져오기
            if(addressList != null)
            {
                for(int i = 0; i<addressList.size(); i++){
                    tempAddressName = "";
                    outAddr = addressList.get(i);
                    addrCount = outAddr.getMaxAddressLineIndex()+1;
                    outAddrStr = new StringBuffer();
                    for(int k =0;k<addrCount;k++){
                        outAddrStr.append(outAddr.getAddressLine(k));
                        tempAddressName = outAddrStr.toString();
                    }
                    setLat(outAddr.getLatitude());
                    setLongi(longi = outAddr.getLongitude());
                }
                notFoundAddress = true;
            }else{
                notFoundAddress = false;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void searchLocation(){
        List<Address> addressList = null;

        try{
            addressList = gc.getFromLocation(getLat(),getLongi(),1);  // 매치되는 주소 최대 10개만 가져오기

            if(addressList != null)
            {
                for(int i = 0; i<addressList.size(); i++){
                    String tempAddressName = "";
                    outAddr = addressList.get(i);
                    addrCount = outAddr.getMaxAddressLineIndex()+1;
                    outAddrStr = new StringBuffer();
                    for(int k =0;k<addrCount;k++) {
                        outAddrStr.append(outAddr.getAddressLine(k));
                        setTempAddressName(outAddrStr.toString());
                    }
                }
                notFoundAddress = true;
            }else{
                notFoundAddress = false;
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLongi() {
        return longi;
    }

    public void setLongi(double longi) {
        this.longi = longi;
    }

    public int getALERTDIALOG_REQUEST_CODE() {
        return ALERTDIALOG_REQUEST_CODE;
    }

    public void setALERTDIALOG_REQUEST_CODE(int ALERTDIALOG_REQUEST_CODE) {
        this.ALERTDIALOG_REQUEST_CODE = ALERTDIALOG_REQUEST_CODE;
    }

    public String getTempAddressName() {
        return tempAddressName;
    }

    public void setTempAddressName(String tempAddressName) {
        this.tempAddressName = tempAddressName;
    }

    public void flagCheckShowToast(){
        if(isRecordInserted){
            Toast.makeText(context,addressItem.getAddressName()+"이(가) 마커리스트에 성공적으로 추가되었습니다.",Toast.LENGTH_SHORT);
            isRecordInserted = false;
        }else {
            Toast.makeText(context,addressItem.getAddressName()+"이(가) 마커리스트에 등록되지 않았습니다.",Toast.LENGTH_SHORT);
        }
    }
}
