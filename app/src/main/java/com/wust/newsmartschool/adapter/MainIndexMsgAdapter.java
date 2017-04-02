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
import com.wust.newsmartschool.domain.UndoData;

import java.util.ArrayList;
import java.util.List;

public class MainIndexMsgAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<UndoData> mData = new ArrayList<UndoData>();
    Context mContext;


    public MainIndexMsgAdapter(Context con, List<UndoData> itemlist) {
        mContext = con;
        mInflater = LayoutInflater.from(con);
        mData = itemlist;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_mainindexmsg, null);
            vh = new ViewHolder();
            vh.flag_imge = (ImageView) convertView
                    .findViewById(R.id.flag_imge);
            vh.mainindex_time = (TextView) convertView
                    .findViewById(R.id.mainindex_time);
            vh.mainindex_content = (TextView) convertView
                    .findViewById(R.id.mainindex_content);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        switch (mData.get(position).getType()) {
            case "inform":
                //设置头像
                Glide.with(mContext)
                        .load(R.drawable.home_notice_gray).into(vh.flag_imge);
                break;
            case "jflow":
                //设置头像
                Glide.with(mContext)
                        .load(R.drawable.home_approve_gray).into(vh.flag_imge);
                break;
            case "meeting":
                //设置头像
                if ((mData.get(position).getType().equals("2")))
                    Glide.with(mContext)
                            .load(R.drawable.home_hospitalmeeting_gray).into(vh.flag_imge);
                else if ((mData.get(position).getType().equals("1")))
                    Glide.with(mContext)
                            .load(R.drawable.home_normalmeeting_gray).into(vh.flag_imge);
                else
                    Glide.with(mContext)
                            .load(R.drawable.home_meeting_gray).into(vh.flag_imge);
                break;
            case "task":
                //设置头像
                Glide.with(mContext)
                        .load(R.drawable.home_task_gray).into(vh.flag_imge);
                break;
            default:
                Glide.with(mContext)
                        .load(R.drawable.my_work_normal).into(vh.flag_imge);
                break;
        }
        if (mData.get(position).getCreateTime() != null) {
            vh.mainindex_time.setText(mData.get(position).getCreateTime());
        } else if (mData.get(position).getTime() != null) {
            vh.mainindex_time.setText(mData.get(position).getTime());
        }
        vh.mainindex_content.setText(mData.get(position).getTitle());

        return convertView;

    }

    private static class ViewHolder {
        ImageView flag_imge;
        TextView mainindex_time;
        TextView mainindex_content;
    }

}
