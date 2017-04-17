package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.AddressItem;

import java.util.ArrayList;


public class AddressListAdapter extends BaseAdapter{
    private ArrayList<AddressItem> addressList;
    private Context context;
    public AddressListAdapter(Context context,ArrayList<AddressItem> addrssList){
        this.context=context;
        this.addressList=addrssList ;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return addressList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return addressList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        TextView department;
        TextView phone;
        //如果是第一屏的数据，没有缓存
        if(convertView==null){
            convertView=LayoutInflater.from(context).inflate(R.layout.address_list_item, null);
            department=(TextView)convertView.findViewById(R.id.department);
            phone=(TextView)convertView.findViewById(R.id.phone);
            Typeface typeface = Typeface.create("黑体", Typeface.NORMAL);//参数后面一个表示设置粗体
            department.setTextSize(15);
            phone.setTextSize(15);
            phone.setTextColor(Color.BLUE);
            department.setTypeface(typeface);
            phone.setTypeface(typeface);
            phone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG + Paint.FAKE_BOLD_TEXT_FLAG);
            ViewBundle viewBundle=new ViewBundle(department,phone);
            convertView.setTag(viewBundle);
        }
        else{
            ViewBundle viewBundle=(ViewBundle)convertView.getTag();
            department=viewBundle.department;
            phone=viewBundle.phone;
        }

        //得到数据，并且填充
        AddressItem addressItem1=(AddressItem)getItem(position);
        department.setText(addressItem1.getDepartment());
        phone.setText(addressItem1.getPhone());
        return convertView;
    }

    public class ViewBundle{
        private TextView department;
        private TextView phone;
        public ViewBundle(TextView department,TextView phone){
            this.phone=phone;
            this.department=department;
        }
    }

}
