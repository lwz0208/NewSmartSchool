package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wust.easeui.Constant;
import com.wust.easeui.widget.GlideRoundTransform;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.Common_TypeMem;

import java.util.ArrayList;
import java.util.List;

public class TypeMembers_Adapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ViewHolder vh = new ViewHolder();
    List<Common_TypeMem.DataBean> data = new ArrayList<>();
    Context mContext;

    public TypeMembers_Adapter(Context con, List<Common_TypeMem.DataBean> data) {
        mContext = con;
        mInflater = LayoutInflater.from(con);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //数据复现了，不知道为啥。。。
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.friends_bottom, null);
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
                        + data.get(position).getId()
                        + ".png").transform(new GlideRoundTransform(mContext)).placeholder(R.drawable.ease_default_avatar)
                .into(vh.imageView1);
        vh.friend_name
                .setText(data.get(position).getName());

        return convertView;
    }

    private static class ViewHolder {
        private ImageView imageView1;
        private ImageView roleName_img;
        private TextView friend_name;
        private TextView depmem_position;
        private TextView deptment_name;
    }

}
