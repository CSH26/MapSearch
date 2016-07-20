package com.example.tj.mapsearch.MakerList;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.tj.mapsearch.R;

// save 시에 Dialog창을 띄우는 클래스
public class AddMakerDialogView extends LinearLayout {

    EditText addmakerDialogView;
    public AddMakerDialogView(final Context context) {
        super(context);

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.add_maker_dialog_view,this,true);

        addmakerDialogView = (EditText)findViewById(R.id.addmakerDialogView);
    }

    public LinearLayout getDialogView(){
        return this;
    }

    public String getAddMakerDialogViewText(){
        return addmakerDialogView.getText().toString();
    }
}
