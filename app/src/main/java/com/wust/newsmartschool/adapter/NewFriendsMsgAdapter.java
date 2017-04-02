/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wust.newsmartschool.adapter;

import java.io.FileNotFoundException;
import java.util.List;

import com.bumptech.glide.Glide;
import com.wust.newsmartschool.DemoApplication;
import com.wust.easeui.Constant;
import com.wust.easeui.utils.CommonUtils;
import com.wust.easeui.utils.EaseUserUtils;
import com.wust.easeui.widget.GlideRoundTransform;
import com.hyphenate.chat.EMClient;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.db.InviteMessgeDao;
import com.wust.newsmartschool.domain.InviteMessage;
import com.wust.newsmartschool.domain.InviteMessage.InviteMesageStatus;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

public class NewFriendsMsgAdapter extends ArrayAdapter<InviteMessage> {

    private Context context;
    private InviteMessgeDao messgeDao;

    public NewFriendsMsgAdapter(Context context, int textViewResourceId,
                                List<InviteMessage> objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        messgeDao = new InviteMessgeDao(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.em_row_invite_msg,
                    null);
            holder.avator = (ImageView) convertView.findViewById(R.id.avatar);
            holder.reason = (TextView) convertView.findViewById(R.id.message);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.agree = (Button) convertView.findViewById(R.id.agree);
            holder.refuse = (Button) convertView.findViewById(R.id.refuse);
            holder.groupContainer = (LinearLayout) convertView
                    .findViewById(R.id.ll_group);
            holder.groupname = (TextView) convertView
                    .findViewById(R.id.tv_groupName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String str1 = context.getResources().getString(
                R.string.Has_agreed_to_your_friend_request);
        String str2 = context.getResources().getString(R.string.agree);
        String str3 = context.getResources().getString(
                R.string.Request_to_add_you_as_a_friend);
        String str4 = context.getResources().getString(
                R.string.Apply_to_the_group_of);
        String str5 = context.getResources().getString(R.string.Has_agreed_to);
        String str6 = context.getResources().getString(R.string.Has_refused_to);
        String str7 = context.getResources().getString(R.string.refuse);
        String str8 = context.getResources().getString(
                R.string.invite_join_group);
        String str9 = context.getResources().getString(
                R.string.accept_join_group);
        String str10 = context.getResources().getString(
                R.string.refuse_join_group);

        final InviteMessage msg = getItem(position);
        if (msg != null) {
            holder.agree.setVisibility(View.GONE);
            if (msg.getGroupId() != null) { // 显示群聊提示
                holder.groupContainer.setVisibility(View.VISIBLE);
                holder.groupname.setText(msg.getGroupName());
            } else {
                holder.groupContainer.setVisibility(View.GONE);
            }

            holder.reason.setText(msg.getReason());
            // 设置昵称，现调用环信的
            EaseUserUtils.setUserNick(msg.getFrom(), holder.name);

            boolean isChinese = CommonUtils.isChinesewords(holder.name.getText().toString());
            String realname = DemoApplication.getInstance().mCache.getAsString(msg.getFrom() + "_realname");
            //如果是中文
            if (!isChinese) {
                Log.e("isNum", isChinese + "");
                if (realname == null || realname.equals("")) {
                    try {
                        CommonUtils.getNickname(context, msg.getFrom(), holder.name);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } else {
                    holder.name.setText(realname);
                }

            }

            if (msg.getStatus() == InviteMesageStatus.BEAGREED) {
                holder.refuse.setVisibility(View.GONE);
                holder.reason.setText(str1);
            } else if (msg.getStatus() == InviteMesageStatus.BEINVITEED
                    || msg.getStatus() == InviteMesageStatus.BEAPPLYED
                    || msg.getStatus() == InviteMesageStatus.GROUPINVITATION) {
                holder.agree.setVisibility(View.VISIBLE);
                holder.agree.setEnabled(true);
                holder.agree.setText(str2);

                holder.refuse.setVisibility(View.VISIBLE);
                holder.refuse.setEnabled(true);
                holder.refuse.setText(str7);
                if (msg.getStatus() == InviteMesageStatus.BEINVITEED) {
                    if (msg.getReason() == null) {
                        // 如果没写理由
                        holder.reason.setText(str3);
                    }
                } else if (msg.getStatus() == InviteMesageStatus.BEAPPLYED) { // 入群申请
                    if (TextUtils.isEmpty(msg.getReason())) {
                        holder.reason.setText(str4 + msg.getGroupName());
                    }
                } else if (msg.getStatus() == InviteMesageStatus.GROUPINVITATION) {
                    if (TextUtils.isEmpty(msg.getReason())) {
                        holder.reason.setText(str8 + msg.getGroupName());
                    }
                }

                // 设置点击事件
                holder.agree.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 同意别人发的好友请求
                        acceptInvitation(holder.agree, holder.refuse, msg);
                    }
                });
                holder.refuse.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 拒绝别人发的好友请求
                        refuseInvitation(holder.agree, holder.refuse, msg);
                    }
                });
            } else if (msg.getStatus() == InviteMesageStatus.AGREED) {
                holder.refuse.setText(str5);
                holder.refuse.setTextColor(context.getResources().getColor(R.color.bg_blue));
                holder.refuse.setBackgroundDrawable(null);
                holder.refuse.setEnabled(false);
            } else if (msg.getStatus() == InviteMesageStatus.REFUSED) {
                holder.refuse.setText(str6);
                holder.refuse.setTextColor(context.getResources().getColor(R.color.grgray));
                holder.refuse.setBackgroundDrawable(null);
                holder.refuse.setEnabled(false);
            } else if (msg.getStatus() == InviteMesageStatus.GROUPINVITATION_ACCEPTED) {
//                String str = msg.getGroupInviter() + str9 + msg.getGroupName();
                String str = "用户" + str9 + msg.getGroupName();
                holder.refuse.setVisibility(View.GONE);
                holder.reason.setText(str);
                holder.reason.setMaxEms(20);
            } else if (msg.getStatus() == InviteMesageStatus.GROUPINVITATION_DECLINED) {
//                String str = msg.getGroupInviter() + str10 + msg.getGroupName();
                String str = "用户" + str10 + msg.getGroupName();
                holder.refuse.setVisibility(View.GONE);
                holder.reason.setText(str);
                holder.reason.setMaxEms(20);
            }

            // 设置用户头像
            Glide.with(context)
                    .load(Constant.GETHEADIMAG_URL
                            + msg.getFrom()
                            + ".png").transform(new GlideRoundTransform(context)).placeholder(R.drawable.ease_default_avatar)
                    .into(holder.avator);
            Log.e("ada", msg.getFrom());
