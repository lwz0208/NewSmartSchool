package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.Psy_RecordListViewAdapter;
import com.wust.newsmartschool.domain.PsyRecordItem;
import com.wust.newsmartschool.utils.GlobalVar;
import com.wust.newsmartschool.utils.HttpServer;

import java.util.ArrayList;


/*我的预约
 * */
public class Psy_MyAppointmentActivity extends Activity {
    // 标题组件
    private TextView head;
    private ImageView back;
    // 预约状态组件
    private TextView expertNameTextView;// 咨询师姓名
    private TextView timeTextView;// 预约时间
    private TextView stateTextView;// 预约状态
    private Button cancelButton;// 取消按钮
    // 预约记录组件
    private ListView myListView;// 预约记录列表
    private ProgressBar progressBar;
    private Psy_RecordListViewAdapter myAdapter;
    private ArrayList<PsyRecordItem> dataArrayList = new ArrayList<PsyRecordItem>();
    //常量
    private static final int DELETE_OK=1;//删除成功
    private static final int DELETE_FALSE=2;//删除失败
    private static final int DELETE_ERROR=3;//网速不好

    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    myAdapter = new Psy_RecordListViewAdapter(
                            Psy_MyAppointmentActivity.this, dataArrayList);
                    myListView.setAdapter(myAdapter);
                    myListView.setClickable(false);
                    progressBar.setVisibility(View.GONE);
                    // 同时刷新“我的预约中的数据显示”
                    for (int i = (dataArrayList.size()-1); i >=0 ; i--) {
                        if (dataArrayList.get(i).getStatus().equals("0")
                                || dataArrayList.get(i).getStatus().equals("1")
                                || dataArrayList.get(i).getStatus().equals("2")) {// 已经提交的预约
                            GlobalVar.psy_name = dataArrayList.get(i)
                                    .getExpertName();
                            GlobalVar.psy_date_time = dataArrayList.get(i)
                                    .getDate()
                                    + (dataArrayList.get(i).getTime().equals("1") ? "上午"
                                    : "下午");
                            GlobalVar.psy_status = dataArrayList.get(i).getStatus()
                                    .equals("0") ? "已提交，请等待" : (dataArrayList
                                    .get(i).getStatus().equals("1") ? "预约成功，请按时赴约"
                                    : "预约成功");
                            setMyMSG();
                            break;
                        }else{
                            GlobalVar.psy_name="无";
                            GlobalVar.psy_date_time="无预约";
                            GlobalVar.psy_status="无";
                            setMyMSG();
                        }

                    }
                    break;
                case DELETE_OK://删除预约成功
                    GlobalVar.psy_name="无";
                    GlobalVar.psy_date_time="无预约";
                    GlobalVar.psy_status="无";
                    setMyMSG();
                    break;
                case DELETE_FALSE://删除预约失败
                    Toast.makeText(Psy_MyAppointmentActivity.this, "删除失败，若数次不行，请联系咨询师", Toast.LENGTH_LONG).show();
                    break;
                case DELETE_ERROR://网速不好等
                    Toast.makeText(Psy_MyAppointmentActivity.this,
                            "网速不好，请检查网络！", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_psy__my_appointment);
        initView();
        getRecodeList();
        // 注册事件监听
        cancelButton.setOnClickListener(new View.OnClickListener() {// 取消预约
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // 需要一个接口去删除（或更改）apponitment表中该条记录
                AlertDialog.Builder builder = new AlertDialog.Builder(Psy_MyAppointmentActivity.this);
                builder.setMessage("确认取消预约吗?");
                builder.setTitle("心理咨询中心");
                builder.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//										SysApplication.getInstance().exit();
                                deleteMyAppointment();
                                dialog.dismiss();
                            }
                        });
                builder.create().show();
            }
        });
    }

    // 获得组件
    private void initView() {
        // TODO Auto-generated method stub
        // 标题
        head = (TextView) findViewById(R.id.head_title);
        back = (ImageView) findViewById(R.id.goback);
        head.setText("我的预约");
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Psy_MyAppointmentActivity.this.finish();
            }
        });
        // 预约状态组件
        expertNameTextView = (TextView) findViewById(R.id.psy_expertname);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        timeTextView = (TextView) findViewById(R.id.psy_time);
        stateTextView = (TextView) findViewById(R.id.psy_state);
        cancelButton = (Button) findViewById(R.id.psy_myapp_cancel_bt);
        // 预约记录组件
        myListView = (ListView) findViewById(R.id.psy_myapp_historylist);
        setMyMSG();
    }
    //用全局变量的数据刷新“我的预约”中的显示
    private void setMyMSG(){
        expertNameTextView.setText(GlobalVar.psy_name);
        timeTextView.setText(GlobalVar.psy_date_time);
        stateTextView.setText(GlobalVar.psy_status);
    }
    private void getRecodeList() {
        Runnable runnable = new Runnable() {
            public void run() {
                HttpServer httpServer = new HttpServer();
                String urlString = "http://202.114.242.198:8090/appointment.php?uid="
                        + GlobalVar.userid;
                String jsonString = httpServer.getData(urlString);
                if (jsonString != null) {
                    dataArrayList = httpServer.getRecodeList(jsonString);
                    myHandler.sendEmptyMessage(100);
                }
            }
        };
        new Thread(runnable).start();
    }
    private void deleteMyAppointment(){
        Runnable runnable = new Runnable() {
            public void run() {
                HttpServer httpServer = new HttpServer();
                String urlString = "http://202.114.242.198:8090/cancelappointment.php?uid="
                        + GlobalVar.userid;
                String jsonString = httpServer.getData(urlString);
                if(jsonString!=null){
                    if(jsonString.equals("1")){//删除成功
                        myHandler.sendEmptyMessage(DELETE_OK);
                    }else if(jsonString.equals("0")){//删除失败
                        myHandler.sendEmptyMessage(DELETE_FALSE);
                    }
                }else{//网速不好
                    myHandler.sendEmptyMessage(DELETE_ERROR);
                }
            }
        };
        new Thread(runnable).start();
    }
}

