package com.example.tj.mapsearch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by csh on 2016-05-02.
 */
public class CompassView extends View {
    private Drawable compass;
    private float direction = 0;
    private int padding = 2;

    public CompassView(Context context){
        super(context);

        this.compass = context.getResources().getDrawable(R.drawable.compass);

    }

    protected void onDraw(Canvas canvas){
        canvas.save();
        canvas.rotate(360- direction,padding+compass.getMinimumWidth() / 2, padding+compass.getMinimumHeight() / 2);
        compass.setBounds(padding,padding,padding+compass.getMinimumWidth(), padding+compass.getMinimumHeight());
        compass.draw(canvas);
        canvas.restore();
        super.onDraw(canvas);
    }

    public void setDirection(float direction){
        this.direction = direction;
    }
}
