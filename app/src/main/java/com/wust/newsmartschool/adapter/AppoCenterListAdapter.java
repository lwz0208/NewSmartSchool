package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.text.format.Time;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.AppoCenterItem;

import java.text.SimpleDateFormat;
import java.util.List;

public class AppoCenterListAdapter extends BaseAdapter
{
    private Context context;
    private List<AppoCenterItem> mLists;

    public AppoCenterListAdapter(Context context, List<AppoCenterItem> mLists)
    {
        this.context = context;
        this.mLists = mLists;
    }

    @Override
    public int getCount()
    {
        return mLists.size();
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        AppoCenterItem appoCenterItem = mLists.get(getCount() - position - 1);
        if (position == 0&& (appoCenterItem.getOldAction().equals("1") || appoCenterItem.getOldAction().equals("2") || appoCenterItem.getOldAction().equals("6") || appoCenterItem.getOldAction().equals("7") ||appoCenterItem.getOldAction().equals("8")))
        {
            try
            {
                View view = View.inflate(context,
                        R.layout.item_lib_seat_appoint_center, null);

                ((TextView) view.findViewById(R.id.tv_seat))
                        .setText(appoCenterItem.getRoom() + "第"
                                + appoCenterItem.getSeat() + "号座位");

                SimpleDateFormat yearFormat = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                Time time = new Time();
                time.set(yearFormat.parse(appoCenterItem.getTime()).getTime());

                ((TextView) view.findViewById(R.id.tv_appoint_time))
                        .setText(appoCenterItem.getTime());

                TextView appoTipTextView = (TextView) view
                        .findViewById(R.id.tv_appoint_center_tip);
                appoTipTextView.setBackgroundResource(R.color.dark_red);

                if (appoCenterItem.getOldAction().equals("2") || appoCenterItem.getOldAction().equals("8"))
                    appoTipTextView.setText("使用中");
                else if(appoCenterItem.getOldAction().equals("7"))
                    appoTipTextView.setText("离开");
                else if(appoCenterItem.getOldAction().equals("6"))
                    appoTipTextView.setText("被标记");

                return view;
            } catch (Exception e)
            {
            }
            return null;
        } else {

            try
            {
                View view = View.inflate(context, R.layout.item_lib_seat_info,
                        null);
                ((TextView) view.findViewById(R.id.tv_seat))
                        .setText(appoCenterItem.getRoom() + "第"
                                + appoCenterItem.getSeat() + "号座位");
                ((TextView) view.findViewById(R.id.tv_action_name))
                        .setText(appoCenterItem.getAction());

                SimpleDateFormat yearFormat = new SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss");
                Time time = new Time();
                time.set(yearFormat.parse(appoCenterItem.getTime()).getTime());

                int hour = time.hour;
                int min = time.minute;
                int month = time.month + 1;
                int day = time.monthDay;

                String monthString = (month < 10 ? "0" + month : month) + "/"
                        + (day < 10 ? "0" + day : day);
                String timeString = (hour < 10 ? "0" + hour : hour) + ":"
                        + (min < 10 ? "0" + min : min);

                ((TextView) view.findViewById(R.id.tv_info_time))
                        .setText(monthString + "\n" + timeString);
                return view;
            } catch (Exception e)
            {
            }
            return convertView;
        }
    }
}

