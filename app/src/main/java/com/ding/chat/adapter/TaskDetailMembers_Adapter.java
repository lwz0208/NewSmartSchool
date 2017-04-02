package com.ding.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ding.chat.R;
import com.ding.chat.domain.DeptMembersEntity_Person;
import com.ding.easeui.Constant;
import com.ding.easeui.widget.GlideCircleImage;
import com.ding.easeui.widget.GlideRoundTransform;

import java.util.ArrayList;
import java.util.List;

public class TaskDetailMembers_Adapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ViewHolder vh;
    List<DeptMembersEntity_Person> persons_list = new ArrayList<DeptMembersEntity_Person>();
    Context mContext;

    public TaskDetailMembers_Adapter(Context con,
                                     List<DeptMembersEntity_Person> itemlist) {
        mContext = con;
        mInflater = LayoutInflater.from(con);
        persons_list = itemlist;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return persons_list.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return persons_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.taskdetail_member, parent, false);
            vh = new ViewHolder();
            vh.taskdetail_header = (ImageView) convertView
                    .findViewById(R.id.taskdetail_header);
            vh.taskdetail_name = (TextView) convertView
                    .findViewById(R.id.taskdetail_name);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext)
                .load(Constant.GETHEADIMAG_URL
                        + persons_list.get(position).getUserId()
                        + ".png").transform(new GlideCircleImage(mContext)).placeholder(R.drawable.ease_default_avatar)
                .into(vh.taskdetail_header);

        vh.taskdetail_name
                .setText(persons_list.get(position).getUserRealname());
        return convertView;
    }

    private static class ViewHolder {
        private ImageView taskdetail_header;
        private TextView taskdetail_name;
    }

}
