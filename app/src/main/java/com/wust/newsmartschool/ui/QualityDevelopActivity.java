package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.MyViewPageAdapter;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class QualityDevelopActivity extends Activity
{

    private TextView tv1, tv2, tv3, tv4;
    private TextView head_title;
    private View.OnClickListener clickListener;
    private ImageView back;
    private ImageView ImgMark;

    private ViewPager vp;
    private LocalActivityManager manager;
    private MyViewPageAdapter viewPageAdapter;
    private ViewPager.OnPageChangeListener pageChangeListener;
    private int textviewW = 0;
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_quality_develop);

        manager = new LocalActivityManager(this, true);
        manager.dispatchCreate(savedInstanceState);

        vp = (ViewPager) findViewById(R.id.QD_news_content);
        InitView();
        // InitPager();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        setImageViewWidth(tv1.getWidth());
    }

    private void InitView()
    {
        head_title = (TextView) findViewById(R.id.head_title);
        head_title.setText("素质拓展");

        back = (ImageView) findViewById(R.id.goback);

        ImgMark = (ImageView) findViewById(R.id.QD_ImgMark);

        tv1 = (TextView) findViewById(R.id.quality_tv1);
        tv2 = (TextView) findViewById(R.id.quality_tv2);
        tv3 = (TextView) findViewById(R.id.quality_tv3);
        tv4 = (TextView) findViewById(R.id.quality_tv4);
        clickListener = new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub

                switch (v.getId())
                {
                    case R.id.quality_tv1:
                        vp.setCurrentItem(0);
                        break;
                    case R.id.quality_tv2:
                        vp.setCurrentItem(1);

                        break;
                    case R.id.quality_tv3:
                        vp.setCurrentItem(2);

                        break;
                    case R.id.quality_tv4:
                        vp.setCurrentItem(3);

                        break;
                    case R.id.goback:
                        QualityDevelopActivity.this.finish();
                        break;
                }
            }
        };

        tv1.setOnClickListener(clickListener);
        tv2.setOnClickListener(clickListener);
        tv3.setOnClickListener(clickListener);
        tv4.setOnClickListener(clickListener);

        back.setOnClickListener(clickListener);

        Matrix matrix = new Matrix();
        matrix.postTranslate(0, 0);
        ImgMark.setImageMatrix(matrix);

        InitPager();
    }

    private void setTextTitleSelectedColor(int arg0)
    {
        switch (arg0)
        {
            case 0:
                tv1.setTextColor(0xffc80000);
                tv2.setTextColor(0xff969696);
                tv3.setTextColor(0xff969696);
                tv4.setTextColor(0xff969696);
                break;
            case 1:
                tv2.setTextColor(0xffc80000);
                tv1.setTextColor(0xff969696);
                tv3.setTextColor(0xff969696);
                tv4.setTextColor(0xff969696);

                break;
            case 2:
                tv3.setTextColor(0xffc80000);
                tv2.setTextColor(0xff969696);
                tv1.setTextColor(0xff969696);
                tv4.setTextColor(0xff969696);

                break;
            case 3:
                tv4.setTextColor(0xffc80000);
                tv2.setTextColor(0xff969696);
                tv3.setTextColor(0xff969696);
                tv1.setTextColor(0xff969696);
                break;
        }
    }

    private void InitPager()
    {

        pageChangeListener = new ViewPager.OnPageChangeListener()
        {

            @Override
            public void onPageSelected(int arg0)
            {
                // TODO Auto-generated method stub
                if (textviewW == 0)
                {
                    textviewW = tv1.getWidth();
                }
                ImgMark = (ImageView) findViewById(R.id.QD_ImgMark);
                Animation animation = new TranslateAnimation(textviewW
                        * currentIndex, textviewW * arg0, 0, 0);
                currentIndex = arg0;
                animation.setFillAfter(true);
                animation.setDuration(300);
                ImgMark.startAnimation(animation);
                setTextTitleSelectedColor(arg0);
                setImageViewWidth(textviewW);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {
                // TODO Auto-generated method stub

            }
        };

        AddActivitiesToViewPager();
        vp.setCurrentItem(0);
        vp.setOnPageChangeListener(pageChangeListener);
    }

    private void AddActivitiesToViewPager()
    {
        List<View> mViews = new ArrayList<View>();
        Intent intent = new Intent();


        intent.setClass(this, QualityActivity.class);
        intent.putExtra("id", 1);
        mViews.add(getView("QualityActivity1", intent));

        intent.setClass(this, QualityActivity.class);
        intent.putExtra("id", 2);
        mViews.add(getView("QualityActivity2", intent));

        intent.setClass(this, QualityActivity.class);
        intent.putExtra("id", 3);
        mViews.add(getView("QualityActivity3", intent));

        intent.setClass(this, QualityActivity.class);
        intent.putExtra("id", 4);
        mViews.add(getView("QualityActivity4", intent));

        viewPageAdapter = new MyViewPageAdapter(mViews);
        vp.setAdapter(viewPageAdapter);

    }

    private View getView(String id, Intent intent)
    {
        return manager.startActivity(id, intent).getDecorView();

    }

    private void setImageViewWidth(int width)
    {
        ImgMark = (ImageView) findViewById(R.id.QD_ImgMark);
        if (width != ImgMark.getWidth())
        {
            // LayoutParams laParams = (LayoutParams) ImgMark.getLayoutParams();
            RelativeLayout.LayoutParams laParams = (RelativeLayout.LayoutParams) ImgMark.getLayoutParams();
            laParams.width = width;
            ImgMark.setLayoutParams(laParams);
        }
    }

}
