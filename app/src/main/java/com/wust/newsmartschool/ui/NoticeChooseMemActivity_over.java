package com.wust.newsmartschool.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.MediaType;

import com.wust.newsmartschool.DemoApplication;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.DeptArchTree;
import com.wust.newsmartschool.domain.DeptArchTree_UserInfo;
import com.wust.newsmartschool.domain.PartyBranchIdEntity;
import com.wust.easeui.utils.PreferenceManager;
import com.wust.newsmartschool.views.ECProgressDialog;
import com.wust.easeui.utils.CommonUtils;
import com.google.gson.Gson;
import com.wust.easeui.Constant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

public class NoticeChooseMemActivity_over extends Activity {
    String TAG = "NoticeChooseMemActivity_over";
    // 下拉菜单的listview
    private ExpandableListView mExpListView;
    // 他的适配器
    private ExpListAdapter mExpListAdapter;
    //选择类别按钮
    private ImageView other_type_notice;
    private RelativeLayout relativeLayout1;
    //PopWindows绑定list
    private ListView title_list;
    private PopupWindow mPopWindow;
    // 三个数据源对应的实体类
    DeptArchTree archTree;
    List<String> departmentList;
    List<List<DeptArchTree_UserInfo>> memberList;
    List<String> departmentList_dept;
    List<List<DeptArchTree_UserInfo>> memberList_dept;
    List<String> departmentList_dang;
    List<List<DeptArchTree_UserInfo>> memberList_dang;
    List<String> departmentList_job;
    List<List<DeptArchTree_UserInfo>> memberList_job;
    List<String> departmentList_posit;
    List<List<DeptArchTree_UserInfo>> memberList_posit;
    private ECProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_choose_mem);
        departmentList = new ArrayList<String>();
        memberList = new ArrayList<List<DeptArchTree_UserInfo>>();
        departmentList_dept = new ArrayList<String>();
        memberList_dept = new ArrayList<List<DeptArchTree_UserInfo>>();
        departmentList_job = new ArrayList<String>();
        memberList_job = new ArrayList<List<DeptArchTree_UserInfo>>();
        departmentList_posit = new ArrayList<String>();
        memberList_posit = new ArrayList<List<DeptArchTree_UserInfo>>();
        departmentList_dang = new ArrayList<String>();
        memberList_dang = new ArrayList<List<DeptArchTree_UserInfo>>();
        progressDialog = new ECProgressDialog(NoticeChooseMemActivity_over.this);
        progressDialog.show();
        initViews();
    }

    private void initViews() {
        // 初始化控件
        mExpListView = (ExpandableListView) findViewById(R.id.explist_select);
        other_type_notice = (ImageView) findViewById(R.id.other_type_notice);
        relativeLayout1 = (RelativeLayout) findViewById(R.id.rl_workflow_choose_id);
        other_type_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });
        View contentView = LayoutInflater.from(NoticeChooseMemActivity_over.this).inflate(
                R.layout.title_popup, null);
        mPopWindow = new PopupWindow(contentView);
        title_list = (ListView) contentView.findViewById(R.id.title_list);
        archTree = new DeptArchTree();
        // Set adapter

        // 获取那个操蛋的树形结构
        try {
            getDeptArch();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getDeptArch() throws JSONException {
        JSONObject DeptArchJson = new JSONObject();
        Log.e("archTree", "getDeptArch调用了");
        Log.e("archTree", DeptArchJson.toString());
        OkHttpUtils.postString().url(Constant.GETDEPTARCH_URL)
                .mediaType(MediaType.parse("application/json"))
                .content(DeptArchJson.toString()).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        progressDialog.dismiss();
                        Log.e("archTree_arg0Before", arg0.toString());
                        arg0 = arg0.replaceAll("\"usersInfo\":\"\"", "\"usersInfo\":[]");
                        Log.e("archTree_arg0After", arg0.toString());
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            if (jObject.getInt("code") == 1) {
                                archTree = new Gson().fromJson(
                                        jObject.getString("data"),
                                        DeptArchTree.class);
                                DemoApplication.getInstance().mCache.put("archTree", archTree);
                                departmentList.clear();
                                memberList.clear();
                                handlerOut(archTree);
                                Log.e("departmentList",
                                        departmentList.toString());
                                departmentList_dept.addAll(departmentList);
                                memberList_dept.addAll(memberList);
                                mExpListAdapter = new ExpListAdapter(
                                        departmentList, memberList);
                                mExpListView.setAdapter(mExpListAdapter);
                                // 添加子项目点击的监听器
                                mExpListView
                                        .setOnChildClickListener(mOnChildClickListener);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        Log.e("archTree",
                                arg0.toString() + "---" + arg1.toString());
                        progressDialog.dismiss();

                    }
                });

    }

    // 递归上面archTree属性结构的数据，使他变为两级菜单
    private void handlerOut(DeptArchTree archTree) {
        departmentList.add(archTree.getDeptName());
        memberList.add(archTree.getUsersInfo());
        handlerDeep(archTree.getChildren());
    }

    private void handlerDeep(List<DeptArchTree> childrenList) {
        for (int i = 0; i < childrenList.size(); i++) {
            departmentList.add(childrenList.get(i).getDeptName());
            memberList.add(childrenList.get(i).getUsersInfo());
            handlerDeep(childrenList.get(i).getChildren());

        }

    }


    //改变数据源，按照党类别划分
    public void changeData(final String typeId) throws JSONException {
        JSONObject typeJson = new JSONObject();
        typeJson.put("typeId", typeId);
        CommonUtils.setCommonJson(NoticeChooseMemActivity_over.this, typeJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.i("typeId", typeId.toString());
        OkHttpUtils.postString().url(Constant.GETMEMBYTYPEBYID_URL)
                .content(typeJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        progressDialog.dismiss();
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            if (jObject.getInt("code") == 1) {
                                PartyBranchIdEntity partyBranchId = new Gson().fromJson(arg0, PartyBranchIdEntity.class);
                                departmentList.clear();
                                memberList.clear();
                                DemoApplication.getInstance().mCache.put(typeId, partyBranchId);
                                for (int i = 0; i < partyBranchId.getData().get(0).getData().size(); i++) {
                                    List<DeptArchTree_UserInfo> temp_List = new ArrayList<>();
                                    departmentList.add(partyBranchId.getData().get(0).getData().get(i).getName());
                                    for (int j = 0; j < partyBranchId.getData().get(0).getData().get(i).getUsersInfo().size(); j++) {
                                        //将新数据源填充到老的类型的数据原来，避免了换Adapter
                                        DeptArchTree_UserInfo temp = new DeptArchTree_UserInfo();
                                        temp.setUserRealname(partyBranchId.getData().get(0).getData().get(i).getUsersInfo().get(j).getUserRealname());
                                        temp.setUserId(partyBranchId.getData().get(0).getData().get(i).getUsersInfo().get(j).getUserId());
                                        Log.e(TAG + "_temp", temp.getUserRealname());
                                        temp_List.add(j, temp);
                                        Log.e(TAG + "_temp_List", temp_List.size() + "");
                                        Log.e(TAG + "_temp_List", temp_List.get(j).getUserRealname());
                                    }

                                    memberList.add(i, temp_List);
                                    Log.e(TAG + "size()", memberList.get(i).size() + "");
                                }
//                                for (int a = 0; a < partyBranchId.getData().get(0).getData().size();
//                                     a++) {
//                                    for (int b = 0; b < partyBranchId.getData().get(0).getData().get(a).getUsersInfo().size(); b++) {
//                                        Log.e(TAG + "_memberList", memberList.get(a).size() + "");
//                                    }
//                                }
                                mExpListAdapter = new ExpListAdapter(
                                        departmentList, memberList);
                                mExpListView.setAdapter(mExpListAdapter);
                                // 添加子项目点击的监听器
                                mExpListView
                                        .setOnChildClickListener(mOnChildClickListener);
                                switch (typeId) {
                                    case "partyBranchId":
                                        Log.e(TAG, "partyBranchId");
                                        departmentList_dang.addAll(departmentList);
                                        memberList_dang.addAll(memberList);
                                        break;
                                    case "positionalTitleId":
                                        Log.e(TAG, "positionalTitleId");
                                        departmentList_posit.addAll(departmentList);
                                        memberList_posit.addAll(memberList);
                                        break;
                                    case "jobClassifyId":
                                        Log.e(TAG, "jobClassifyId");
                                        departmentList_job.addAll(departmentList);
                                        memberList_job.addAll(memberList);
                                        break;
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        Log.e(TAG, arg0.toString());
                        progressDialog.dismiss();
                    }
                });
    }

    // 子项目监听器
    private OnChildClickListener mOnChildClickListener = new OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v,
                                    int groupPosition, int childPosition, long id) {
            // 当子项目被点击（父View，父View，父position，子position，不知道干嘛的）
            // 调用adapter中的自定义的onChildClick函数
            mExpListAdapter.onChildClick(groupPosition, childPosition);
            return true;
        }
    };

    private class ExpListAdapter extends BaseExpandableListAdapter {
        // 子项目的初始化
        // String[][] childResIds = new String[100][100];
        String[] parent;
        // 子项目中被选中对象的存储list
        private ArrayList<SelectChildData> list;
        // 就相当于HashMap的键值对，键是整型，值为一个SET对象存的整型
        private HashMap<Integer, HashSet<Integer>> mSelectIds;

        @SuppressLint("UseSparseArrays")
        public ExpListAdapter(List<String> departmentList,
                              List<List<DeptArchTree_UserInfo>> memberList) {
            // 实例化
            mSelectIds = new HashMap<Integer, HashSet<Integer>>();
            // 子项目中被选中对象的存储list
            list = new ArrayList<SelectChildData>();
            // 初始化子项目数据
            // childResIds = new String[][] { { "夏斌" }, { "张远航", "卢海川" },
            // { "刘凯" }, { "王嵩00", "李文钊" }, { "张银萍", "周小林" },
            // { "顾进广", "吴飞", "顾俊伟", "陈达" } };

            // 初始化父项目数据
            // parent = new String[] { "总部", "前端部", "后端部", "安卓部", "IOS部",
            // "管理部" };
            // parent = (String[]) getArray(departmentList);
            parent = departmentList.toArray(new String[]{});

            Log.e("parent", parent.toString());

            // for语句填充子类对象
            for (int i = 0, size = parent.length; i < size; i++) {
                // new一个List里面存的对象是子元素类
                ArrayList<SelectChildData> childDatasList = new ArrayList<SelectChildData>();
                // new一个子对象的数据
                SelectChildData childDatas = new SelectChildData();
                // new一个数组保存二维数组中的一维数据,child数组得到的是i个部门的子数据元素
                List<DeptArchTree_UserInfo> child = new ArrayList<DeptArchTree_UserInfo>();
                child.addAll(memberList.get(i));
                for (int j = 0, len = child.size(); j < len; j++) {
                    SelectChildData c = new SelectChildData();
                    c.id = j;
                    if (child.get(j).getUserRealname() != null) {
                        c.name = child.get(j).getUserRealname().toString();
                    }
                    c.userId = String.valueOf(child.get(j).getUserId());
                    childDatasList.add(c);

                }
                childDatas.id = i;
                childDatas.name = parent[i];
                childDatas.children = childDatasList;
                list.add(childDatas);
                mSelectIds.put(i, new HashSet<Integer>());
            }
        }

        public void onChildClick(int groupPosition, int childPosition) {
            // HashSet<Integer>相当于一个数组，该children数组存的元素是Integer
            HashSet<Integer> children = mSelectIds.get(groupPosition);
            if (children.contains(childPosition)) {
                children.remove(childPosition);
            } else {
                children.add(childPosition);
            }
            notifyDataSetChanged();
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            if (list.get(groupPosition).children == null) {
                return null;
            }
            return list.get(groupPosition).children.get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            ChildViewHolder holder;
            if (null == convertView) {
                convertView = View.inflate(NoticeChooseMemActivity_over.this,
                        R.layout.explist_child_item, null);
                holder = new ChildViewHolder();
                holder.mContent = (TextView) convertView
                        .findViewById(R.id.explist_child_item_name);
                holder.mIcon = (ImageView) convertView
                        .findViewById(R.id.explist_child_item_select);
                convertView.setTag(holder);
            } else {
                holder = (ChildViewHolder) convertView.getTag();
            }
            SelectChildData data = (SelectChildData) getChild(groupPosition,
                    childPosition);
            holder.mContent.setText(data.name);
            if (mSelectIds.get(groupPosition).contains(data.id)) {
                holder.mIcon.setSelected(true);
            } else {
                holder.mIcon.setSelected(false);
            }
            return convertView;
        }

        private class ChildViewHolder {
            protected TextView mContent;
            protected ImageView mIcon;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if (list.get(groupPosition).children == null) {
                return 0;
            }
            return list.get(groupPosition).children.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return list.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return list.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            GroupViewHolder holder;
            if (null == convertView) {
                convertView = View.inflate(NoticeChooseMemActivity_over.this,
                        R.layout.explist_group_item, null);
                holder = new GroupViewHolder();
                holder.mTitle = (TextView) convertView
                        .findViewById(R.id.explist_group_title);
                holder.mSelNum = (TextView) convertView
                        .findViewById(R.id.explist_group_selected_num);
                holder.mIndicator = (ImageView) convertView
                        .findViewById(R.id.explist_group_indicator);
                holder.mGroupIcon = (ImageView) convertView
                        .findViewById(R.id.explist_group_item_select);
                convertView.setTag(holder);
            } else {
                holder = (GroupViewHolder) convertView.getTag();
            }

            SelectChildData data = (SelectChildData) getGroup(groupPosition);
            holder.mTitle.setText(data.name);
            ArrayList<SelectChildData> list = data.children;
            int cnt = 0, num = 0;
            if (null != list && !list.isEmpty()) {
                HashSet<Integer> cids = mSelectIds.get(groupPosition);
                for (SelectChildData d : list) {
                    if (cids.contains(d.id)) {
                        num++;
                    }
                }
                cnt = list.size();
            }
            holder.mSelNum.setText(String.format(
                    getResources().getString(R.string.str_trans_receiver_num),
                    num, cnt));
            if (!holder.mGroupIcon.isSelected()) {
                holder.mGroupIcon.setSelected(true);
            } else {
                holder.mGroupIcon.setSelected(false);
            }
            if (isExpanded) {
                holder.mIndicator.setImageResource(R.drawable.icon_sub);
                convertView.setSelected(true);
            } else {
                holder.mIndicator.setImageResource(R.drawable.icon_add);
                convertView.setSelected(false);
            }
            return convertView;
        }

        private class GroupViewHolder {
            protected TextView mTitle;
            protected TextView mSelNum;
            protected ImageView mIndicator;
            protected ImageView mGroupIcon;
        }

        @Override
        public void onGroupExpanded(int groupPosition) {
            for (int i = 0, cnt = getGroupCount(); i < cnt; i++) {
                if (groupPosition != i && mExpListView.isGroupExpanded(i)) {
                    mExpListView.collapseGroup(i);
                }
            }
            super.onGroupExpanded(groupPosition);
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        // 使用点击子项目能获取到点击事件
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public String getSelectInfoEntity() {
            String s = "";
            for (int i = 0, size = mSelectIds.size(); i < size; i++) {
                HashSet<Integer> children = mSelectIds.get(i);
                if (!children.isEmpty()) {

                    ArrayList<SelectChildData> cl = list.get(i).children;
                    for (int j = 0, len = cl.size(); j < len; j++) {
                        if (children.contains(cl.get(j).id)) {
                            String temp = cl.get(j).name + "、";
                            s = temp + s;
                        }
                    }
                }
            }
            if (s.length() > 0) {
                return s.toString();
            } else {
                return "";
            }
        }

        public String getSelectPhoneEntity() {
            String s = "";
            for (int i = 0, size = mSelectIds.size(); i < size; i++) {
                HashSet<Integer> children = mSelectIds.get(i);
                if (!children.isEmpty()) {

                    ArrayList<SelectChildData> cl = list.get(i).children;
                    for (int j = 0, len = cl.size(); j < len; j++) {
                        if (children.contains(cl.get(j).id)) {
                            String temp = cl.get(j).userPhone + ",";
                            s = temp + s;
                        }
                    }
                }
            }
            if (s.length() > 0) {
                return s.toString();
            } else {
                return "";
            }
        }

        public String getSelectUserIdEntity() {
            String s = "";
            for (int i = 0, size = mSelectIds.size(); i < size; i++) {
                HashSet<Integer> children = mSelectIds.get(i);
                if (!children.isEmpty()) {

                    ArrayList<SelectChildData> cl = list.get(i).children;
                    for (int j = 0, len = cl.size(); j < len; j++) {
                        if (children.contains(cl.get(j).id)) {
                            String temp = cl.get(j).userId + ",";
                            s = temp + s;
                        }
                    }
                }
            }
            if (s.length() > 0) {
                return s.toString();
            } else {
                return "";
            }
        }

        public void clearSelectInfo() {
            boolean clear = false;
            for (int i = 0, size = mSelectIds.size(); i < size; i++) {
                HashSet<Integer> children = mSelectIds.get(i);
                if (!children.isEmpty()) {
                    children.clear();
                    if (!clear)
                        clear = true;
                }
            }
            if (clear)
                notifyDataSetChanged();
        }
    }

    ;

    private class SelectChildData {
        public int id;
        public String name;
        public String userPhone;
        public String userId;
        public ArrayList<SelectChildData> children;

    }

    public void onOkClick(View view) {
        if (mExpListAdapter.getSelectInfoEntity() != null) {
            setResult(
                    RESULT_OK,
                    new Intent()
                            .putExtra("members_userid",
                                    mExpListAdapter.getSelectUserIdEntity())
                            .putExtra("members_phone",
                                    mExpListAdapter.getSelectPhoneEntity())
                            .putExtra("members_name",
                                    mExpListAdapter.getSelectInfoEntity()));
        }

        finish();

    }


    public void back(View view) {
        mExpListAdapter.clearSelectInfo();
        finish();

    }

    private void showPopupWindow() {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Map<String, Object> value = new HashMap<String, Object>();
//        value.put("icon", R.drawable.partybranchid);
        value.put("text", "按党支部分类");
        data.add(value);
        value = new HashMap<String, Object>();
//        value.put("icon", R.drawable.jobtitleid);
        value.put("text", "按职务分类");
        data.add(value);
        value = new HashMap<String, Object>();
//        value.put("icon", R.drawable.positionaltitleid);
        value.put("text", "按职称分类");
        data.add(value);
        value = new HashMap<String, Object>();
//        value.put("icon", R.drawable.departmentid);
        value.put("text", "按科室分类");
        data.add(value);

        SimpleAdapter adapter = new SimpleAdapter(this, data,
                R.layout.title_popup_item, new String[]{"icon", "text"},
                new int[]{R.id.iv_icon, R.id.tv_text});
        title_list.setAdapter(adapter);
        title_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                mPopWindow.dismiss();
                switch (position) {
                    case 0://按党支部分类
                        progressDialog.show();
                        if (departmentList_dang.size() != 0 && memberList_dang.size() != 0) {
                            departmentList.clear();
                            memberList.clear();
                            departmentList.addAll(departmentList_dang);
                            memberList.addAll(memberList_dang);
                            mExpListAdapter = new ExpListAdapter(
                                    departmentList, memberList);
                            mExpListView.setAdapter(mExpListAdapter);
                            // 添加子项目点击的监听器
                            mExpListView
                                    .setOnChildClickListener(mOnChildClickListener);
                            progressDialog.dismiss();
                        } else {
                            try {
                                changeData("partyBranchId");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case 1://按职务分类
                        progressDialog.show();
                        if (departmentList_job.size() != 0 && memberList_job.size() != 0) {
                            departmentList.clear();
                            memberList.clear();
                            departmentList.addAll(departmentList_job);
                            memberList.addAll(memberList_job);
                            mExpListAdapter = new ExpListAdapter(
                                    departmentList, memberList);
                            mExpListView.setAdapter(mExpListAdapter);
                            // 添加子项目点击的监听器
                            mExpListView
                                    .setOnChildClickListener(mOnChildClickListener);
                            progressDialog.dismiss();
                        } else {
                            try {
                                changeData("jobClassifyId");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case 2://按职称分类
                        progressDialog.show();
                        if (departmentList_posit.size() != 0 && memberList_posit.size() != 0) {
                            departmentList.clear();
                            memberList.clear();
                            departmentList.addAll(departmentList_posit);
                            memberList.addAll(memberList_posit);
                            mExpListAdapter = new ExpListAdapter(
                                    departmentList, memberList);
                            mExpListView.setAdapter(mExpListAdapter);
                            // 添加子项目点击的监听器
                            mExpListView
                                    .setOnChildClickListener(mOnChildClickListener);
                            progressDialog.dismiss();
                        } else {
                            try {
                                changeData("positionalTitleId");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case 3://按科室分类
                        // 获取那个操蛋的树形结构
                        progressDialog.show();
                        if (departmentList_dept.size() != 0 && memberList_dept.size() != 0) {
                            departmentList.clear();
                            memberList.clear();
                            departmentList.addAll(departmentList_dept);
                            memberList.addAll(memberList_dept);
                            mExpListAdapter = new ExpListAdapter(
                                    departmentList, memberList);
                            mExpListView.setAdapter(mExpListAdapter);
                            // 添加子项目点击的监听器
                            mExpListView
                                    .setOnChildClickListener(mOnChildClickListener);
                            progressDialog.dismiss();
                        } else {
                            try {
                                getDeptArch();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                }


            }
        });

        // 获得listview的宽度，使popwindow设置为自适应的宽度
        title_list.measure(View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED);
        mPopWindow.setWidth(title_list.getMeasuredWidth());
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.setFocusable(true);
        // 获取状态栏高度
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        mPopWindow.showAtLocation(other_type_notice, Gravity.RIGHT | Gravity.TOP,
                Dp2Px(this, 5f),
                frame.top + relativeLayout1.getHeight());
    }


    // 将Android的dp转换为屏幕的px
    public int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
