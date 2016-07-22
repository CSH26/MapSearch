package com.example.tj.mapsearch;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tj.mapsearch.AddressList.AddressItem;
import com.example.tj.mapsearch.AddressList.AddressListAdapter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddressConvert extends AppCompatActivity {

    private final String TAG = "AddressConvert";
    private final static int ADDRESS_CONVERT_ACTIVITY_SHOW_MAP_ANIMATECAMERA_BUTTON = 200;
    private int previousPosition = -1;
    private int forwordPosition = -1;
    Geocoder gc;
    TextView countView, address;
    Address outAddr;
    int addrCount;
    StringBuffer outAddrStr;
    Button mapAnimateCamera;
    ListView addressList;
    Animation showAnim, behindAnim;
    LinearLayout slidingLayout;
    AddressListAdapter addressListAdapter;
    SlidingPageAnimationListener animationListener;
    double lat, longi;
    boolean notFoundAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_convert);

        slidingLayout = (LinearLayout)findViewById(R.id.slidingLayout);
        showAnim = AnimationUtils.loadAnimation(this,R.anim.translate_selected_list);
        behindAnim = AnimationUtils.loadAnimation(this,R.anim.translate_nonselected_list);
        behindAnim.setFillAfter(true);
        animationListener = new SlidingPageAnimationListener(slidingLayout,ADDRESS_CONVERT_ACTIVITY_SHOW_MAP_ANIMATECAMERA_BUTTON);
        showAnim.setAnimationListener(animationListener);
        behindAnim.setAnimationListener(animationListener);
        addressList = (ListView)findViewById(R.id.addressList);

        final View header = getLayoutInflater().inflate(R.layout.listview_header,null,false);

        addressListAdapter = new AddressListAdapter(this);
        addressList.setAdapter(addressListAdapter);
        addressList.addHeaderView(header);
        addressList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position == getPreviousPosition()){
                    if(animationListener.isPageOpen()){
                        slidingLayout.startAnimation(behindAnim);
                        setPreviousPosition(position);
                    }
                    else{
                        slidingLayout.setVisibility(View.VISIBLE);
                        slidingLayout.startAnimation(showAnim);
                        setPreviousPosition(position);
                        setForwordPosition(position);
                    }
                }else {
                    slidingLayout.setVisibility(View.VISIBLE);
                    slidingLayout.startAnimation(showAnim);
                    setPreviousPosition(position);
                    setForwordPosition(position);
                }
            }
        });

        mapAnimateCamera = (Button)findViewById(R.id.mapAnimateCamera);
        mapAnimateCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("LATITUDE",addressListAdapter.getItem(0).getLatitude());
                resultIntent.putExtra("LONGITUDE",addressListAdapter.getItem(0).getLongitude());
                resultIntent.putExtra("ADDRESS_NAME",addressListAdapter.getItem(0).getAddressName());
                setResult(RESULT_OK,resultIntent);
                finish();
            }
        });

        gc = new Geocoder(this, Locale.KOREA);
        Intent addressConvertIntent = getIntent();
        String search = addressConvertIntent.getStringExtra("ADDRESS").toString();
        countView = (TextView)header.findViewById(R.id.count);
        address = (TextView)header.findViewById(R.id.address);

        findLatlng(search);
        searchLocation();
        address.setText("["+search+"]검색 결과");
        countView.setText("결과 ["+addressListAdapter.getCount()+"]");
        if(notFoundAddress){
            Toast.makeText(getApplicationContext(),search+"로 검색된 결과 입니다. ",Toast.LENGTH_SHORT);
        }else {
            Toast.makeText(getApplicationContext(),search+"로 검색된 주소가 없습니다. ",Toast.LENGTH_SHORT);
        }
    }

    public void findLatlng(String searchStr){
        List<Address> addressList = null;

        try{
            addressList = gc.getFromLocationName(searchStr,1);  // 매치되는 주소 최대 1개만 가져오기
            if(addressList != null)
            {
                for(int i = 0; i<addressList.size(); i++){
                    String addressName = "";
                    outAddr = addressList.get(i);
                    addrCount = outAddr.getMaxAddressLineIndex()+1;
                    outAddrStr = new StringBuffer();
                    for(int k =0;k<addrCount;k++){
                        outAddrStr.append(outAddr.getAddressLine(k));
                        addressName = outAddrStr.toString();
                    }
                    lat = outAddr.getLatitude();
                    longi = outAddr.getLongitude();
                }notFoundAddress = true;
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
            addressList = gc.getFromLocation(lat,longi,20);  // 매치되는 주소 최대 20개만 가져오기
            if(addressList != null)
            {
                for(int i = 0; i<addressList.size(); i++){
                    String addressName = "";
                    outAddr = addressList.get(i);
                    addrCount = outAddr.getMaxAddressLineIndex()+1;
                    outAddrStr = new StringBuffer();
                    for(int k =0;k<addrCount;k++){
                        outAddrStr.append(outAddr.getAddressLine(k));
                        addressName = outAddrStr.toString();
                    }
                    addressListAdapter.addItem(new AddressItem(addressName,outAddr.getLatitude(),outAddr.getLongitude()));
                }notFoundAddress = true;
            }else{
                notFoundAddress = false;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public int getPreviousPosition() {
        return previousPosition;
    }

    public void setPreviousPosition(int previousPosition) {
        this.previousPosition = previousPosition;
    }

    public int getForwordPosition() {
        return forwordPosition;
    }

    public void setForwordPosition(int forwordPosition) {
        this.forwordPosition = forwordPosition;
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
}
