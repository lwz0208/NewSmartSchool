package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.CommonListviewAdapter;
import com.wust.newsmartschool.utils.GlobalVar;


public class SchoolServiceActivity extends BaseActivity {
    private CommonListviewAdapter adapter2;
    private ListView listView2;
    //	private CommonListviewAdapter adapter1, adapter2;
//	private ListView listView1, listView2;
    // 以下是功能组件
    private TextView onlineRepairTextView, order_meal;
    //	private static final int[] imgList1 = { R.drawable.school_alert,
//			R.drawable.school_security };
//	private static final String[] item1 = { "警情通报", "安保互动" };
    private static final int[] imgList2 = {R.drawable.school_bus_commonent};
    private static final String[] item2 = {"班车评价"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_school_service);

        // 初始化功能组件
        InitView();
        // 初始化列表
        //	InitListview();
    }

    private void InitView() {
        onlineRepairTextView = (TextView) findViewById(R.id.online_repair);
        order_meal = (TextView) findViewById(R.id.order_meal);
        order_meal.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (GlobalVar.username == null || GlobalVar.username.equals("")) {
//                    Intent intent = new Intent();
//                    intent.setClass(SchoolServiceActivity.this,
//                            LoginActivity.class);
//                    startActivity(intent);
                } else {
//                    Intent intent = new Intent();
//                    intent.setClass(SchoolServiceActivity.this,
//                            MealActivity.class);
                    Log.v("eef1", "1111111111");
                    Log.v("11cc11", GlobalVar.username);
                    Log.v("1111111", GlobalVar.usertype);
//                    startActivity(intent);
//					AlertDialog.Builder builder = new Builder(
//							SchoolServiceActivity.this);
//					builder.setMessage("正在开发中，电话订餐请拨打：88856818");
//					builder.setTitle("提示");
//					builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							// TODO Auto-generated method stub
//							Toast.makeText(getApplicationContext(), "", 1);
//						}
//					});
//					builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//						public void onClick(DialogInterface dialog, int which) {
//							Toast.makeText(getApplicationContext(), "", 1);
//						}
//					});
//					builder.create().show();
                }
            }
        });

        onlineRepairTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (GlobalVar.username == null || GlobalVar.username.equals("")) {
//                    Intent intent = new Intent();
//                    intent.setClass(SchoolServiceActivity.this,
//                            LoginActivity.class);
//                    startActivity(intent);
                } else {
//                    Intent intent = new Intent();
//                    intent.setClass(SchoolServiceActivity.this,
//                            Logistics_MsgActivity.class);
//                    startActivity(intent);
                }
            }
        });
    }

    private void InitListview() {
//		listView1 = (ListView) findViewById(R.id.school_list1);
        listView2 = (ListView) findViewById(R.id.school_list2);
//		adapter1 = new CommonListviewAdapter(this, imgList1, item1);
        adapter2 = new CommonListviewAdapter(this, imgList2, item2);
//		listView1.setAdapter(adapter1);
        listView2.setAdapter(adapter2);

//		listView1.setOnItemClickListener(new OnItemClickListener() {
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				if (position == 0) {
//				}
//			}
//		});

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent();
                    if (GlobalVar.username == null || GlobalVar.username.equals("")) {
                        intent = new Intent();
                        intent.setClass(SchoolServiceActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
//                        intent = new Intent();
//                        intent.setClass(SchoolServiceActivity.this,
//                                BusCommentActivity.class);
//                        startActivity(intent);
                    }
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

