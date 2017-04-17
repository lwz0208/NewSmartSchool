package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.TrainningItem;

import java.util.ArrayList;


public class TrainningAdapter extends BaseAdapter implements SectionIndexer
{

    private Context context;
    private ArrayList<TrainningItem> list;
    private LayoutInflater inflater;

    public TrainningAdapter(Context context, ArrayList<TrainningItem> list)
    {
        super();
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    public void updateListView(ArrayList<TrainningItem> list)
    {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public TrainningItem getItem(int position)
    {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        final TrainningItem trainningItem = list.get(position);
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.trainningitem, null);
            holder = new ViewHolder();
            holder.tv1 = (TextView) convertView.findViewById(R.id.tainning_id);
            holder.tv2 = (TextView) convertView
                    .findViewById(R.id.tainning_name);
            holder.tv3 = (TextView) convertView
                    .findViewById(R.id.tainning_time);
            holder.tv4 = (TextView) convertView
                    .findViewById(R.id.tainning_credit);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        TrainningItem item = list.get(position);
        holder.tv1.setText(item.getKCH());
        holder.tv2.setText(item.getKCMC());
        holder.tv3.setText(item.getZXS());
        holder.tv4.setText(item.getXF());
        return convertView;
    }

    private class ViewHolder
    {
        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position)
    {
        return list.get(position).getSortLetter().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetter();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String  sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }

}

