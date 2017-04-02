package com.ding.chat.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ding.chat.DemoApplication;
import com.ding.chat.domain.GroupMemsEntity;
import com.ding.easeui.Constant;
import com.ding.easeui.R;
import com.ding.easeui.domain.DeptMembersEntity_Person;
import com.ding.easeui.domain.EaseUser;
import com.ding.easeui.domain.GroupMems;
import com.ding.easeui.utils.CommonUtils;
import com.ding.easeui.utils.EaseUserUtils;
import com.ding.easeui.widget.GlideRoundTransform;
import com.hyphenate.chat.EMGroup;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ChooseMembers_Adapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ViewHolder vh;
    List<GroupMemsEntity.DataBean.DataBean1.AffiliationsBean> persons_list;
    Context mContext;

    public ChooseMembers_Adapter(Context con,
                                 List<GroupMemsEntity.DataBean.DataBean1.AffiliationsBean> itemlist) {
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
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        if (!persons_list.get(position).getMember().equals("")) {
            Glide.with(mContext)
                    .load(Constant.GETHEADIMAG_URL
                            + persons_list.get(position).getMember()
                            + ".png").transform(new GlideRoundTransform(mContext)).placeholder(R.drawable.ease_default_avatar)
                    .into(vh.imageView1);
        } else {
            Glide.with(mContext)
                    .load(Constant.GETHEADIMAG_URL
                            + persons_list.get(position).getOwner()
                            + ".png").transform(new GlideRoundTransform(mContext)).placeholder(R.drawable.ease_default_avatar)
                    .into(vh.imageView1);
        }
        vh.friend_name
                .setText(persons_list.get(position).getName());

        return convertView;
    }

    private static class ViewHolder {
        private ImageView imageView1;
        private TextView friend_name;
        private TextView depmem_position;
    }

}
