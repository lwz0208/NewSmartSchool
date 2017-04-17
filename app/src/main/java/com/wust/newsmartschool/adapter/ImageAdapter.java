package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.wust.newsmartschool.R;


public class ImageAdapter extends BaseAdapter {
    int mGalleryItemBackground;
    private Context mContext;
    private int[] resIds = new int[] { R.drawable.bs1, R.drawable.bs2, R.drawable.bs3,
            R.drawable.bs4, R.drawable.bs5, R.drawable.bs6, R.drawable.bs7,
            R.drawable.bs8, R.drawable.bs9, R.drawable.bs10, R.drawable.bs11,
            R.drawable.bs12,R.drawable.bs13, R.drawable.bs14,R.drawable.bs15, R.drawable.bs16};

    public ImageAdapter(Context context) {
        mContext = context;
    }

    // 返回图像总数
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    // 返回具体位置的ImageView对象
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        // 设置当前图像的图像（position为当前图像列表的位置）
        imageView.setImageResource(resIds[position%resIds.length]);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new Gallery.LayoutParams(300, 300));
        // 设置Gallery组件的背景风格
        imageView.setBackgroundResource(mGalleryItemBackground);
        return imageView;
    }
}
