package com.ding.chat.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.ding.chat.R;
import com.ding.chat.views.ECProgressDialog;

import uk.co.senab.photoview.PhotoViewAttacher;

public class HeadImgActivity extends Activity {
    private ImageView iv_headimg;
    //放图的控件
    PhotoViewAttacher attacher;
    ECProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_img);
        iv_headimg = (ImageView) findViewById(R.id.iv_headimg);
//        iv_headimg.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                attacher = new PhotoViewAttacher(iv_headimg);
//                return false;
//            }
//        });
        attacher = new PhotoViewAttacher(iv_headimg);
        pd = new ECProgressDialog(HeadImgActivity.this);
        pd.show();
        Glide.with(HeadImgActivity.this)
                .load(getIntent().getStringExtra("headUrl"))
                .placeholder(R.drawable.ease_default_avatar)
                .into(new GlideDrawableImageViewTarget(iv_headimg) {
                    @Override
                    public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                        super.onResourceReady(drawable, anim);
                        //在这里添加一些图片加载完成的操作
                        pd.dismiss();
                        attacher.update();
                    }
                });


    }

    public void back(View view) {
        finish();
    }
}
