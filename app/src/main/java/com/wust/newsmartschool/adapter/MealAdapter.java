package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.MealItem;
import com.wust.newsmartschool.utils.GlobalVar;
import com.wust.newsmartschool.utils.PictureServer2;

import java.util.ArrayList;


/**
 * @Description:
 * @Author: Effall
 * @Date: 2015年2月9日
 * @Update by YorekLiu
 */

public class MealAdapter extends BaseAdapter
{
    private ArrayList<MealItem> items;
    private Context context;
    private CallBack callBack = null;

    public static final int ADD = 1;
    public static final int SUB = 0;

    public MealAdapter(Context context, ArrayList<MealItem> items)
    {
        this.items = items;
        this.context=context;

    }

    public interface CallBack
    {
        public void onCountChanged(int type, String price,MealItem item);
    };

    public void setCallback(CallBack callBack)
    {
        this.callBack = callBack;
    }

    public int getCount()
    {
        return items.size();
    }



    public MealItem getItem(int position)
    {
        return items.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;
        if (convertView == null)
        {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.meal_item, null);
            holder.imageView = (ImageView) convertView.findViewById(R.id.meal_img);
            holder.name = (TextView) convertView.findViewById(R.id.meal_name);
            holder.mealId= (TextView) convertView.findViewById(R.id.meal_id);
            holder.num = (TextView) convertView.findViewById(R.id.meal_num);//月销量
            holder.price = (TextView) convertView.findViewById(R.id.meal_price);

            holder.addButton = (Button) convertView.findViewById(R.id.meal_add);
            holder.subButton = (Button) convertView.findViewById(R.id.meal_sub);
            holder.descripation=(TextView) convertView.findViewById(R.id.meal_descripation);
            holder.mealNumTextView = (TextView) convertView.findViewById(R.id.tv_meal_number);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        final MealItem item = (MealItem) getItem(position);
        holder.name.setText(item.getName());
        holder.mealId.setText(item.getMealId());
        holder.num.setText("月售量:" + item.getSaleNum() + "份");
        holder.price.setText("￥"+item.getPrice());

        holder.descripation.setText(item.getDescripation());

        PictureServer2 pictureServer=new PictureServer2();
        pictureServer.asyncDownPic(holder.imageView, item.getImgUrl(), GlobalVar.cacheFile, 4);
        holder.imageView.setTag(item.getImgUrl());

        if(items.get(position).getCount() != 0)
        {
            holder.mealNumTextView.setVisibility(View.VISIBLE);
            holder.mealNumTextView.setText("" + item.getCount());
            holder.subButton.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.mealNumTextView.setVisibility(View.INVISIBLE);
            holder.subButton.setVisibility(View.INVISIBLE);
        }

        holder.addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                item.setCount(item.getCount() + 1);
                notifyDataSetChanged();
                if (callBack != null)
                {
                    callBack.onCountChanged(MealAdapter.ADD, item.getPrice(),item);
                }
            }
        });
        holder.subButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                item.setCount(item.getCount() - 1);
                notifyDataSetChanged();
                if (callBack != null)
                {
                    callBack.onCountChanged(MealAdapter.SUB, item.getPrice(),item);
                }
            }
        });
        return convertView;
    }


    private class ViewHolder
    {
        private ImageView imageView;
        private TextView name;
        private TextView num;
        private TextView price;
        private TextView mealId;
        private Button addButton;
        private Button subButton;
        private TextView descripation;
        private TextView mealNumTextView;
    }



}
