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
    private int ANIMATION_TYPE;


    public SlidingPageAnimationListener(LinearLayout slidingpage, int ANIMATION_TYPE) {
        this.slidingpage = slidingpage;
        this.ANIMATION_TYPE = ANIMATION_TYPE;
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        switch (ANIMATION_TYPE){
            case 100:
                if(isPageOpen){
                    slidingpage.setVisibility(View.INVISIBLE);
                    isPageOpen = false;
                }
                else
                {
                    isPageOpen = true;
                }
                break;
            case 200:
                if(isPageOpen){
                    slidingpage.setVisibility(View.INVISIBLE);
                    isPageOpen = false;
                }
                else
                {
                    isPageOpen = true;
                }
                break;
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
