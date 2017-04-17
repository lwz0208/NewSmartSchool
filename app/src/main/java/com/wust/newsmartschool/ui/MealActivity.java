package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.MealAdapter;
import com.wust.newsmartschool.domain.MealAdd;
import com.wust.newsmartschool.domain.MealItem;
import com.wust.newsmartschool.views.CommonListView;
import com.wust.newsmartschool.utils.HttpServer;
import com.wust.newsmartschool.utils.HttpTools;
import com.wust.newsmartschool.utils.ImageAdapter1;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * @Description: 校园订餐activity
 * @Author: 朱
 * @Date: 2015年5月7日
 */

public class MealActivity extends Activity implements MealAdapter.CallBack
{
    private ImageView back;
    private TextView head_title;
    private int temp=0;
    private CommonListView listView;
    private MealAdapter adapter;
    private TextView totalMoneyTextView;
    private ArrayList<MealItem> mealItems;//数组
    private ImageView meal_more,img_package,img_special,img_chaocai;
    private ImageView imageView;
    private static ArrayList<MealItem> changeItems=new ArrayList<MealItem>();
    private static int itemNum=0;
    private Button modify;
    private EditText address;
    private ProgressBar progressBar;
    private ArrayList<MealItem> itemList=new ArrayList<MealItem>();
    //联系电话控件
    private TextView telTextView1;

    public List<String> urls ;
    public ViewPager images_ga;
    private int positon = 0;
    private Thread timeThread = null;
    public boolean timeFlag = true;
    private boolean isExit = false;
    public ImageTimerTask timeTaks = null;
    private Uri uri;
    private Intent intent;
    private int gallerypisition = 0;

    private final int GET_AD = 10000;
    private HttpTools httpTools = new HttpTools();
    private String addUrl = "http://202.114.242.198:8090/wust-hgggg/home/ads.php";

    private Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if (msg.what == 100)
            {
                mealItems = (ArrayList<MealItem>) msg.obj;
                progressBar.setVisibility(View.GONE);
                if (mealItems.size() == 0)
                {
                    Toast.makeText(getApplicationContext(), "暂无信息",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Log.e("大小所得到的", "mealItems" + mealItems.size());
                    adapter = new MealAdapter(MealActivity.this,mealItems);
                    adapter.setCallback(MealActivity.this);
                    listView.setAdapter(adapter);
                }
            }
            else if (msg.what == 101)
            {
                Toast.makeText(getApplicationContext(), "网速不给力",Toast.LENGTH_SHORT).show();
            }
            else if(msg.what==102)
            {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "暂无信息", Toast.LENGTH_LONG).show();
            }
            else if(msg.what == GET_AD)
            {
                String result = (String)msg.obj;

                SharedPreferences sharedPreferences = getSharedPreferences(
                        "meal_ad", Context.MODE_PRIVATE);
                ArrayList<MealAdd> ads  = httpTools.getAds(result);
                if(ads != null)
                {

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("meal_ads", result);
                    editor.commit();
                    ads = httpTools.getAds(result);
                }
                else
                {
                    result = sharedPreferences.getString("meal_ad", "");
                    ads = httpTools.getAds(result);
                }

                if(ads != null)
                {
                    ImageAdapter1 imageAdapter = new ImageAdapter1(ads,MealActivity.this);
                    images_ga.setAdapter(imageAdapter);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "广告获取失败", Toast.LENGTH_SHORT).show();
                }


            }

        }
    };

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_meal);
        changeItems = new ArrayList<MealItem>();//
        head_title = (TextView) findViewById(R.id.head_title);
        listView = (CommonListView) findViewById(R.id.activity_meal_list);
        head_title.setText("校园订餐");
        back = (ImageView) findViewById(R.id.goback);
        init();
        back.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {
                finish();
            }
        });
        timeTaks = new ImageTimerTask();
        autoGallery.scheduleAtFixedRate(timeTaks, 1000, 1000);
        timeThread = new Thread()
        {
            public void run()
            {
                while(!isExit)
                {
                    try
                    {
                        Thread.sleep(1500);
                    }
                    catch (InterruptedException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    synchronized (timeTaks)
                    {
                        if(!timeFlag)
                        {
                            timeTaks.timeCondition = true;
                            timeTaks.notifyAll();
                        }
                    }
                    timeFlag = true;
                }
            };
        };
        timeThread.start();
        meal_more=(ImageView)findViewById(R.id.meal_more);
        img_package=(ImageView)findViewById(R.id.img_package);
        img_chaocai=(ImageView)findViewById(R.id.img_chaocai);
        img_special=(ImageView)findViewById(R.id.img_special);
        progressBar = (ProgressBar)findViewById(R.id.progressbar);
        telTextView1=(TextView)findViewById(R.id.telephone1);
        meal_more.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
//                Intent intent=new Intent(MealActivity.this,MealMoreActivity.class);
//                startActivity(intent);
            }
        });
        img_package.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
