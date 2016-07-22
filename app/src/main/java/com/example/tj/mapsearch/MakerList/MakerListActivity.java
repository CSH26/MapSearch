package com.example.tj.mapsearch.MakerList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.example.tj.mapsearch.AlertDialogClickListener;
import com.example.tj.mapsearch.Database.DatabaseOpenHelper;
import com.example.tj.mapsearch.GoogleMapData;
import com.example.tj.mapsearch.R;
import com.example.tj.mapsearch.SlidingPageAnimationListener;

public class MakerListActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = "MakerListActivity";
    private final static int MAINACTIVITY_ALERTDIALOG_REQUEST_CODE = 2;
    private final static int MAKER_LIST_ACTIVITY = 300;
    private final static String TABLE_NAME = "makerlist";
    private final static String DATABASE_NAME = "maker.db";
    private final static int DATABASE_VERSION = 1;
    private Context context;
    private int previousPosition = -1;
    private int forwordPosition = -1;
    TextView makerCount;
    Button deleteMaker, moveMaker;
    ListView makerListView;
    Animation showAnim, behindAnim;
    LinearLayout slidingLayout;
    SlidingPageAnimationListener animationListener;
    MakerListAdapter makerListAdapter;
    SQLiteDatabase sqLiteDatabase;
    DatabaseOpenHelper databaseOpenHelper;
    AlertDialogClickListener alertDialogClickListener;
    AlertDialog.Builder aBuilder;
    AddMakerDialogView addMakerDialogView;
    GoogleMapData googleMapData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maker_list);

        context = getApplicationContext();
        databaseOpenHelper = new DatabaseOpenHelper(getApplicationContext(),DATABASE_NAME,MODE_WORLD_WRITEABLE,DATABASE_VERSION);
        sqLiteDatabase = openOrCreateDatabase(DATABASE_NAME,MODE_WORLD_READABLE,null);
        sqLiteDatabase = databaseOpenHelper.getReadableDatabase();
        aBuilder = new AlertDialog.Builder(MakerListActivity.this); // save 작동시에 띄워줄 dialog창
        addMakerDialogView = new AddMakerDialogView(this);
        deleteMaker = (Button)findViewById(R.id.deleteMaker);
        deleteMaker.setOnClickListener(this);
        moveMaker = (Button)findViewById(R.id.moveMaker);
        moveMaker.setOnClickListener(this);
        slidingLayout = (LinearLayout)findViewById(R.id.slidingLayout);
        showAnim = AnimationUtils.loadAnimation(this,R.anim.translate_selected_list);
        behindAnim = AnimationUtils.loadAnimation(this,R.anim.translate_nonselected_list);
        behindAnim.setFillAfter(true);
        animationListener = new SlidingPageAnimationListener(slidingLayout,MAKER_LIST_ACTIVITY);
        showAnim.setAnimationListener(animationListener);
        behindAnim.setAnimationListener(animationListener);
        makerListView = (ListView)findViewById(R.id.makerListView);

        Intent intent = getIntent();
        googleMapData = intent.getParcelableExtra("GOOGLE_MAP");

        final View header = getLayoutInflater().inflate(R.layout.makerlistview_header,null,false);

        makerListAdapter = new MakerListAdapter(this);
        makerListView.setAdapter(makerListAdapter);
        makerListView.addHeaderView(header);
        getMakerList();

        makerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(makerListAdapter.getCount()!=0){
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
            }
        });

        makerListAdapter.setGoogleMap(googleMapData.getGoogleMap());
        alertDialogClickListener = new AlertDialogClickListener(context, addMakerDialogView, MAINACTIVITY_ALERTDIALOG_REQUEST_CODE, makerListAdapter, databaseOpenHelper);

        makerCount = (TextView)header.findViewById(R.id.makerCount);
        makerCount.setText("마커 수 : "+makerListAdapter.getCount());

    }

    public void getMakerList(){
        String SELECT_RECORD_SQL = "select * from "+TABLE_NAME+";";
        Cursor cursor = sqLiteDatabase.rawQuery(SELECT_RECORD_SQL,null);

        if(cursor.getCount() != 0){
            cursor.moveToFirst();
            makerListAdapter.addItem(new AddressItem(cursor.getString(1),Double.parseDouble(cursor.getString(2)),Double.parseDouble(cursor.getString(3))));

            while (cursor.moveToNext()){
                makerListAdapter.addItem(new AddressItem(cursor.getString(1),Double.parseDouble(cursor.getString(2)),Double.parseDouble(cursor.getString(3))));
            }
        }else {
            Toast.makeText(getApplicationContext(),"등록된 마커가 없습니다.",Toast.LENGTH_SHORT).show();
            Log.d(TAG," 조회에 실패 하였습니다. ");
        }
        cursor.close();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.deleteMaker:
                String DELETE_RECORD_SQL = "delete from "+databaseOpenHelper.getTableName()+" where "
                        +"latitude = \""+makerListAdapter.getItem(getForwordPosition()-1).getLatitude().toString()+"\""
                        +" and longitude = \""+makerListAdapter.getItem(getForwordPosition()-1).getLongitude().toString()+"\";";
                Log.d(TAG,"구문결과->"+DELETE_RECORD_SQL);

                sqLiteDatabase = databaseOpenHelper.getWritableDatabase();

                try {
                    sqLiteDatabase.execSQL(DELETE_RECORD_SQL);
                    Toast.makeText(getApplicationContext(),"선택된 마커가 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                    makerListAdapter.removeItem(getForwordPosition()-1);
                    makerCount.setText("마커 수 : "+makerListAdapter.getCount());
                    slidingLayout.startAnimation(behindAnim);
                    makerListAdapter.notifyDataSetChanged();
                }catch (Exception e){
                    Log.d(TAG,"EXCEPTION IN DELETE_RECORD_SQL.",e);
                }
                break;
            case R.id.moveMaker:
                Intent resultIntent = new Intent();
                resultIntent.putExtra("LATITUDE",makerListAdapter.getItem(getForwordPosition()-1).getLatitude());
                resultIntent.putExtra("LONGITUDE",makerListAdapter.getItem(getForwordPosition()-1).getLongitude());
                setResult(RESULT_OK,resultIntent);
                finish();
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_addmaker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addmaker:
                createAlertDialog();
                aBuilder.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createAlertDialog(){
        aBuilder.setTitle("Maker Insert");
        aBuilder.setIcon(R.drawable.smallstar);
        aBuilder.setView(addMakerDialogView.getDialogView());
        aBuilder.setMessage("마커를 등록할 주소명을 입력하세요.");
        aBuilder.setPositiveButton("확인", alertDialogClickListener);
        aBuilder.setNegativeButton("취소", alertDialogClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume call!.");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        makerCount.setText("마커 수 : "+makerListAdapter.getCount());
        Log.d(TAG,"onWindowFocusChanged call!.");
    }
}
