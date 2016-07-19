package com.example.tj.mapsearch.MakerList;

import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tj.mapsearch.AddressList.AddressListAdapter;
import com.example.tj.mapsearch.R;
import com.example.tj.mapsearch.SlidingPageAnimationListener;

public class MakerListActivity extends AppCompatActivity {

    private final String TAG = "MakerListActivity";
    private final static int MAKER_LIST_ACTIVITY = 300;
    private int previousPosition = -1;
    private int forwordPosition = -1;
    TextView makerActivityView, makerCount;
    Button deleteMaker;
    ListView makerListView;
    Animation showAnim, behindAnim;
    LinearLayout slidingLayout;
    MakerListActivity makerListActivity;
    SlidingPageAnimationListener animationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maker_list);
    }
}
