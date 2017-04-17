package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.ClassRoomAdapter;
import com.wust.newsmartschool.domain.BuildingItem;
import com.wust.newsmartschool.domain.ClassroomItem;
import com.wust.newsmartschool.domain.DateItem;
import com.wust.newsmartschool.utils.HttpServer;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

public class EmptyClassroomActivity extends Activity implements
        AdapterView.OnItemSelectedListener {
    private static final String TAG = "TAG";
    private Context context;
    private ImageView backImageView;
    private TextView title;
    private ListView myListView;
    private ClassRoomAdapter mAdapter;
    private Spinner tSpinner;
    private String XQID,xqid;// 校区id、校区名称
    private ArrayList<BuildingItem> building_array = new ArrayList<BuildingItem>();
    private ArrayList<DateItem> week_array = new ArrayList<DateItem>();
    private ArrayList<String> time_array = new ArrayList<String>();
    private ArrayList<String> room_array = new ArrayList<String>();
    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;
    private ArrayAdapter<BuildingItem> adapter1;
    private ArrayAdapter<DateItem> adapter2;
    private ArrayAdapter<String> adapter3;
    private ArrayAdapter<String> tadapter;
    private ArrayList<String> locationList = new ArrayList<String>();

    private Calendar calendar;
    private int year, month, day;
    // DateItem dateItem;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 100) {

                Toast.makeText(EmptyClassroomActivity.this, "网速不给力",
                        Toast.LENGTH_LONG).show();
            } else if (msg.what == 101) {
                ClassroomItem classroomItem = (ClassroomItem) (msg.obj);
                building_array = classroomItem.buildings;
                week_array = classroomItem.dateList;

                // week_array.add(dateItem);
                time_array = toJcList(classroomItem.jcList);
                room_array = classroomItem.classrooms;
                adapter1 = new ArrayAdapter<BuildingItem>(
                        EmptyClassroomActivity.this, R.layout.spinner_board,
                        building_array);
                adapter2 = new ArrayAdapter<DateItem>(
                        EmptyClassroomActivity.this, R.layout.spinner_board,
                        week_array);
                adapter3 = new ArrayAdapter<String>(
                        EmptyClassroomActivity.this, R.layout.spinner_board,
                        time_array);
                adapter1.setDropDownViewResource(R.layout.spinner_item);
                adapter2.setDropDownViewResource(R.layout.spinner_item);
                adapter3.setDropDownViewResource(R.layout.spinner_item);
                spinner1.setAdapter(adapter1);
                spinner2.setAdapter(adapter2);
                spinner3.setAdapter(adapter3);
            } else if (msg.what == 102) {
                ArrayList<String> roomlistArrayList = (ArrayList<String>) (msg.obj);
                mAdapter = new ClassRoomAdapter(context, roomlistArrayList);
                myListView.setAdapter(mAdapter);
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_empty_classroom);

        context = getApplicationContext();
        title = (TextView) findViewById(R.id.head_title);
        title.setText("空闲教室查询");
        backImageView = (ImageView) findViewById(R.id.goback);
        tSpinner = (Spinner) findViewById(R.id.spinner0);
        locationList.add("青山校区");
        locationList.add("黄家湖校区");
        tadapter = new ArrayAdapter<String>(EmptyClassroomActivity.this,
                R.layout.spinner_board, locationList);
        tadapter.setDropDownViewResource(R.layout.spinner_item);
        tSpinner.setAdapter(tadapter);

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        myListView = (ListView) findViewById(R.id.classroom_list);
        XQID = "00001";// 默认青山校区
        getEmptyclassroomList(XQID);

        // 添加事件Spinner事件监听
        tSpinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        spinner1.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        spinner2.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        spinner3.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        backImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EmptyClassroomActivity.this.finish();
            }
        });

    }

    public void getEmptyclassroomList(final String XQID) {
        Runnable runnable = new Runnable() {
            public void run() {

                HttpServer httpServer = new HttpServer();
                String buildingUrlString = "http://202.114.242.198:8090/jxlmc.php?xqid="
                        + XQID;// 教学楼
                String dateUrlString = "http://202.114.242.198:8090/newdate.php";
                // String dateUrlString =
                // "http://202.114.242.198:8090/date.php";
                String jcListUrlString = "http://202.114.242.198:8090/jcxx.php?xnxqid=2014-2015-2";// 节次
                String roomsUrlString = "http://202.114.242.198:8090/kjs.php?xnxqid=2014-2015-2&xq=2&js=2015-03-15&jc=0102&xqid=00006&jzwid=00043";
                // String roomsUrlString =
                // "http://202.114.242.198:8090/kjs.php?xnxqid=2014-2015-1&xq="+num+"&js="+date+"&jc=0102&xqid="+XQID+"&jzwid="+jzwid;
                String buildingJsonString = httpServer
                        .getData(buildingUrlString);
                String dateJsonString = httpServer.getData(dateUrlString);
                String jcListJsonString = httpServer.getData(jcListUrlString);
                String roomJsonString = httpServer.getData(roomsUrlString);

                if (buildingJsonString != null && dateJsonString != null
                        && jcListJsonString != null) {
                    ArrayList<BuildingItem> building = httpServer
                            .getBuildingList(buildingJsonString);
                    ArrayList<DateItem> dateList = httpServer
                            .getDateList(dateJsonString);
                    ArrayList<String> jcList = httpServer
                            .getJcList(jcListJsonString);
                    ArrayList<String> roomsList = httpServer
                            .getRoomsList(roomJsonString);

                    if (building != null && jcList != null) {
                        ClassroomItem classroomItem = new ClassroomItem();
                        classroomItem.jcList = jcList;
                        classroomItem.buildings = building;
                        classroomItem.classrooms = roomsList;
                        classroomItem.dateList = dateList;
                        handler.sendMessage(handler.obtainMessage(101,
                                classroomItem));
                    }
                } else {
                    handler.sendMessage(handler.obtainMessage(100));
                }
            }
        };
        new Thread(runnable).start();
    }

    public void getEmptyClassroom2(final String XQID, final String num,
                                   final String date, final String jzwid, final String jc,
                                   final String xqid, final String jzwmc) {
        Runnable runnable = new Runnable() {
            public void run() {
                HttpServer httpServer = new HttpServer();
                String roomsUrlString = "http://202.114.242.198:8090/kjs.php?xnxqid=2014-2015-2&xq="
                        + num
                        + "&js="
                        + date
                        + "&jc="
                        + jc
                        + "&xqid="
                        + XQID
                        + "&jzwid=" + jzwid;
                String roomsUrlString2 = "http://202.114.242.198:8090/classroom_select.php?xnxqid=2014-2015-2&xq="
                        + num
                        + "&js="
                        + date
                        + "&jc="
                        + jc
                        + "&xqid="
                        + xqid
                        + "&jzwid=" + jzwmc;

                String roomJsonString = httpServer.getData(roomsUrlString);
                String roomJsonString2 = httpServer.getData(roomsUrlString2);
                ArrayList<String> roomsList = null, roomsList2 = null;
                if (roomJsonString2 != null) {
                    roomsList2 = httpServer
                            .getRoomsList(roomJsonString2);

                    if (roomsList2 != null&&roomsList2.size()>0) {
                        Log.i("TAG", "roomsList2.size():"+roomsList2.size());
                        handler.sendMessage(handler.obtainMessage(102,
                                roomsList2));

                    }else if(roomJsonString!=null){
                        roomsList = httpServer
                                .getRoomsList(roomJsonString);

                        if (roomsList != null) {
                            handler.sendMessage(handler.obtainMessage(102,
                                    roomsList));

                        }
                    }

                }else{
                    handler.sendMessage(handler.obtainMessage(100));
                }
            }
        };
        new Thread(runnable).start();
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        switch (parent.getId()) {
            case R.id.spinner0:
                switch (tSpinner.getSelectedItemPosition()) {
                    case 0:// 青山校区
                        XQID = "00001";
                        xqid = URLEncoder.encode("青山校区");
                        getEmptyclassroomList(XQID);
                        break;
                    case 1:// 黄家湖校区
                        XQID = "00006";
                        xqid = URLEncoder.encode("黄家湖校区");
                        getEmptyclassroomList(XQID);
                        break;
                    default:
                        break;
                }
                break;
            case R.id.spinner1:
                refresh();
                break;
            case R.id.spinner2:
                refresh();
                break;
            case R.id.spinner3:
                refresh();
                break;
        }
    }
    public void refresh() {
        String jzwid = ((BuildingItem) spinner1.getSelectedItem()).getJzwid();
        String jzwmc = URLEncoder.encode(((BuildingItem) spinner1
                .getSelectedItem()).getJzwmc());
        String num = ((DateItem) spinner2.getSelectedItem()).getNum();
        String date = ((DateItem) spinner2.getSelectedItem()).getDate();
        String jc = spinner3.getSelectedItem().toString();
        jc = jc.substring(0, 2) + jc.substring(3, 5);
        if (!jzwid.equals("") && !jzwmc.equals("") && !num.equals("")
                && !date.equals("") && !jc.equals(""))
            getEmptyClassroom2(XQID, num, date, jzwid, jc, xqid, jzwmc);
    }
    public ArrayList<String> toJcList(ArrayList<String> jcString) {
        ArrayList<String> jclist = new ArrayList<String>();
        for (int i = 0; i < jcString.size(); i++) {
            String string = jcString.get(i);
            String oneString = string.substring(0, 2);
            String twoString = string.substring(2, 4);
            string = oneString + "-" + twoString + " 节";
            jclist.add(string);
        }
        return jclist;
    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            week_array.get(7).setDate(
                    year + "-" + monthOfYear + "-" + dayOfMonth);
        }
    };

    public Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                return new DatePickerDialog(this, onDateSetListener, year, month,
                        day);
        }
        return super.onCreateDialog(id);
    }

}

