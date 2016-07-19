package com.example.tj.mapsearch.MakerList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.tj.mapsearch.AddressList.AddressItem;

import java.util.ArrayList;
import java.util.List;

public class MakerListAdapter extends BaseAdapter {

    public static final String TAG = "MakerListAdapter";
    private Context context;
    public List<AddressItem> mItems = new ArrayList<AddressItem>();

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
}
