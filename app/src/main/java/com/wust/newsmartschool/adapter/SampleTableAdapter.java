package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wust.newsmartschool.ui.CalendarHolidayActivity;

/**
 * A sample adapter for table.
 * @author Yorek Liu
 * @version 1.1
 */
public abstract class SampleTableAdapter implements MyTableAdapter {
    private final Context context;
    private final LayoutInflater inflater;

    public SampleTableAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public Context getContext() {
        return context;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    @Override
    public View getView(final int row, final int column, View converView, ViewGroup parent) {
        converView = inflater.inflate(getLayoutResource(row, column), parent, false);
        setText(converView, getCellString(row, column));

        // 如果是表头且为内容的第1列或者第7列， 则把其字体设置为红色
        if (row == -1 && (column == 0 || column == 6))
            ((TextView) converView.findViewById(android.R.id.text1)).setTextColor(Color.RED);

        // 如果是清明节、劳动节、端午节,则有点击事件
        if ((row == 5 && column == 0) || (row == 8 && column == 5) || (row == 15 && column == 6)
                || (row == 4 && column == 6) || (row == 5 && column == 1) || (row == 8 && column == 6)
                || (row == 9 && column == 0) || (row == 16 && column == 0) || (row == 16 && column == 1)
                || (row == 17 && column == 6)) {
            final Intent intent = new Intent(getContext(), CalendarHolidayActivity.class);
            intent.putExtra("holiday", row + column);
            converView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    getContext().startActivity(intent);
                }
            });
        }

        return converView;
    }

    private void setText(View view, String text) {
        ((TextView) view.findViewById(android.R.id.text1)).setText(text);
    }

    public abstract String getCellString(int row, int column);

    public abstract int getLayoutResource(int row, int column);
}