//            if (msg.getFrom().equals("1475892132016")) {
//                Glide.with(context)
//                        .load(R.drawable.my_admin).transform(new GlideRoundTransform(context)).placeholder(R.drawable.em_default_avatar)
//                        .into(holder.avator);
//            }
        }

        return convertView;
    }

    /**
     * 同意好友请求或者群申请
     *
     * @param buttonAgree
     * @param buttonRefuse
     * @param msg
     */
    private void acceptInvitation(final Button buttonAgree,
                                  final Button buttonRefuse, final InviteMessage msg) {
        final ProgressDialog pd = new ProgressDialog(context);
        String str1 = context.getResources().getString(R.string.Are_agree_with);
        final String str2 = context.getResources().getString(
                R.string.Has_agreed_to);
        final String str3 = context.getResources().getString(
                R.string.Agree_with_failure);
        pd.setMessage(str1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        new Thread(new Runnable() {
            public void run() {
                // 调用sdk的同意方法
                try {
                    if (msg.getStatus() == InviteMesageStatus.BEINVITEED) {// 同意好友请求
                        EMClient.getInstance().contactManager()
                                .acceptInvitation(msg.getFrom());
                    } else if (msg.getStatus() == InviteMesageStatus.BEAPPLYED) { // 同意加群申请
                        EMClient.getInstance()
                                .groupManager()
                                .acceptApplication(msg.getFrom(),
                                        msg.getGroupId());
                    } else if (msg.getStatus() == InviteMesageStatus.GROUPINVITATION) {
                        EMClient.getInstance()
                                .groupManager()
                                .acceptInvitation(msg.getGroupId(),
                                        msg.getGroupInviter());
                    }
                    msg.setStatus(InviteMesageStatus.AGREED);
                    // 更新db
                    ContentValues values = new ContentValues();
                    values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg
                            .getStatus().ordinal());
                    messgeDao.updateMessage(msg.getId(), values);
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            pd.dismiss();
                            buttonAgree.setText(str2);
                            buttonAgree.setTextColor(context.getResources().getColor(R.color.bg_blue));
                            buttonAgree.setBackgroundDrawable(null);
                            buttonAgree.setEnabled(false);

                            buttonRefuse.setVisibility(View.GONE);
                        }
                    });
                } catch (final Exception e) {
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(context, str3 + e.getMessage(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });

                }
            }
        }).start();
    }

    /**
     * 拒绝好友请求或者群申请
     *
     * @param buttonAgree
     * @param buttonRefuse
     * @param msg
     */
    private void refuseInvitation(final Button buttonAgree,
                                  final Button buttonRefuse, final InviteMessage msg) {
        final ProgressDialog pd = new ProgressDialog(context);
        String str1 = context.getResources()
                .getString(R.string.Are_refuse_with);
        final String str2 = context.getResources().getString(
                R.string.Has_refused_to);
        final String str3 = context.getResources().getString(
                R.string.Refuse_with_failure);
        pd.setMessage(str1);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        new Thread(new Runnable() {
            public void run() {
                // 调用sdk的拒绝方法
                try {
                    if (msg.getStatus() == InviteMesageStatus.BEINVITEED) {// 拒绝好友请求
                        EMClient.getInstance().contactManager()
                                .declineInvitation(msg.getFrom());
                    } else if (msg.getStatus() == InviteMesageStatus.BEAPPLYED) { // 同意加群申请
                        EMClient.getInstance()
                                .groupManager()
                                .declineApplication(msg.getFrom(),
                                        msg.getGroupId(), "");
                    } else if (msg.getStatus() == InviteMesageStatus.GROUPINVITATION) {
                        EMClient.getInstance()
                                .groupManager()
                                .declineInvitation(msg.getGroupId(),
                                        msg.getGroupInviter(), "");
                    }
                    msg.setStatus(InviteMesageStatus.REFUSED);
                    // 更新db
                    ContentValues values = new ContentValues();
                    values.put(InviteMessgeDao.COLUMN_NAME_STATUS, msg
                            .getStatus().ordinal());
                    messgeDao.updateMessage(msg.getId(), values);
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            pd.dismiss();
                            buttonRefuse.setText(str2);
                            buttonRefuse.setTextColor(context.getResources().getColor(R.color.grgray));
                            buttonRefuse.setBackgroundDrawable(null);
                            buttonRefuse.setEnabled(false);

                            buttonAgree.setVisibility(View.GONE);
                        }
                    });
                } catch (final Exception e) {
                    ((Activity) context).runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(context, str3 + e.getMessage(), Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });

                }
            }
        }).start();
    }

    private static class ViewHolder {
        ImageView avator;
        TextView name;
        TextView reason;
        Button agree;
        Button refuse;
        LinearLayout groupContainer;
        TextView groupname;
    }

}
