package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wust.easeui.Constant;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.utils.WebServiceUtils;
import com.wust.newsmartschool.utils.appUseUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.LinkedHashMap;

public class PjActivity extends Activity implements View.OnClickListener {
    private String URLXNXQ = "http://202.114.255.100/Xsda/SelectCourse/get_school_year.php";
    private String URLPCMC = "http://202.114.255.100/Xsda/evaluate/get_evaluate_name.php";
    private String URLPJLB = "http://202.114.255.100/Xsda/evaluate/get_evaluate_type.php";
    private String URLPJSJ = "http://202.114.255.100/Xsda/evaluate/get_evaluate_time.php";
    private LinearLayout linearLayout;
    private Button search_button = null, reset_button = null;
    private Spinner spinner_xn = null, spinner_xq = null, spinner_sort = null;
    private TextView head_title = null, pjsj = null;
    private ImageView goback = null;
    private String[] xn = {"2015-2016-1", "2015-2016-2", "2016-2017-1", "2016-2017-2"};
    private String[] xq = {"2015-2016-1(期中)学生评教", "2015-2016-2(期末)学生评教"};
    private String[] sort = {"理论课评教", "实验课评教", "双语课评教", "体育课评教"};
    private ArrayAdapter<String> adapter_xn, adapter_xq, adapter_sort;
    private Intent intent;
    private boolean isread = false;
    private Context context = null;
    private final static String KEY = "webservice_whkdapp";
    private String selectxn, selectxq, selectsort;
    private String pjpcId, pjlbId;

    JSONArray jsonArray1 ;
    JSONArray jsonArray3 ;
    private String result1;

    private Handler handler;

    //String xh = GlobalVar.userid;

    ProgressDialog progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pj);
        init();
        progressBar = ProgressDialog.show(PjActivity.this,null, "请稍后…");

        getxnxqdata();



        search_button.setOnClickListener(this);

          handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    // System.out.println(msg.obj);
                    String s = msg.obj.toString();
                    System.out.println("学年学期接口访问成功");
                    System.out.println(s);
                    String str1=s.substring(s.indexOf("=")+1, s.length()-3);
                    JSONArray jsonArray = JSON.parseArray(str1);

                    for (int i = 0; i < jsonArray.size(); i++)

                    {
                        JSONObject job = jsonArray.getJSONObject(i);


                        adapter_xn.add(job.get("xnxq01id").toString());


                    }
                    adapter_xn.notifyDataSetChanged();


                }
                else if (msg.what == 1000){
                    String s = msg.obj.toString();
                    System.out.println("批次名称接口访问成功");
                    System.out.println(s);

                    String str1=s.substring(s.indexOf("=")+1, s.length()-3);
                    JSONArray jsonArray = JSON.parseArray(str1);

                    adapter_xq.clear();

                    for (int i = 0; i < jsonArray.size(); i++)

                    {
                        JSONObject job = jsonArray.getJSONObject(i);

                        adapter_xq.add(job.get("pjpcmc").toString());



                    }
                    adapter_xq.notifyDataSetChanged();

                    jsonArray1=jsonArray;

                }
                else if (msg.what == 10000){
                    String s = msg.obj.toString();
                    System.out.println("评价课类别接口访问成功");
                    System.out.println(s);
                    progressBar.dismiss();
                    linearLayout.setVisibility(View.VISIBLE );
                    String str1=s.substring(s.indexOf("=")+1, s.length()-3);

                    JSONArray jsonArray2 = JSON.parseArray(str1);

                    adapter_sort.clear();
                    for (int i = 0; i < jsonArray2.size(); i++)

                    {
                        JSONObject job = jsonArray2.getJSONObject(i);

                        adapter_sort.add(job.get("pjdlmc").toString());


                    }
                    adapter_sort.notifyDataSetChanged();
                    jsonArray3=jsonArray2;

                    pjsj.setText("评价时间："+ appUseUtils.getTime());
                   }

                else if(msg.what == 101) {

                    System.out.println("网速不给力");

                }

            }

        };
        reset_button.setOnClickListener(this);
        goback.setOnClickListener(this);
        adapter_xn = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter_xn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //  adapter_xn = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, xn);
        spinner_xn.setAdapter(adapter_xn);
        spinner_xn.setSelection(0, false);
        spinner_xn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectxn = parent.getItemAtPosition(position).toString();

            //    Toast.makeText(PjActivity.this, "你点击的是:" + selectxn, Toast.LENGTH_SHORT).show();

