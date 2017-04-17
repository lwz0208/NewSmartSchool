package com.wust.newsmartschool.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.wust.easeui.Constant;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.DepartOnline_Adapter;
import com.wust.newsmartschool.views.ECProgressDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by Ren on 17/4/11.
 */

public class DepartOnlineActivity extends BaseActivity {

    private ExpandableListView listview;
    private DepartOnline_Adapter madapter;
    private ECProgressDialog pd;
    JSONArray jsarr;

    private List<String> listDataHeader = new ArrayList<>();
    private List<String> listDataChild = new ArrayList<>();
    ProgressDialog progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departonline);
        listview = (ExpandableListView) findViewById(R.id.depart_listView);

        progressBar = ProgressDialog.show(DepartOnlineActivity.this, null, "请稍后…");

    }

    public void back(View view) {
        finish();
        if (android.os.Build.VERSION.SDK_INT > 5) {
            overridePendingTransition(
                    R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        //getList();

        getonlineList();

        progressBar.dismiss();
    }

    public void getList() {

        listDataHeader = new ArrayList<>();
        listDataChild = new ArrayList<>();
        listDataHeader.add("课程名称:" + "上课班级：" + "总评分1：");
        listDataHeader.add("课程名称:" + "上课班级：" + "总评分2：");

        listDataHeader.add("课程名称:" + "上课班级：" + "总评分3：");

        listDataHeader.add("课程名称:" + "上课班级：" + "总评分4：");


        listDataChild.add("总评分1：");
        listDataChild.add("总评分2：");

        listDataChild.add("总评分3：");

        listDataChild.add("总评分4：");

        madapter = new com.wust.newsmartschool.adapter.DepartOnline_Adapter(DepartOnlineActivity.this, listDataHeader, listDataChild, jsarr);
        listview.setAdapter(madapter);


        listview.setGroupIndicator(null);
        listview.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //  listview.collapseGroup(0);
                for (int i = 0; i < listDataHeader.size(); i++) {
                    if (groupPosition != i) {
                        listview.collapseGroup(i);
                    }
                }
            }


        });


    }

    public void getonlineList() {
        OkHttpUtils.postString().url(Constant.DEPARTONLINE_URL).content("")
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.i("DepartOnlineActivity", "部门列表访问失败：" + call + "---" + e);
                        Toast.makeText(DepartOnlineActivity.this, "网速不给力" + e.toString(),
                                Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        listDataHeader.clear();
                        listDataChild.clear();
                        try {
                            JSONObject jObject = JSON.parseObject(response);//json字符串转换成jsonobject对象
                            System.out.println(jObject);
                            jsarr = jObject.getJSONArray("data");//jsonobject对象取得data对应的jsonarray数组
                            System.out.println(jsarr);
                            for (int i = 0; i < jsarr.size(); i++) {
                                JSONObject job = jsarr.getJSONObject(i);
                                {
                                    //获取数组中对象的对象
                                    listDataHeader.add(job.getString("departmentname"));
                                    listDataChild.add(job.getString("content").replaceAll("<br>", "\n"));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        madapter = new com.wust.newsmartschool.adapter.DepartOnline_Adapter(DepartOnlineActivity.this, listDataHeader, listDataChild, jsarr);
                        listview.setAdapter(madapter);
                        listview.setGroupIndicator(null);
                        listview.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                            @Override
                            public void onGroupExpand(int groupPosition) {
                                //  listview.collapseGroup(0);
                                for (int i = 0; i < listDataHeader.size(); i++) {
                                    if (groupPosition != i) {
                                        listview.collapseGroup(i);
                                    }
                                }

                            }
                        });

                    }

                });

    }
}
