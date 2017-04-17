package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.wust.easeui.Constant;
import com.wust.easeui.utils.PreferenceManager;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.ui.HaveSelectActivity;
import com.wust.newsmartschool.utils.WebServiceUtils;
import com.wust.newsmartschool.utils.appUseUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus-pc on 2016/10/14.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private String xkurl="";
    private String txurl="";

    private Context context;
    private List<Map<String, String>> listDataHeader ;//= new ArrayList<Map<String, String>>(); // header titles
    // child data in format of header title, child title
    private List<String> listDataChild;
    private int width;
    private boolean haveSelectContext=false;
    JSONArray jsonArray;
    String returnyx;
    String returnkx;

    String returnkxdj;
    int position;
    String xh= PreferenceManager.getInstance().getCurrentUserId();
    public JSONArray flagpj=new JSONArray();
    public ExpandableListAdapter(Context context, List<Map<String, String>> listDataHeader,
                                 List<String> listDataChild , JSONArray jsonArray) {
        this.context = context;
        this.listDataHeader=listDataHeader;
        this.listDataChild=listDataChild;
        this.jsonArray=jsonArray;
        if(context.toString().contains("HaveSelectActivity")){
            haveSelectContext=true;
        }
        width=context.getResources().getDisplayMetrics().widthPixels;


    }
    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//      if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_pjbasic, null);
            LinearLayout basiclinear= (LinearLayout) convertView.findViewById(R.id.basiclinear);
            basiclinear.setMinimumWidth(width);
            TextView numText= (TextView) convertView.findViewById(R.id.textView2);
            numText.setText(listDataHeader.get(groupPosition).get("序号"));
            numText.setGravity(Gravity.CENTER);
            TextView nameText= (TextView) convertView.findViewById(R.id.textView3);
            nameText.setText(listDataHeader.get(groupPosition).get("课程名"));
            nameText.setGravity(Gravity.CENTER);
            TextView scoreText= (TextView) convertView.findViewById(R.id.textView4);
            scoreText.setGravity(Gravity.CENTER);
            scoreText.setText(listDataHeader.get(groupPosition).get("学分"));
            TextView proText= (TextView) convertView.findViewById(R.id.textView5);
            proText.setText(listDataHeader.get(groupPosition).get("课程属性"));
            proText.setGravity(Gravity.CENTER);
            if(groupPosition==0)
            {
                convertView.setBackgroundColor(context.getResources().getColor(R.color.top));
            }else if(groupPosition%2==0){
                convertView.setBackgroundColor(context.getResources().getColor(R.color.text_greay));
            }
//        }
        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        //if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandcoursr_item, null);
            RelativeLayout linear= (RelativeLayout) convertView.findViewById(R.id.expandlinear);
            linear.setMinimumWidth(width);
            TextView expandtext= (TextView) convertView.findViewById(R.id.expand_text);
            expandtext.setText(listDataChild.get(groupPosition));
            expandtext.setGravity(Gravity.CENTER);
        position=groupPosition;
        Button expandbutton= (Button) convertView.findViewById(R.id.expand_button);
            if(haveSelectContext){
                expandbutton.setText("退选");
            }

            expandbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //选中按钮的点击事件
               if(haveSelectContext) {
                   //已选列表点击事件
                   txkc();

               }else{
                   //可选列表点击事件
                   xzkc();

               }
                }
            });
           // }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

//选中课程
    public void xzkc(){
        /*JSONObject aobject = new JSONObject();
        //选课还需要很多属性
        aobject.put("xh", GlobalVar.userid);
        OkHttpUtils.postString().url(xkurl).content(aobject.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        Toast.makeText(context, "选课失败，稍后重试" , Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Log.i("Select", "选课类别访问成功：" + response);
                        Toast.makeText(context, "选课成功" , Toast.LENGTH_SHORT).show();
                        NoSelectActivity noselectactivity= (NoSelectActivity) context;
                        noselectactivity.onStart();
                    }
                });*/



        LinkedHashMap mapParams = new LinkedHashMap();
        mapParams.put("kxkc", returnkx);
        mapParams.put("yxkc",returnkxdj);
        mapParams.put("xh",xh);
        mapParams.put("time", appUseUtils.getTime());
        mapParams.put("chkvalue", appUseUtils.getParamtowebservice());
        WebServiceUtils.call(Constant.SERVICE_URL, Constant.NAMESPACE, "xsxk", mapParams, new WebServiceUtils.Response() {


            @Override
            public void onSuccess(final SoapObject result) {
                System.out.println(result);
                String s=result.toString();
                //String str1=s.substring(s.indexOf("=")+3, s.length()-3);
                // System.out.println(str1);
                if (s.contains("选课失败"))

                { Toast.makeText(HaveSelectActivity.haveSelectctivity, "选课失败！", Toast.LENGTH_SHORT).show();}
                else if (s.contains("选课成功"))
                { Toast.makeText(HaveSelectActivity.haveSelectctivity, "选课成功！", Toast.LENGTH_SHORT).show();}



            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    public void txkc(){

        LinkedHashMap mapParams = new LinkedHashMap();
     // returnyx  = (String) jsonArray.getJSONObject(position-1).get("jx0404id");
     //    returnkxdj = (String) jsonArray.getJSONObject(position-1).get("jx0502id");

        mapParams.put("yxkc", returnyx);
        mapParams.put("xh",xh);

        mapParams.put("yxxk",returnkxdj);
        mapParams.put("time", appUseUtils.getTime());
        mapParams.put("chkvalue", appUseUtils.getParamtowebservice());
        System.out.println(returnkxdj+returnyx);
        WebServiceUtils.call(Constant.SERVICE_URL, Constant.NAMESPACE, "xsxktx", mapParams, new WebServiceUtils.Response() {


            @Override
            public void onSuccess(SoapObject result) {

                System.out.println(result);
                String s=result.toString();
                //String str1=s.substring(s.indexOf("=")+3, s.length()-3);
               // System.out.println(str1);
                if (s.contains("退选失败"))

                { Toast.makeText(HaveSelectActivity.haveSelectctivity, "退选失败！", Toast.LENGTH_SHORT).show();}
                else if (s.contains("退选成功"))
                { Toast.makeText(HaveSelectActivity.haveSelectctivity, "退选失败！", Toast.LENGTH_SHORT).show();}

            }


            @Override
            public void onError(Exception e) {

            }
        });


    }

}
