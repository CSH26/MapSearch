package com.example.tj.mapsearch.AddressList;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class AddressListAdapter extends BaseAdapter {

    public static final String TAG = "AddressListAdapter";
    private Context context;
    public List<AddressItem> mItems = new ArrayList<AddressItem>();

    public AddressListAdapter(Context context) {
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
        AddressListView addressListView;

        if(convertView == null){
            addressListView = new AddressListView(context,mItems.get(position));

        }else {
            addressListView = (AddressListView) convertView;
        }
        addressListView.setText(mItems.get(position).getAddressName());
        addressListView.setText(mItems.get(position).getLatitude(),1);
        addressListView.setText(mItems.get(position).getLongitude(),2);
        return addressListView;
    }
}
