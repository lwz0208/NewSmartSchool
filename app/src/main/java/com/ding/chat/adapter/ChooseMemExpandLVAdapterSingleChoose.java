package com.ding.chat.adapter;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ding.chat.R;
import com.ding.chat.domain.ChildrenItem;
import com.ding.chat.domain.GroupItem;
import com.ding.chat.views.ListViewForScrollView;
import com.ding.easeui.Constant;
import com.ding.easeui.widget.GlideRoundTransform;
import com.zhy.http.okhttp.utils.Exceptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseMemExpandLVAdapterSingleChoose extends BaseExpandableListAdapter {
    private List<GroupItem> groups;
    private LayoutInflater inflater;
    private Context mContext;

    public List<ChildrenItem> checkedChildrenList = new ArrayList<ChildrenItem>();
    public List<GroupItem> checkedGroupList = new ArrayList<GroupItem>();

    private ExpandableListView MyexpandableListView;

    private Map<String, Integer> groupCheckedStateMap = new HashMap<String, Integer>();

    public ChooseMemExpandLVAdapterSingleChoose(Context context, List<GroupItem> dataList, ExpandableListView expandableList) {
        this.groups = dataList;
        mContext = context;
        inflater = LayoutInflater.from(context);
        MyexpandableListView = expandableList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        final GroupItem groupItem = groups.get(groupPosition);
        if (groupItem == null || groupItem.getChildrenItems() == null
                || groupItem.getChildrenItems().isEmpty()) {
            return null;
        }
        return groupItem.getChildrenItems().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, final ViewGroup parent) {
        ChildrenItem childrenItem = (ChildrenItem) getChild(groupPosition, childPosition);
        ChildViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ChildViewHolder();
            convertView = inflater.inflate(R.layout.children_item, null);
            viewHolder.childrenNameTV = (TextView) convertView.findViewById(R.id.children_name);
            viewHolder.childrenCB = (CheckBox) convertView.findViewById(R.id.children_cb);
            viewHolder.children_jobtitle = (TextView) convertView.findViewById(R.id.children_jobtitle);
            viewHolder.children_deptname = (TextView) convertView.findViewById(R.id.children_deptname);
            viewHolder.childrenroleIV = (ImageView) convertView.findViewById(R.id.roleName_img);
            viewHolder.user_head_avatar = (ImageView) convertView.findViewById(R.id.user_head_avatar);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }
        Glide.with(mContext)
                .load(Constant.GETHEADIMAG_URL
                        + childrenItem.getId()
                        + ".png").transform(new GlideRoundTransform(mContext)).placeholder(R.drawable.ease_default_avatar)
                .into(viewHolder.user_head_avatar);

        viewHolder.childrenNameTV.setText(childrenItem.getName());
        if (childrenItem.getJobtitleName() != null) {
            if (!childrenItem.getJobtitleName().equals("")) {
                viewHolder.children_jobtitle.setText(childrenItem.getJobtitleName());
            } else {
                viewHolder.children_jobtitle.setText("");
            }
        } else {
            viewHolder.children_jobtitle.setText("");
        }
        //判断是临时工还是正式工显示对应的图标1是管理员2是正式职工3是临时职工
        if (childrenItem.getRoleId() == 1) {
            Glide.with(mContext)
                    .load(R.drawable.rolename_guan).into(viewHolder.childrenroleIV);
        } else if (childrenItem.getRoleId() == 2) {
            Glide.with(mContext)
                    .load(R.drawable.rolename_zheng).into(viewHolder.childrenroleIV);
        } else if (childrenItem.getRoleId() == 3) {
            Glide.with(mContext)
                    .load(R.drawable.rolename_lin).into(viewHolder.childrenroleIV);
        }
        viewHolder.children_deptname.setText(childrenItem.getDepartmentName());
        final ChildrenItem mchildren = childrenItem;

        viewHolder.childrenCB.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!checkedChildrenList.contains(mchildren)) {
                        checkedChildrenList.add(mchildren);
                    }
                } else {
                    checkedChildrenList.remove(mchildren);
                }
                setGroupItemCheckedState(groups.get(groupPosition));
                ChooseMemExpandLVAdapterSingleChoose.this.notifyDataSetChanged();
            }
        });
        if (checkedChildrenList.contains(mchildren)) {
            viewHolder.childrenCB.setChecked(true);
        } else {
            viewHolder.childrenCB.setChecked(false);
        }

        return convertView;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        final GroupItem groupItem = groups.get(groupPosition);
        if (groupItem == null || groupItem.getChildrenItems() == null
                || groupItem.getChildrenItems().isEmpty()) {
            return 0;
        }
        return groupItem.getChildrenItems().size();
    }


    @Override
    public Object getGroup(int groupPosition) {
        if (groups == null) {
            return null;
        }
        return groups.get(groupPosition);
    }


    @Override
    public int getGroupCount() {
        if (groups == null) {
            return 0;
        }
        return groups.size();
    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        try {
            GroupItem groupItem = groups.get(groupPosition);
            GroupViewHolder viewHolder = null;
            //不知为何用Tags会出现复现的情况。
//            if (null == convertView) {
            viewHolder = new GroupViewHolder();
            convertView = inflater.inflate(R.layout.group_item, null);
            viewHolder.groupNameTV = (TextView) convertView.findViewById(R.id.group_name);
            viewHolder.groupCBLayout = (LinearLayout) convertView.findViewById(R.id.cb_layout);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.group_cb);
            convertView.setTag(viewHolder);
//            } else {
//                viewHolder = (GroupViewHolder) convertView.getTag();
//            }

            viewHolder.checkBox.setOnClickListener(new Group_CheckBox_Click(groupItem, groupPosition));
            viewHolder.groupNameTV.setText(groupItem.getName());
            int state = groupCheckedStateMap.get(groupItem.getId());
            switch (state) {
                case 1:
                    viewHolder.checkBox.setChecked(true);
                    if (!checkedGroupList.contains(groupItem))
                        checkedGroupList.add(groupItem);
                    break;
                case 3:
                    viewHolder.checkBox.setChecked(false);
                    if (checkedGroupList.contains(groupItem))
                        checkedGroupList.remove(groupItem);
                    break;
                default:
                    viewHolder.checkBox.setChecked(false);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void setGroupItemCheckedState(GroupItem groupItem) {
        List<ChildrenItem> childrenItems = groupItem.getChildrenItems();
        if (childrenItems == null || childrenItems.isEmpty()) {
            groupCheckedStateMap.put(groupItem.getId(), 3);
            return;
        }

        int checkedCount = 0;
        for (ChildrenItem childrenItem : childrenItems) {
            if (checkedChildrenList.contains(childrenItem)) {
                checkedCount++;
            }
        }
        int state = 1;
        if (checkedCount == childrenItems.size()) {
            state = 1;
        } else {
            state = 3;
        }

        groupCheckedStateMap.put(groupItem.getId(), state);

    }


    final static class GroupViewHolder {
        TextView groupNameTV;
        ImageView groupCBImg;
        CheckBox checkBox;
        LinearLayout groupCBLayout;
    }

    final static class ChildViewHolder {
        TextView children_jobtitle;
        TextView childrenNameTV;
        TextView children_deptname;
        ImageView childrenroleIV;
        ImageView user_head_avatar;
        CheckBox childrenCB;
    }


    public class Group_CheckBox_Click implements OnClickListener {
        private GroupItem groupItem;
        private int groupPosition;

        public Group_CheckBox_Click(GroupItem groupItem, int groupPosition) {
            this.groupItem = groupItem;
            this.groupPosition = groupPosition;
        }

        @Override
        public void onClick(View v) {
            List<ChildrenItem> childrenItems = groupItem.getChildrenItems();
            if (childrenItems == null || childrenItems.isEmpty()) {
                if (!checkedGroupList.contains(groupItem)) {
//                    MyexpandableListView.expandGroup(groupPosition);
                    groupCheckedStateMap.put(groupItem.getId(), 1);
                } else {
                    groupCheckedStateMap.put(groupItem.getId(), 3);
                }
                ChooseMemExpandLVAdapterSingleChoose.this.notifyDataSetChanged();
            } else {
                int checkedCount = 0;
                for (ChildrenItem childrenItem : childrenItems) {
                    if (checkedChildrenList.contains(childrenItem)) {
                        checkedCount++;
                    }
                }
//
                boolean checked = false;
                if (checkedCount == childrenItems.size()) {
                    checked = false;
                    groupCheckedStateMap.put(groupItem.getId(), 3);
                } else {
                    checked = true;
                    groupCheckedStateMap.put(groupItem.getId(), 1);
                }
//
                for (ChildrenItem childrenItem : childrenItems) {
                    ChildrenItem holderKey = childrenItem;
                    if (checked) {
                        if (!checkedChildrenList.contains(holderKey)) {
                            checkedChildrenList.add(holderKey);
                        }
                    } else {
                        checkedChildrenList.remove(holderKey);
                    }
                }

                ChooseMemExpandLVAdapterSingleChoose.this.notifyDataSetChanged();
            }
        }
    }

    public List<ChildrenItem> getCheckedChildren() {
        return checkedChildrenList;
    }

    public List<GroupItem> getCheckedGroup() {
        return checkedGroupList;
    }

    public void AllCheck() {
        checkedChildrenList.clear();
        checkedGroupList.clear();
        for (int i = 0; i < groups.size(); i++) {
            groupCheckedStateMap.put(groups.get(i).getId(), 1);
            checkedGroupList.add(groups.get(i));
            try {
                if (groups.get(i).getChildrenItems() != null) {
                    for (int j = 0; j < groups.get(i).getChildrenItems().size(); j++) {
                        checkedChildrenList.add(groups.get(i).getChildrenItems().get(j));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        // 刷新数据
        ChooseMemExpandLVAdapterSingleChoose.this.notifyDataSetChanged();
    }

    public void removeAllCheck() {
        checkedChildrenList.clear();
        checkedGroupList.clear();
        for (int i = 0; i < groups.size(); i++) {
            groupCheckedStateMap.put(groups.get(i).getId(), 3);
        }
        // 刷新数据
        ChooseMemExpandLVAdapterSingleChoose.this.notifyDataSetChanged();
    }

}
