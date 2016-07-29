package com.example.tj.mapsearch.MakerList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.tj.mapsearch.AddressList.AddressItem;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.List;

public class MakerListAdapter extends BaseAdapter {

    public static final String TAG = "MakerListAdapter";
    private Context context;
    public List<AddressItem> mItems = new ArrayList<AddressItem>();
    GoogleMap googleMap;
    boolean insertSuccessState;

    public MakerListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public AddressItem getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void addItem(AddressItem addressItem){
        mItems.add(addressItem);
    }

    public void setItemName(int arrayPosition, String setText){
        mItems.get(arrayPosition).setAddressName(setText);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        MakerListView makerListView;

        if(convertView == null){
            makerListView = new MakerListView(context,mItems.get(position));

        }else {
            makerListView = (MakerListView) convertView;
        }
        makerListView.setText(mItems.get(position).getAddressName());
        makerListView.setText(mItems.get(position).getLatitude(),1);
        makerListView.setText(mItems.get(position).getLongitude(),2);
        return makerListView;
    }

    public void removeItem(int position){
        mItems.remove(position);
    }

    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    public boolean isInsertSuccessState() {
        return insertSuccessState;
    }

    public void setInsertSuccessState(boolean insertSuccessState) {
        this.insertSuccessState = insertSuccessState;
    }
}
