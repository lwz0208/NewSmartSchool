package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.ImageAdapter;


public class BeautifulSchoolActivity extends Activity implements
        AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory {

    private ImageView goback;
    private TextView title;
    private ImageSwitcher imageSwitcher;
    private ImageAdapter imageAdapter;
    Gallery gallery;
    private int[] resIds = new int[] { R.drawable.bs1, R.drawable.bs2,
            R.drawable.bs3, R.drawable.bs4, R.drawable.bs5, R.drawable.bs6,
            R.drawable.bs7, R.drawable.bs8, R.drawable.bs9, R.drawable.bs10,
            R.drawable.bs11, R.drawable.bs12, R.drawable.bs13, R.drawable.bs14,
            R.drawable.bs15, R.drawable.bs16 };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_beautiful_school);

        title=(TextView)findViewById(R.id.head_title);
        title.setText("美丽校园");
        goback = (ImageView) findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                finish();
            }
        });

        // 装载Gallery组件
        gallery = (Gallery) findViewById(R.id.beautiful_gallery);
        // 创建用于描述图像数据的ImageAdapter对象
        imageAdapter = new ImageAdapter(this);
        // 设置Gallery组件的Adapter对象
        gallery.setAdapter(imageAdapter);
        gallery.setSelection(2);
        gallery.setOnItemSelectedListener(this);
        imageSwitcher = (ImageSwitcher) findViewById(R.id.imageswitcher);
        // 设置ImageSwitcher组件的工厂对象
        imageSwitcher.setFactory(this);
        // 设置ImageSwitcher组件显示图像的动画效果
        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in));
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out));
        imageSwitcher.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                int position = gallery.getSelectedItemPosition();
                int id_picture = resIds[position % resIds.length];
                Intent intent = new Intent(BeautifulSchoolActivity.this,PictureClickActivity.class);
                intent.putExtra("id_picture", id_picture);
                startActivity(intent);
            }
        });

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position,long id)
    {
        // 选中Gallery中某个图像时，在ImageSwitcher组件中放大显示该图像
        imageSwitcher.setImageResource(resIds[position % resIds.length]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {
    }

    @Override
    // ImageSwitcher组件需要这个方法来创建一个View对象（一般为ImageView对象）
    // 来显示图像
    public View makeView()
    {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundColor(0xFF000000);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        return imageView;
    }
}

