package com.wust.newsmartschool.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.format.Time;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.AppoCenterListAdapter;
import com.wust.newsmartschool.adapter.LibrarySeatGridViewAdapter;
import com.wust.newsmartschool.domain.AppoCenterItem;
import com.wust.newsmartschool.utils.GlobalVar;
import com.wust.newsmartschool.utils.MyListView;
import com.wust.newsmartschool.utils.StreamTools;
import com.wust.newsmartschool.utils.URL_UNIVERSAL;
import com.wust.newsmartschool.zxing.activity.CaptureActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;


public class LibSeatActivity extends AppCompatActivity
{
    private View view1, view2, view3, view4;//需要滑动的页卡
    private TabLayout mTabLayout;
    private ViewPager viewPager;//viewpager
    private List<View> viewList;//把需要滑动的页卡添加到这个list中
    private List<String> titleList;//viewpager的标题

    private Toolbar toolbar;
    private MyListView appoCenterListView;
    private List<AppoCenterItem> appoCenterLists;

    private boolean isAdmin, isRefreshOnewkey;
    private String scanRoom, scanSeat;

    private TextView onekeyAppoResultTextView;
    private Button onekeyAppoButton;
    private ImageButton adUpdate;
    private Animation animation;

    private Spinner campusSpinner;
    private Spinner roomSpinner;
    private Spinner campusSpinner1;
    private Spinner roomSpinner1;
    private TextView resultTextView;
    private GridView seatGridView;
    private List<String> lists;
    private ArrayAdapter<String> roomAdapter;
    private JSONArray emptySeatsJSONArray;
    private HashMap<String, String> resultsMap;

    String[] campus = {
            "黄家湖校区", "青山校区" };
    String[] campusId = {
            "1", "0" };
    String[] huangRooms = {
            "请选择", "南三(社会科学阅览室)", "南四(工程技术阅览室)", "南五(基础科学阅览室)", "南六(政法经管阅览室)",
            "北三(建筑,艺术阅览室)", "北四(生物,医药阅览室)", "期刊(阅览室)" };
    String[] huangRoomsId = {
            "0", "1", "2", "3", "4",
            "5", "6", "8" };
    String[] qingRooms = {
            "请选择", "一楼阅览室", "三楼阅览室", "四楼阅览室", "五楼阅览室", "六楼阅览室" };
    String[] qingRoomsId = {
            "10", "11", "13", "14",
            "15", "16" };

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case 0:	// 高级预约预约功能
                {
                    String obj = (String) msg.obj;
                    if (TextUtils.isEmpty(obj))
                        Toast.makeText(getApplicationContext(), "网络错误，请检查网络", Toast.LENGTH_SHORT).show();
                    else {
                        obj = resultsMap.get(obj);
                        if (!obj.contains("Sorry"))
                        {
                            viewPager.setCurrentItem(0);
                            new GetAppoInfo(GlobalVar.userid).execute();
                        }
                        Toast.makeText(LibSeatActivity.this, obj, Toast.LENGTH_SHORT).show();
                    }
                }
                break;

