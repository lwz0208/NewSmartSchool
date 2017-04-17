package com.wust.newsmartschool.ui;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wust.easeui.Constant;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.utils.WebServiceUtils;
import com.wust.newsmartschool.utils.appUseUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

public class HaveSelectActivity extends Activity {
    public static HaveSelectActivity haveSelectctivity;


    private String url="http://202.114.255.100/Xsda/SelectCourse/get_selected_course.php";//获取已选课程列表
    private ExpandableListView expandableListView_one;
    private List<Map<String, String>> listDataHeader =new ArrayList<Map<String, String>>();;
    private List<String> listDataChild=new ArrayList<>();;
    private ExpandableListAdapter madapter;
    //获取已选课程列表 需要的参数
    private String xnxq;
    private String xkxz;
    //private List<CourseItem> coursrList;
    Bundle bundle;
    Handler handler;
    public JSONArray returnid=new JSONArray();
    String result1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_have_select);
        expandableListView_one= (ExpandableListView) findViewById(R.id.expand_list);

        bundle=getIntent().getExtras();


        haveSelectctivity=this;

    }
    @Override
    public void onStart() {
        super.onStart();
       // getList();
        //getListOne();
        getyxkclb();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    // System.out.println(msg.obj);
                    String s = msg.obj.toString();
                    System.out.println(s);
                    JSONArray jsonArray = JSON.parseArray(s);
                    listDataHeader.clear();
                    listDataChild.clear();
                    int count=1;
                    Map map=new HashMap();
                    map.put("序号","序号");
                    map.put("课程名","课程名");
                    map.put("学分","学分");
                    map.put("课程属性","课程属性");
                    listDataHeader.add(map);
                    listDataChild.add("");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject job = jsonArray.getJSONObject(i);

                            Map map1 = new HashMap();
                            map1.put("序号", ""+count++);
                            map1.put("课程名",job.get("kcmc"));
                            map1.put("学分", job.get("xf").toString());
                            map1.put("课程属性", job.get("kcxzmc"));
                            listDataHeader.add(map1);

                            String child = "开课教室:"+job.get("jsmc") + "\n上课老师:"+job.get("jsxm") + "\n上课班级:" + job.get("ktmc")+"\n开课时间:";
                            listDataChild.add(child);




                    }

                    madapter=new com.wust.newsmartschool.adapter.ExpandableListAdapter(HaveSelectActivity.this,listDataHeader,listDataChild,returnid);
                    expandableListView_one.setAdapter(madapter);

                } else if (msg.what == 101) {
                    System.out.println("网速不给力");
                }


            }
        };
        expandableListView_one.setGroupIndicator(null);
        expandableListView_one.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                expandableListView_one.collapseGroup(0);
                for(int i=0;i<listDataHeader.size();i++){
                    if(groupPosition!=i){
                        expandableListView_one.collapseGroup(i);
                    }
                }
            }


        });
    }

    //在线数据hu获取列表内容
    public void getList(){
                 final String xh="201313137038";
                JSONObject aobject = new JSONObject();
                aobject.put("xh","201313137038");
                aobject.put("xnxq","2016-2017-1");
                OkHttpUtils.postString().url(url).content(aobject.toString())
                        .mediaType(MediaType.parse("application/json")).build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {
                                Log.i("HaveSelect", "可选课程列表访问失败：" + call + "---" + e);


                                Toast.makeText(HaveSelectActivity.this, "网速不给力"+e.toString(),
                                        Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onResponse(String response) {
                                Log.i("HaveSelect", "可选课程列表访问成功：" + response);

                                Toast.makeText(HaveSelectActivity.this, response, Toast.LENGTH_SHORT).show();

//                                JSONArray jsonArray = JSON.parseArray(response);
////CourseItem 的属性是否 接口方法一样？？
//                                List<CourseItem> courselist=JSONArray.parseArray(jsonArray.toJSONString(),CourseItem.class);
//                                listDataHeader.clear();
//                                listDataChild.clear();
//                                int count=1;
//                                Map map=new HashMap();
//                                map.put("序号","序号");
//                                map.put("课程名","课程名");
//                                map.put("学分","学分");
//                                map.put("课程属性","课程属性");
//                                listDataHeader.add(map);
//                                listDataChild.add("");
//                                for(int i=1;i<=coursrList.size();i++){
//                                    CourseItem acourse=coursrList.get(i);
//                                    if(acourse.getKcxz().equals(xkxz)) {
//                                        Map map1=new HashMap();
//                                        map1.put("序号", count++);
//                                        map1.put("课程名", acourse.getKcmc());
//                                        map1.put("学分", acourse.getXf());
//                                        map1.put("课程属性", acourse.getKcsx());
//                                        listDataHeader.add(map1);
//                                        String child="开课教室:"+acourse.getSkdd()+"\n上课老师:"+acourse.getSkjs()+"\n上课班级:"+acourse.getSkbj()+"\n开课时间:"+acourse.getSksj()+"\n";
//                                        listDataChild.add(child);
//
//                                    }
//                                }
//                                madapter=new com.adapter.ExpandableListAdapter(HaveSelectActivity.this,listDataHeader,listDataChild);
//                                expandableListView_one.setAdapter(madapter);
                            }
                        });

    }

   // 写死数据获取列表内容
   public void getListOne(){
        Map amap=new HashMap();
        amap.put("序号","序号");
        amap.put("课程名","课程名");
        amap.put("学分","学分");
        amap.put("课程属性","课程属性");
        listDataHeader.add(amap);
        Map bmap=new HashMap();
        bmap.put("序号","1");
        bmap.put("课程名","高数1");
        bmap.put("学分","5");
        bmap.put("课程属性","通识教育");
        listDataHeader.add(bmap);
        Map cmap=new HashMap();
        cmap.put("序号","2");
        cmap.put("课程名","英语");
        cmap.put("学分","5");
        cmap.put("课程属性","通识教育");
        listDataHeader.add(cmap);
        listDataChild.add("");
      listDataChild.add("开课教室:"+"\n上课老师:"+"\n上课班级:"+"\n开课时间:");
       listDataChild.add("开课教室:"+"\n上课老师:"+"\n上课班级:"+"\n开课时间:");

        /*listDataChild.add("详细信息2详细信息\n" +
                "详细信息2详细信息\n" +
                "详细信息2详细信息");
        listDataChild.add("详细信息2详细信息\n详细信息2详细信息\n详细信息2详细信息");*/
     //  madapter=new com.adapter.ExpandableListAdapter(this,listDataHeader,listDataChild);
       expandableListView_one.setAdapter(madapter);


    }
    private void getyxkclb() {



        LinkedHashMap mapParams = new LinkedHashMap();

        mapParams.put("time", appUseUtils.getTime());
        mapParams.put("chkvalue", appUseUtils.getParamtowebservice());
        WebServiceUtils.call(Constant.SERVICE_URL, Constant.NAMESPACE, "getyxkclb", mapParams, new WebServiceUtils.Response() {


            @Override
            public void onSuccess(final SoapObject result) {
                result1 = result.toString();
                Runnable runnable = new Runnable() {

                    @Override
                    public void run() {
                        if (result1 != null) {
                            System.out.println(result1);
                            handler.sendMessage(handler.obtainMessage(100, result1));

                        } else {
                            handler.sendMessage(handler.obtainMessage(101));
                        }
                    }
                };
                new Thread(runnable).start();

            }

            @Override
            public void onError(Exception e) {

            }
        });

    }


    }
