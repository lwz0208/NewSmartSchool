package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.TrainningAdapter;
import com.wust.newsmartschool.domain.CharacterParser;
import com.wust.newsmartschool.domain.TrainningItem;
import com.wust.newsmartschool.utils.ClearEditText;
import com.wust.newsmartschool.utils.GlobalVar;
import com.wust.newsmartschool.utils.HttpServer;
import com.wust.newsmartschool.utils.MyLinearLayout;
import com.wust.newsmartschool.utils.PinyinComparator;

import java.util.ArrayList;
import java.util.Collections;


public class TrainingPlanActivity extends Activity implements
        AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener
{

    private ClearEditText clearEditText;
    private MyLinearLayout mainLinearLayout;
    private LinearLayout layout;
    private CharacterParser characterParser;
    private PinyinComparator pinyinComparator;
    private ListView listView;
    private TrainningAdapter adapter;
    private Context context;
    private ImageView backImage;
    private TextView title;
    private ProgressDialog progressDialog;
    private Spinner spinner;
    private ArrayAdapter<String> xzAdapter;
    private String[] kcxzs =
            { "全部", "通识教育平台课程", "学科基础平台课程", "专业课程模块", "实践教学模块", "素质拓展模块" };
    private String[] urls =
            {
                    "http://202.114.242.198:8090/vstupyfa.php?XH=" + GlobalVar.userid,
                    "http://202.114.242.198:8090/vstupyfa_tsjy.php?XH="
                            + GlobalVar.userid + "&KCXZ=1",
                    "http://202.114.242.198:8090/vstupyfa_tsjy.php?XH="
                            + GlobalVar.userid + "&KCXZ=2",
                    "http://202.114.242.198:8090/vstupyfa_tsjy.php?XH="
                            + GlobalVar.userid + "&KCXZ=3",
                    "http://202.114.242.198:8090/vstupyfa_tsjy.php?XH="
                            + GlobalVar.userid + "&KCXZ=4",
                    "http://202.114.242.198:8090/vstupyfa_tsjy.php?XH="
                            + GlobalVar.userid + "&KCXZ=5" };

    private ArrayList<TrainningItem> trainningItems;

    private Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if (msg.what == 100)
            {
                makeToast("网速不给力");
                progressDialog.dismiss();
            }
            else if (msg.what == 101)
            {
                progressDialog.dismiss();
                ArrayList<TrainningItem> trainningItems = (ArrayList<TrainningItem>) (msg.obj);
                if(trainningItems!=null&&trainningItems.size()!=0){
                    adapter = new TrainningAdapter(context, trainningItems);
                    listView.setAdapter(adapter);
                }else{
                    makeToast("暂时没有获取到信息");
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_training_plan);

        init();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("数据加载中，请稍后...");
        progressDialog.setTitle(null);
        progressDialog.setProgress(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(true);
        progressDialog.setIndeterminate(false);

        context = getApplicationContext();
        title = (TextView) findViewById(R.id.head_title);
        title.setText("培养方案");

        spinner = (Spinner) findViewById(R.id.spinner_kcxz);
        xzAdapter = new ArrayAdapter<String>(this, R.layout.spinner_board,
                kcxzs);
        xzAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(xzAdapter);
        spinner.setOnItemSelectedListener(this);

        backImage = (ImageView) findViewById(R.id.goback);
        backImage.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                TrainingPlanActivity.this.finish();
            }
        });

        listView = (ListView) findViewById(R.id.TainningCourse_listview);
        listView.setOnItemClickListener(this);
        getTrainningList(0);

    }

    private void init()
    {
        layout = (LinearLayout) findViewById(R.id.Trainning_LL1);
        mainLinearLayout = (MyLinearLayout) findViewById(R.id.Trainning_Main_Layout);

        mainLinearLayout.setOnSizeChangedListenr(new MyLinearLayout.OnSizeChangeListener()
        {

            @Override
            public void onSizeChanged(int w, int h, int oldw, int oldh)
            {
                // TODO Auto-generated method stub
                if (oldh > h)// 键盘弹出
                {
                    layout.setVisibility(View.GONE);
                }
                else if (oldh <= h) // 键盘关闭
                {
                    layout.setVisibility(View.VISIBLE);
                }
            }
        });

        characterParser = CharacterParser.getInstance();
        pinyinComparator = new PinyinComparator();
        clearEditText = (ClearEditText) findViewById(R.id.clear_search_edittext);

        clearEditText.addTextChangedListener(new TextWatcher()
        {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count)
            {
                // TODO Auto-generated method stub
                filterData(s.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after)
            {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                // TODO Auto-generated method stub

            }
        });

        // clearEditText.setOnFocusChangeListener(new OnFocusChangeListener()
        // {
        //
        // @Override
        // public void onFocusChange(View v, boolean hasFocus)
        // {
        // // TODO Auto-generated method stub
        // if (hasFocus)
        // {
        // layout.setVisibility(View.GONE);
        // }
        // else {
        // layout.setVisibility(View.VISIBLE);
        // }
        // }
        // });
    }

    private void sortData()
    {
        Log.d("TAG", "size11=" + trainningItems.size());
        for (int i = 0; i < trainningItems.size(); i++)
        {
            Log.d("TAG", "size22=" + trainningItems.size());
            TrainningItem item = new TrainningItem();
            item = trainningItems.get(i);
            String pinyin = characterParser.getSelling(item.getKCMC());
            String sortString = pinyin.substring(0, 1);

            if (sortString.matches("[A-Z]"))
            {
                item.setSortLetter(sortString.toUpperCase());
            }
            else
            {
                item.setSortLetter("#");
            }
        }

        Collections.sort(trainningItems, pinyinComparator);
    }

    private void getTrainningList(final int position)
    {
        progressDialog.show();
        new Thread(new Runnable()
        {

            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                HttpServer httpServer = new HttpServer();
                String urlString = urls[position];
                String jsonString = httpServer.getData(urlString);
                if (jsonString != null || jsonString != "")
                {

                    trainningItems = httpServer.getTrainningItems(jsonString);
                    if (trainningItems != null && trainningItems.size() != 0)
                    {
                        Log.d("TAG", "size=" + trainningItems.size());
                        sortData();
                        handler.obtainMessage(101, trainningItems)
                                .sendToTarget();
                    }
                }
                else
                {
                    handler.obtainMessage(100).sendToTarget();
                }

            }
        }).start();
    }

    private void makeToast(String string)
    {
        Toast.makeText(this, string, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id)
    {
        // TODO Auto-generated method stub
        TrainningItem trainningItem = adapter.getItem(position);
        Intent intent = new Intent();
        intent.setClass(context, TrainningPlanDetail.class);
        Bundle bundle = new Bundle();
        bundle.putString("KCH", trainningItem.getKCH());
        bundle.putString("KCM", trainningItem.getKCMC());
        bundle.putString("KKYX", trainningItem.getKKYX());
        bundle.putString("SKYX", trainningItem.getSKYX());
        bundle.putString("SKZY", trainningItem.getSKZY());
        bundle.putString("KCXZ", trainningItem.getKCXZ());
        bundle.putString("XF", trainningItem.getXF());
        bundle.putString("KSXQ", trainningItem.getKSXQ());
        bundle.putString("JKXS", trainningItem.getJKXS());
        bundle.putString("SYXS", trainningItem.getSYXS());
        bundle.putString("SJXS", trainningItem.getSJXS());
        bundle.putString("ZXS", trainningItem.getZXS());
        if (trainningItem.getFXMC().equals("null"))
            bundle.putString("FXMC", " ");
        else
            bundle.putString("FXMC", trainningItem.getFXMC());// 所属方向
        bundle.putString("HKFS", trainningItem.getHKFS());// 考核方式
        bundle.putString("ZHXS", trainningItem.getZHXS());// 周学时
        bundle.putString("SHZT", trainningItem.getSHZT());// 审核状态
        intent.putExtra("datas", bundle);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id)
    {
        // TODO Auto-generated method stub
        if (parent.getId() == R.id.spinner_kcxz)
        {
            getTrainningList(position);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0)
    {
        // TODO Auto-generated method stub

    }

    private void filterData(String filterStr)
    {
        ArrayList<TrainningItem> filterDateList = new ArrayList<TrainningItem>();

        if (TextUtils.isEmpty(filterStr))
        {
            filterDateList = trainningItems;
        }
        else
        {
            filterDateList.clear();
            for (TrainningItem item : trainningItems)
            {
                String name = item.getKCMC();
                if (name.indexOf(filterStr.toString()) != -1
                        || characterParser.getSelling(name).startsWith(
                        filterStr.toString()))
                {
                    filterDateList.add(item);
                }
            }
        }

        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }
}

