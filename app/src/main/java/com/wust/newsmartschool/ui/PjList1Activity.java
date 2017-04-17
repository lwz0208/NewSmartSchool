package com.wust.newsmartschool.ui;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wust.easeui.Constant;
import com.wust.easeui.utils.PreferenceManager;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.ExpandableListAdapter1;
import com.wust.newsmartschool.domain.UserInfoEntity;
import com.wust.newsmartschool.utils.WebServiceUtils;
import com.wust.newsmartschool.utils.appUseUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PjList1Activity extends Activity {
    private ExpandableListView listview1;
    private ExpandableListAdapter1  madapter;
    private List<Map<String, String>> listDataHeader=new ArrayList<Map<String, String>>() ;
    private List<String> listDataChild=new ArrayList<>();
    private TextView head_title=null;
    private ImageView goback=null;
    private String coursetype1,coursetype2,studentId,semesterId;
    private String URL="http://202.114.255.100/Xsda/evaluate/get_evaluate_list.php";
    List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private Handler handler;
    public JSONArray flagpj=new JSONArray();
    ProgressDialog progressBar;
   //String xh = GlobalVar.userid;
   UserInfoEntity userInfoEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pj_list1);
        listview1 = (ExpandableListView) findViewById(R.id.pj_listView1);
        head_title = (TextView) findViewById(R.id.head_title);
        goback = (ImageView) findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        head_title.setText("评价列表");

        Bundle bundle=getIntent().getExtras();
       semesterId=bundle.getString("semesterId");
        coursetype1=bundle.getString("coursetype1");
        coursetype2=bundle.getString("coursetype2");

    }
        @Override
        public void onStart() {
            super.onStart();
           // Toast.makeText(PjList1Activity.this, "start" , Toast.LENGTH_SHORT).show();
            //getListOne1();
          //  getpjkcb();

            progressBar = ProgressDialog.show(PjList1Activity.this,null, "请稍后…");

            LinkedHashMap mapParams = new LinkedHashMap();

            mapParams.put("xh", PreferenceManager.getInstance().getCurrentUserId());
            mapParams.put("xnxq", semesterId);
            mapParams.put("pj05id",coursetype1);
            mapParams.put("pj01id", coursetype2);
            mapParams.put("time", appUseUtils.getTime());
            mapParams.put("chkvalue", appUseUtils.getParamtowebservice());

            // final Dialog dlg = DialogUtils.showProgressDialog(this, "正在登录...");
            WebServiceUtils.call(Constant.SERVICE_URL, Constant.NAMESPACE, "getpjkcb", mapParams, new WebServiceUtils.Response() {

                @Override
                public void onSuccess(SoapObject result) {
                    System.out.println(result);
                    String s=result.toString();
                    // System.out.println(msg.obj);
                    //  String s = msg.obj.toString();
                    //  System.out.println(s);
                    // System.out.println(s);
                    String str1=s.substring(s.indexOf("=")+1, s.length()-3);
                    System.out.println(str1);
                    progressBar.dismiss();

                    if (!str1.equals("没有数据"))

                   {
                       System.out.println(str1);

                       JSONArray jsonArray = JSON.parseArray(str1);

                       listDataHeader.clear();
                       listDataChild.clear();
                       flagpj.clear();
                       flagpj= jsonArray;
                       int count = 1;
                       Map map = new HashMap();
                       map.put("num", "序号");
                       map.put("coursename", "课程名");
                       map.put("credict", "学分");
                       map.put("teacher", "上课老师");
                       listDataHeader.add(map);
                       listDataChild.add("");
                       for (int i = 0; i < jsonArray.size(); i++) {
                           JSONObject job = jsonArray.getJSONObject(i);
                           {
                               Map map1 = new HashMap();
                               map1.put("num", count++ + "");
                               map1.put("coursename", job.get("kcmc"));
                               //   map1.put("credict",job.get("xf").toString());
                               map1.put("teacher", job.get("skjs"));
                               listDataHeader.add(map1);
                               String child = "课程名称:" + job.get("kcmc") + "\n上课老师:" + job.get("skjs") + "\n上课班级:" + "\n总评分:" + job.get("zpf") + "\n已评:" + job.get("iskpj") + "\n是否提交:" + job.get("issubmit");
                               listDataChild.add(child);


                           }

                       }
                   }else{
                       Toast.makeText(getApplicationContext(), "没有数据", Toast.LENGTH_SHORT).show();
                       Map map = new HashMap();
                       map.put("num", "序号");
                       map.put("coursename", "课程名");
                       map.put("credict", "学分");
                       map.put("teacher", "上课老师");
                       listDataHeader.add(map);
                       listDataChild.add("");
                   }

                        madapter=new ExpandableListAdapter1(PjList1Activity.this,listDataHeader,listDataChild,flagpj);
                        listview1.setAdapter(madapter);



                    listview1.setGroupIndicator(null);
                    listview1.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                        @Override
                        public void onGroupExpand(int groupPosition) {
                            listview1.collapseGroup(0);
                            for(int i=0;i<listDataHeader.size();i++){
                                if(groupPosition!=i){
                                    listview1.collapseGroup(i);
                                }
                            }
                        }


                    });




                }

                @Override
                public void onError(Exception e) {

                }
            });



            handler = new Handler() {
               @Override
                public void handleMessage(Message msg) {
                    if (msg.what == 100) {
                        // System.out.println(msg.obj);
                        String s = msg.obj.toString();
                        System.out.println(s);
                        String str1=s.substring(s.indexOf("=")+1, s.length()-3);

                        JSONArray jsonArray = JSON.parseArray(str1);
                            listDataHeader.clear();
                            listDataChild.clear();
                            int count = 1;
                            Map map = new HashMap();
                            map.put("num", "序号");
                            map.put("coursename", "课程名");
                            map.put("credict", "学分");
                            map.put("teacher", "上课老师");
                            listDataHeader.add(map);
                            listDataChild.add("");
                            for (int i = 0; i < jsonArray.size(); i++) {
                                JSONObject job = jsonArray.getJSONObject(i);
                                {
                                    Map map1 = new HashMap();
                                    map1.put("num", count++ + "");
                                    map1.put("coursename", job.get("kcmc"));
                                    //   map1.put("credict",job.get("xf").toString());
                                    map1.put("teacher", job.get("skjs"));
                                    listDataHeader.add(map1);
                                    String child = "课程名称:" + job.get("kcmc") + "\n上课老师:" + job.get("skjs") + "\n上课班级:" + "\n总评分:" + job.get("zpf") + "\n已评:" + job.get("iskpj") + "\n是否提交:" + job.get("issubmit");
                                    listDataChild.add(child);

                                }


                            }

                       // madapter=new com.adapter.ExpandableListAdapter1(PjList1Activity.this,listDataHeader,listDataChild);
                        listview1.setAdapter(madapter);

                    }

                        else if (msg.what == 101) {
                        System.out.println("网速不给力");
                    }


                }
            };



           // madapter=new com.adapter.ExpandableListAdapter1(this,listDataHeader,listDataChild);
           // listview1.setAdapter(madapter);

            listview1.setGroupIndicator(null);
            listview1.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {
                    listview1.collapseGroup(0);
                    for(int i=0;i<listDataHeader.size();i++){
                        if(groupPosition!=i){
                            listview1.collapseGroup(i);
                        }
                    }
                }


            });
        }


        public void getListOne1(){
            listDataHeader=new ArrayList<Map<String, String>>();
            listDataChild=new ArrayList<>();
            Map amap=new HashMap();
        amap.put("num","序号");
        amap.put("coursename","课程名");
        amap.put("credict","学分");
        amap.put("teacher","上课老师");
            amap.put("issubmit","是");
        listDataHeader.add(amap);

        Map bmap=new HashMap();
        bmap.put("num","1");
        bmap.put("coursename","高数");
        bmap.put("credict","5");
        bmap.put("teacher","小明");
        listDataHeader.add(bmap);

        Map cmap=new HashMap();
        cmap.put("num","2");
        cmap.put("coursename","英语");
        cmap.put("credict","5");
        cmap.put("teacher","小王");
        listDataHeader.add(cmap);


            String a = "A", b = "B", c = "C", d = "D", e = "E";
            listDataChild.add("");
            listDataChild.add("课程名称:"+ d+"\n"+"上课班级："+"\n"+"上课老师："+"\n"+"总评分："+"\n"+"已评："+"否"+"\n"+"是否提交：");
            listDataChild.add("课程名称:"+ e+"\n"+"上课班级："+"\n"+"上课老师："+"\n"+"总评分："+"\n"+"已评："+"\n"+"是否提交：");
            listDataChild.add("课程名称:"+ a+"\n"+"上课班级："+"\n"+"上课老师："+"\n"+"总评分："+"\n"+"已评："+"\n"+"是否提交：");



        }



}

