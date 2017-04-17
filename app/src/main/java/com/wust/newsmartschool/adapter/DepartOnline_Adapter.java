package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.ui.ChatActivity;

import java.util.List;

/**
 * Created by Ren on 17/4/11.
 */

public class DepartOnline_Adapter extends BaseExpandableListAdapter {
    private List<String> listDataHeader ;
    private List<String> listDataChild;
    private Context  context;
    JSONArray jsonArray;
    private int width;
    int position;



    public DepartOnline_Adapter(Context context, List<String> listDataHeader, List<String> listDataChild, JSONArray jsonArray) {
        this.context = context;
        this.listDataHeader=listDataHeader;
        this.listDataChild=listDataChild;
        this.jsonArray=jsonArray;
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
        return groupPosition;    }

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
        LayoutInflater infalInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.expend_parent, null);

        LinearLayout basiclinear= (LinearLayout) convertView.findViewById(R.id.expand_parent);
        basiclinear.setMinimumWidth(width);
        TextView numText= (TextView) convertView.findViewById(R.id.expand_text);
        numText.setText(listDataHeader.get(groupPosition));

       /* numText.setText(listDataHeader.get(groupPosition).get("num"));
        numText.setGravity(Gravity.CENTER);
        TextView nameText= (TextView) convertView.findViewById(R.id.textView3);
        nameText.setText(listDataHeader.get(groupPosition).get("coursename"));
        nameText.setGravity(Gravity.CENTER);
        TextView scoreText= (TextView) convertView.findViewById(R.id.textView4);
        scoreText.setGravity(Gravity.CENTER);
        scoreText.setText(listDataHeader.get(groupPosition).get("credict"));
        TextView proText= (TextView) convertView.findViewById(R.id.textView5);
        proText.setText(listDataHeader.get(groupPosition).get("teacher"));
        proText.setGravity(Gravity.CENTER);
        if(groupPosition==0)
        {
            convertView.setBackgroundColor(context.getResources().getColor(R.color.top));
        }else if(groupPosition%2==0){
            convertView.setBackgroundColor(context.getResources().getColor(R.color.text_greay));
        }*/
        return convertView;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater infalInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = infalInflater.inflate(R.layout.expend_child, null);
        RelativeLayout linear= (RelativeLayout) convertView.findViewById(R.id.expandlinear1);
        linear.setMinimumWidth(width);
        TextView expandtext= (TextView) convertView.findViewById(R.id.expand_text1);
        expandtext.setText(listDataChild.get(groupPosition));
        Button expandbutton1= (Button) convertView.findViewById(R.id.btn_sentmessage);
        position=groupPosition;
        expandbutton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(context, ChatActivity.class).putExtra("userId",(String) jsonArray.getJSONObject(position).get("cardnumber"));
                context.startActivity(intent);

            }
        });
        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }





}
