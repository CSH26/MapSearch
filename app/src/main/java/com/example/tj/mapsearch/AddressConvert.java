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

    private final static int ADDRESS_CONVERT_ACTIVITY_SHOW_MAP_ANIMATECAMERA_BUTTON = 200;
    private int previousPosition = -1;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_convert);

        address = (TextView)findViewById(R.id.address);
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
                    }
                }else {
                    slidingLayout.setVisibility(View.VISIBLE);
                    slidingLayout.startAnimation(showAnim);
                    setPreviousPosition(position);
                }
            }
        });

        mapAnimateCamera = (Button)findViewById(R.id.mapAnimateCamera);
        mapAnimateCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("LATITUDE",outAddr.getLatitude());
                resultIntent.putExtra("LONGITUDE",outAddr.getLongitude());

                setResult(RESULT_OK,resultIntent);
                finish();
            }
        });

        gc = new Geocoder(this, Locale.KOREA);
        Bundle extras = getIntent().getExtras();
        if(extras == null)
            Toast.makeText(getApplicationContext(),"없는 주소이거나 주소명을 잘못 입력하셨습니다.",Toast.LENGTH_LONG).show();

        String search = extras.getString("ADDRESS").toString();
        countView = (TextView)header.findViewById(R.id.count);
        address.setText("["+search+"]검색 결과");
        countView.setText("주소의 수: "+addressListAdapter.getCount());
        searchLocation(search);
    }

    private void searchLocation(String searchStr){
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
                    addressListAdapter.addItem(new AddressItem(addressName,outAddr.getLatitude(),outAddr.getLongitude()));

                }
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
}
