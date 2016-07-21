package com.example.tj.mapsearch;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.tj.mapsearch.Database.DatabaseOpenHelper;
import com.example.tj.mapsearch.MakerList.MakerListActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,View.OnClickListener {

    private final static int MAINACTIVITY_ALERTDIALOG_REQUEST_CODE = 1;
    private final static int MAINACTIVITY_SHOW_SEARCH_BUTTON = 100;
    private final static int REQUEST_CODE_ADDRESS_CONVERT_ACTIVITY = 2000;
    private final static int REQUEST_CODE_MAKER_LIST_ACTIVITY = 3000;
    private final static String TAG = "MainActivity";
    private final static String DATABASE_NAME = "maker.db";
    private final static String TABLE_NAME = "makerlist";
    private final static int DATABASE_VERSION = 1;
    private GoogleMap googleMap;
    private MapFragment mapFragment;
    private CompassView compassView;
    private RelativeLayout mainLayout;
    private SensorManager sensorManager;
    private boolean compassEnabled;
    SlidingPageAnimationListener slidingPageAnimationListener;
    LinearLayout sliding;
    Animation translateBottomAnim, translateTopAnim;
    Button spreadButton, addressSearch;
    EditText addressBox;
    CheckBox normalBox, satelliteBox;
    AlertDialogClickListener alertDialogClickListener;
    AlertDialog.Builder aBuilder;
    DialogView dialogView;
    DatabaseOpenHelper databaseOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseOpenHelper = new DatabaseOpenHelper(getApplicationContext(),DATABASE_NAME,MODE_WORLD_WRITEABLE,DATABASE_VERSION);
        databaseOpenHelper.createTable();
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dialogView = new DialogView(this);
        aBuilder = new AlertDialog.Builder(MainActivity.this); // save 작동시에 띄워줄 dialog창

        normalBox = (CheckBox)findViewById(R.id.normal);
        satelliteBox = (CheckBox)findViewById(R.id.satellite);
        addressBox = (EditText) findViewById(R.id.addressBox);
        spreadButton = (Button)findViewById(R.id.spread);
        addressSearch = (Button)findViewById(R.id.addressSearch);
        sliding = (LinearLayout)findViewById(R.id.sliding);
        slidingPageAnimationListener = new SlidingPageAnimationListener(sliding,MAINACTIVITY_SHOW_SEARCH_BUTTON);
        translateBottomAnim = AnimationUtils.loadAnimation(this,R.anim.translate_bottom);
        translateBottomAnim.setFillAfter(true);
        translateTopAnim = AnimationUtils.loadAnimation(this,R.anim.translate_top);
        translateBottomAnim.setAnimationListener(slidingPageAnimationListener);
        translateTopAnim.setAnimationListener(slidingPageAnimationListener);
        mainLayout = (RelativeLayout)findViewById(R.id.mainLayout) ;
        //메인 레이아웃 참조
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        compassView = new CompassView(this);
        compassView.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        mainLayout.addView(compassView,params);
        compassEnabled = true;

        registerClickListener();

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerClickListener(){
        spreadButton.setOnClickListener(this);
        addressSearch.setOnClickListener(this);
        normalBox.setOnClickListener(this);
        satelliteBox.setOnClickListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        try {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG,"퍼미션이 허용되지않았으므로 내 위치를 표시할 수 없습니다.");
                return;
            }
            else {
                Log.d(TAG,"퍼미션이 허용. 내 위치를 표시합니다.");
                this.googleMap.setMyLocationEnabled(true);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        alertDialogClickListener = new AlertDialogClickListener(getApplicationContext(),googleMap,databaseOpenHelper, dialogView, MAINACTIVITY_ALERTDIALOG_REQUEST_CODE);
        GoogleMapLongClickListener googleMapLongClickListener = new GoogleMapLongClickListener(getApplicationContext(), alertDialogClickListener, aBuilder, dialogView);
        this.googleMap.setOnMapLongClickListener(googleMapLongClickListener);
        MapClass mapClass = new MapClass(googleMap, getApplicationContext());
        startLocationService(mapClass);
        checkDangerousPermissions();
        drawMaker();
    }

    private void startLocationService(MapClass mapClass) {
        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        // 위치 관리자 객체 참조
        GPSListener gpsListener = new GPSListener(mapClass);
        //위치 관리자가 알려주는 현재 위치는 위치리스너를 통해 받으므로 새로운 리스너(GPS리스너)를 구현하여 처리

        long minTime = 0;   // 10초에 한번씩 알려주기
        float minDistance = 0;   // 값이 설정 되면 그 거리 만큼 이동 했을 때 위치정보 전달

        try{
            // 안드로이드가 제공하는 위치제공자를 크게 2가지로 분류(Network 제공자도 업데이트 해야 내위치 카메라와 마커 활성화)
            // 위치관리자에게 위치정보가 변경될 때마다 알려주기
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,minTime,minDistance,gpsListener);
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,minTime,minDistance,gpsListener);

            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            // 최근위치 정보 확인해야하기 때문에 위치 제공자 정보를 넘김(getLastKnownLocation)
            // Location 객체는 위도와 경도 값을 가진다.

            if (lastLocation != null){
                Double latitude = lastLocation.getLatitude();   // 위도
                Double longitude = lastLocation.getLongitude(); // 경도
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),15));
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

    protected void onResume() {
        super.onResume();
        // 액티비티가 나타나면 센서매니저에 리스너 등록
        if(googleMap != null){
            Log.d(TAG," onResume called! ");
            drawMaker();
        }
        if (compassEnabled){
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                    SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        //리스너 해제
        if(compassEnabled){
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    private void checkDangerousPermissions(){

        String [] permissions = {
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;

        for(int i=0;i<permissions.length;i++){
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if(permissionCheck == PackageManager.PERMISSION_DENIED){
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Map Service Activated!! \n 잠시 후 현재위치로 이동 합니다. ",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this,"Map Service Fail!!",Toast.LENGTH_SHORT).show();
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])){
                Toast.makeText(this,"불가",Toast.LENGTH_SHORT).show();
            }else
                ActivityCompat.requestPermissions(this, permissions,1);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            for(int i=0;i<permissions.length; i++){
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, permissions[i]+"승인 ",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, permissions[i]+"불허 ",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.spread:
                if(slidingPageAnimationListener.isPageOpen()){
                    sliding.startAnimation(translateTopAnim);
                    spreadButton.setText("주소 탐색");
                }else
                {
                    sliding.setVisibility(View.VISIBLE);
                    sliding.startAnimation(translateBottomAnim);
                    addressBox.setText("");
                    spreadButton.setText("접기");
                }
                break;
            case R.id.addressSearch:
                Intent addressConvertIntent = new Intent(this,AddressConvert.class);
                addressConvertIntent.putExtra("ADDRESS",addressBox.getText().toString());
                startActivityForResult(addressConvertIntent,REQUEST_CODE_ADDRESS_CONVERT_ACTIVITY);
                break;
            case R.id.normal:
                if(normalBox.isChecked()){
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    satelliteBox.setChecked(false);
                }else {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    normalBox.setChecked(false);
                    satelliteBox.setChecked(true);
                }
                break;
            case R.id.satellite:
                if(satelliteBox.isChecked()){
                    googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    normalBox.setChecked(false);
                }
                else {
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    satelliteBox.setChecked(false);
                    normalBox.setChecked(true);
                }
                break;
        }
    }

    public void drawMaker(){
        googleMap.clear();
        SQLiteDatabase sqLiteDatabase = databaseOpenHelper.getSqLiteDatabase();

        String SELECT_RECORD_SQL = "select * from "+TABLE_NAME+";";
        Cursor cursor = sqLiteDatabase.rawQuery(SELECT_RECORD_SQL,null);

        Log.d(TAG," 마커 개수 : "+cursor.getCount());
        if(cursor.getCount() == 0){

        }else {
            cursor.moveToFirst();
            alertDialogClickListener.addMakers(cursor.getString(1),Double.parseDouble(cursor.getString(2)),Double.parseDouble(cursor.getString(3)));
            while (cursor.moveToNext()){
                alertDialogClickListener.addMakers(cursor.getString(1),Double.parseDouble(cursor.getString(2)),Double.parseDouble(cursor.getString(3)));
            }
        }

        cursor.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_ADDRESS_CONVERT_ACTIVITY){
            if(resultCode == RESULT_OK){
                String addressName = data.getExtras().getString("ADDRESS_NAME");
                double latitude = data.getExtras().getDouble("LATITUDE");
                double longitude = data.getExtras().getDouble("LONGITUDE");

                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),15));
                alertDialogClickListener.setPlaceInfomation(addressName,latitude,longitude);
                alertDialogClickListener.setALERTDIALOG_REQUEST_CODE(MAINACTIVITY_ALERTDIALOG_REQUEST_CODE);
                createAlertDialog();
                aBuilder.show();
            }
        }else if(requestCode == REQUEST_CODE_MAKER_LIST_ACTIVITY){
            if(resultCode == RESULT_OK){
                double latitude = data.getExtras().getDouble("LATITUDE");
                double longitude = data.getExtras().getDouble("LONGITUDE");
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),15));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.makerList:
                Intent makerListActivitytIntent = new Intent(this,MakerListActivity.class);
                GoogleMapData googleMapData = new GoogleMapData(this.googleMap);

                makerListActivitytIntent.putExtra("GOOGLE_MAP",googleMapData);
                startActivityForResult(makerListActivitytIntent,REQUEST_CODE_MAKER_LIST_ACTIVITY);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createAlertDialog(){
        aBuilder.setTitle("Maker Insert");
        aBuilder.setIcon(R.drawable.smallstar);
        aBuilder.setView(dialogView.getDialogView());
        aBuilder.setPositiveButton("예", alertDialogClickListener);
        aBuilder.setNegativeButton("아니오", alertDialogClickListener);
    }

}
