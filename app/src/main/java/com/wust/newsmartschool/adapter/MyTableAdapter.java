package com.wust.newsmartschool.adapter;

import android.view.View;
import android.view.ViewGroup;


/**
 * A adapter for table.
 * @author Yorek Liu
 * @version 1.0
 */
public interface MyTableAdapter {

    public final static int IGNORE_ITEM_VIEW_TYPE = -1;

    public int getRowCount();

    public int getColumnCount();

    public View getView(int row, int column, View convertView, ViewGroup parent);

    public int getWidth(int column);

    public int getHeight(int row);

    public int getItemViewType(int row, int column);

    public int getViewTypeCount();
}

