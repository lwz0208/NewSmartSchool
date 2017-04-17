package com.wust.newsmartschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.CommonListviewAdapter;


public class ComprehensiveServiceActivity extends BaseActivity {
    private ListView listView;
    private CommonListviewAdapter listAdapter;
    private static final String[] nameList = {"部门服务黄家湖校区时间安排表", "黄家湖校区校车运行信息", "校历", "通讯录", "安全保卫"};
    private static final int[] imgList = {R.drawable.campus_bus_service_hjh, R.drawable.campus_bus_hjh, R.drawable.campus_calendar, R.drawable.campus_contact, R.drawable.security_guide};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprehensive_service);


        listView = (ListView) findViewById(R.id.comprehensive_list);
        listAdapter = new CommonListviewAdapter(this, imgList, nameList);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                Intent intent = new Intent();
                switch (position) {
                    case 0:
                        intent = new Intent(ComprehensiveServiceActivity.this, HjhDepartmentActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(ComprehensiveServiceActivity.this, HjhBusActivity.class);
                        intent.putExtra("typeid", "00500170701");
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(ComprehensiveServiceActivity.this, CalendarActivity.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(ComprehensiveServiceActivity.this, AddressListActivity.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(ComprehensiveServiceActivity.this, Security_Department_Activity.class);
                        startActivity(intent);
                        break;
                }

            }

        });
    }


    public void back(View view) {
        finish();
        if (android.os.Build.VERSION.SDK_INT > 5) {
            overridePendingTransition(
                    R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
    }
}