//                JSONObject bobject = new JSONObject();
//               bobject.put("semesterId","2014-2015-1");
//                Toast.makeText(PjActivity.this,bobject.toString(),Toast.LENGTH_SHORT).show();
//                OkHttpUtils.post().url(URLPCMC).addParams("semesterId",selectxn)
//                        .build()
//                        .execute(new StringCallback() {
//                            @Override
//                            public void onError(Call call, Exception e) {
//
//                                Toast.makeText(PjActivity.this,"网络错误",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onResponse(String response) {
//                               // Toast.makeText(PjActivity.this,"批次名称"+response,Toast.LENGTH_SHORT).show();
//                                Log.i("PjActivity", "批次名称接口访问成功：" + response);
//                                if(!response.equals("没有数据")) {
//                                JSONArray jsonArray = JSON.parseArray(response);
//                                xqlist=JSONArray.parseArray(jsonArray.toJSONString(),Pcmc.class);
//                                    adapter_xq.clear();
//                                for(int i=0;i<xqlist.size();i++){
//                                    adapter_xq.add(xqlist.get(i).getPjpcmc());
//                                }
//                                    adapter_xq.notifyDataSetChanged();
//                                }
//                            }
//                        });
//
//
                getpcmcdata();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_xn.setVisibility(View.VISIBLE);

        adapter_xq = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter_xq.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      //  adapter_xq = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, xq);


        spinner_xq.setAdapter(adapter_xq);
        spinner_xq.setSelection(0, false);

        spinner_xq.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectxq = parent.getItemAtPosition(position).toString();
                pjpcId = (jsonArray1.getJSONObject(position).get("pj05id")).toString();
                System.out.println(pjpcId);
                System.out.println(jsonArray1);

            //  Toast.makeText(PjActivity.this, "你点击的是:" + selectxq, Toast.LENGTH_SHORT).show();
//                pjpcId=xqlist.get(position).getPj05id();
                //            Toast.makeText(PjActivity.this,"pjpcId:"+pjpcId,Toast.LENGTH_SHORT).show();
//                OkHttpUtils.post().url(URLPJSJ).addParams("evaluateId",pjpcId)
//                        .build()
//                        .execute(new StringCallback() {
//                            @Override
//                            public void onError(Call call, Exception e) {
//
//                                Toast.makeText(PjActivity.this,"网络错误",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onResponse(String response) {
//                                // Toast.makeText(PjActivity.this,"批次名称"+response,Toast.LENGTH_SHORT).show();
//                                Log.i("PjActivity", "评教时间接口访问成功：" + response);
//                                if(!response.equals("没有数据")) {
//                                    JSONArray jsonArray = JSON.parseArray(response);
//                                    pjsjlist=JSONArray.parseArray(jsonArray.toJSONString(),Pjsj.class);
//                                    if(pjsjlist.size()>0){
//                                        pjsj.setText(pjsjlist.get(0).getKssj()+"-"+pjsjlist.get(0).getJssj());
//
//                                    }
////                                    Toast.makeText(PjActivity.this,"获取数据成功",Toast.LENGTH_SHORT).show();
//
//                                }
//                            }
//                        });


                getkclbdata();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_xq.setVisibility(View.VISIBLE);

        adapter_sort = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter_sort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //adapter_sort = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sort);


        spinner_sort.setAdapter(adapter_sort);
        spinner_sort.setSelection(0, false);

        spinner_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectsort = parent.getItemAtPosition(position).toString();
                    pjlbId = (String) jsonArray3.getJSONObject(position).get("pj01id");

                    System.out.println(pjlbId);

                // Toast.makeText(PjActivity.this, "你点击的是:" + selectsort, Toast.LENGTH_SHORT).show();

