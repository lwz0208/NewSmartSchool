package com.ding.chat.adapter;

import android.annotation.SuppressLint;
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
import com.ding.chat.R;
import com.ding.chat.domain.NoticeCommentEntity;
import com.ding.easeui.Constant;
import com.ding.easeui.utils.CommonUtils;
import com.ding.easeui.utils.EaseUserUtils;
import com.ding.easeui.widget.GlideRoundTransform;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/*
 * @author Erick
 * 评论适配器均写在了自己的Activity里，这个已弃用。备份用
 * */

public class Notice_CommentListAdapter extends BaseAdapter {
    private List<NoticeCommentEntity.DataBean> mData;
    private Context mCt;
    private LayoutInflater inflater;
    ViewBundle viewBundle;

    public Notice_CommentListAdapter(ArrayList<NoticeCommentEntity.DataBean> data,
                                     Context ct) {
        mData = data;
        mCt = ct;
        inflater = (LayoutInflater) mCt
                .getSystemService(mCt.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }

    @Override
    public Object getItem(int arg0) {
        return mData.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int arg0, View convertView, ViewGroup arg2) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_notice_comment, null);
            viewBundle = new ViewBundle();
            viewBundle.noticecomment_avatar = (ImageView) convertView
                    .findViewById(R.id.noticecomment_avatar);
            viewBundle.noticecomment_name = (TextView) convertView
                    .findViewById(R.id.noticecomment_name);
            viewBundle.noticecomment_content = (TextView) convertView
                    .findViewById(R.id.noticecomment_content);
            viewBundle.noticecomment_time = (TextView) convertView
                    .findViewById(R.id.noticecomment_time);
            viewBundle.noticecomment_reply = (TextView) convertView
                    .findViewById(R.id.noticecomment_reply);
            convertView.setTag(viewBundle);
        } else {
            viewBundle = (ViewBundle) convertView.getTag();
        }

        Glide.with(mCt)
                .load(Constant.GETHEADIMAG_URL
                        + mData.get(arg0).getUserId()
                        + ".png").transform(new GlideRoundTransform(mCt)).placeholder(com.ding.easeui.R.drawable.ease_default_avatar)
                .into(viewBundle.noticecomment_avatar);
        try {
            if (!mData.get(arg0)
                    .getUserRealname().equals("")) {
                viewBundle.noticecomment_name.setText(mData.get(arg0)
                        .getUserRealname());
            } else {
                viewBundle.noticecomment_name.setText("");
            }
        } catch (Exception e) {
            viewBundle.noticecomment_name.setText("");
        }
        try {
            if (!mData.get(arg0)
                    .getContent().equals("")) {
                viewBundle.noticecomment_content.setText(mData.get(arg0)
                        .getContent());
            } else {
                viewBundle.noticecomment_content.setText("");
            }
        } catch (Exception e) {
            viewBundle.noticecomment_content.setText("");
        }
        try {
            if (!mData.get(arg0)
                    .getCreateTime().equals("")) {
                viewBundle.noticecomment_time.setText(mData.get(arg0)
                        .getCreateTime());
            } else {
                viewBundle.noticecomment_time.setText("");
            }
        } catch (Exception e) {
            viewBundle.noticecomment_time.setText("");
        }
        return convertView;
    }

    private class ViewBundle {
        TextView noticecomment_reply;
        ImageView noticecomment_avatar;
        TextView noticecomment_name;
        TextView noticecomment_content;
        TextView noticecomment_time;
    }

}
