package com.wust.newsmartschool.utils;

import android.widget.LinearLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class MyLinearLayout extends LinearLayout
{
    private OnSizeChangeListener listener = null;
    public MyLinearLayout(Context context)
    {
        // TODO Auto-generated constructor stub
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs)
    {

        // TODO Auto-generated constructor stub
        super(context, attrs);
    }

    @SuppressLint("NewApi")
    public MyLinearLayout(Context context, AttributeSet attrs, int defStyle)
    {
        // TODO Auto-generated constructor stub
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        // TODO Auto-generated method stub
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        // TODO Auto-generated method stub
        super.onSizeChanged(w, h, oldw, oldh);

        if (listener!=null)
        {
            listener.onSizeChanged(w, h, oldw, oldh);
        }
    }

    public void setOnSizeChangedListenr(OnSizeChangeListener listener)
    {
        this.listener = listener;
    }

    public interface OnSizeChangeListener
    {
        public void onSizeChanged(int w, int h, int oldw, int oldh);
    }
}