                case 1: // 高级预约查询功能
                {
                    stopAnim();
                    String obj = (String) msg.obj;
                    if (TextUtils.isEmpty(obj))
                    {
                        resultTextView.setText("网络错误，请检查网络");
                        Toast.makeText(getApplicationContext(), "网络错误，请检查网络", Toast.LENGTH_SHORT).show();
                        seatGridView.setVisibility(View.INVISIBLE);
                        return;
                    }
                    if (obj.contains("unavailable")) {
                        resultTextView.setText("此阅览室暂时不可用");
                        seatGridView.setVisibility(View.INVISIBLE);
                        return;
                    }
                    String[] results = obj.split("#");
                    results[0] = "剩余" + results[0] + "个空闲座位\n共有" + results[1] + "个座位";
                    resultTextView.setText(results[0]);
                    if (emptySeatsJSONArray.length() != 0)
                    {
                        lists = new ArrayList<>();
                        //不知道什么原因，获取空座位的接口返回的座位最后一位是空，所以长度取到前一位即可
                        for (int i = 0; i < emptySeatsJSONArray.length() - 1; i++)
                            try {
                                lists.add(emptySeatsJSONArray.getString(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        seatGridView.setAdapter(new LibrarySeatGridViewAdapter(LibSeatActivity.this, lists));
                        seatGridView.setVisibility(View.VISIBLE);
                    } else {
                        seatGridView.setVisibility(View.INVISIBLE);
                    }
                }
                break;

                case 2: // 一键预约
                {
                    String obj = (String) msg.obj;
                    if (TextUtils.isEmpty(obj))
                    {
                        onekeyAppoResultTextView.setText("网络错误，请检查网络");
                        Toast.makeText(getApplicationContext(), "网络错误，请检查网络", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (obj.contains("#")) {
                        String campu = obj.split("#")[0];
                        String room = obj.split("#")[1];

                        switch (room.length())
                        {
                            case 1:
                                room = "00" + room;
                                break;

                            case 2:
                                room = "0" + room;
                                break;

                            default:
                                break;
                        }

                        if(Integer.parseInt(campu) <= 8) {
                            if(Integer.parseInt(campu) == 8)
                                campu = "7";
                            onekeyAppoResultTextView.setText(huangRooms[Integer.parseInt(campu)] + "第" + room + "号座位");
                        } else {
                            if(Integer.parseInt(campu) == 11)
                                campu = "12";
                            onekeyAppoResultTextView.setText(qingRooms[Integer.parseInt(campu) - 11] + "第" + room + "号座位");
                        }

                        viewPager.setCurrentItem(0);
                        new GetAppoInfo(GlobalVar.userid).execute();
                        Toast.makeText(LibSeatActivity.this, "预约成功", Toast.LENGTH_SHORT).show();
                        isRefreshOnewkey = false;
                        return;
                    }
                    onekeyAppoResultTextView.setText(resultsMap.get(obj));
                    Toast.makeText(getApplicationContext(), resultsMap.get(obj), Toast.LENGTH_SHORT).show();
                }
                break;

                case 3:	// 信息中心
                {
                    if (appoCenterLists.size() == 0)
                        Toast.makeText(LibSeatActivity.this, "无信息", Toast.LENGTH_SHORT).show();
                    else
                        appoCenterListView.setAdapter(new AppoCenterListAdapter(LibSeatActivity.this, appoCenterLists));
                    appoCenterListView.stopRefresh();
                }
                break;

                case 4:	// 取消预约
                {
                    String obj = (String) msg.obj;
                    if (TextUtils.isEmpty(obj))
                    {
                        Toast.makeText(LibSeatActivity.this, "网络错误，请检查网络", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Toast.makeText(LibSeatActivity.this, resultsMap.get(obj), Toast.LENGTH_SHORT).show();
                    }
                }
                break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_seat);
        initView();
    }

    private void initView()
    {
        resultsMap = new HashMap<String, String>();
        resultsMap.put("slow", "Sorry，下手太慢");
        resultsMap.put("had occupied", "Sorry，你正在使用一个座位");
        resultsMap.put("had appointed", "Sorry，你已经预约了一个座位");
        resultsMap.put("cancel success", "取消预约成功");
        resultsMap.put("cancel failed", "取消预约失败");
        resultsMap.put("success", "恭喜，预约成功");
        resultsMap.put("unavailable", "此阅览室暂时不可用");

        isRefreshOnewkey = false;
        isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_scan:
                        Intent intent = new Intent(LibSeatActivity.this, CaptureActivity.class);
                        startActivityForResult(intent,0);
                        break;
                }
                return true;
            }
        });

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        LayoutInflater lf = LayoutInflater.from(this);
        view1 = lf.inflate(R.layout.view_appointment_center, null);
        view2 = lf.inflate(R.layout.view_onekey_appointment, null);
        view3 = lf.inflate(R.layout.view_advanced_appointment, null);

        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);

