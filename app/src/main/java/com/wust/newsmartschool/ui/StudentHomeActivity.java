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
import android.widget.Toast;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.CommonListviewAdapter;
import com.wust.newsmartschool.utils.GlobalVar;


public class StudentHomeActivity extends BaseActivity implements View.OnClickListener {

    private CommonListviewAdapter adapter4;
    private ListView listView4;
    private static final int[] imgList4 = {R.drawable.student_quality, R.drawable.xianfeng};
    private static final String[] item4 = {"素质拓展", "先锋在线"};
    private TextView emptyroom;

    private TextView tableTextView;
    private TextView gradeTextView;
    private TextView ScoreCount;

    private TextView tv5;
    private TextView tv6;

    private TextView tv7;
    private TextView tv8;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_student_home);

        InitListview();

        emptyroom = (TextView) findViewById(R.id.emptyroom);
        emptyroom.setOnClickListener(this);
        ScoreCount = (TextView) findViewById(R.id.stu_3);
        ScoreCount.setOnClickListener(this);
        tableTextView = (TextView) findViewById(R.id.stu_1);
        tableTextView.setOnClickListener(this);
        gradeTextView = (TextView) findViewById(R.id.stu_2);
        gradeTextView.setOnClickListener(this);
        tv5 = (TextView) findViewById(R.id.stu_5);
        tv6 = (TextView) findViewById(R.id.stu_6);
        tv7 = (TextView) findViewById(R.id.stu_7);
        tv8 = (TextView) findViewById(R.id.stu_8);

        tv5.setOnClickListener(this);
        tv6.setOnClickListener(this);
        tv7.setOnClickListener(this);
        tv8.setOnClickListener(this);


        setTextColor(GlobalVar.userid);
        Log.w("wxf", "A" + GlobalVar.userid + GlobalVar.username);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        //检测手机是否回收掉static变量
        if (GlobalVar.isInfoGCBySystem()) {
            GlobalVar.initGlobalvar(this);
            setTextColor(GlobalVar.userid);
        }
        super.onResume();
    }

    private void InitListview() {
        listView4 = (ListView) findViewById(R.id.student_list4);

        adapter4 = new CommonListviewAdapter(this, imgList4, item4);

        listView4.setAdapter(adapter4);

        listView4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    Intent intent = new Intent();
                    intent.setClass(StudentHomeActivity.this, QualityDevelopActivity.class);
                    startActivity(intent);
                } else if (position == 1) {
                    Intent intent = new Intent(StudentHomeActivity.this, DocumentActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    public void setTextColor(String userid) {
        if (userid == null || GlobalVar.username == null
                || GlobalVar.username.equals("") || userid.length() < 12) {
            emptyroom.setTextColor(getResources().getColor(R.color.black));
            tableTextView.setTextColor(getResources().getColor(R.color.gray));
            tv5.setTextColor(getResources().getColor(R.color.gray));
            tv6.setTextColor(getResources().getColor(R.color.gray));
            gradeTextView.setTextColor(getResources().getColor(R.color.gray));
            ScoreCount.setTextColor(getResources().getColor(R.color.gray));

            //emptyroom.setEnabled(false);
            emptyroom.setEnabled(true);
            tableTextView.setEnabled(false);
            tv5.setEnabled(false);
            tv6.setEnabled(false);
            gradeTextView.setEnabled(false);
            ScoreCount.setEnabled(false);
        } else if (userid.length() == 12) {
            emptyroom.setTextColor(getResources().getColor(R.color.black));
            tableTextView.setTextColor(getResources().getColor(R.color.black));
            tv5.setTextColor(getResources().getColor(R.color.black));
            tv6.setTextColor(getResources().getColor(R.color.black));
            gradeTextView.setTextColor(getResources().getColor(R.color.black));
            ScoreCount.setTextColor(getResources().getColor(R.color.black));

            emptyroom.setEnabled(true);
            tableTextView.setEnabled(true);
            tv5.setEnabled(true);
            tv6.setEnabled(true);
            gradeTextView.setEnabled(true);
            ScoreCount.setEnabled(true);
        }
    }

    public void onClick(View v) {

        Intent intent;
        switch (v.getId()) {
            case R.id.emptyroom:
                intent = new Intent();
//			Log.i("TAG", "GlobalVar.usertype:"+GlobalVar.usertype.trim().length());
                if (GlobalVar.usertype != null && GlobalVar.usertype.trim().equals("0130")) {
//				Log.i("TAG", "GlobalVar.usertype:"+GlobalVar.usertype.trim());
                    intent.setClass(StudentHomeActivity.this,
                            EmptyClassroom2Activity.class);
                    startActivity(intent);
                } else {
//				Log.i("TAG", "GlobalVar.usertype:"+GlobalVar.usertype.trim());
                    intent.setClass(StudentHomeActivity.this, EmptyClassroomActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.stu_3:
                Toast.makeText(getApplicationContext(), "暂未开放", Toast.LENGTH_SHORT).show();

			/*if (GlobalVar.username == null || GlobalVar.username.equals(""))
            {
				intent = new Intent();
				intent.setClass(StudentHomeActivity.this, LoginActivity.class);
				startActivity(intent);
				break;
			}
			else
			{
				intent = new Intent();
				intent.setClass(StudentHomeActivity.this,
						Student_Credit_Activity.class);
				startActivity(intent);
				break;

			}*/
                break;
            case R.id.stu_1:
                if (GlobalVar.username == null || GlobalVar.username.equals("")) {
                    intent = new Intent();
                    intent.setClass(StudentHomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    break;
                } else {
                    intent = new Intent();
                    intent.setClass(StudentHomeActivity.this,
                            PersonTimetableActivity.class);
                    startActivity(intent);
                    break;
                }

            case R.id.stu_2:
                if (GlobalVar.username == null || GlobalVar.username.equals("")) {
                    intent = new Intent();
                    intent.setClass(StudentHomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    break;
                } else {
                    intent = new Intent(StudentHomeActivity.this,
                            PersonalGradeActivity.class);
                    startActivity(intent);
                    break;
                }
            case R.id.stu_5:
                if (GlobalVar.username == null || GlobalVar.username.equals("")) {
                    intent = new Intent();
                    intent.setClass(StudentHomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent();
                    intent.setClass(StudentHomeActivity.this,
                            TrainingPlanActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.stu_6:
                if (GlobalVar.username == null || GlobalVar.username.equals("")) {
                    intent = new Intent();
                    intent.setClass(StudentHomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent();
                    intent.setClass(StudentHomeActivity.this,
                            CostDetailActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.stu_7:
                intent = new Intent();
                intent.setClass(StudentHomeActivity.this,
                        JobInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.stu_8:
                intent = new Intent();
                intent.setClass(StudentHomeActivity.this,
                        PsychologyConsultingActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    public void back(View view) {
        finish();
        if (android.os.Build.VERSION.SDK_INT > 5) {
            overridePendingTransition(
                    R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
    }

}

