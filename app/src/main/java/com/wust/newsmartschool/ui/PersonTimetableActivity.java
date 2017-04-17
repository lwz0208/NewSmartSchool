package com.wust.newsmartschool.ui;

import android.annotation.SuppressLint;
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
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.TimeTableItem;
import com.wust.newsmartschool.utils.GlobalVar;
import com.wust.newsmartschool.utils.HttpServer;
import com.wust.newsmartschool.utils.MyGridView;
import com.wust.newsmartschool.utils.StudentHomeDBHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonTimetableActivity extends Activity implements
        AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener
{

    private Spinner spinner;// 学期下拉框
    private ArrayList<String> semList = new ArrayList<String>();// 学期数据
    private ArrayList<TimeTableItem> timeTableItems;
    private ArrayList<TimeTableItem> timeTableItems2;
    private ArrayAdapter<String> semAdapter;
    // private String[] classid =new String[35];
    private int[] place;
    private MyGridView gridView;

    private TextView title;
    private Context context;
    private ImageView backImageView;
    private ImageView RightHeadView;

    private StudentHomeDBHelper dbHelper;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            if (msg.what == 100)
            {
                Toast.makeText(PersonTimetableActivity.this, "网速不给力",
                        Toast.LENGTH_LONG).show();
            } else if (msg.what == 101)
            {
                semList = (ArrayList<String>) (msg.obj);

                dbHelper.InitStudentCourse(semList);

                Log.i("TAG", semList.get(0));
                semAdapter = new ArrayAdapter<String>(
                        PersonTimetableActivity.this, R.layout.spinner_board,
                        semList);
                semAdapter.setDropDownViewResource(R.layout.spinner_item);
                spinner.setAdapter(semAdapter);
            } else if (msg.what == 102)
            {
                // ArrayList<GradeItem> gradeItems = (ArrayList<GradeItem>)
                // (msg.obj);
                // adapter = new GradeAdapter(context, gradeItems);
                // listView.setAdapter(adapter);
                List<Map<String, Object>> cells = (List<Map<String, Object>>) (msg.obj);
                SimpleAdapter dAdapter = new SimpleAdapter(context, cells,
                        R.layout.girditem_timetable, new String[]
                        { "kcmc" }, new int[]
                        { R.id.timetable });
                gridView.setAdapter(dAdapter);
            } else if (msg.what == 103)
            {
                semList = (ArrayList<String>) (msg.obj);

                Log.i("TAG", semList.get(0));
                semAdapter = new ArrayAdapter<String>(
                        PersonTimetableActivity.this, R.layout.spinner_board,
                        semList);
                semAdapter.setDropDownViewResource(R.layout.spinner_item);
                spinner.setAdapter(semAdapter);
            } else if (msg.what == 104)
            {
                Toast.makeText(PersonTimetableActivity.this,
                        "从数据库中读取数据失败，正在从网络中重新获取，请稍等...", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_person_timetable);
        context = getApplicationContext();

        dbHelper = new StudentHomeDBHelper(this, GlobalVar.Student_DB_version);

        title = (TextView) findViewById(R.id.head_title);
        title.setText("个人课表");
        backImageView = (ImageView) findViewById(R.id.goback);
        backImageView.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                PersonTimetableActivity.this.finish();
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

        spinner = (Spinner) findViewById(R.id.spinner_semester);
        spinner.setOnItemSelectedListener(this);
        gridView = (MyGridView) findViewById(R.id.timetable_gridview);
        gridView.setOnItemClickListener(this);

        ArrayList<String> semlist = dbHelper.getCourses();
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
        gridView.setAdapter(null);

        if (semList != null && semList.size() != 0)
        {
            dbHelper.DeleteAllCourseInfo(semList);
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
                // String semUrlString =
                // "http://202.114.242.198:8090/Xqcx.php?XH="+GlobalVar.userid;
                String semUrlString = "http://202.114.242.198:8090/Xqcx.php?XH="
                        + GlobalVar.userid;
                String semJsonString = httpServer.getData(semUrlString);
                if (semJsonString != null && semJsonString != "")
                {
                    semList = httpServer.getSemList(semJsonString);
                    if (semList != null && semList.size() != 0)
                    {
                        getTable(semList.get(0));
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

    // 获得课表
    private void getTable(final String XQ)
    {
        Runnable runnable = new Runnable()
        {
            public void run()
            {
                HttpServer httpServer = new HttpServer();
                // String
                // urlString="http://202.114.242.198:8090/kbcx.php?XNXQ=2014-2015-1&XH=201213137069";
                String urlString = "http://202.114.242.198:8090/kbcx.php?XNXQ="
                        + XQ + "&XH=" + GlobalVar.userid;
                String jsonString = httpServer.getData(urlString);
                if (jsonString != null && jsonString != "")
                {
                    // 保存至数据库
                    dbHelper.InsertCourseInfoAtSemester(XQ, jsonString);
                    timeTableItems = httpServer.getTimeTableItems(jsonString);

                    if (timeTableItems != null)
                    {
                        List<Map<String, Object>> tableStrings = getTime(timeTableItems);
                        handler.sendMessage(handler.obtainMessage(102,
                                tableStrings));
                    } else
                    {
                        handler.sendMessage(handler.obtainMessage(100));
                    }
                } else
                {
                    handler.sendMessage(handler.obtainMessage(100));
                }
            }
        };
        new Thread(runnable).start();
    }

    private List<Map<String, Object>> getTime(ArrayList<TimeTableItem> items)
    {
        List<Map<String, Object>> cells = new ArrayList<Map<String, Object>>();
        String[][] times = new String[7][5];
        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                times[i][j] = "";
            }
        }
        place = new int[timeTableItems.size()];
        for (int i = 0; i < timeTableItems.size(); i++)
        {
            TimeTableItem item = timeTableItems.get(i);
            String time = item.getKcsj();// 10102
            String week = time.substring(0, 1);// 周几：1
            String jc = time.substring(1, 5);// 节次：0102
            int x = Integer.parseInt(week);
            if (jc == "0102" || jc.equals("0102"))
            {
                int jcnum;
                times[x - 1][0] = times[x - 1][0] + item.getKcmc() + "\n";
                place[i] = (x - 1) * 5;
            } else if (jc == "0304" || jc.equals("0304"))
            {
                times[x - 1][1] = times[x - 1][1] + item.getKcmc() + "\n";
                place[i] = (x - 1) * 5 + 1;
            } else if (jc == "0506" || jc.equals("0506"))
            {
                times[x - 1][2] = times[x - 1][2] + item.getKcmc() + "\n";
                place[i] = (x - 1) * 5 + 2;
            } else if (jc == "0708" || jc.equals("0708"))
            {
                times[x - 1][3] = times[x - 1][3] + item.getKcmc() + "\n";
                place[i] = (x - 1) * 5 + 3;
            } else if (jc == "0910" || jc.equals("0910"))
            {
                times[x - 1][4] = times[x - 1][4] + item.getKcmc() + "\n";
                place[i] = (x - 1) * 5 + 4;
            }
        }
        // for(int i=0;i<place.length;i++){
        // Log.i("TAG", "place:"+place[i]);
        // }
        // 冒泡排序
        // for(int j=0;j<place.length-1;j++){
        // for(int i=0;i<place.length-j-1;i++){
        // int temp;
        // if(place[i]>place[i+1]){
        // temp=place[i+1];
        // place[i+1]=place[i];
        // place[i]=temp;
        // }
        // }
        //
        // }
        // for(int i=0;i<place.length;i++){
        // Log.i("TAG", "place1111111:"+place[i]);
        // }
        // ArrayList<Map<int, TimeTableItem>>=new ArrayList<Map<int,
        // TimeTableItem>>();
        for (int i = 0; i < 7; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                Map<String, Object> cell = new HashMap<String, Object>();
                cell.put("kcmc", times[i][j]);
                cells.add(cell);
            }
            // Log.i("TAG", "place:"+place[i]);
        }
        return cells;

    }

    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id)
    {
        if (parent.getId() == R.id.spinner_semester)
        {
            HttpServer httpServer = new HttpServer();
            String semester = (String) spinner.getSelectedItem().toString();
            Log.d("semester", semester);
            String jsonString = dbHelper.getCourseBySemester(semester);
            if (jsonString != null)
            {
                Log.d("courseJson", jsonString);
                timeTableItems = httpServer.getTimeTableItems(jsonString);
                if (timeTableItems != null)
                {
                    List<Map<String, Object>> tableStrings = getTime(timeTableItems);
                    handler.sendMessage(handler
                            .obtainMessage(102, tableStrings));
                } else
                {
                    handler.sendMessage(handler.obtainMessage(104));
                    getTable(semester);
                }
            } else
            {
                getTable(semester);
            }
        }
    }

    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id)
    {
        Log.i("TAG", "position:" + position);
        int num = 0;
        ArrayList<TimeTableItem> lists = new ArrayList<TimeTableItem>();
        for (int i = 0; i < place.length; i++)
        {
            if (place[i] == position)
            {
                num++;
                TimeTableItem item = timeTableItems.get(i);
                lists.add(item);

            }
        }

        if (lists.size() != 0)
        {
            Intent intent = new Intent(PersonTimetableActivity.this,
                    TimeTableDetailActivity.class);
            intent.putExtra("items", (Serializable) lists);
            startActivity(intent);
        }
    }
}
