package com.example.tj.mapsearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

// save 시에 Dialog창을 띄우는 클래스
public class DialogView extends LinearLayout {

    public DialogView(final Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.dialog_view,this,true);

    }

    public LinearLayout getDialogView(){
        return this;
    }


}
