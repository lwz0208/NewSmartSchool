package com.wust.newsmartschool.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;


public class ImageRotation
{
    private Context mContext;
    private int drawableId;
    private float Rotation;
    private Matrix matrix;
    private Bitmap bitmap;
    public ImageRotation(Context context,int drawableId, float rotation)
    {
        super();
        mContext = context;
        this.drawableId = drawableId;
        Rotation = rotation;
        MakeRotation();
    }

    public Bitmap getBitmap()
    {
        return bitmap;
    }

    public void setRotation(float rotation)
    {
        Rotation = rotation;
        MakeRotation();
    }

    private void MakeRotation()
    {
        matrix = new Matrix();
        matrix.setRotate(Rotation);
        bitmap = ((BitmapDrawable)mContext.getResources().getDrawable(drawableId)).getBitmap();
        bitmap = Bitmap.createBitmap(bitmap, 0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
    }


}

