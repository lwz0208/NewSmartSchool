package com.wust.newsmartschool.views;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.wust.easeui.Constant;

public class MyViewPager extends ViewPager {
	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(this.getCurrentItem() != 0)
				Constant.viewpageflip = true;
			break;
		case MotionEvent.ACTION_MOVE:
			if(this.getCurrentItem() != 0)
				Constant.viewpageflip = true;
			break;
		case MotionEvent.ACTION_UP:
			Constant.viewpageflip = false;
		break;
		default:
			break;
		}
	
		return super.onTouchEvent(event);
	}
	
}