//                Intent intent=new Intent(MealActivity.this,MealPackageActivity.class);
//                startActivity(intent);
            }
        });
        img_chaocai.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
//                Intent intent =new Intent(MealActivity.this,MealChaocaiActivity.class);
//                startActivity(intent);
            }

        });

        img_special.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                img_special.setClickable(false);
                img_special.setEnabled(false);
                Toast.makeText(MealActivity.this, "该美食暂未开放！", Toast.LENGTH_LONG).show();
				/*Intent intent=new Intent(MealActivity.this,SpecialMealActivity.class);
				startActivity(intent);*/
            }
        });

        telTextView1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        telTextView1.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                String urlString=telTextView1.getText().toString();
                urlString=urlString.substring(0, 12);
                try
                {
                    Intent phoneIntent =new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+urlString));
                    phoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(phoneIntent);
                }
                catch (Exception e)
                {
                    Toast.makeText(MealActivity.this, "拨打电话不成功", Toast.LENGTH_LONG).show();
                    // TODO: handle exception
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id)
            {
                // TODO Auto-generated method stub
                String  name=((TextView)findViewById(R.id.meal_name)).getText().toString();
                String price=((TextView)findViewById(R.id.meal_price)).getText().toString();
                String descripation=((TextView)findViewById(R.id.meal_descripation)).getText().toString();
                String mealId=((TextView)findViewById(R.id.meal_id)).getText().toString();
                imageView=(ImageView)findViewById(R.id.meal_img);

                //Intent intent=new Intent(MealActivity.this,ChaocaiMealDetailActivity.class);

                MealItem item =(MealItem) adapter.getItem(position);//

                Bundle bundle = new Bundle();//
                bundle.putParcelable("item", item);//

                bundle.putString("name", name);
                bundle.putString("price", price);
                bundle.putString("descripation",descripation);

                intent.putExtras(bundle);//
                startActivity(intent);
            }

        });
        Button summit=(Button)findViewById(R.id.summit);
        summit.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                if(temp!=0)
                {
                    String totalMoneyTextView = ((TextView) findViewById(R.id.tv_total_money)).getText().toString();
                    //String address=((EditText)findViewById(R.id.address)).getText().toString();//获取地址布局
                    //Intent intent=new Intent(MealActivity.this,OrderMealActivity.class);
                    Bundle bundle=new Bundle();
                    // bundle.putCharSequence("address", address);//传递地址
                    bundle.putParcelableArrayList("mealItems", changeItems);//
                    bundle.putString("totalMoneyTextView",totalMoneyTextView);//保存总价钱
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "sorry!购物车是空的，请选择菜单",1).show();
                }

            }

        });
        totalMoneyTextView = (TextView) findViewById(R.id.tv_total_money);
        getMealList();

        getAdd();
    }
    private void getMealList()
    {
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                HttpServer httpServer = new HttpServer();
                String urlString = "http://202.114.242.198:8090/wust-hgggg/home/mt_dish_select.php?op=1";
                String jsonString = httpServer.getData(urlString);

                if (jsonString != null)
                {
                    ArrayList<MealItem> mealItems = new ArrayList<MealItem>();
                    mealItems = httpServer.getMealList(jsonString);
                    if (mealItems != null)
                    {
                        handler.sendMessage(handler.obtainMessage(100,mealItems));
                    }
                    else
                    {
                        handler.sendMessage(handler.obtainMessage(102));
                    }
                }
                else
                {
                    handler.sendMessage(handler.obtainMessage(101));
                }
            }
        };
        new Thread(runnable).start();
    }
    private int getMealInfoByMealId(String mealId)//
    {
        int b = -1;
        for (int i = 0; i < changeItems.size(); i++)
        {
            if (changeItems.get(i).getMealId().equals(mealId))
            {
                b = i;
                break;
            }
        }
        return b;

    }
    @Override
    public void onCountChanged(int type, String price,MealItem item)//价钱改变函数
    {


        double changePrice = Double.parseDouble(price);//
        double currentPrice = 0.0;//当前价钱
        if (!totalMoneyTextView.getText().toString().equals("购物车是空的")) //购物车不为空
        {
            this.temp=1;
            currentPrice = Double.parseDouble(totalMoneyTextView.getText().toString().substring(1, totalMoneyTextView.getText().toString().length()));
        }

        if (type == MealAdapter.ADD)
        {

            this.temp=1;
            totalMoneyTextView.setText("￥" + (currentPrice + changePrice));
        }
        else if(type == MealAdapter.SUB)
        {
            double diff = currentPrice - changePrice;
            if (diff == 0.0)
            {
                this.temp=0;
                totalMoneyTextView.setText("购物车是空的");
                //
                Toast.makeText(getApplicationContext(), "sorry!购物车是空的，请选择菜单",1).show();

            }
            //
            else
            {

                totalMoneyTextView.setText("￥" + diff);

            }
        }
        if (type==MealAdapter.ADD)
        {
            int position = getMealInfoByMealId(item.getMealId());

            //Toast.makeText(this,""+position, Toast.LENGTH_SHORT).show();
            //Toast.makeText(this,""+item.getMealId(), Toast.LENGTH_SHORT).show();
            if (position!=-1)
            {
                changeItems.remove(position);
            }
            changeItems.add(item);
        }
        else
        {
            int position = getMealInfoByMealId(item.getMealId());
            if (position!=-1)
            {
                changeItems.remove(position);
            }
            if (item.getCount()!=0)
            {
                changeItems.add(item);
            }
        }
    }
    @Override
    protected void onStart()
    {
        // TODO Auto-generated method stub
        super.onStart();
        //init();
    }

    private void init()
    {

        Bitmap image= BitmapFactory.decodeResource(getResources(),R.drawable.icon);
        //imagesCache.put("background_non_load",image);  //设置缓存中默认的图片

        int width = this.getResources().getDisplayMetrics().widthPixels;
        int height = width/2;
        android.widget.RelativeLayout.LayoutParams lp = new android.widget.RelativeLayout.LayoutParams(width, height);
        images_ga = (ViewPager) findViewById(R.id.image_wall);
        images_ga.setLayoutParams(lp);
        //images_ga.setImageActivity(this);

        LinearLayout pointLinear = (LinearLayout) findViewById(R.id.gallery_point_linear);
        pointLinear.setBackgroundColor(Color.argb(200, 135, 135, 152));
        for (int i = 0; i < 4; i++)
        {
            ImageView pointView = new ImageView(this);
            if(i==0)
            {
                pointView.setBackgroundResource(R.drawable.feature_point_cur);
            }
            else
                pointView.setBackgroundResource(R.drawable.feature_point);
            pointLinear.addView(pointView);
        }
    }

    public void changePointView(int cur)
    {
        LinearLayout pointLinear = (LinearLayout) findViewById(R.id.gallery_point_linear);
        View view = pointLinear.getChildAt(positon);
        View curView = pointLinear.getChildAt(cur);
        if(view!=null&& curView!=null)
        {
            ImageView pointView = (ImageView)view;
            ImageView curPointView = (ImageView)curView;
            pointView.setBackgroundResource(R.drawable.feature_point);
            curPointView.setBackgroundResource(R.drawable.feature_point_cur);
            positon = cur;
        }
    }

    final Handler autoGalleryHandler = new Handler()
    {
        public void handleMessage(Message message)
        {
            super.handleMessage(message);
            switch (message.what)
            {
                case 1:
                    //images_ga.setSelection(message.getData().getInt("pos"));
                    break;
            }
        }
    };

    @Override
    protected void onResume()
    {
        // TODO Auto-generated method stub
        super.onResume();
        timeFlag = false;
    }

    @Override
    protected void onPause()
    {
        // TODO Auto-generated method stub
        super.onPause();
        timeTaks.timeCondition = false;
    }

    public class ImageTimerTask extends TimerTask
    {
        public volatile boolean timeCondition = true;
        // int gallerypisition = 0;
        public void run()
        {
            synchronized (this)
            {
                while(!timeCondition)
                {
                    try
                    {
                        Thread.sleep(100);
                        wait();
                    }
                    catch (InterruptedException e)
                    {
                        Thread.interrupted();
                    }
                }
            }
            try
            {
                System.out.println(gallerypisition+"");
                Message msg = new Message();
                Bundle date = new Bundle();// 存放数据
                date.putInt("pos", gallerypisition);
                msg.setData(date);
                msg.what = 1;//消息标识
                autoGalleryHandler.sendMessage(msg);
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    Timer autoGallery = new Timer();


    private void getAdd()
    {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                String result = httpTools.getJsonString(addUrl);
                handler.sendMessage(handler.obtainMessage(GET_AD, result));
            }
        }).start();
    }


}
