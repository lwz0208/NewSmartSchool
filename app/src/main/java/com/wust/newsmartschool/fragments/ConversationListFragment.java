package com.wust.newsmartschool.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wust.newsmartschool.domain.ChildrenItem;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.db.InviteMessgeDao;
import com.wust.newsmartschool.ui.ChatActivity;
import com.wust.newsmartschool.ui.MainActivity;
import com.wust.easeui.Constant;
import com.wust.easeui.ui.EaseConversationListFragment;
import com.hyphenate.util.NetUtils;
import com.wust.newsmartschool.zxing.activity.BarCodeTestActivity;
import com.wust.newsmartschool.zxing.activity.CaptureActivity;

/*
 * 对话列表
 * 
 * */
public class ConversationListFragment extends EaseConversationListFragment
        implements OnClickListener {
    private TextView errorText;
    List<ChildrenItem> exListView_father = null;
    Map<String, List<ChildrenItem>> map = null;
    protected ImageView scanQR_icon;

    @Override
    protected void initView() {
        super.initView();
        View errorView = (LinearLayout) View.inflate(getActivity(),
                R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);
        scanQR_icon = (ImageView) getView().findViewById(R.id.scanqr_icon);
        scanQR_icon.setOnClickListener(this);
        initData();
        exListView.setAdapter(new MyAdapter());
        exListView.setOnChildClickListener(new OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                switch (childPosition) {
                    case 0:
                        Toast.makeText(getContext(), "通知", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case 1:
                        Toast.makeText(getContext(), "流程", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case 2:
                        Toast.makeText(getContext(), "任务", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case 3:
                        Toast.makeText(getContext(), "会议", Toast.LENGTH_SHORT)
                                .show();
                        break;
                }

                return true;
            }
        });

    }

    //外部调用的改变此页面的未读数据接口
    public void changeUnreadDate() {
    }

    public void initData() {
        exListView_father = new ArrayList<>();
        ChildrenItem temp = new ChildrenItem();
        temp.setId("11");
        temp.setName("待办事项");
        exListView_father.add(temp);
        map = new HashMap<>();
        List<ChildrenItem> list = new ArrayList<>();
        ChildrenItem tempchild1 = new ChildrenItem();
        tempchild1.setName("通知");
        tempchild1.setId("0");
        ChildrenItem tempchild2 = new ChildrenItem();
        tempchild2.setName("流程");
        tempchild2.setId("5");
        ChildrenItem tempchild3 = new ChildrenItem();
        tempchild3.setName("任务");
        tempchild3.setId("6");
        ChildrenItem tempchild4 = new ChildrenItem();
        tempchild4.setName("会议");
        tempchild4.setId("0");
        list.add(tempchild1);
        list.add(tempchild2);
        list.add(tempchild3);
        list.add(tempchild4);
        map.put("待办事项", list);

    }

    class MyAdapter extends BaseExpandableListAdapter {

        // 得到子item需要关联的数据
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            String key = exListView_father.get(groupPosition).getName();
            return (map.get(key).get(childPosition));
        }

        // 得到子item的ID
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        // 设置子item的组件
        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            String key = exListView_father.get(groupPosition).getName();
            String info = map.get(key).get(childPosition).getName();
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.exlist_children, null);
            }
            TextView tv = (TextView) convertView
                    .findViewById(R.id.exlist_children_tv);
            tv.setText(info);
            TextView childnum = (TextView) convertView
                    .findViewById(R.id.exlist_children_nums);
            if (!map.get(key).get(childPosition).getId().equals("0"))
                childnum.setText(map.get(key).get(childPosition).getId());
            else
                childnum.setVisibility(View.INVISIBLE);
            return convertView;
        }

        // 获取当前父item下的子item的个数
        @Override
        public int getChildrenCount(int groupPosition) {
            String key = exListView_father.get(groupPosition).getName();
            int size = map.get(key).size();
            return size;
        }

        // 获取当前父item的数据
        @Override
        public Object getGroup(int groupPosition) {
            return exListView_father.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return exListView_father.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        // 设置父item组件
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.exlist_parent, null);
            }
            TextView tv = (TextView) convertView
                    .findViewById(R.id.exlist_parent_tv);
            TextView num = (TextView) convertView
                    .findViewById(R.id.exlist_parent_allnums);
            tv.setText(exListView_father.get(groupPosition).getName());
            if (!exListView_father.get(groupPosition).getId().equals("0"))
                num.setText(exListView_father.get(groupPosition).getId());
            else
                num.setVisibility(View.INVISIBLE);
            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

    @Override
    protected void setUpView() {
        super.setUpView();
        // 注册上下文菜单
        registerForContextMenu(conversationListView);
        conversationListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                EMConversation conversation = conversationListView
                        .getItem(position);
                String username = conversation.getUserName();
                if (username.equals(EMClient.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(),
                            R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                else {
                    // 进入聊天页面
                    Intent intent = new Intent(getActivity(),
                            ChatActivity.class);
                    if (conversation.isGroup()) {
                        if (conversation.getType() == EMConversationType.ChatRoom) {
                            // it's group chat
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE,
                                    Constant.CHATTYPE_CHATROOM);
                        } else {
                            intent.putExtra(Constant.EXTRA_CHAT_TYPE,
                                    Constant.CHATTYPE_GROUP);
                        }

                    }
                    // it's single chat
                    intent.putExtra(Constant.EXTRA_USER_ID, username);

                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())) {
            errorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
            errorText.setText(R.string.the_current_network);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean deleteMessage = false;

        if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
        } else if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
        }
        EMConversation tobeDeleteCons = conversationListView
                .getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
        if (tobeDeleteCons == null) {
            return true;
        }
        try {
            // 删除此会话
            EMClient.getInstance()
                    .chatManager()
                    .deleteConversation(tobeDeleteCons.getUserName(),
                            deleteMessage);
            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
            inviteMessgeDao.deleteMessage(tobeDeleteCons.getUserName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh();

        // 更新消息未读数
        ((MainActivity) getActivity()).updateUnreadLabel();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scanqr_icon:
                Intent startScan = new Intent(getContext(), CaptureActivity.class);
                startActivityForResult(startScan, Constant.scanQR);
                break;

            default:
                break;
        }

    }

    public void showscanQR(int requestCode, int resultCode, Intent data) {
        if (!TextUtils.isEmpty(data.getExtras().getString("result"))) {

            String result = data.getExtras().getString("result");
            Log.i("二维码内容为:", result);
            Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == MainActivity.RESULT_OK) {

            switch (requestCode) {
                case Constant.scanQR:
                    showscanQR(requestCode, resultCode, data);
                    break;

            }
        }

    }
}
