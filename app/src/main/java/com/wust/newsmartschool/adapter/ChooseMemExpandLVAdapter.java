package com.wust.newsmartschool.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.ChildrenItem;
import com.wust.newsmartschool.domain.GroupItem;

public class ChooseMemExpandLVAdapter extends BaseExpandableListAdapter {
    private List<GroupItem> groups;
    private LayoutInflater inflater;

    private List<ChildrenItem> checkedChildrenList = new ArrayList<ChildrenItem>();
    private List<GroupItem> checkedGroupList = new ArrayList<GroupItem>();

    private Map<String, Integer> groupCheckedStateMap = new HashMap<String, Integer>();

    public ChooseMemExpandLVAdapter(Context context, List<GroupItem> dataList) {
        this.groups = dataList;
        inflater = LayoutInflater.from(context);
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
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ChildViewHolder) convertView.getTag();
        }

        viewHolder.childrenNameTV.setText(childrenItem.getName());
        if (childrenItem.getJobtitleName() != null) {
            if (!childrenItem.getJobtitleName().equals("")) {
                try {
                    viewHolder.children_jobtitle.setVisibility(View.VISIBLE);
                    viewHolder.children_jobtitle.setText(childrenItem.getJobtitleName());
                } catch (NullPointerException e) {
                    viewHolder.children_jobtitle.setVisibility(View.GONE);
                }
            } else {
                viewHolder.children_jobtitle.setVisibility(View.GONE);
            }
        } else {
            viewHolder.children_jobtitle.setVisibility(View.GONE);
        }
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
                ChooseMemExpandLVAdapter.this.notifyDataSetChanged();
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
            //viewHolder.groupCBImg = (ImageView) convertView.findViewById(R.id.group_cb_img);
            viewHolder.groupCBLayout = (LinearLayout) convertView.findViewById(R.id.cb_layout);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.group_cb);
            convertView.setTag(viewHolder);
//            } else {
//                viewHolder = (GroupViewHolder) convertView.getTag();
//            }

            viewHolder.checkBox.setOnClickListener(new Group_CheckBox_Click(groupItem));
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
        CheckBox childrenCB;
    }


    public class Group_CheckBox_Click implements OnClickListener {

        private GroupItem groupItem;

        public Group_CheckBox_Click(GroupItem groupItem) {
            this.groupItem = groupItem;
        }

        @Override
        public void onClick(View v) {
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

            boolean checked = false;
            if (checkedCount == childrenItems.size()) {
                checked = false;
                groupCheckedStateMap.put(groupItem.getId(), 3);
            } else {
                checked = true;
                groupCheckedStateMap.put(groupItem.getId(), 1);
            }

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

            ChooseMemExpandLVAdapter.this.notifyDataSetChanged();
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
            for (int j = 0; j < groups.get(i).getChildrenItems().size(); j++) {
                checkedChildrenList.add(groups.get(i).getChildrenItems().get(j));
            }
        }
        // 刷新数据
        ChooseMemExpandLVAdapter.this.notifyDataSetChanged();
    }

    public void removeAllCheck() {
        checkedChildrenList.clear();
        checkedGroupList.clear();
        for (int i = 0; i < groups.size(); i++) {
            groupCheckedStateMap.put(groups.get(i).getId(), 3);
        }
        // 刷新数据
        ChooseMemExpandLVAdapter.this.notifyDataSetChanged();
    }

}
