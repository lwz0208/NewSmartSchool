package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.NoticeDetail_SeeMemList;
import com.wust.easeui.Constant;
import com.wust.easeui.widget.GlideRoundTransform;

import java.util.ArrayList;
import java.util.List;

public class NoticeDetailSeeMembers_Adapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ViewHolder vh;
    List<NoticeDetail_SeeMemList.MenbersBean> persons_list = new ArrayList<NoticeDetail_SeeMemList.MenbersBean>();
    Context mContext;

    public NoticeDetailSeeMembers_Adapter(Context con,
                                          List<NoticeDetail_SeeMemList.MenbersBean> itemlist) {
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
            convertView = mInflater.inflate(R.layout.noticedetailsee_members, parent, false);
            vh = new ViewHolder();
            vh.imageView1 = (ImageView) convertView
                    .findViewById(R.id.imageView1);
            vh.friend_name = (TextView) convertView
                    .findViewById(R.id.friend_name);
            vh.depmem_position = (TextView) convertView
                    .findViewById(R.id.depmem_position);
            vh.roleName_img = (ImageView) convertView
                    .findViewById(R.id.roleName_img);
            vh.deptment_name = (TextView) convertView
                    .findViewById(R.id.deptment_name);
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
        vh.depmem_position.setText(persons_list.get(position).getJobtitleName());
        vh.deptment_name.setText(persons_list.get(position).getDepartmentName());
        //判断是临时工还是正式工显示对应的图标1是管理员2是正式职工3是临时职工
        if (persons_list.get(position).getRoleId() == 1) {
            Glide.with(mContext)
                    .load(R.drawable.rolename_guan).into(vh.roleName_img);
        } else if (persons_list.get(position).getRoleId() == 2) {
            Glide.with(mContext)
                    .load(R.drawable.rolename_zheng).into(vh.roleName_img);
        } else if (persons_list.get(position).getRoleId() == 3) {
            Glide.with(mContext)
                    .load(R.drawable.rolename_lin).into(vh.roleName_img);
        }
        return convertView;
    }

    private static class ViewHolder {
        private ImageView imageView1;
        private TextView friend_name;
        private TextView deptment_name;
        private ImageView roleName_img;
        private TextView depmem_position;
    }

}
