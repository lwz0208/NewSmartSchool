package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wust.easeui.Constant;
import com.wust.easeui.utils.PreferenceManager;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.utils.WebServiceUtils;
import com.wust.newsmartschool.utils.appUseUtils;
import com.wust.newsmartschool.views.MyListView;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class SelectActivity extends Activity implements MyListView.IXListViewListener {
    private String url="";//获取选课阶段列表
    private ImageView back;
    private TextView head_title;
    private com.wust.newsmartschool.views.MyListView mylist;
    private ProgressBar progressBar ;
    private Intent intent;
    private SimpleAdapter mSimpleAdapter;
    private Handler handler;
    private String result1;
    //   String xh = GlobalVar.userid;

    ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();/*在数组中存放数据*/
    //private List<Xkjd> xkjdlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        head_title = (TextView) findViewById(R.id.head_title);
        back = (ImageView) findViewById(R.id.goback);
        mylist = (MyListView) findViewById(R.id.selectList);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        mylist.setPullLoadEnable(true); // 使能下拉加载跟多
        mylist.setXListViewListener(this);
        head_title.setText("选课系统");
        back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        mylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                 map=listItem.get(position-1);
                //System.out.println(map);

                Bundle bundle=new Bundle();

                intent = new Intent(SelectActivity.this, CourseListActivity.class);
               // Bundle bundle = new Bundle();
                // bundle.putString("courseText", map.get("xnxq").toString());

//                HashMap<String, Object> map = (HashMap<String, Object>) ((ListView) parent).getItemAtPosition(position);

                bundle.putString("学年学期", map.get("xnxq").toString());
                bundle.putString("jx0502id", map.get("jx0502id").toString());

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //getlocal();

        getdata();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    // System.out.println(msg.obj);
                    String s = msg.obj.toString();
                    System.out.println(s);
                    if (s.contains("服务器错误")) {
                        Toast.makeText(getApplicationContext(), "服务器错误", Toast.LENGTH_SHORT).show();
                    } else{
                        JSONArray jsonArray = JSON.parseArray(s);
                    listItem.clear();

                    for (int i = 0; i < jsonArray.size(); i++)

                    {
                        JSONObject job = jsonArray.getJSONObject(i);

                        HashMap<String, Object> map = new HashMap<String, Object>();
                        map.put("courseText", job.get("xklb"));
                        String detail = "开始时间:" + job.get("xkkssj") + "\n结束时间:" + job.get("xkjzsj") + "\n选课阶段:" + job.get("xkjd");
                        map.put("courseDetail", detail);
                        map.put("xnxq", job.get("xnmc"));
                        map.put("jx0502id", job.get("jx0502id"));
                        map.put("xkfs", job.get("xkfs"));
                        //System.out.println(map);
                        listItem.add(map);
                        System.out.println(listItem);


                    }
                    mSimpleAdapter = new SimpleAdapter(SelectActivity.this, listItem,//需要绑定的数据
                            R.layout.coursekinditem,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                            new String[]{"courseText", "courseDetail"}, new int[]{R.id.courseText, R.id.courseDetail}
                    );
                    mylist.setAdapter(mSimpleAdapter);
                }

                } else if (msg.what == 101) {
                    System.out.println("网速不给力");
                }
            }
        };


    }


    private void getlocal(){
        HashMap<String, Object> map1 = new HashMap<String, Object>();
        //map.put("ItemImage", R.drawable.icon);//加入图片
        map1.put("courseText", "1.在线通识教育选课");
        map1.put("courseDetail","开放时间：2016-9-14 10:00\n结束时间：2016-9-19 14:00\n（选课阶段：一选）");
        map1.put("xnxq","xnxq");
        map1.put("xkxz","xkxz");
        listItem.add(map1);
        HashMap<String, Object> map2 = new HashMap<String, Object>();
        map2.put("courseText", "2.公共选修课");
        map2.put("courseDetail","开放时间：2016-9-14 10:00\n结束时间：2016-9-19 14:00\n（选课阶段：一选）");
        map2.put("xnxq","xnxq");
        map2.put("xkxz","xkxz");
        listItem.add(map2);
        mSimpleAdapter = new SimpleAdapter(this,listItem,//需要绑定的数据
                R.layout.coursekinditem,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                new String[] {"courseText","courseDetail"},new int[] {R.id.courseText, R.id.courseDetail}
        );
        mylist.setAdapter(mSimpleAdapter);
    }

    private void getdata() {
        LinkedHashMap mapParams = new LinkedHashMap();
        mapParams.put("xh", PreferenceManager.getInstance().getCurrentUserId());
        mapParams.put("time", appUseUtils.getTime());
        mapParams.put("chkvalue", appUseUtils.getParamtowebservice());
        WebServiceUtils.call(Constant.SERVICE_URL, Constant.NAMESPACE, "getxkjdlb", mapParams, new WebServiceUtils.Response() {


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

/*


        new Thread(new Runnable() {

            @Override
            public void run() {

               String Method = "getxkjdlb";
                HttpTransportSE ht = new HttpTransportSE(URL_UNIVERSAL.SERVICE_URL);
                ht.debug = true;
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                SoapObject request = new SoapObject(URL_UNIVERSAL.NAMESPACE, Method);

                request.addProperty("xh", GlobalVar.userid);
                // request.addProperty("psd", "201213137182");
                request.addProperty("time", new Common().getTime());
                request.addProperty("chkvalue", new Common().key());

                envelope.bodyOut = request;

                try {
                    ht.call(URL_UNIVERSAL.SOAP_ACTION, envelope);
                    if (envelope.getResponse() != null) {

                        Message msg=handler.obtainMessage();
                        msg.what=100;
                        msg.obj=envelope.getResponse();
                        handler.sendMessage(msg);

                        */
/*JSONArray jsonArray = JSON.parseArray(s);
                       // xkjdlist=JSONArray.parseArray(jsonArray.toJSONString(),Xkjd.class);
                        listItem.clear();

                       for(int i=0;i<jsonArray.size();i++)

                        {
                            JSONObject job = jsonArray.getJSONObject(i);

                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("courseText",job.get("xklb"));
                            String detail="开始时间:"+job.get("xkkssj")+"\n结束时间:"+job.get("xkjzsj")+"\n选课阶段:"+job.get("xkjd");
                            map.put("courseDetail",detail);
                            map.put("xnxq",job.get("xnmc"));
                            map.put("xkxz",job.get("xklb"));
                            map.put("xkjd",job.get("xkkssj"));
                            System.out.println(map);
                            listItem.add(map);
                            System.out.println(listItem);


                        }
                        mSimpleAdapter = new SimpleAdapter(SelectActivity.this,listItem,//需要绑定的数据
                                R.layout.coursekinditem,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                                new String[] {"courseText","courseDetail"},new int[] {R.id.courseText,R.id.courseDetail}
                        );
                        mylist.setAdapter(mSimpleAdapter);

                        }
                    System.out.println("fdaljkshf");

*//*

                }
                    else {
                        Message msg=handler.obtainMessage();
                        msg.what=101;
                        msg.obj=envelope.getResponse();

                        handler.sendMessage(msg);

                    }*/
/*catch (SoapFault soapFault) {
                    soapFault.printStackTrace();*//*

                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            }).start();
*/


        /*
        final String xh= GlobalVar.userid;
        JSONObject aobject = new JSONObject();
        OkHttpUtils.postString().url(url).content(aobject.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.i("Select", "选课类别访问失败：" + call + "---" + e);
                        Toast.makeText(SelectActivity.this, "网速不给力",
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.i("Select", "选课类别访问成功：" + response);
                        JSONArray jsonArray = JSON.parseArray(response);
                        xkjdlist=JSONArray.parseArray(jsonArray.toJSONString(),Xkjd.class);
                        listItem.clear();

                        for(int i=0;i<xkjdlist.size();i++)
                        {
                            Xkjd axkjd=xkjdlist.get(i);
                            HashMap<String, Object> map = new HashMap<String, Object>();
                            map.put("courseText",axkjd.getXkxz());
                            String detail="开始时间:"+axkjd.getXkkssj()+"\n结束时间:"+axkjd.getXkjssj()+"\n(选课阶段:"+axkjd.getXkjd()+")";
                            map.put("courseDetail",detail);
                            map.put("xnxq",axkjd.getXnxq());
                            map.put("xkxz",axkjd.getXkxz());
                            map.put("xkjd",axkjd.getXkjd());
                            listItem.add(map);
                        }
                        mSimpleAdapter = new SimpleAdapter(SelectActivity.this,listItem,//需要绑定的数据
                                R.layout.coursekinditem,//每一行的布局//动态数组中的数据源的键对应到定义布局的View中
                                new String[] {"courseText","courseDetail"},new int[] {R.id.courseText,R.id.courseDetail}
                        );
                        mylist.setAdapter(mSimpleAdapter);
                    }
                });*/
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }


}