//                pjlbId=sortlist.get(position).getPj01id();
//                Toast.makeText(PjActivity.this,"pjlbid:"+pjlbId,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_sort.setVisibility(View.VISIBLE);
        // getContent();
/*


*/
    }


    public void init() {
        linearLayout =(LinearLayout) findViewById(R.id.LinearLayout);
        linearLayout.setVisibility(View.INVISIBLE );
        search_button = (Button) findViewById(R.id.pj_button1);
        reset_button = (Button) findViewById(R.id.pj_button2);
        spinner_xn = (Spinner) findViewById(R.id.pj_spinner1);
        spinner_xq = (Spinner) findViewById(R.id.pj_spinner2);
        spinner_sort = (Spinner) findViewById(R.id.pj_spinner3);
        head_title = (TextView) findViewById(R.id.head_title);
        goback = (ImageView) findViewById(R.id.goback);
        pjsj = (TextView) findViewById(R.id.pjtime);

        head_title.setText("教学评价");
        context = getApplicationContext();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pj_button1:
               if(selectxn!=null&&pjpcId!=null&&pjlbId!=null) {
                    intent = new Intent();
                    intent.setClass(PjActivity.this, PjList1Activity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("semesterId", selectxn);
                    bundle.putString("coursetype1", pjpcId);//批次id
                    bundle.putString("coursetype2", pjlbId);//类别id
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                /*intent = new Intent();
                intent.setClass(PjActivity.this, PjList1Activity.class);
                startActivity(intent);*/


                break;
            case R.id.pj_button2:
                spinner_xn.setSelection(0);
                spinner_xq.setSelection(0);
                spinner_sort.setSelection(0);
                break;
            case R.id.goback:
                finish();
                break;
            default:
                ;
        }
    }


    private void getxnxqdata() {




        LinkedHashMap mapParams = new LinkedHashMap();

        mapParams.put("time", appUseUtils.getTime());
        mapParams.put("chkvalue", appUseUtils.getParamtowebservice());
        WebServiceUtils.call(Constant.SERVICE_URL, Constant.NAMESPACE, "getpjxnxq", mapParams, new WebServiceUtils.Response() {


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

    private void getpcmcdata() {

        LinkedHashMap mapParams = new LinkedHashMap();
        mapParams.put("xh", selectxn);
        mapParams.put("time", appUseUtils.getTime());
        mapParams.put("chkvalue", appUseUtils.getParamtowebservice());
        WebServiceUtils.call(Constant.SERVICE_URL, Constant.NAMESPACE, "getpjpcmc", mapParams, new WebServiceUtils.Response() {


            @Override
            public void onSuccess(final SoapObject result) {
                result1 = result.toString();
                Runnable runnable = new Runnable() {

                    @Override
                    public void run() {
                        if (result1 != null) {
                            System.out.println(result1);
                            handler.sendMessage(handler.obtainMessage(1000, result1));

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


    private void getkclbdata() {

        LinkedHashMap mapParams = new LinkedHashMap();
        mapParams.put("time", appUseUtils.getTime());
        mapParams.put("chkvalue", appUseUtils.getParamtowebservice());
        WebServiceUtils.call(Constant.SERVICE_URL, Constant.NAMESPACE, "getpjkclb", mapParams, new WebServiceUtils.Response() {


            @Override
            public void onSuccess(final SoapObject result) {
                result1 = result.toString();
                Runnable runnable = new Runnable() {

                    @Override
                    public void run() {
                        if (result1 != null) {
                            System.out.println(result1);
                            handler.sendMessage(handler.obtainMessage(10000, result1));

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