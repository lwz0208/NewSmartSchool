package com.ding.chat.adapter;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.ding.chat.R;
import com.ding.chat.domain.DeptMembersEntity_Person;
import com.ding.easeui.Constant;
import com.ding.easeui.widget.GlideRoundTransform;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DeptMembers_Adapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ViewHolder vh;
    List<DeptMembersEntity_Person> persons_list = new ArrayList<DeptMembersEntity_Person>();
    Context mContext;

    public DeptMembers_Adapter(Context con,
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
            convertView = mInflater.inflate(R.layout.friends_bottom, parent, false);
            vh = new ViewHolder();
            vh.imageView1 = (ImageView) convertView
                    .findViewById(R.id.imageView1);
            vh.friend_name = (TextView) convertView
                    .findViewById(R.id.friend_name);
            vh.depmem_position = (TextView) convertView
                    .findViewById(R.id.depmem_position);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        Glide.with(mContext)
                .load(Constant.GETHEADIMAG_URL
                        + persons_list.get(position).getUserId()
                        + ".png").transform(new GlideRoundTransform(mContext)).placeholder(R.drawable.ease_default_avatar)
                .into(vh.imageView1);
        vh.friend_name
                .setText(persons_list.get(position).getUserRealname());
        vh.depmem_position.setText(persons_list.get(position).getPosition());
        return convertView;
    }

    private static class ViewHolder {
        private ImageView imageView1;
        private TextView friend_name;
        private TextView depmem_position;
    }

}
