package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.GradeAdapter;
import com.wust.newsmartschool.domain.GradeItem;
import com.wust.newsmartschool.utils.GlobalVar;
import com.wust.newsmartschool.utils.HttpServer;
import com.wust.newsmartschool.utils.StudentHomeDBHelper;

import java.util.ArrayList;


public class PersonalGradeActivity extends Activity implements
        AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener
{

    private Spinner spinner;// 学期下拉框
    private ListView listView;// 成绩列表
    private GradeAdapter adapter;
    private ArrayList<String> semList = new ArrayList<String>();// 学期数据
    private ArrayAdapter<String> semAdapter;
    private ArrayList<GradeItem> gradeItems;
    // private String XH;//学号

    private TextView title;
    private Context context;
    private ImageView backImageView;
    private ImageView RightHeadView;

    private StudentHomeDBHelper dbHelper;

    private Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if (msg.what == 100)
            {
                Toast.makeText(PersonalGradeActivity.this, "网速不给力",
                        Toast.LENGTH_LONG).show();
            } else if (msg.what == 101)
            {
                semList = (ArrayList<String>) (msg.obj);

                dbHelper.InitStudentGrade(semList);

                Log.i("TAG", semList.get(0));
                semAdapter = new ArrayAdapter<String>(
                        PersonalGradeActivity.this, R.layout.spinner_board,
                        semList);
                semAdapter.setDropDownViewResource(R.layout.spinner_item);
                spinner.setAdapter(semAdapter);
            } else if (msg.what == 102)
            {
                ArrayList<GradeItem> gradeItems = (ArrayList<GradeItem>) (msg.obj);
                adapter = new GradeAdapter(context, gradeItems);
                listView.setAdapter(adapter);
            } else if (msg.what == 103)
            {
                semList = (ArrayList<String>) (msg.obj);

                Log.i("TAG", semList.get(0));
                semAdapter = new ArrayAdapter<String>(
                        PersonalGradeActivity.this, R.layout.spinner_board,
                        semList);
                semAdapter.setDropDownViewResource(R.layout.spinner_item);
                spinner.setAdapter(semAdapter);
            } else if (msg.what == 104)
            {
                Toast.makeText(PersonalGradeActivity.this,
                        "从数据库中读取数据失败，正在从网络中重新获取，请稍等...", Toast.LENGTH_SHORT)
                        .show();
            }

        }
    };

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_personal_grade);

        context = getApplicationContext();

        dbHelper = new StudentHomeDBHelper(this, GlobalVar.Student_DB_version);

        title = (TextView) findViewById(R.id.head_title);
        title.setText("个人成绩");
        backImageView = (ImageView) findViewById(R.id.goback);
        backImageView.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                PersonalGradeActivity.this.finish();
            }
        });

        RightHeadView = (ImageView) findViewById(R.id.user1);
        RightHeadView.setImageResource(R.drawable.abc_ic_menu_refresh);
        RightHeadView.setVisibility(View.VISIBLE);
        RightHeadView.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                Refresh();
            }

        });

        listView = (ListView) findViewById(R.id.grade_listview);
        listView.setOnItemClickListener(this);
        spinner = (Spinner) findViewById(R.id.spinner_semester);
        spinner.setOnItemSelectedListener(this);

        // 首先从数据库中获取学期信息
        ArrayList<String> semlist = dbHelper.getGrades();
        if (semlist != null && semlist.size() != 0)
        {
            handler.obtainMessage(103, semlist).sendToTarget();
        } else
        {
            getSemList();
        }

    }

    // 从网络端重新刷新数据
    private void Refresh()
    {
        // TODO Auto-generated method stub
        // 首先清空数据库
        semAdapter.clear();
        semAdapter.notifyDataSetChanged();
        listView.setAdapter(null);

        if (semList != null && semList.size() != 0)
        {
            dbHelper.DeleteAllGradeInfo(semList);
        }

        getSemList();

    }

    // 获得学期
    private void getSemList()
    {
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                HttpServer httpServer = new HttpServer();
                String semUrlString = "http://202.114.242.198:8090/Xqcx.php?XH="
                        + GlobalVar.userid;
                // String semUrlString =
                // "http://202.114.242.198:8090/Xqcx.php?XH="+"201213137075";
                String semJsonString = httpServer.getData(semUrlString);
                if (semJsonString != null && semJsonString != "")
                {
                    semList = httpServer.getSemList(semJsonString);
                    if (semList != null && semList.size() != 0)
                    {
                        getGradeList(semList.get(0));
                        handler.sendMessage(handler.obtainMessage(101, semList));
                    }
                } else
                {
                    handler.sendMessage(handler.obtainMessage(100));
                }
            }
        };
        new Thread(runnable).start();

    }

    // 获得成绩
    private void getGradeList(final String semester)
    {
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                HttpServer httpServer = new HttpServer();
                // String gradeUrlString =
                // "http://202.114.242.198:8090/Cjcx.php?XH="+"201213137075"
                // + "&KKXQ="+semester;
                String gradeUrlString = "http://202.114.242.198:8090/Cjcx.php?XH="
                        + GlobalVar.userid + "&KKXQ=" + semester;
                String gradeJsonString = httpServer.getData(gradeUrlString);
                if (gradeJsonString != null && gradeJsonString != "")
                {
                    // ArrayList<GradeItem> gradeItems = httpServer
                    // .getGradeItems(gradeJsonString);
                    // 保存至数据库
                    dbHelper.InsertGradeInfoAtSemester(semester,
                            gradeJsonString);
                    gradeItems = httpServer.getGradeItems(gradeJsonString);
                    Log.i("TAG", "gradeItems size=" + gradeItems.size());
                    handler.sendMessage(handler.obtainMessage(102, gradeItems));
                } else
                {
                    handler.sendMessage(handler.obtainMessage(100));
                }
            }
        };
        new Thread(runnable).start();
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id)
    {
        if (parent.getId() == R.id.spinner_semester)
        {
            HttpServer httpServer = new HttpServer();

            String semester = (String) spinner.getSelectedItem().toString();
            Log.d("semester", semester);
            // 先从数据库中获取
            String jsonString = dbHelper.getGradeBySemester(semester);
            if (jsonString != null)
            {
                Log.d("GradeJson_Gain", jsonString);
                gradeItems = httpServer.getGradeItems(jsonString);
                if (gradeItems != null)
                {
                    handler.sendMessage(handler.obtainMessage(102, gradeItems));
                } else
                {
                    handler.sendMessage(handler.obtainMessage(104));
                    getGradeList(semester);
                }
            } else
            {
                getGradeList(semester);
            }

        }
    }

    public void onNothingSelected(AdapterView<?> parent)
    {
    }

    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id)
    {
        GradeItem item = (GradeItem) adapter.getItem(position);
        Bundle bundle = new Bundle();
        bundle.putString("position", "" + (position + 1));
        bundle.putString("jd", item.getJd());
        bundle.putString("kcmc", item.getKcmc());// 课程名称
        bundle.putString("kcsx", item.getKcsx());// 课程属性
        bundle.putString("kcxz", item.getKcxz());// 课程性质
        // bundle.putString("kkdw", item.getKkdw());
        bundle.putString("kkxq", spinner.getSelectedItem().toString());// 开课学期
        // bundle.putString("ksxz", item.getKsxz());
        bundle.putString("xf", item.getXf());// 学分
        // bundle.putString("xm", item.getXm());
        bundle.putString("zcj", item.getZcj());// 总成绩
        bundle.putString("zxs", item.getZxs());// 总学时

        Intent intent = new Intent(PersonalGradeActivity.this,
                GradeDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

