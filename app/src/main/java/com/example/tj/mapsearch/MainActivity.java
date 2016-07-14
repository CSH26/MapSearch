package com.example.tj.mapsearch;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    // test
    private final String TAG = "MainActivity";
    private GoogleMap googleMap;
    private MapFragment mapFragment;
    private CompassView compassView;
    private RelativeLayout mainLayout;
    private SensorManager sensorManager;
    private boolean compassEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        // 맵프래그먼트 객체 참조

        mainLayout = (RelativeLayout)findViewById(R.id.mainLayout) ;
        //메인 레이아웃 참조
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        compassView = new CompassView(this);
        compassView.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        mainLayout.addView(compassView,params);
        compassEnabled = true;

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.4733326, 126.9400000))
                .title("Marker"));
        double homeX = 37.4733326;
        double homeY = 126.9400000;

        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(homeX,homeY),15));

        //MapClass mapClass = new MapClass(googleMap, getApplicationContext());
        //startLocationService();
    }

    private void startLocationService() {
        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        // 위치 관리자 객체 참조
        GPSListener gpsListener = new GPSListener();
        //위치 관리자가 알려주는 현재 위치는 위치리스너를 통해 받으므로 새로운 리스너(GPS리스너)를 구현하여 처리

        long minTime = 10000;   // 10초에 한번씩 알려주기
        float minDistance = 0;   // 값이 설정 되면 그 거리 만큼 이동 했을 때 위치정보 전달

        try{
            // 안드로이드가 제공하는 위치제공자를 크게 2가지로 분류(Network 제공자도 업데이트 해야 내위치 카메라와 마커 활성화)
            // 위치관리자에게 위치정보가 변경될 때마다 알려주기
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,minTime,minDistance,gpsListener);
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,minTime,minDistance,gpsListener);

            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            // 최근위치 정보 확인해야하기 때문에 위치 제공자 정보를 넘김(getLastKnownLocation)
            // Location 객체는 위도와 경도 값을 가진다.

            double homeX = 37.4733326;
            double homeY = 126.9400000;

            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(homeX,homeY),15));

            Log.d(TAG, "뜨나요");
            if (lastLocation != null){
                Double latitude = lastLocation.getLatitude();   // 위도
                Double longitude = lastLocation.getLongitude(); // 경도

            }

        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        private int orientation = -1;
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(orientation < 0){
                orientation = ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
            }
            // 센서의 방향 값이 변하면 값을 설정하고
            compassView.setDirection(event.values[0] + 90*orientation);
            compassView.invalidate();  // 다시 나침반이 그려지게 함
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

}
