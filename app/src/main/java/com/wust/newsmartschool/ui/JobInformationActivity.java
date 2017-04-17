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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.MyViewPageAdapter;

import java.util.ArrayList;
import java.util.List;


public class JobInformationActivity extends Activity implements View.OnClickListener
{

    private TextView head_title;
    private ImageView back, ImgMark;
    private TextView tv1, tv2;
    private ViewPager vp;
    private LocalActivityManager manager;
    private MyViewPageAdapter viewPageAdapter;
    private ViewPager.OnPageChangeListener pageChangeListener;
    private int textviewW = 0;
    private int currentIndex = 0;

    @SuppressWarnings("deprecation")
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_job_information);
        manager = new LocalActivityManager(this, true);
        manager.dispatchCreate(savedInstanceState);
        vp = (ViewPager) findViewById(R.id.JB_news_content);
        InitView();
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
        head_title.setText("就业信息");

        back = (ImageView) findViewById(R.id.goback);

        ImgMark = (ImageView) findViewById(R.id.JB_ImgMark);

        tv1 = (TextView) findViewById(R.id.jb_announcement);
        tv2 = (TextView) findViewById(R.id.jb_recruit);

        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);

        back.setOnClickListener(this);

        Matrix matrix = new Matrix();
        matrix.postTranslate(0, 0);
        ImgMark.setImageMatrix(matrix);

        InitPager();
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
                ImgMark = (ImageView) findViewById(R.id.JB_ImgMark);
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

    private void setTextTitleSelectedColor(int arg0)
    {
        switch (arg0)
        {
            case 0:
                tv1.setTextColor(0xffc80000);
                tv2.setTextColor(0xff969696);
                break;
            case 1:
                tv2.setTextColor(0xffc80000);
                tv1.setTextColor(0xff969696);
                break;
        }
    }

    private void AddActivitiesToViewPager()
    {
        List<View> mViews = new ArrayList<View>();
        Intent intent = new Intent();

        intent.setClass(this, JobMessageActivity.class);
        intent.putExtra("id", 1);
        mViews.add(getView("JobMessageActivity", intent));

        intent.setClass(this, RecruitmentActivity.class);
        intent.putExtra("id", 2);
        mViews.add(getView("RecruitmentActivity", intent));

        viewPageAdapter = new MyViewPageAdapter(mViews);
        vp.setAdapter(viewPageAdapter);

    }

    private View getView(String id, Intent intent)
    {
        return manager.startActivity(id, intent).getDecorView();

    }

    private void setImageViewWidth(int width)
    {
        ImgMark = (ImageView) findViewById(R.id.JB_ImgMark);
        if (width != ImgMark.getWidth())
        {
            // LayoutParams laParams = (LayoutParams) ImgMark.getLayoutParams();
            LinearLayout.LayoutParams laParams = (LinearLayout.LayoutParams) ImgMark.getLayoutParams();
            laParams.width = width;
            ImgMark.setLayoutParams(laParams);
        }
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.goback:
                JobInformationActivity.this.finish();
                break;
            case R.id.jb_announcement:
                vp.setCurrentItem(0);
                break;
            case R.id.jb_recruit:
                vp.setCurrentItem(1);
                break;
            default:
                break;
        }
    }
}

