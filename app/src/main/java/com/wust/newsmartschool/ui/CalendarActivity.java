package com.wust.newsmartschool.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.CalendarTableAdapter;
import com.wust.newsmartschool.utils.TableFixHeaders;


public class CalendarActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TableFixHeaders tfh_table;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tfh_table = (TableFixHeaders) findViewById(R.id.calendar_table);
        tfh_table.setAdapter(new CalendarTableAdapter(CalendarActivity.this));

        int tableHeight = getWindowManager().getDefaultDisplay().getHeight() -
                toolbar.getHeight() * 11 / 6;
        tfh_table.setTableHeight(tableHeight);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

