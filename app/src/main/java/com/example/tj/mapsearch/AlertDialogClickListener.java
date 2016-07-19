package com.example.tj.mapsearch;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.example.tj.mapsearch.AddressList.AddressItem;
import com.example.tj.mapsearch.Database.DatabaseOpenHelper;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by TJ on 2016-07-19.
 */
public class AlertDialogClickListener implements DialogInterface.OnClickListener{
    private final static String TAG = "AlertDialogClickListener";
    GoogleMap googleMap;
    Context context;
    AddressItem addressItem;
    SQLiteDatabase sqLiteDatabase;
    DatabaseOpenHelper databaseOpenHelper;
    boolean isRecordInserted;

    public AlertDialogClickListener(Context context, GoogleMap googleMap, DatabaseOpenHelper databaseOpenHelper) {
        this.context = context;
        this.googleMap = googleMap;
        this.databaseOpenHelper = databaseOpenHelper;
        this.sqLiteDatabase = this.databaseOpenHelper.getSqLiteDatabase();
        isRecordInserted = false;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        switch (which){
            case -1: // 예
                addMakers();

                isRecordInserted = insertingRecords();

                if(isRecordInserted){
                    Toast.makeText(context,addressItem.getAddressName()+"이(가) 마커리스트에 성공적으로 추가되었습니다.",Toast.LENGTH_SHORT);
                    selectRecord();
                }else {
                    Toast.makeText(context,addressItem.getAddressName()+"이(가) 마커리스트에 등록되지 않았습니다.",Toast.LENGTH_SHORT);
                }
                break;
            case -2:  // 아니오

                break;
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
        maker.title(" 장소명 : "+addressItem.getAddressName());
        maker.draggable(true);
        Bitmap srcImg = BitmapFactory.decodeResource(context.getResources(),R.drawable.smallstar);

        maker.icon(BitmapDescriptorFactory.fromBitmap(srcImg));

        googleMap.addMarker(maker);
    }

    public boolean insertingRecords(){
        Log.d(TAG,"테이블 네임은 : "+databaseOpenHelper.getTableName());
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

    public void selectRecord(){
        String SELECT_RECORD_SQL = "select * from "+databaseOpenHelper.getTableName()+";";
        Cursor cursor = sqLiteDatabase.rawQuery(SELECT_RECORD_SQL,null);

        if(cursor != null){
            Log.d(TAG, "count : "+cursor.getCount());
            cursor.moveToFirst();
            Log.d(TAG, "_id : "+cursor.getString(0));
            Log.d(TAG, "address_name : "+cursor.getString(1));
            Log.d(TAG, "latitude : "+cursor.getString(2));
            Log.d(TAG, "longitude : "+cursor.getString(3));

            while (cursor.moveToNext()){
                Log.d(TAG, "_id : "+cursor.getString(0));
                Log.d(TAG, "address_name : "+cursor.getString(1));
                Log.d(TAG, "latitude : "+cursor.getString(2));
                Log.d(TAG, "longitude : "+cursor.getString(3));
            }
        }else {
            Log.d(TAG," 조회에 실패 하였습니다. ");
        }

    }
}
