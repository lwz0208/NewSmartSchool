package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.CommonListviewAdapter;
import com.wust.newsmartschool.utils.GlobalVar;

public class PsychologyConsultingActivity extends Activity implements View.OnClickListener {

    private TextView head_title;
    private ImageView back;
    private TextView tv1,tv2;
    //	private TextView tv1,tv2,tv3,tv4;
    private CommonListviewAdapter adapter1,adapter2,adapter3;
    private ListView listView1,listView2,listView3;
    private static final int [] imgList1={R.drawable.psyexpertinfo};
    private static final String[] item1={"专家介绍"};
    private static final int [] imgList2={R.drawable.psymyappoint};
    private static final String [] item2={"我的预约"};
    private static final int [] imgList3={R.drawable.psyappointment};
    private static final String [] item3={"一键预约"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_psychology_consulting);
        InitView();
        InitListview();
    }

    private void InitView(){
        tv1=(TextView)findViewById(R.id.psychology_tv1);//中心简介
        tv2=(TextView)findViewById(R.id.psychology_tv2);//最新公告
//		tv3=(TextView)findViewById(R.id.psychology_tv3);//值班安排
//		tv4=(TextView)findViewById(R.id.psychology_tv4);//常见问题
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
//		tv3.setOnClickListener(this);
//		tv4.setOnClickListener(this);

        head_title=(TextView)findViewById(R.id.head_title);
        back = (ImageView)findViewById(R.id.goback);
        head_title.setText("心理咨询");
        back.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    private void InitListview(){
        listView1=(ListView)findViewById(R.id.psychology_tv5);//专家介绍
        listView2=(ListView)findViewById(R.id.psychology_tv6);//我的预约
        listView3=(ListView)findViewById(R.id.psychology_tv7);//一键预约
        adapter1=new CommonListviewAdapter(this, imgList1, item1);
        adapter2=new CommonListviewAdapter(this, imgList2, item2);
        adapter3=new CommonListviewAdapter(this, imgList3, item3);
        listView1.setAdapter(adapter1);
        listView2.setAdapter(adapter2);
        listView3.setAdapter(adapter3);
        //专家介绍
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(position==0){
                    Intent intent=new Intent();
                    intent.setClass(PsychologyConsultingActivity.this, Psy_ExpertIntroduceActivity.class);
                    startActivity(intent);
                }
            }
        });
        //我的预约
        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(position==0){//判断位置，今后可能会添加Item，所以这里的设计师热插式
                    if(GlobalVar.username==null||GlobalVar.username.equals("")){
                        Intent  intent=new Intent();
                        intent.setClass(PsychologyConsultingActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else{
                        Intent  intent=new Intent();
                        intent.setClass(PsychologyConsultingActivity.this, Psy_MyAppointmentActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
        //一键预约
        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(position==0){
                    if(GlobalVar.username==null||GlobalVar.username.equals("")){
                        Intent  intent=new Intent();
                        intent.setClass(PsychologyConsultingActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }else{
                        Intent  intent=new Intent();
                        intent.setClass(PsychologyConsultingActivity.this, Psy_DirectAppointActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    public void onClick(View v) {
        Intent intent=new Intent();
        switch (v.getId()) {
//            case R.id.psychology_tv1://中心简介
//                intent.setClass(PsychologyConsultingActivity.this, CenterIntroductionActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.psychology_tv2://最新公告
//			intent.setClass(PsychologyConsultingActivity.this, Psy_NewestAnnouncementActivity.class);
//			startActivity(intent);
               // break;
//		case R.id.psychology_tv3://值班安排
//			intent.setClass(PsychologyConsultingActivity.this, Psy_DutyScheduleActivity.class);
//			startActivity(intent);
//			break;
//		case R.id.psychology_tv4://常见问题
////			intent.setClass(PsychologyConsultingActivity.this, Psy_FrequentlyAskedQuesActivity.class);
////			startActivity(intent);
//			break;
            default:
                break;
        }
    }
}

