package com.wust.newsmartschool.utils;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {
    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(this.getCurrentItem() != 0)
                    GlobalVar.viewpageflip = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if(this.getCurrentItem() != 0)
                    GlobalVar.viewpageflip = true;
                break;
            case MotionEvent.ACTION_UP:
                GlobalVar.viewpageflip = false;
                break;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }

}

