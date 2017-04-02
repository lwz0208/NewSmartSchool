package com.wust.newsmartschool.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.SearchFriendsEntity;
import com.wust.easeui.Constant;
import com.wust.easeui.utils.PreferenceManager;
import com.wust.easeui.widget.GlideRoundTransform;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

public class AddFriends_Adapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ViewHolder vh;
    List<SearchFriendsEntity.DataBean> persons_list = new ArrayList<>();
    Context mContext;
    int userId;

    public AddFriends_Adapter(Context con,
                              List<SearchFriendsEntity.DataBean> itemlist) {
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
            convertView = mInflater.inflate(R.layout.addfriends_layout, parent, false);
            vh = new ViewHolder();
            vh.search_headerimg = (ImageView) convertView
                    .findViewById(R.id.search_headerimg);
            vh.search_name = (TextView) convertView
                    .findViewById(R.id.search_name);
            vh.addfriend = (TextView) convertView
                    .findViewById(R.id.addfriend);
            vh.depmemt_name = (TextView) convertView
                    .findViewById(R.id.depmemt_name);
            vh.depmem_position = (TextView) convertView
                    .findViewById(R.id.depmem_position);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        userId = persons_list.get(position).getId();
        Glide.with(mContext)
                .load(Constant.GETHEADIMAG_URL
                        + persons_list.get(position).getId()
                        + ".png").transform(new GlideRoundTransform(mContext)).placeholder(R.drawable.ease_default_avatar)
                .into(vh.search_headerimg);
        vh.search_name
                .setText(persons_list.get(position).getName());
        vh.depmemt_name.setText(persons_list.get(position).getDepartmentName());
        vh.depmem_position.setText(persons_list.get(position).getJobtitleName());
        vh.addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext)
                        .setMessage("你确定要加对方为好友吗？")
                        .setCancelable(true)
                        .setPositiveButton("是",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        new Thread(new Runnable() {
                                            public void run() {
                                                String s = PreferenceManager.getInstance().getCurrentRealName();
                                                try {
                                                    EMClient.getInstance()
                                                            .contactManager()
                                                            .addContact(userId + "",
                                                                    s + "请求加你为好友");
                                                } catch (HyphenateException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).start();
                                        Toast.makeText(mContext,
                                                "已发送请求，请等待对方同意", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        ).
                        setNegativeButton("否",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                    }
                                }

                        ).

                        show();
//                new AlertDialog(mContext).builder().setTitle("提示")
//                        .setMsg("你确定要加对方为好友吗？")
//                        .setPositiveButton("确认", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                new Thread(new Runnable() {
//                                    public void run() {
//                                        String s = PreferenceManager.getInstance().getCurrentRealName();
//                                        try {
//                                            EMClient.getInstance()
//                                                    .contactManager()
//                                                    .addContact(PreferenceManager.getInstance().getCurrentUserId(),
//                                                            s + "请求加你为好友");
//                                        } catch (HyphenateException e) {
//                                            // TODO Auto-generated catch block
//                                            e.printStackTrace();
//                                        }
//                                    }
//                                }).start();
//                                Toast.makeText(mContext,
//                                        "已发送请求，请等待", Toast.LENGTH_SHORT).show();
//
//                            }
//                        }).setNegativeButton("取消", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //取消操作
//                    }
//                }).show();
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        private ImageView search_headerimg;
        private TextView search_name;
        private TextView addfriend;
        private TextView depmemt_name;
        private TextView depmem_position;

    }

}
