package com.example.tj.mapsearch;

import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;

/**
 * Created by TJ on 2016-07-14.
 */
public class SlidingPageAnimationListener implements Animation.AnimationListener{

    private boolean isPageOpen = false;
    private LinearLayout slidingpage;

    public SlidingPageAnimationListener(LinearLayout slidingpage) {
        this.slidingpage = slidingpage;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if(isPageOpen){
            slidingpage.setVisibility(View.INVISIBLE);
            isPageOpen = false;
        }
        else
        {
            isPageOpen = true;
        }
    }
    @Override
    public void onAnimationStart(Animation animation) {
    }
    @Override
    public void onAnimationRepeat(Animation animation) {
    }

    public boolean isPageOpen() {
        return isPageOpen;
    }

    public void setPageOpen(boolean pageOpen) {
        isPageOpen = pageOpen;
    }
}
