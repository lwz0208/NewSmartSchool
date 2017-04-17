package com.wust.newsmartschool.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.MealAdd;

import java.util.ArrayList;
import java.util.List;


public class ImageAdapter1 extends PagerAdapter
{
    private List<MealAdd> ads;
    private Context context;
    Uri uri;
    Intent intent;
    ImageView imageView;

    public ImageAdapter1(ArrayList<MealAdd> ads, Context ct)
    {
        this.ads = ads;
        context = ct;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // TODO Auto-generated method stub
        final MealAdd ad = ads.get(position%ads.size());

        View view = LayoutInflater.from(context).inflate(R.layout.item,null);
        imageView = (ImageView) view.findViewById(R.id.gallery_image);
        TextView title = (TextView) view.findViewById(R.id.title);
        imageView.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Intent intent = new Intent(context,AdDetailActivity.class);
                intent.putExtra("img", ad.getImgUrl());
                intent.putExtra("content", ad.getContent());
                intent.putExtra("title", ad.getTitle());
                context.startActivity(intent);
            }
        });
        Log.i("ads_url",ad.getImgUrl());
        //Constants.imageloader.displayImage(ad.getImgUrl(), imageView);
        if(!TextUtils.isEmpty(ad.getTitle()) && !ad.getTitle().equals("null"))
        {
            title.setVisibility(View.VISIBLE);
            title.setText(ad.getTitle());
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        //super.destroyItem(container, position, object);
        //container.removeViewAt(position);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return ads.size();
    }



    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

}


