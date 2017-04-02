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
package com.wust.newsmartschool.ui;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.wust.easeui.utils.PreferenceManager;
import com.wust.easeui.widget.GlideCircleImage;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.chat.EMGroup;
import com.bumptech.glide.Glide;
import com.wust.newsmartschool.DemoApplication;
import com.wust.newsmartschool.R;
import com.wust.easeui.Constant;
import com.wust.easeui.utils.CommonUtils;
import com.wust.easeui.widget.EaseAlertDialog;
import com.wust.easeui.widget.EaseAlertDialog.AlertDialogUser;
import com.wust.easeui.widget.EaseExpandGridView;
import com.wust.easeui.widget.EaseSwitchButton;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.NetUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class GroupDetailsActivity extends BaseActivity implements
        OnClickListener {
    private static final String TAG = "GroupDetailsActivity";
    private static final int REQUEST_CODE_ADD_USER = 0;
    private static final int REQUEST_CODE_EXIT = 1;
    private static final int REQUEST_CODE_EXIT_DELETE = 2;
    private static final int REQUEST_CODE_EDIT_GROUPNAME = 5;

    private EaseExpandGridView userGridview;
    private String groupId;
    private ProgressBar loadingPB;
    private Button exitBtn;
    private Button deleteBtn;
    private EMGroup group;
    private GridAdapter adapter;
    private GridAdapterNoOwner adapternoOwner;
    private ProgressDialog progressDialog;

    private RelativeLayout rl_switch_block_groupmsg;

    public static GroupDetailsActivity instance;

    String st = "";
    // 清空所有聊天记录
    private RelativeLayout clearAllHistory;
    private RelativeLayout blacklistLayout;
    private RelativeLayout changeGroupNameLayout;
    private RelativeLayout idLayout;
    private RelativeLayout rl_group_name;
    private RelativeLayout rl_switch_getmsg_nobibi;
    private RelativeLayout rl_group_notice;
    private RelativeLayout ll_allgroupnums;
    private EaseSwitchButton switch_btn_getmsg_nobibi;
    private TextView idText;
    private EaseSwitchButton switchButton;
    private GroupChangeListener groupChangeListener;
    private RelativeLayout searchLayout;

    // 自己添加的字段
    private ScrollView group_detail_outscroll;
    private TextView bottom_group_name;
    private LinearLayout ll_groupdetails_search;
    private LinearLayout ll_groupdetails_notice;
    private TextView tv_group_name_value;

    private TextView allgroupnums;
    List<String> members;
    List<String> memberstoadapter;
    //群主是否是自己
    private boolean isGroupOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 获取传过来的groupid
        groupId = getIntent().getStringExtra("groupId");
        group = EMClient.getInstance().groupManager().getGroup(groupId);

        // we are not supposed to show the group if we don't find the group
        if (group == null) {
            finish();
            return;
        }

        setContentView(R.layout.em_activity_group_details);
        instance = this;
        int width = MainActivity.getScreenWidth(getApplicationContext()) / 4;
        int avatar_width = MainActivity.getScreenWidth(getApplicationContext()) / 10;
        allgroupnums = (TextView) findViewById(R.id.allgroupnums);
        ll_allgroupnums = (RelativeLayout) findViewById(R.id.ll_allgroupnums);
        st = getResources().getString(R.string.people);
        bottom_group_name = (TextView) findViewById(R.id.bottom_group_name);
        clearAllHistory = (RelativeLayout) findViewById(R.id.clear_all_history);
        userGridview = (EaseExpandGridView) findViewById(R.id.gridview);
        loadingPB = (ProgressBar) findViewById(R.id.progressBar);
        group_detail_outscroll = (ScrollView) findViewById(R.id.group_detail_outscroll);
        group_detail_outscroll.smoothScrollTo(0, 0);
        tv_group_name_value = (TextView) findViewById(R.id.tv_group_name_value);
        tv_group_name_value.setText(group.getGroupName());
        exitBtn = (Button) findViewById(R.id.btn_exit_grp);
        ll_groupdetails_search = (LinearLayout) findViewById(R.id.ll_groupdetails_search);
        ll_groupdetails_notice = (LinearLayout) findViewById(R.id.ll_groupdetails_notice);
        ll_groupdetails_search.setOnClickListener(this);
        ll_groupdetails_notice.setOnClickListener(this);
        deleteBtn = (Button) findViewById(R.id.btn_exitdel_grp);
        blacklistLayout = (RelativeLayout) findViewById(R.id.rl_blacklist);
        changeGroupNameLayout = (RelativeLayout) findViewById(R.id.rl_change_group_name);
        idLayout = (RelativeLayout) findViewById(R.id.rl_group_id);

        rl_group_name = (RelativeLayout) findViewById(R.id.rl_group_name);
        rl_group_name.setVisibility(View.VISIBLE);
        idLayout.setVisibility(View.VISIBLE);
        idText = (TextView) findViewById(R.id.tv_group_id_value);

        rl_switch_block_groupmsg = (RelativeLayout) findViewById(R.id.rl_switch_block_groupmsg);
        switchButton = (EaseSwitchButton) findViewById(R.id.switch_btn);
        searchLayout = (RelativeLayout) findViewById(R.id.rl_search);
        rl_group_notice = (RelativeLayout) findViewById(R.id.rl_group_notice);
        //新添加
        rl_switch_getmsg_nobibi = (RelativeLayout) findViewById(R.id.rl_switch_getmsg_nobibi);
        switch_btn_getmsg_nobibi = (EaseSwitchButton) findViewById(R.id.switch_btn_getmsg_nobibi);
        // 保证每次进详情看到的都是最新的group
        updateGroup();
        idText.setText(groupId);
        if (group.getOwner() == null
                || "".equals(group.getOwner())
                || !group.getOwner().equals(
                EMClient.getInstance().getCurrentUser())) {
            isGroupOwner = false;
            exitBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
            blacklistLayout.setVisibility(View.GONE);
            changeGroupNameLayout.setVisibility(View.GONE);
        } else {
            // 如果自己是群主，显示解散按钮
            isGroupOwner = true;
            exitBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.VISIBLE);
        }

        groupChangeListener = new GroupChangeListener();
        EMClient.getInstance().groupManager()
                .addGroupChangeListener(groupChangeListener);

        ((TextView) findViewById(R.id.group_name))
                .setText(group.getGroupName());
        allgroupnums.setText("全部群成员" + "(" + group.getAffiliationsCount() + st);
        bottom_group_name.setText(group.getGroupName());

        members = new ArrayList<String>();
        memberstoadapter = new ArrayList<String>();
        members.addAll(group.getMembers());
        //如果不是群主，就显示五个人。
        if (!isGroupOwner) {
            if (members.size() > 5) {
                for (int i = 0; i < 5; i++) {
                    memberstoadapter.add(members.get(i));
                }
            } else {
                memberstoadapter.addAll(members);
            }
            adapternoOwner = new GridAdapterNoOwner(this, R.layout.em_grid, memberstoadapter);
            userGridview.setAdapter(adapternoOwner);

        } else {
            //如果不是群主，就显示三个人。外加+-按钮一共也是五个。
            if (members.size() > 3) {
                for (int i = 0; i < 3; i++) {
                    memberstoadapter.add(members.get(i));
                }

            } else {
                memberstoadapter.addAll(members);
            }
            adapter = new GridAdapter(this, R.layout.em_grid, memberstoadapter);
            userGridview.setAdapter(adapter);
        }

        Log.e("members.size()", members.size() + "");


        // 设置OnTouchListener
        userGridview.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (adapter.isInDeleteMode) {
                            adapter.isInDeleteMode = false;
                            adapter.notifyDataSetChanged();
                            return true;
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });


        clearAllHistory.setOnClickListener(this);
        rl_group_name.setOnClickListener(this);
        blacklistLayout.setOnClickListener(this);
        changeGroupNameLayout.setOnClickListener(this);
        rl_switch_block_groupmsg.setOnClickListener(this);
        rl_switch_getmsg_nobibi.setOnClickListener(this);
        searchLayout.setOnClickListener(this);
        rl_group_notice.setOnClickListener(this);
        ll_allgroupnums.setOnClickListener(this);

        if (PreferenceManager.getInstance().getGroupMsgNobibi(groupId).equals("true")) {
            switch_btn_getmsg_nobibi.openSwitch();
        } else {
            switch_btn_getmsg_nobibi.closeSwitch();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String st1 = getResources().getString(R.string.being_added);
        String st2 = getResources().getString(R.string.is_quit_the_group_chat);
        String st3 = getResources().getString(R.string.chatting_is_dissolution);
        String st4 = getResources().getString(R.string.are_empty_group_of_news);
        String st5 = getResources()
                .getString(R.string.is_modify_the_group_name);
        final String st6 = getResources().getString(
                R.string.Modify_the_group_name_successful);
        final String st7 = getResources().getString(
                R.string.change_the_group_name_failed_please);

        if (resultCode == RESULT_OK) {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(GroupDetailsActivity.this);
                progressDialog.setMessage(st1);
                progressDialog.setCanceledOnTouchOutside(false);
            }
            switch (requestCode) {
                case REQUEST_CODE_ADD_USER:// 添加群成员
                    String pickeduserid = data.getStringExtra("members_userid");
                    String[] newmembers = pickeduserid.split(",");
                    progressDialog.setMessage(st1);
                    progressDialog.show();

                    addMembersToGroup(newmembers);
                    break;
                case REQUEST_CODE_EXIT: // 退出群
                    progressDialog.setMessage(st2);
                    progressDialog.show();
                    exitGrop();
                    break;
                case REQUEST_CODE_EXIT_DELETE: // 解散群
                    progressDialog.setMessage(st3);
                    progressDialog.show();
                    deleteGrop();
                    break;

                case REQUEST_CODE_EDIT_GROUPNAME: // 修改群名称
                    final String returnData = data.getStringExtra("data");
                    if (!TextUtils.isEmpty(returnData)) {
                        progressDialog.setMessage(st5);
                        progressDialog.show();

                        new Thread(new Runnable() {
                            public void run() {
                                try {
                                    EMClient.getInstance().groupManager()
                                            .changeGroupName(groupId, returnData);
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            ((TextView) findViewById(R.id.group_name))
                                                    .setText(returnData);
                                            allgroupnums.setText("全部群成员" + "(" + group.getAffiliationsCount() + st);
                                            tv_group_name_value.setText(returnData);
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(),
                                                    st6, Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            progressDialog.dismiss();
                                            Toast.makeText(getApplicationContext(),
                                                    st7, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }).start();
                    }
                    break;
                default:
                    refreshMembers();
                    break;
            }
        }
    }

    protected void addUserToBlackList(final String username) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);
        pd.setMessage(getString(R.string.Are_moving_to_blacklist));
        pd.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().groupManager()
                            .blockUser(groupId, username);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            refreshMembers();
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    R.string.Move_into_blacklist_success, Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                } catch (HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    R.string.failed_to_move_into, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void refreshMembers() {
        members.clear();
        memberstoadapter.clear();
        members.addAll(group.getMembers());
        allgroupnums.setText("全部群成员" + "(" + group.getAffiliationsCount() + st);
        //如果不是群主，就显示五个人。
        if (!isGroupOwner) {
            memberstoadapter.clear();
            if (members.size() > 5) {
                for (int i = 0; i < 5; i++) {
                    memberstoadapter.add(members.get(i));
                }
            } else {
                memberstoadapter.addAll(members);
            }
            adapternoOwner = new GridAdapterNoOwner(this, R.layout.em_grid, memberstoadapter);
            userGridview.setAdapter(adapternoOwner);

        } else {
            //如果不是群主，就显示三个人。外加+-按钮一共也是五个。
            if (members.size() > 3) {
                for (int i = 0; i < 3; i++) {
                    memberstoadapter.add(members.get(i));
                }

            } else {
                memberstoadapter.addAll(members);
            }
            adapter = new GridAdapter(this, R.layout.em_grid, memberstoadapter);
            userGridview.setAdapter(adapter);
        }

    }

    /**
     * 点击退出群组按钮
     *
     * @param view
     */
    public void exitGroup(View view) {
        startActivityForResult(new Intent(this, ExitGroupDialog.class),
                REQUEST_CODE_EXIT);
    }

    /**
     * 点击解散群组按钮
     *
     * @param view
     */
    public void exitDeleteGroup(View view) {
        startActivityForResult(
                new Intent(this, ExitGroupDialog.class).putExtra("deleteToast",
                        getString(R.string.dissolution_group_hint)),
                REQUEST_CODE_EXIT_DELETE);
    }

    /**
     * 清空群聊天记录
     */
    private void clearGroupHistory() {

        EMConversation conversation = EMClient
                .getInstance()
                .chatManager()
                .getConversation(group.getGroupId(),
                        EMConversationType.GroupChat);
        if (conversation != null) {
            conversation.clearAllMessages();
        }
        Toast.makeText(this, R.string.messages_are_empty, Toast.LENGTH_SHORT).show();
    }

    /**
     * 退出群组
     */
    private void exitGrop() {
        String st1 = getResources().getString(
                R.string.Exit_the_group_chat_failure);
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().groupManager().leaveGroup(groupId);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            setResult(RESULT_OK);
                            finish();
                            if (ChatActivity.activityInstance != null)
                                ChatActivity.activityInstance.finish();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(
                                    getApplicationContext(),
                                    getResources()
                                            .getString(
                                                    R.string.Exit_the_group_chat_failure)
                                            + " " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 解散群组
     */
    private void deleteGrop() {
        final String st5 = getResources().getString(
                R.string.Dissolve_group_chat_tofail);
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().groupManager().destroyGroup(groupId);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            setResult(RESULT_OK);
                            finish();
                            if (ChatActivity.activityInstance != null)
                                ChatActivity.activityInstance.finish();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    st5 + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 增加群成员
     *
     * @param newmembers
     */
    private void addMembersToGroup(final String[] newmembers) {
        final String st6 = getResources().getString(
                R.string.Add_group_members_fail);
        new Thread(new Runnable() {

            public void run() {
                try {
                    // 创建者调用add方法
                    if (EMClient.getInstance().getCurrentUser()
                            .equals(group.getOwner())) {
                        EMClient.getInstance().groupManager()
                                .addUsersToGroup(groupId, newmembers);
                    } else {
                        // 一般成员调用invite方法
                        EMClient.getInstance().groupManager()
                                .inviteUser(groupId, newmembers, null);
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(instance, "邀请已发出，待对方同意。", Toast.LENGTH_SHORT)
                                    .show();
                            refreshMembers();
                            ((TextView) findViewById(R.id.group_name))
                                    .setText(group.getGroupName());
                            allgroupnums.setText("全部群成员" + "(" + group.getAffiliationsCount() + st);
                            progressDialog.dismiss();

                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    st6 + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_switch_block_groupmsg: // 屏蔽或取消屏蔽群组
                toggleBlockGroup();
                break;
            case R.id.rl_switch_getmsg_nobibi: // 屏蔽哔哔但还是需接受消息
                if (switch_btn_getmsg_nobibi.isSwitchOpen()) {
                    switch_btn_getmsg_nobibi.closeSwitch();
                    //将信息缓存起来
                    PreferenceManager.getInstance().setGroupMsgNobibi(groupId, "false");
                } else {
                    switch_btn_getmsg_nobibi.openSwitch();
                    PreferenceManager.getInstance().setGroupMsgNobibi(groupId, "true");
                }
                break;

            case R.id.clear_all_history: // 清空聊天记录
                String st9 = getResources().getString(R.string.sure_to_empty_this);
                new EaseAlertDialog(GroupDetailsActivity.this, null, st9, null,
                        new AlertDialogUser() {

                            @Override
                            public void onResult(boolean confirmed, Bundle bundle) {
                                if (confirmed) {
                                    clearGroupHistory();
                                }
                            }
                        }, true).show();

                break;

            case R.id.rl_blacklist: // 黑名单列表
                startActivity(new Intent(GroupDetailsActivity.this,
                        GroupBlacklistActivity.class).putExtra("groupId", groupId));
                break;

            case R.id.rl_change_group_name:
                startActivityForResult(
                        new Intent(this, EditActivity.class).putExtra("data",
                                group.getGroupName()), REQUEST_CODE_EDIT_GROUPNAME);
                break;
            case R.id.rl_search:
                startActivity(new Intent(this, GroupSearchMessageActivity.class)
                        .putExtra("groupId", groupId));

                break;
            case R.id.rl_group_name:
                if (group.getOwner() == null
                        || "".equals(group.getOwner())
                        || !group.getOwner().equals(
                        EMClient.getInstance().getCurrentUser())) {
                    Toast.makeText(this, "抱歉你无权修改群名", Toast.LENGTH_SHORT).show();

                } else if (EMClient.getInstance().getCurrentUser()
                        .equals(group.getOwner())) {
                    startActivityForResult(
                            new Intent(this, EditActivity.class).putExtra("data",
                                    group.getGroupName()),
                            REQUEST_CODE_EDIT_GROUPNAME);

                }

                break;
            case R.id.ll_groupdetails_search:
                startActivity(new Intent(this, GroupSearchMessageActivity.class)
                        .putExtra("groupId", groupId));
                break;
            case R.id.ll_groupdetails_notice:
                startActivity(new Intent(this, EditeNoticeActivity.class).putExtra(
                        "groupId", groupId));
                break;
            case R.id.ll_allgroupnums:
                startActivityForResult(new Intent(this, GroupMembersActivity.class).putExtra(
                        "members", (Serializable) members).putExtra("isGroupOwner", isGroupOwner).putExtra("groupId", groupId), 0);
                break;
            case R.id.rl_group_notice:
                startActivity(new Intent(this, EditeNoticeActivity.class).putExtra(
                        "groupId", groupId));
                break;
            default:
                break;
        }

    }

    private void toggleBlockGroup() {
        if (switchButton.isSwitchOpen()) {
            EMLog.d(TAG, "change to unblock group msg");
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(GroupDetailsActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
            }
            progressDialog.setMessage(getString(R.string.Is_unblock));
            progressDialog.show();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        EMClient.getInstance().groupManager()
                                .unblockGroupMessage(groupId);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                switchButton.closeSwitch();
                                progressDialog.dismiss();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),
                                        R.string.remove_group_of, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        } else {
            String st8 = getResources().getString(R.string.group_is_blocked);
            final String st9 = getResources().getString(
                    R.string.group_of_shielding);
            EMLog.d(TAG, "change to block group msg");
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(GroupDetailsActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
            }
            progressDialog.setMessage(st8);
            progressDialog.show();
            new Thread(new Runnable() {
                public void run() {
                    try {
                        EMClient.getInstance().groupManager()
                                .blockGroupMessage(groupId);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                switchButton.openSwitch();
                                progressDialog.dismiss();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), st9, Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                    }

                }
            }).start();
        }
    }


    /**
     * 群组成员gridadapter
     *
     * @author admin_new
     */
    public class GridAdapter extends ArrayAdapter<String> {

        private int res;
        public boolean isInDeleteMode;
        private List<String> objects;

        public GridAdapter(Context context, int textViewResourceId,
                           List<String> objects) {
            super(context, textViewResourceId, objects);
            this.objects = objects;
            res = textViewResourceId;
            isInDeleteMode = false;
        }

        @Override
        public View getView(final int position, View convertView,
                            final ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(res,
                        null);
                holder.imageView = (ImageView) convertView
                        .findViewById(R.id.iv_avatar);
                holder.textView = (TextView) convertView
                        .findViewById(R.id.tv_name);
                holder.badgeDeleteView = (ImageView) convertView
                        .findViewById(R.id.badge_delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final LinearLayout button = (LinearLayout) convertView
                    .findViewById(R.id.button_avatar);
            // 最后一个item，减人按钮
            if (position == getCount() - 1) {
                holder.textView.setText("");
                // 设置成删除按钮
                holder.imageView
                        .setImageResource(R.drawable.em_smiley_minus_btn);
                // 如果不是创建者或者没有相应权限，不提供加减人按钮
                if (!group.getOwner().equals(
                        EMClient.getInstance().getCurrentUser())) {
                    // if current user is not group admin, hide add/remove btn
                    convertView.setVisibility(View.INVISIBLE);
                } else { // 显示删除按钮
                    if (isInDeleteMode) {
                        // 正处于删除模式下，隐藏删除按钮
                        convertView.setVisibility(View.INVISIBLE);
                    } else {
                        // 正常模式
                        convertView.setVisibility(View.VISIBLE);
                        convertView.findViewById(R.id.badge_delete)
                                .setVisibility(View.INVISIBLE);
                    }
                    final String st10 = getResources().getString(
                            R.string.The_delete_button_is_clicked);
                    button.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EMLog.d(TAG, st10);
                            isInDeleteMode = true;
                            notifyDataSetChanged();
                        }
                    });
                }
            } else if (position == getCount() - 2) { // 添加群组成员按钮
                holder.textView.setText("");
                holder.imageView.setImageResource(R.drawable.em_smiley_add_btn);
                // 如果不是创建者或者没有相应权限
                if (!group.isAllowInvites()
                        && !group.getOwner().equals(
                        EMClient.getInstance().getCurrentUser())) {
                    convertView.setVisibility(View.INVISIBLE);
                } else {
                    // 正处于删除模式下,隐藏添加按钮
                    if (isInDeleteMode) {
                        convertView.setVisibility(View.INVISIBLE);
                    } else {
                        convertView.setVisibility(View.VISIBLE);
                        convertView.findViewById(R.id.badge_delete)
                                .setVisibility(View.INVISIBLE);
                    }
                    final String st11 = getResources().getString(
                            R.string.Add_a_button_was_clicked);
                    button.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EMLog.d(TAG, st11);
                            // 进入选人页面
                            startActivityForResult((new Intent(
                                    GroupDetailsActivity.this,
                                    NoticeChooseMemActivitySingleChoose.class).putExtra(
                                    "groupId", groupId)), REQUEST_CODE_ADD_USER);
                        }
                    });
                }
            } else { // 正常普通item，显示群组成员
                final String username = getItem(position);
                convertView.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
                String Key_RealName;
                Key_RealName = username + "_realname";
                String realname = DemoApplication.getInstance().mCache
                        .getAsString(Key_RealName);
                if (realname == null) {
                    try {
                        CommonUtils.getNickname(getContext(), username,
                                holder.textView);
                    } catch (FileNotFoundException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    } catch (JSONException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }

                } else if (DemoApplication.getInstance().mCache
                        .getAsString(Key_RealName) != null) {
                    holder.textView
                            .setText(DemoApplication.getInstance().mCache
                                    .getAsString(Key_RealName));
                } else if (realname != null) {
                    holder.textView.setText(realname);
                }

                Glide.with(getContext())
                        .load(Constant.GETHEADIMAG_URL + username + ".png").transform(new GlideCircleImage(getContext()))
                        .placeholder(R.drawable.ease_default_avatar)
                        .into(holder.imageView);
                if (isInDeleteMode) {
                    // 如果是删除模式下，显示减人图标
                    convertView.findViewById(R.id.badge_delete).setVisibility(
                            View.VISIBLE);
                } else {
                    convertView.findViewById(R.id.badge_delete).setVisibility(
                            View.INVISIBLE);
                }
                final String st12 = getResources().getString(
                        R.string.not_delete_myself);
                final String st13 = getResources().getString(
                        R.string.Are_removed);
                final String st14 = getResources().getString(
                        R.string.Delete_failed);
                final String st15 = getResources().getString(
                        R.string.confirm_the_members);
                button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isInDeleteMode) {
                            // 如果是删除自己，return
                            if (EMClient.getInstance().getCurrentUser()
                                    .equals(username)) {
                                new EaseAlertDialog(GroupDetailsActivity.this,
                                        st12).show();
                                return;
                            }
                            if (!NetUtils.hasNetwork(getApplicationContext())) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        getString(R.string.network_unavailable),
                                        Toast.LENGTH_SHORT).show();
                                return;
                            }
                            EMLog.d("group", "remove user from group:"
                                    + username);
                            deleteMembersFromGroup(username);
                        } else {
                            // 正常情况下点击user，可以进入用户详情或者聊天页面等等
                            startActivity(new Intent(getApplicationContext(),
                                    DeptMemInfoActivity2.class).putExtra("userId",
                                    username));
                            if (android.os.Build.VERSION.SDK_INT > 5) {
                                overridePendingTransition(R.anim.slide_in_from_right,
                                        R.anim.slide_out_to_left);
                            }

                        }
                    }

                    /**
                     * 删除群成员
                     *
                     * @param username
                     */
                    protected void deleteMembersFromGroup(final String username) {
                        final ProgressDialog deleteDialog = new ProgressDialog(
                                GroupDetailsActivity.this);
                        deleteDialog.setMessage(st13);
                        deleteDialog.setCanceledOnTouchOutside(false);
                        deleteDialog.show();
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    // 删除被选中的成员
                                    EMClient.getInstance()
                                            .groupManager()
                                            .removeUserFromGroup(groupId,
                                                    username);
                                    isInDeleteMode = false;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            deleteDialog.dismiss();
                                            refreshMembers();
                                            ((TextView) findViewById(R.id.group_name))
                                                    .setText(group
                                                            .getGroupName());
                                            allgroupnums.setText("全部群成员" + "(" + group.getAffiliationsCount() + st);
                                        }
                                    });
                                } catch (final Exception e) {
                                    deleteDialog.dismiss();
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(
                                                    getApplicationContext(),
                                                    st14 + e.getMessage(), Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    });
                                }

                            }
                        }).start();
                    }
                });

                button.setOnLongClickListener(new OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {
                        if (EMClient.getInstance().getCurrentUser()
                                .equals(username))
                            return true;
                        if (group.getOwner().equals(
                                EMClient.getInstance().getCurrentUser())) {
                            new EaseAlertDialog(GroupDetailsActivity.this,
                                    null, st15, null, new AlertDialogUser() {

                                @Override
                                public void onResult(boolean confirmed,
                                                     Bundle bundle) {
                                    if (confirmed) {
                                        addUserToBlackList(username);
                                    }
                                }
                            }, true).show();

                        }
                        return false;
                    }
                });
            }
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount() + 2;
        }
    }

    /**
     * 群组成员gridadapter
     *
     * @author admin_new
     */
    public class GridAdapterNoOwner extends ArrayAdapter<String> {

        private int res;
        private List<String> objects;

        public GridAdapterNoOwner(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            this.objects = objects;
            res = textViewResourceId;
        }

        @Override
        public View getView(final int position, View convertView,
                            final ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(getContext()).inflate(res,
                        null);
                holder.imageView = (ImageView) convertView
                        .findViewById(R.id.iv_avatar);
                holder.textView = (TextView) convertView
                        .findViewById(R.id.tv_name);
                holder.badgeDeleteView = (ImageView) convertView
                        .findViewById(R.id.badge_delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final LinearLayout button = (LinearLayout) convertView
                    .findViewById(R.id.button_avatar);
            final String username = getItem(position);
            convertView.setVisibility(View.VISIBLE);
            button.setVisibility(View.VISIBLE);
            String Key_RealName;
            Key_RealName = username + "_realname";
            String realname = DemoApplication.getInstance().mCache
                    .getAsString(Key_RealName);
            if (realname == null) {
                try {
                    CommonUtils.getNickname(getContext(), username,
                            holder.textView);
                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (JSONException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

            } else if (DemoApplication.getInstance().mCache
                    .getAsString(Key_RealName) != null) {
                holder.textView
                        .setText(DemoApplication.getInstance().mCache
                                .getAsString(Key_RealName));
            } else if (realname != null) {
                holder.textView.setText(realname);
            }

            Glide.with(getContext())
                    .load(Constant.GETHEADIMAG_URL + username + ".png").transform(new GlideCircleImage(getContext()))
                    .placeholder(R.drawable.ease_default_avatar)
                    .into(holder.imageView);
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 正常情况下点击user，可以进入用户详情或者聊天页面等等
                    startActivity(new Intent(getApplicationContext(),
                            DeptMemInfoActivity2.class).putExtra("userId",
                            username));
                    if (android.os.Build.VERSION.SDK_INT > 5) {
                        overridePendingTransition(R.anim.slide_in_from_right,
                                R.anim.slide_out_to_left);
                    }

                }

            });
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount();
        }
    }


    protected void updateGroup() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    EMClient.getInstance().groupManager()
                            .getGroupFromServer(groupId);

                    runOnUiThread(new Runnable() {
                        public void run() {
                            ((TextView) findViewById(R.id.group_name))
                                    .setText(group.getGroupName());
                            allgroupnums.setText("全部群成员" + "(" + group.getAffiliationsCount() + st);
                            loadingPB.setVisibility(View.INVISIBLE);
                            refreshMembers();
                            if (EMClient.getInstance().getCurrentUser()
                                    .equals(group.getOwner())) {
                                // 显示解散按钮
                                exitBtn.setVisibility(View.GONE);
                                deleteBtn.setVisibility(View.VISIBLE);
                            } else {
                                // 显示退出按钮
                                exitBtn.setVisibility(View.VISIBLE);
                                deleteBtn.setVisibility(View.GONE);
                            }

                            // update block
                            EMLog.d(TAG,
                                    "group msg is blocked:"
                                            + group.isMsgBlocked());
                            if (group.isMsgBlocked()) {
                                switchButton.openSwitch();
                            } else {
                                switchButton.closeSwitch();
                            }
                        }
                    });

                } catch (Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            loadingPB.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        }).start();
    }

    public void back(View view) {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView textView;
        ImageView badgeDeleteView;
    }

    private class GroupChangeListener implements EMGroupChangeListener {

        @Override
        public void onInvitationReceived(String groupId, String groupName,
                                         String inviter, String reason) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onApplicationReceived(String groupId, String groupName,
                                          String applyer, String reason) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onApplicationAccept(String groupId, String groupName,
                                        String accepter) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onApplicationDeclined(String groupId, String groupName,
                                          String decliner, String reason) {

        }

        @Override
        public void onInvitationAccepted(String groupId, String inviter,
                                         String reason) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    refreshMembers();
                }

            });

        }

        @Override
        public void onInvitationDeclined(String groupId, String invitee,
                                         String reason) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onUserRemoved(String groupId, String groupName) {
            finish();

        }

        @Override
        public void onGroupDestroyed(String groupId, String groupName) {
            finish();

        }

        @Override
        public void onAutoAcceptInvitationFromGroup(String groupId,
                                                    String inviter, String inviteMessage) {
            // TODO Auto-generated method stub

        }

    }

}
