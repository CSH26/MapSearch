package com.example.tj.mapsearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by TJ on 2016-07-28.
 */
public class UpdateMakerDialogView extends LinearLayout {

    TextView baseMakerName;
    EditText updateMakerName;

    public UpdateMakerDialogView(Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.update_maker_dialog_view,this,true);

        baseMakerName = (TextView)findViewById(R.id.baseMakerName);
        updateMakerName = (EditText)findViewById(R.id.updateMakerName);
    }

    public void setBaseMakerName(String text){
        baseMakerName.setText(text);
    }

    public String getBaseMakerName(){
        return baseMakerName.getText().toString();
    }

    public String getUpdateMakerName(){
        return updateMakerName.getText().toString();
    }

    public LinearLayout getUpdateMakerDialogView(){
        return this;
    }
}
