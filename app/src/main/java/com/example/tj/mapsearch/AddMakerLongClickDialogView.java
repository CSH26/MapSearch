package com.example.tj.mapsearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by TJ on 2016-07-27.
 */
public class AddMakerLongClickDialogView extends LinearLayout {

    EditText updateLongClickMakerName;

    public AddMakerLongClickDialogView(final Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.add_maker_longclick_dialog_view,this,true);

        updateLongClickMakerName = (EditText)findViewById(R.id.updateLongClickMakerName);
    }

    public LinearLayout getDialogView(){
        return this;
    }

    public String getUpdateLongClickMakerNameText(){
        return updateLongClickMakerName.getText().toString();
    }

    public void setUpdateLongClickMakerNameText(String text){
        updateLongClickMakerName.setText(text);
    }
}