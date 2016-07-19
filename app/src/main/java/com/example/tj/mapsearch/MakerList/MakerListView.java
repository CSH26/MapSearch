package com.example.tj.mapsearch.MakerList;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tj.mapsearch.AddressList.AddressItem;
import com.example.tj.mapsearch.R;

public class MakerListView extends LinearLayout {

    private TextView addressName;
    private TextView addressLatitude;
    private TextView addressLongitude;

    public MakerListView(Context context, AddressItem aItem){
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.listitem,this,true);

        addressName = (TextView)findViewById(R.id.addressName);
        addressName.setText("# 주소명 : "+aItem.getAddressName());
        addressLatitude = (TextView)findViewById(R.id.addressLatitude);
        addressLatitude.setText(" 위도 : "+aItem.getLatitude());
        addressLongitude = (TextView)findViewById(R.id.addressLongitude);
        addressLongitude.setText(" 경도 : "+aItem.getLongitude());

    }

    public void setText(String data){
        addressName.setText("# 주소명 : "+data);
    }

    public void setText(Double latitudeOrLongitude, int index){
        if(index == 1){
            addressLatitude.setText(" 위도 : "+latitudeOrLongitude);
        }else {
            addressLongitude.setText(" 경도 : "+latitudeOrLongitude);
        }
    }


}
