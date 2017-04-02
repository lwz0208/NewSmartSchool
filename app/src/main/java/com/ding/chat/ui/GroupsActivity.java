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
package com.ding.chat.ui;

import java.util.List;

import com.ding.easeui.utils.PreferenceManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager.EMGroupOptions;
import com.ding.chat.R;
import com.ding.chat.adapter.GroupAdapter;
import com.ding.easeui.Constant;
import com.hyphenate.exceptions.HyphenateException;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class GroupsActivity extends BaseActivity {
    public static final String TAG = "GroupsActivity_Debugs";
    private ListView groupListView;
    protected List<EMGroup> grouplist;
    private GroupAdapter groupAdapter;
    private InputMethodManager inputMethodManager;
    public static GroupsActivity instance;
    private View progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog progressDialog;

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            swipeRefreshLayout.setRefreshing(false);
            switch (msg.what) {
                case 0:
                    refresh();
                    break;
                case 1:
                    Toast.makeText(GroupsActivity.this,
                            R.string.Failed_to_get_group_chat_information,
                            Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_fragment_groups);
        instance = this;
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        grouplist = EMClient.getInstance().groupManager().getAllGroups();
        groupListView = (ListView) findViewById(R.id.list);
        // show group list
        groupAdapter = new GroupAdapter(this, 1, grouplist);
        groupListView.setAdapter(groupAdapter);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright,
                R.color.holo_green_light, R.color.holo_orange_light,
                R.color.holo_red_light);
        // 下拉刷新
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().groupManager()
                                    .getJoinedGroupsFromServer();
                            handler.sendEmptyMessage(0);
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(1);
                        }
                    }
                }.start();
            }
        });

        groupListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 1) {
                    // 新建群聊
                    // startActivityForResult(new Intent(GroupsActivity.this,
                    // NewGroupActivity.class), 0);
//                    startActivityForResult(new Intent(GroupsActivity.this,
//                            GroupPickContactsActivity.class), 0);
                    startActivityForResult((new Intent(GroupsActivity.this,
                                    NoticeChooseMemActivitySingleChoose.class)),
                            Constant.CHOOSE_NOTICEPEOPLE);
                } else if (position == 2) {
                    // 添加公开群
                    startActivityForResult(new Intent(GroupsActivity.this,
                            PublicGroupsActivity.class), 0);
                } else {
                    // 进入群聊
                    Intent intent = new Intent(GroupsActivity.this,
                            ChatActivity.class);
                    // it is group chat
                    intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
                    intent.putExtra("userId", groupAdapter
                            .getItem(position - 3).getGroupId());
                    startActivityForResult(intent, 0);
                }
            }

        });
        groupListView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                    if (getCurrentFocus() != null)
                        inputMethodManager.hideSoftInputFromWindow(
                                getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String st1 = getResources().getString(
                R.string.Is_to_create_a_group_chat);
        final String st2 = getResources().getString(
                R.string.Failed_to_create_groups);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.CHOOSE_NOTICEPEOPLE:// 添加群成员
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage(st1);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    String checkisone = data.getStringExtra("members_userid");
                    String[] checkmemnum = checkisone.split(",");
                    if (checkmemnum.length > 1) {
                        // 新建群组
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 调用sdk创建群组方法
//                            String[] members = data.getStringArrayExtra("newmembers");
                                //返回来的选择人员的userId
                                String pickeduserid = data.getStringExtra("members_userid");
                                String[] members = pickeduserid.split(",");
                                String groupName = PreferenceManager.getInstance().getCurrentRealName()
                                        + "的讨论组";

                                String desc = PreferenceManager.getInstance().getCurrentRealName()
                                        + "建立的讨论组";
                                try {
                                    EMGroupOptions option = new EMGroupOptions();
                                    option.maxUsers = 200;

                                    // String reason = GroupsActivity.this
                                    // .getString(R.string.invite_join_group);
                                    // reason = EMClient.getInstance().getCurrentUser()
                                    // + reason + groupName;
                                    String reason = "我邀请你加入我的群聊:" + groupName;

                                    // if (publibCheckBox.isChecked())
                                    // {
                                    // option.style = memberCheckbox.isChecked() ?
                                    // EMGroupStyle.EMGroupStylePublicJoinNeedApproval
                                    // : EMGroupStyle.EMGroupStylePublicOpenJoin;
                                    // } else
                                    // {
                                    // option.style = memberCheckbox.isChecked() ?
                                    // EMGroupStyle.EMGroupStylePrivateMemberCanInvite
                                    // : EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                                    // }
                                    EMClient.getInstance()
                                            .groupManager()
                                            .createGroup(groupName, desc, members, reason,
                                                    option);
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            showToastShort("建群成功，等待其他人同意");
                                            progressDialog.dismiss();
                                            refresh();
                                            setResult(RESULT_OK);
                                        }
                                    });
                                } catch (final HyphenateException e) {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            progressDialog.dismiss();
                                            Toast.makeText(GroupsActivity.this,
                                                    st2 + e.getLocalizedMessage(), Toast.LENGTH_SHORT)
                                                    .show();
                                        }
                                    });
                                }

                            }
                        }).start();
                    } else {
                        progressDialog.dismiss();
                        startActivity(new Intent(getApplicationContext(),
                                ChatActivity.class).putExtra("userId", checkmemnum[0]));
                        if (android.os.Build.VERSION.SDK_INT > 5) {
                            overridePendingTransition(R.anim.slide_in_from_right,
                                    R.anim.slide_out_to_left);
                        }
                    }
                    break;
            }
        }

    }

    /**
     * 进入公开群聊列表
     */

    public void onPublicGroups(View view) {
        startActivity(new Intent(this, PublicGroupsActivity.class));
    }

    @Override
    public void onResume() {
        refresh();
        super.onResume();
    }

    private void refresh() {
        grouplist = EMClient.getInstance().groupManager().getAllGroups();
        groupAdapter = new GroupAdapter(this, 1, grouplist);
        groupListView.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    /**
     * 返回
     *
     * @param view
     */
    public void back(View view) {
        finish();
        if (android.os.Build.VERSION.SDK_INT > 5) {
            overridePendingTransition(
                    R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
    }
}
