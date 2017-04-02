package com.wust.newsmartschool.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wust.newsmartschool.DemoApplication;
import com.wust.newsmartschool.R;
import com.wust.easeui.Constant;
import com.wust.easeui.utils.CommonUtils;
import com.wust.easeui.widget.EaseAlertDialog;
import com.wust.easeui.widget.EaseExpandGridView;
import com.wust.easeui.widget.GlideRoundTransform;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.util.NetUtils;

import org.json.JSONException;

import java.io.FileNotFoundException;
import java.util.List;

public class GroupMembersActivity extends BaseActivity {
    String TAG = "GroupMembersActivity_Debugs";
    private static final int REQUEST_CODE_ADD_USER = 0;
    private String groupId;
    List<String> members;
    private EaseExpandGridView gridviewdetail;
    private GridAdapter adapter;
    private boolean isGroupOwner;
    private ProgressDialog progressDialog;
    private TextView groupmemdetail;
    private ScrollView scroll_view_groupmembers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_members);
        gridviewdetail = (EaseExpandGridView) findViewById(R.id.gridviewdetail);
        groupmemdetail = (TextView) findViewById(R.id.groupmemdetail);
        scroll_view_groupmembers = (ScrollView) findViewById(R.id.scroll_view_groupmembers);
        scroll_view_groupmembers.smoothScrollTo(0, 0);
        members = (List<String>) getIntent().getSerializableExtra("members");
        isGroupOwner = getIntent().getBooleanExtra("isGroupOwner", false);
        groupId = getIntent().getStringExtra("groupId");
//        Log.e(TAG, "isGroupOwner=" + isGroupOwner);
//        Log.e(TAG, "groupId=" + groupId);
        if (members != null) {
            groupmemdetail.setText("聊天信息(" + members.size() + ")");
            adapter = new GridAdapter(this, R.layout.em_grid, members);
            gridviewdetail.setAdapter(adapter);
        }
        // 设置OnTouchListener
        gridviewdetail.setOnTouchListener(new View.OnTouchListener() {

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
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView textView;
        ImageView badgeDeleteView;
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
                progressDialog = new ProgressDialog(GroupMembersActivity.this);
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

                default:
                    break;
            }
        }
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
                    if (isGroupOwner) {
                        EMClient.getInstance().groupManager()
                                .addUsersToGroup(groupId, newmembers);
                    } else {
                        // 一般成员调用invite方法
                        EMClient.getInstance().groupManager()
                                .inviteUser(groupId, newmembers, null);
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(GroupMembersActivity.this, "邀请已发出，待对方同意。", Toast.LENGTH_SHORT)
                                    .show();
                            progressDialog.dismiss();
                            setResult(RESULT_OK);

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

    private void refreshMembers() {
        EMGroup group = EMClient.getInstance().groupManager().getGroup(groupId);
        members.clear();
        members.addAll(group.getMembers());
        groupmemdetail.setText("聊天信息(" + members.size() + ")");
        adapter.notifyDataSetChanged();
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
                if (!isGroupOwner) {
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
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isInDeleteMode = true;
                            notifyDataSetChanged();
                        }
                    });
                }
            } else if (position == getCount() - 2) { // 添加群组成员按钮
                holder.textView.setText("");
                holder.imageView.setImageResource(R.drawable.em_smiley_add_btn);
                // 如果不是创建者或者没有相应权限
                if (!isGroupOwner) {
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
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // 进入选人页面
                            startActivityForResult((new Intent(
                                    GroupMembersActivity.this,
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
                        .load(Constant.GETHEADIMAG_URL + username + ".png").transform(new GlideRoundTransform(getContext()))
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
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isInDeleteMode) {
                            // 如果是删除自己，return
                            if (EMClient.getInstance().getCurrentUser()
                                    .equals(username)) {
                                new EaseAlertDialog(GroupMembersActivity.this,
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
                            Log.e(TAG, "remove user from group:"
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
                                GroupMembersActivity.this);
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
                                            setResult(RESULT_OK);
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

            }
            return convertView;
        }

        @Override
        public int getCount() {
            return super.getCount() + 2;
        }
    }

    public void back(View view) {
        finish();
    }
}