        titleList = new ArrayList<String>();// 每个页面的Title数据
        titleList.add("信息中心");
        titleList.add("一键预约");
        titleList.add("高级预约");

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mTabLayout.addTab(mTabLayout.newTab().setText(titleList.get(0)));//添加tab选项卡
        mTabLayout.addTab(mTabLayout.newTab().setText(titleList.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titleList.get(2)));

        MyPagerAdapter mAdapter = new MyPagerAdapter(viewList);
        viewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(viewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
        viewPager.setCurrentItem(1);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageSelected(int arg0)
            {
                switch (arg0)
                {
                    case 0:
                        new GetAppoInfo(GlobalVar.userid).execute();
                        break;
                    case 1:
                        if(isRefreshOnewkey)
                            onekeyAppoResultTextView.setText("");
                        break;
                    case 2:
                        if(campusSpinner.getSelectedItem().toString().equals("黄家湖校区")) {
                            if(!(roomSpinner.getSelectedItem().toString().equals("请选择")))
                                new GetDataTask(huangRoomsId[roomSpinner.getSelectedItemPosition()]).execute();
                        } else {
                            if(!(roomSpinner.getSelectedItem().toString().equals("请选择")))
                                new GetDataTask(qingRoomsId[roomSpinner.getSelectedItemPosition()]).execute();
                        }

                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {

            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {

            }
        });

        if (isAdmin) {
            //view4 = findViewById(R.layout.view_lib_seat_admin);
            view4 = lf.inflate(R.layout.view_lib_seat_admin, null);
            viewList.add(view4);
            titleList.add("志愿者管理");

            initAdmin();
        }

        initOneKeyAppo();
        initAdvancedAppo();
        initAppoCenter();
    }

    /**
     * 志愿者管理
     */
    private void initAdmin() {
        LinearLayout adminBar = (LinearLayout) view4.findViewById(R.id.ll_lib_seat_admin);
        adminBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//				Intent intent = new Intent(LibSeatActivity.this, CaptureActivity.class);
//				intent.putExtra("isAdmin", isAdmin);
//				startActivity(intent);
            }
        });
    }

    /**
     * 信息中心
     */
    private void initAppoCenter()
    {
        appoCenterListView = (MyListView) view1.findViewById(R.id.lv_appoint_center);
        appoCenterListView.setPullLoadEnable(false);
        new GetAppoInfo(GlobalVar.userid).execute();

        appoCenterListView.setXListViewListener(new MyListView.IXListViewListener()
        {
            @Override
            public void onRefresh()
            {
                new GetAppoInfo(GlobalVar.userid).execute();
            }

            @Override
            public void onLoadMore()
            {

            }
        });

        appoCenterListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
            {
                if (position == 1) {
                    final AppoCenterItem item = appoCenterLists.get(appoCenterLists.size() - position);
                    if (item.getOldAction().equals("1"))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LibSeatActivity.this);
                        builder.setTitle("取消预约");
                        builder.setMessage("是否取消预约" + item.getRoom() + "第" + item.getSeat()
                                + "号座位\n");
                        builder.setPositiveButton("取消预约", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Time time = new Time();
                                    time.set(yearFormat.parse(item.getTime()).getTime());

                                    int hour = time.hour;

                                    int pot = 0;
                                    if (hour >= 0 && hour < 11)
                                        pot = 1;
                                    else if (hour >= 11 && hour < 13)
                                        pot = 2;
                                    else if (hour >= 13 && hour < 16)
                                        pot = 3;
                                    else if (hour >= 16 && hour < 18)
                                        pot = 4;
                                    else
                                        pot = 5;


                                    new GetDataTask(item.getOldRoom(), item.getOldSeat(), GlobalVar.userid, pot).execute();
                                    new GetAppoInfo(GlobalVar.userid).execute();
                                } catch (Exception exception) {
                                    Toast.makeText(LibSeatActivity.this, "Sorry~出现未知错误", Toast.LENGTH_SHORT).show();
                                }
                            };
                        }).setNegativeButton("保留预约", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    } else if (item.getOldAction().equals("2") || item.getOldAction().equals("8")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LibSeatActivity.this);
                        builder.setTitle("当前座位正在使用中");
                        builder.setMessage("是否更改当前作为状态为");
                        builder.setPositiveButton("释放座位", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UpdateSeatStatus(scanRoom, scanSeat, 1);

                            }
                        }).setNegativeButton("暂时离开", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UpdateSeatStatus(scanRoom, scanSeat, 0);
                            }
                        });
                        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                }
                return true;
            }
        });

        appoCenterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppoCenterItem item = appoCenterLists.get(appoCenterLists.size() - position);
                if (position == 1 && (item.getOldAction().equals("1") || item.getOldAction().equals("2") || item.getOldAction().equals("6")|| item.getOldAction().equals("7") ||item.getOldAction().equals("8"))) {
                    Intent intent = new Intent(LibSeatActivity.this, CaptureActivity.class);
//					intent.putExtra("isAdmin", isAdmin);
                    startActivityForResult(intent,0);
                }
            }
        });
    }

    /**
     * 高级预约
     */
    private void initAdvancedAppo()
    {
        ArrayAdapter<String> campusAdapter;
        campusSpinner = (Spinner) view3.findViewById(R.id.spinner_campus);
        roomSpinner = (Spinner) view3.findViewById(R.id.spinner_room);
        resultTextView = (TextView) view3.findViewById(R.id.tv_query_result);
        seatGridView = (GridView) view3.findViewById(R.id.gv_empty_seat);
        animation = AnimationUtils.loadAnimation(this, R.anim.update_circle_rotate);
        adUpdate = (ImageButton) view3.findViewById(R.id.ibtn_update);

        campusAdapter = new ArrayAdapter<String>(
                LibSeatActivity.this, android.R.layout.simple_spinner_item, campus);
        campusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campusSpinner.setAdapter(campusAdapter);

        adUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnim();
                int position = roomSpinner.getSelectedItemPosition();
                if (position == 0) {
                    stopAnim();
                    return ;
                }
                String campus = campusId[campusSpinner.getSelectedItemPosition()];
                String room = null;
                if (campus.equals("1"))
                    room = huangRoomsId[position];
                else
                    room = qingRoomsId[position];
                new GetDataTask(room).execute();

            }
        });



        campusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id)
            {
                stopAnim();
                if (position == 0) {
                    roomAdapter = new ArrayAdapter<String>(LibSeatActivity.this,
                            android.R.layout.simple_spinner_item, huangRooms);
                } else {
                    roomAdapter = new ArrayAdapter<String>(LibSeatActivity.this,
                            android.R.layout.simple_spinner_item, qingRooms);
//					seatGridView.setVisibility(View.INVISIBLE);
//					resultTextView.setText("青山校区暂时不可用");
                }
                resultTextView.setText("");
                roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                roomSpinner.setAdapter(roomAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        roomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                stopAnim();
                String campus = campusId[campusSpinner.getSelectedItemPosition()];
                String room = null;
                if (campus.equals("1"))
                    room = huangRoomsId[position];
                else
                    room = qingRoomsId[position];

                if (position == 0) {
                    resultTextView.setText("请选择阅览室");
                    seatGridView.setVisibility(View.INVISIBLE);
                    return ;
                }
                new GetDataTask(room).execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        seatGridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String campus = campusId[campusSpinner.getSelectedItemPosition()];
                String room = null;
                if (campus.equals("1"))
                    room = huangRoomsId[roomSpinner.getSelectedItemPosition()];
                else
                    room = qingRoomsId[roomSpinner.getSelectedItemPosition()];
                String seat = lists.get(position);
                showAppointDialog(campus, room, seat);
            }
        });
    }

    /**
     * 初始化一键预约组件
     */
    private void initOneKeyAppo()
    {
        TextView messageTextView = (TextView) view2.findViewById(R.id.tv_message_tip);
        String messageText = messageTextView.getText().toString();
        int start = messageText.indexOf("高级预约");
        int end = start + "高级预约".length();
        SpannableStringBuilder builder = new SpannableStringBuilder(messageText);
        ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.BLUE);
        builder.setSpan(blueSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        messageTextView.setText(builder);

        messageTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                viewPager.setCurrentItem(2);
            }
        });

        onekeyAppoResultTextView = (TextView) view2.findViewById(R.id.tv_appo_result);
        onekeyAppoButton = (Button) view2.findViewById(R.id.btn_onekey_appoint);
