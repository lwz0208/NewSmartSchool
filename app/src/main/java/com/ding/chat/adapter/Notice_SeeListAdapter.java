package com.ding.chat.adapter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.ding.chat.DemoApplication;
import com.ding.chat.R;
import com.ding.chat.domain.NoticeCommentEntity;
import com.ding.easeui.Constant;
import com.ding.easeui.utils.CommonUtils;
import com.ding.easeui.utils.EaseUserUtils;
import com.ding.easeui.widget.GlideRoundTransform;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/*
 * @author rockstore
 * */

public class Notice_SeeListAdapter extends BaseAdapter {
    private List<String> mData;
    private Context mCt;
    private LayoutInflater inflater;
    ViewBundle viewBundle;

    public Notice_SeeListAdapter(ArrayList<String> data,
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
    public String getItem(int arg0) {
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
            convertView = inflater.inflate(R.layout.item_notice_see, null);
            viewBundle = new ViewBundle();
            viewBundle.noticecomment_avatar = (ImageView) convertView
                    .findViewById(R.id.noticecomment_avatar);
            viewBundle.noticecomment_name = (TextView) convertView
                    .findViewById(R.id.noticecomment_name);
            viewBundle.noticecomment_content = (TextView) convertView
                    .findViewById(R.id.noticecomment_content);
            viewBundle.noticecomment_time = (TextView) convertView
                    .findViewById(R.id.noticecomment_time);
            convertView.setTag(viewBundle);
        } else {
            viewBundle = (ViewBundle) convertView.getTag();
        }
        Glide.with(mCt)
                .load(Constant.GETHEADIMAG_URL
                        + mData.get(arg0)
                        + ".png").transform(new GlideRoundTransform(mCt)).placeholder(com.ding.easeui.R.drawable.ease_default_avatar)
                .into(viewBundle.noticecomment_avatar);
        viewBundle.noticecomment_name.setText(mData.get(arg0));
        // 设置昵称，现调用环信的
//        EaseUserUtils.setUserNick(mData.get(arg0)
//                + "", viewBundle.noticecomment_name);
//        String realname = DemoApplication.getInstance().mCache.getAsString(mData.get(arg0)
//                + "_realname");
//        boolean isChinese = CommonUtils.isChinesewords(viewBundle.noticecomment_name.getText().toString());
//        Log.e("isChinese", isChinese + "");
//        Log.e("realname", realname + "");
//        if (!isChinese) {
//            if (realname == null || realname.equals("")) {
//                try {
//                    CommonUtils.getNickname(mCt, mData.get(arg0)
//                            + "", viewBundle.noticecomment_name);
//                } catch (FileNotFoundException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//            } else {
//                viewBundle.noticecomment_name.setText(realname);
//            }
//        } else {
//            EaseUserUtils.setUserNick(mData.get(arg0)
//                    + "", viewBundle.noticecomment_name);
//        }
        return convertView;
    }

    private class ViewBundle {

        ImageView noticecomment_avatar;
        TextView noticecomment_name;
        TextView noticecomment_content;
        TextView noticecomment_time;
    }

}
