package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.utils.GlobalVar;
import com.wust.newsmartschool.utils.HttpServer;

import org.json.JSONArray;
import org.json.JSONObject;


public class CostDetailActivity extends Activity {

    // title组件
    private TextView title;
    private ImageView backImageView;
    // 功能组件
    private TextView idTextView;
    private TextView termTextView;
    private TextView statusTextView;

    private Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    idTextView.setText(msg.obj.toString());
                    break;
                case 2:
                    termTextView.setText(msg.obj.toString());
                    break;
                case 3:
                    statusTextView.setText(msg.obj.toString());
                    break;
                case 4:
                    Toast.makeText(CostDetailActivity.this, "网速不给力", Toast.LENGTH_LONG).show();
                    break;
                case 5:
                    idTextView.setText(GlobalVar.userid);
                    statusTextView.setText("缴费正常，无欠费");
                    Toast.makeText(CostDetailActivity.this, "加载完成", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }

    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_cost_detail);
        InitView();
        setCostStatus();
    }

    private void InitView() {
        // TODO Auto-generated method stub
        title = (TextView) findViewById(R.id.head_title);
        title.setText("学生欠费明细");
        backImageView = (ImageView) findViewById(R.id.goback);
        backImageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CostDetailActivity.this.finish();
            }
        });
        // 功能组件
        idTextView = (TextView) findViewById(R.id.cost_detail_id);
        termTextView = (TextView) findViewById(R.id.cost_detail_term);
        statusTextView = (TextView) findViewById(R.id.cost_detail_status);
    }

    /**
     * 连接网络，获得对应学号的欠费情况的操作
     */
    private void setCostStatus() {
        Runnable runnable = new Runnable() {
            public void run() {
                HttpServer httpServer = new HttpServer();
                String urlString = "http://202.114.242.198:8090/Qfmx.php?XH="
                        +GlobalVar.userid;
                String jsonString = httpServer.getData(urlString);
                if (jsonString != null) {
                    // 解析JSON数据并赋值到组件
                    try {
                        JSONArray jsonArray = new JSONArray(jsonString);

                        JSONObject jsonObject = jsonArray.optJSONObject(0);
                        if(jsonObject==null){
                            //网络访问成功，但是为空返回值，这个时候也表示“缴费正常”
                            myHandler.sendEmptyMessage(5);
                        }else{
                            myHandler.sendMessage(myHandler.obtainMessage(1, jsonObject.optString("XH")));
                            myHandler.sendMessage(myHandler.obtainMessage(2, jsonObject.optString("XN")));
                            myHandler.sendMessage(myHandler.obtainMessage(3, jsonObject.optString("JFZT")));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // 返回数据为空，表示无欠费。为默认显示
                    myHandler.sendEmptyMessage(4);
                }
            }
        };
        new Thread(runnable).start();
    }
}