//		final String[] huangRooms = {
//				"南三(社会科学阅览室)", "南四(工程技术阅览室)", "南五(基础科学阅览室)", "南六(政法经管阅览室)",
//				"北三(建筑,艺术阅览室)", "北四(生物,医药阅览室)", "期刊(阅览室)" };
//		final String[] huangRoomsId = {
//				"1", "2", "3", "4",
//				"5", "6", "8" };
        campusSpinner1 = (Spinner) view2.findViewById(R.id.spinner_campus1);
        roomSpinner1 = (Spinner) view2.findViewById(R.id.spinner_room1);

        ArrayAdapter<String> campusAdapter = new ArrayAdapter<String>(
                LibSeatActivity.this, android.R.layout.simple_spinner_item, campus);
        campusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campusSpinner1.setAdapter(campusAdapter);

        campusSpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id)
            {
                if (position == 0)
                {
                    roomAdapter = new ArrayAdapter<String>(LibSeatActivity.this,
                            android.R.layout.simple_spinner_item, huangRooms);
                }
                else
                {
                    roomAdapter = new ArrayAdapter<String>(LibSeatActivity.this,
                            android.R.layout.simple_spinner_item, qingRooms);
                    //onekeyAppoResultTextView.setText("青山校区暂时不可用");

                }
                onekeyAppoResultTextView.setText("");
                roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                roomSpinner1.setAdapter(roomAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        onekeyAppoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(roomSpinner1.getSelectedItem().toString().equals("请选择")) {
                    onekeyAppoResultTextView.setText("请选择阅览室");
                    return;
                }

                String room;
                if(campusSpinner1.getSelectedItemPosition() == 0)
                    room = huangRoomsId[roomSpinner1.getSelectedItemPosition()];
                else
                    room = qingRoomsId[roomSpinner1.getSelectedItemPosition()];
//				if (campus.equals("0"))
//				{
//					onekeyAppoResultTextView.setText("青山校区暂时不可用");
//					return;
//				}
                new GetDataTask(room, GlobalVar.userid).execute();
            }
        });

    }

    protected void showAppointDialog(final String campus, final String room, final String seat)
    {
        String seatString = seat.substring(0, seat.length());
        switch (seatString.length())
        {
            case 1:
                seatString = "00" + seatString;
                break;
            case 2:
                seatString = "0" + seatString;
                break;
            default:
                break;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("座位预约");
        builder.setMessage("是否预约" + campusSpinner.getSelectedItem().toString() + roomSpinner.getSelectedItem().toString()
                + "第" + seatString + "号座位\n");
        builder.setPositiveButton("立即预约", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new GetDataTask(room, seat, GlobalVar.userid).execute();
            };
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void startAnim() {
        adUpdate.startAnimation(animation);
    }

    private void stopAnim() {
        adUpdate.clearAnimation();
    }

    /**
     * @author Yorek
     * type = 1 查询座位, type = 0 预约座位, type = 2 一键预约, type = 4 取消预约
     */
    private class GetDataTask extends AsyncTask<Void, Void, String>
    {
        private String room;
        private String seat;
        private String stuNum;
        private int type;
        private int pot;

        public GetDataTask(String roomId)
        {
            room = roomId;
            type = 1;
        }

        public GetDataTask(String roomId, String stuNum)
        {
            room = roomId;
            this.stuNum = stuNum;
            type = 2;
        }

        public GetDataTask(String roomId, String seatId, String stuNum)
        {
            room = roomId;
            seat = seatId;
            this.stuNum = stuNum;
            this.type = 0;
        }

        public GetDataTask(String roomId, String seatId, String stuNum, int pot)
        {
            room = roomId;
            seat = seatId;
            this.stuNum = stuNum;
            this.type = 4;
            this.pot = pot;
        }

        @Override
        protected String doInBackground(Void... params)
        {
            String result = null;
            switch (type)
            {
                case 0:
                    result = doAppoint();
                    break;

                case 1:
                    result = getEmptySeat();
                    break;

                case 2:
                    result = onekeyAppo();
                    break;

                case 4:
                    result = cancelAppo();
                    break;

                default:
                    result = null;
                    break;
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result)
        {

            Message message = Message.obtain();
            message.what = type;
            message.obj = result;
            handler.sendMessage(message);

            super.onPostExecute(result);

        }

        //空闲座位查询
        private String getEmptySeat()
        {
            String list = null;

            try
            {
                URL url = new URL(URL_UNIVERSAL.URL_EMPTY_SEAT + "?room=" + room);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(3000);
                int responseCode = connection.getResponseCode();

                if (responseCode == 200)
                {
                    InputStream is = connection.getInputStream();
                    String result = StreamTools.readFromStream(is);


                    JSONObject jsonObject = new JSONObject(result);

                    String rest = jsonObject.optString("rest");
                    String total = jsonObject.optString("total");
                    //String seats = jsonObject.optString("lists");
                    emptySeatsJSONArray = jsonObject.getJSONArray("lists");

                    list = rest + "#" + total;

                    return list;
                }
            }
            catch (Exception exception)
            {

            }

            return list;
        }

        //高级预约
        private String doAppoint()
        {
            String list = null;
            try
            {
                URL url = new URL(URL_UNIVERSAL.URL_ADVANCED_RESERVE + "?room=" + room + "&seatid=" + seat + "&stunum=" + stuNum);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(3000);
                int responseCode = connection.getResponseCode();

                if (responseCode == 200)
                {
                    InputStream is = connection.getInputStream();
                    String result = StreamTools.readFromStream(is);


                    JSONObject jsonObject = new JSONObject(result);

                    String queryResult = jsonObject.optString("result");

                    Log.i("reserve status", "高级预约：" + queryResult);
                    return queryResult;
                }
            }
            catch (Exception exception) {

            }

            return list;
        }

        //一键预约
        private String onekeyAppo()
        {
            String list = null;

            try
            {
                URL url = new URL(URL_UNIVERSAL.URL_ONEKEY_RESERVE + "?room=" + room + "&stunum=" + stuNum);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(3000);
                int responseCode = connection.getResponseCode();

                if (responseCode == 200)
                {
                    InputStream is = connection.getInputStream();
                    String result = StreamTools.readFromStream(is);


                    JSONObject jsonObject = new JSONObject(result);

                    String queryResult = jsonObject.optString("result");

                    Log.i("reserve status", "一键预约：" + queryResult);
                    return queryResult;
                }
            }
            catch (Exception exception)
            {

            }

            return list;
        }

        //取消预约
        private String cancelAppo()
        {
            String list = null;
            try
            {
                URL url = new URL(URL_UNIVERSAL.URL_CANCEL_RESERVE + "?&room=" + room + "&seatid=" + seat + "&stunum=" + stuNum + "&type=" + pot);

                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(3000);
                int responseCode = connection.getResponseCode();

                if (responseCode == 200)
                {
                    InputStream is = connection.getInputStream();
                    String result = StreamTools.readFromStream(is);


                    JSONObject jsonObject = new JSONObject(result);

                    String queryResult = jsonObject.optString("result");
                    Log.i("reserve status", "取消预约：" + queryResult);
                    return queryResult;
                }
            }
            catch (Exception exception)
            {

            }

            return list;
        }
    }

    private class GetAppoInfo extends AsyncTask<Void, Void, List<AppoCenterItem>>
    {
        private String stuNum;

        public GetAppoInfo(String stuNum)
        {
            this.stuNum = stuNum;
        }

        @Override
        protected List<AppoCenterItem> doInBackground(Void... params)
        {
            List<AppoCenterItem> result = getAppoInfo();

            return result;
        }

        @Override
        protected void onPostExecute(List<AppoCenterItem> list)
        {
            Message message = Message.obtain();
            message.what = 3;
            appoCenterLists = list;
            handler.sendMessage(message);

            super.onPostExecute(list);

        }

        //信息中心
        private List<AppoCenterItem> getAppoInfo()
        {
            List<AppoCenterItem> list = new ArrayList<AppoCenterItem>();

            try
            {
                URL url = new URL(URL_UNIVERSAL.URL_INFORMATION_CENTER + "?stunum=" + stuNum);
                HttpURLConnection connection = (HttpURLConnection) url
                        .openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(3000);
                int responseCode = connection.getResponseCode();

                if (responseCode == 200)
                {
                    InputStream is = connection.getInputStream();
                    String result = StreamTools.readFromStream(is);

                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.optJSONObject(i);
                        AppoCenterItem appoCenterItem = new AppoCenterItem();
                        appoCenterItem.setRoom(jsonObject.optString("room"));
                        appoCenterItem.setSeat(jsonObject.optString("seatid"));
                        appoCenterItem.setAction(jsonObject.optString("action"));
                        appoCenterItem.setTime(jsonObject.optString("time"));
                        appoCenterItem.setClickable(jsonObject.optString("type").equals("0") ? true : false);
                        list.add(appoCenterItem);
                        if(i == jsonArray.length()-1) {
                            if(jsonObject.optString("action").equals("2") || jsonObject.optString("action").equals("8")) {
                                scanRoom = jsonObject.optString("room");
                                scanSeat = jsonObject.optString("seatid");

                            }

                            if(jsonObject.optString("action").equals("1") || jsonObject.optString("action").equals("2") ||jsonObject.optString("action").equals("6") ||jsonObject.optString("action").equals("7") ||jsonObject.optString("action").equals("8"))
                                list.add(appoCenterItem);

                            if(!(jsonObject.optString("action").equals("1")))
                                isRefreshOnewkey = true;

                        }
                    }
                }
            }
            catch (Exception exception) {
            }

            return list;
        }
    }

    //处理扫描二维码返回的字符串
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==RESULT_OK){
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result=bundle.getString("result");	//扫描二维码后返回的字符串
                Log.i("扫描二维码返回串", result);
                //返回格式:http://202.114.242.198:8090/smewm.php?room=12&seatid=1
                String[] param = result.split("=");
                //根据=号分割字符串，第二个分割串再根据&分割后的第一个字符串、第三个分割串分别是roomid和seatid
                final String room = param[1].split("&")[0];
                final String seat = param[2];

                String URL = URL_UNIVERSAL.URL_SCAN_QR_CODE + "?room=" + room + "&seatid=" + seat + "&stunum=" + GlobalVar.userid + "&admin=0";
                JSONObject jsonObject = new JSONObject();
                OkHttpUtils.postString().url(URL).content(jsonObject.toString())
                        .mediaType(MediaType.parse("application/json")).build()
                        .execute(new StringCallback()
                        {
                            @Override
                            public void onError(Call call, Exception e) {
                                Log.i("图书馆扫描二维码接口访问失败", call.toString() + "--" + e.toString());
                                Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response) {
                                Log.i("图书馆扫描二维码接口访问成功", response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String result = jsonObject.getString("result");
                                    switch(result) {
                                        case "room unavailable":
                                            Toast.makeText(getApplicationContext(), "阅览室不可用", Toast.LENGTH_SHORT).show();
                                            break;
                                        case "seat unavailable":
                                            Toast.makeText(getApplicationContext(), "座位不可用", Toast.LENGTH_SHORT).show();
                                            break;
                                        case "using by other":
                                            Toast.makeText(getApplicationContext(), "已被他人占用", Toast.LENGTH_SHORT).show();
                                            break;
                                        case "error":
                                            Toast.makeText(getApplicationContext(), "找不到记录", Toast.LENGTH_SHORT).show();
                                            break;
                                        case "had occupied":
                                            Toast.makeText(getApplicationContext(), "该学号已占用一个座位", Toast.LENGTH_SHORT).show();
                                            break;
                                        case "had appointed":
                                            Toast.makeText(getApplicationContext(), "该学号已预约一个座位", Toast.LENGTH_SHORT).show();
                                            break;
                                        case "occupy failed":
                                            Toast.makeText(getApplicationContext(), "占用失败", Toast.LENGTH_SHORT).show();
                                            break;
                                        case "occupy success":
                                            scanRoom = room;
                                            scanSeat = seat;
                                            new GetAppoInfo(GlobalVar.userid).execute();
                                            Toast.makeText(getApplicationContext(), "占用成功", Toast.LENGTH_SHORT).show();
                                            break;
                                        case "remark success":
                                            Toast.makeText(getApplicationContext(), "标记成功", Toast.LENGTH_SHORT).show();
                                            break;
                                        case "had remark":
                                            Toast.makeText(getApplicationContext(), "该座位处于标记状态", Toast.LENGTH_SHORT).show();
                                            break;
                                        case "occupied":
                                            AlertDialog.Builder builder = new AlertDialog.Builder(LibSeatActivity.this);
                                            builder.setTitle("当前座位正在使用中");
                                            builder.setMessage("是否更改当前作为状态为");
                                            builder.setPositiveButton("释放座位", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    UpdateSeatStatus(room, seat, 1);

                                                };
                                            }).setNegativeButton("暂时离开", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    UpdateSeatStatus(room, seat, 0);
                                                }
                                            });
                                            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                @Override
                                                public void onCancel(DialogInterface dialog) {
                                                    dialog.dismiss();
                                                }
                                            });
                                            builder.show();
                                            break;
                                        case "The seat has occupied":
                                            Toast.makeText(getApplicationContext(), "该座位正在被其他人使用", Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }
        }

    }

    //暂时离开和释放座位
    private void UpdateSeatStatus(final String room, String seat, int mark) {
        String URL = URL_UNIVERSAL.URL_LEAVE_RELEASE + "?room=" + room + "&seatid=" + seat + "&stunum=" + GlobalVar.userid + "&mark=" + mark;
        JSONObject jsonObject = new JSONObject();
        OkHttpUtils.postString().url(URL).content(jsonObject.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback()
                {

                    @Override
                    public void onError(Call call, Exception e) {
                        Log.i("暂时离开立即释放接口访问失败", call.toString() + "--" + e.toString());
                        Toast.makeText(getApplicationContext(), "网络异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.i("暂时离开立即释放接口访问成功", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String result = jsonObject.getString("result");
                            switch (result) {
                                case "leaving too often":
                                    Toast.makeText(getApplicationContext(), "频繁操作，距离上次离开时间小于20分钟", Toast.LENGTH_SHORT).show();
                                    break;
                                case "leaving success":
                                    Toast.makeText(getApplicationContext(), "暂时离开成功", Toast.LENGTH_SHORT).show();
                                    new GetAppoInfo(GlobalVar.userid).execute();
                                    break;
                                case "had leaving":
                                    Toast.makeText(getApplicationContext(), "已经离开当前座位", Toast.LENGTH_SHORT).show();
                                    break;
                                case "leaving fail":
                                    Toast.makeText(getApplicationContext(), "离开失败", Toast.LENGTH_SHORT).show();
                                    break;
                                case "error":
                                    Toast.makeText(getApplicationContext(), "找不到记录", Toast.LENGTH_SHORT).show();
                                    break;
                                case "using by others":
                                    Toast.makeText(getApplicationContext(), "座位已被他人使用", Toast.LENGTH_SHORT).show();
                                    break;
                                case "release seat":
                                    Toast.makeText(getApplicationContext(), "下线成功", Toast.LENGTH_SHORT).show();
                                    new GetAppoInfo(GlobalVar.userid).execute();
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_tool_bar, menu);
        return true;
    }

    class MyPagerAdapter extends PagerAdapter {
        private List<View> mViewList;

        public MyPagerAdapter(List<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public int getCount() {
            return mViewList.size();//页卡数
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;//官方推荐写法
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mViewList.get(position));//添加页卡
            return mViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViewList.get(position));//删除页卡
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);//页卡标题
        }

    }
}

