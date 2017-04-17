package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.content.res.Resources;

import com.wust.newsmartschool.R;

/**
 * 日历表格Adapter
 * @author Yorek Liu
 * @version 1.1
 * @caution row和column都从-1开始
 */
public class CalendarTableAdapter extends SampleTableAdapter {

    private final int width;		// 每个格子的宽高度
    private final int height;

    /**
     * 表格中所有格子的内容
     */
    private final String[][] calendarString = {
            {"", "周日", "周一", "周二", "周三", "周四", "周五", "周六"},
            {"1", "30/8", "31", "1/9", "2", "3", "4", "5"},
            {"2", "6", "7", "8", "9", "10", "11", "12"},
            {"3", "13", "14", "15", "16", "17", "18", "19"},
            {"4", "20", "21", "22", "23", "24", "25", "26"},
            {"5", "27", "28", "29", "30", "1/10", "2", "3"},
            {"6", "4", "5", "6", "7", "8", "9", "10"},
            {"7", "11", "12", "13", "14", "15", "16", "17"},
            {"8", "18", "19", "20", "21", "22", "23", "24"},
            {"9", "25", "26", "27", "28", "29", "30", "31"},
            {"10", "1/11", "2", "3", "4", "5", "6", "7"},
            {"11", "8", "9", "10", "11", "12", "13", "14"},
            {"12", "15", "16", "17", "18", "19", "20", "21"},
            {"13", "22", "23", "24", "25", "26", "27", "28"},
            {"14", "29", "30", "1/12", "2", "3", "4", "5"},
            {"15", "6", "7", "8", "9", "10", "11", "12"},
            {"16", "13", "14", "15", "16", "17", "18", "19"},
            {"17", "20", "21", "22", "23", "24", "25", "26"},
            {"18", "27", "28", "29", "30", "31", "1/1", "2"},
            {"19", "3", "4", "5", "6", "7", "8", "9"},
            {"20", "10", "11", "12", "13", "14", "15", "16"},
    };

    public CalendarTableAdapter(Context context) {
        super(context);

        Resources resources = context.getResources();

        // 从dimen.xml文件中获取格子的宽高度
        width = resources.getDimensionPixelSize(R.dimen.table_width);
        height = resources.getDimensionPixelSize(R.dimen.table_height);
    }

    /**
     * 返回表格的行数
     */
    @Override
    public int getRowCount() {
        return calendarString.length - 1;
    }

    /**
     * 返回表格的列数
     */
    @Override
    public int getColumnCount() {
        return calendarString[0].length - 1;
    }

    @Override
    public int getWidth(int column) {
        return width;
    }

    @Override
    public int getHeight(int row) {
        return height;
    }

    @Override
    public String getCellString(int row, int column) {
        return calendarString[row + 1][column + 1];		// row 和 column都从-1开始，而不是0
    }

    /**
     * 根据getItemViewType(row, column)返回值确定(row, column)用什么布局文件填充
     * @see #getItemViewType(int, int)
     */
    @Override
    public int getLayoutResource(int row, int column) {
        final int layoutResource;
        switch (getItemViewType(row, column)) {
            case 4:
                layoutResource = R.drawable.calendar_item_table_first_cell;
                break;
            case 3:
                layoutResource = R.drawable.calendar_item_table1_header;
                break;
            case 2:
                layoutResource = R.drawable.calendar_item_table_holiday;
                break;
            case 1:
                layoutResource = R.drawable.calendar_item_table_weekend;
                break;
            case 0:
                layoutResource = R.drawable.calendar_item_table1;
                break;
            default:
                throw new RuntimeException("wtf?");
        }
        return layoutResource;
    }

    /**
     * 根据具体的校历确定返回什么值
     */
    @Override
    public int getItemViewType(int row, int column) {
        // 节假日(清明节、劳动节等等)放假日期,根据校历来的
        if (row == -1 && column == -1) {		// 横竖表头交界的地方
            return 4;
        } else if (row < 0 || column < 0) {		// 横竖表头
            return 3;
        } else if ((row == 5 && column == 0) || (row == 8 && column == 5) || (row == 15 && column == 6)
                || (row == 4 && column == 6) || (row == 5 && column == 1) || (row == 8 && column == 6)
                || (row == 9 && column == 0) || (row == 16 && column == 0) || (row == 16 && column == 1)
                || (row == 17 && column == 6)) {	// 假日
            return 2;
        } else if (checkIsHoliday(row, column)) {	// 双休日
            return 1;
        } else		// 普通的格子
            return 0;
    }

    /**
     * 检查是否是节假日
     * @return true if it is, otherwise return false
     */
    private boolean checkIsHoliday(int row, int column) {
        if ((column == 0 || column == 6) || (row == 8 && column == 5) ||
                (row == 5 && column == 1) || (row == 16 && column == 1))
            return true;
        return false;
    }

    /**
     * 填充类型的种类,一定要与上面保持一致
     * @see #getLayoutResource(int, int)
     */
    @Override
    public int getViewTypeCount() {
        return 5;
    }
}

