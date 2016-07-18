package com.example.tj.mapsearch;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddressConvert extends AppCompatActivity {
    Geocoder gc;
    TextView contentsText;
    Address outAddr;
    int addrCount;
    StringBuffer outAddrStr;
    Button mapAnimate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_convert);
        Toast.makeText(getApplicationContext(),"Ok",Toast.LENGTH_SHORT).show();
        contentsText = (TextView)findViewById(R.id.textView);
        mapAnimate = (Button)findViewById(R.id.mapAnimate);
        mapAnimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("latitude",outAddr.getLatitude());
                resultIntent.putExtra("longitude",outAddr.getLongitude());

                setResult(RESULT_OK,resultIntent);
                finish();
            }
        });

        gc = new Geocoder(this, Locale.KOREA);
        Bundle extras = getIntent().getExtras();
        if(extras == null)
            Toast.makeText(getApplicationContext(),"주소 입력 안함",Toast.LENGTH_LONG).show();

        String search = extras.getString("address");
        searchLocation(search);
    }

    private void searchLocation(String searchStr){
        List<Address> addressList = null;

        try{
            addressList = gc.getFromLocationName(searchStr,1);  // 매치되는 주소 최대 1개만 가져오기
            if(addressList != null)
            {
                contentsText.append("\n["+searchStr+"] 주소의 수: "+addressList.size()+"\n");
                for(int i = 0; i<addressList.size(); i++){
                    outAddr = addressList.get(i);
                    addrCount = outAddr.getMaxAddressLineIndex()+1;
                    outAddrStr = new StringBuffer();
                    for(int k =0;k<addrCount;k++){
                        outAddrStr.append(outAddr.getAddressLine(k));
                    }
                    outAddrStr.append("\n 위도 : "+outAddr.getLatitude());
                    outAddrStr.append("\n 경도 : "+outAddr.getLongitude()+"\n");

                    contentsText.append( "# "+(i+1)+" 번째 : "+outAddrStr.toString() );
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
