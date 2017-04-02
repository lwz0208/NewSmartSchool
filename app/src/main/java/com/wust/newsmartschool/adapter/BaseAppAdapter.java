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
import com.wust.newsmartschool.domain.FirstPage_entity;
import com.wust.easeui.Constant;
import com.wust.easeui.widget.GlideRoundTransform;

import java.util.ArrayList;
import java.util.List;

public class BaseAppAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ViewHolder vh = new ViewHolder();
    List<FirstPage_entity.SubMenuBean> data = new ArrayList<>();
    Context mContext;

    public BaseAppAdapter(Context con, List<FirstPage_entity.SubMenuBean> data) {
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
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.baseapp_items, null);
            vh.item_img = (ImageView) convertView
                    .findViewById(R.id.item_img);
            vh.item_text = (TextView) convertView
                    .findViewById(R.id.item_text);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        /**
         * 如果RUL为空，就表示是基础应用的。
         * 如果RUL不为空，就表示是常用申请（工作流）的
         * */
        if (null == data.get(position).getUrl()) {
            Glide.with(mContext)
                    .load(Constant.FP_IMAGEURL + data.get(position).getId() + ".png").transform(new GlideRoundTransform(mContext)).placeholder(R.drawable.base_application_default)
                    .into(vh.item_img);
        } else {
            Glide.with(mContext)
                    .load(Constant.BASE_URL + data.get(position).getPicture()).transform(new GlideRoundTransform(mContext)).placeholder(R.drawable.flow_approval_default)
                    .into(vh.item_img);
        }
        vh.item_text.setText(data.get(position).getName());

        return convertView;
    }

    private static class ViewHolder {
        private ImageView item_img;
        private TextView item_text;
    }

}
