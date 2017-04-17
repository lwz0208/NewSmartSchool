package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.Jynr;
import com.wust.newsmartschool.domain.Pjjg;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

public class DetailPJActivity extends Activity implements View.OnClickListener,RadioGroup.OnCheckedChangeListener{
    private Button buttonsave=null,buttoncommit=null;
    private String URL="http://202.114.255.100/Xsda/evaluate/commit_save_evaluate.php";
    private String URLJY="http://202.114.255.100/Xsda/evaluate/get_suggest_content.php";
    private String URLPJ="http://202.114.255.100/Xsda/evaluate/get_evaluate_option_09.php";
    private RadioGroup group1,group2,group3,group4,group5;
    private RadioButton rbutton1,rbutton2,rbutton3,rbutton4,rbutton5,rbutton6,rbutton7,rbutton8,rbutton9,
            rbutton10,rbutton11,rbutton12,rbutton13,rbutton14,rbutton15,rbutton16,rbutton17,rbutton18,rbutton19,rbutton20,
            rbutton21,rbutton22,rbutton23,rbutton24,rbutton25;
    private String pj1,pj2,pj3,pj4,pj5,jy1,jy2;
    private float pjnum1,pjnum2,pjnum3,pjnum4,pjnum5,score=0;
    private TextView head_title=null;
    private ImageView goback=null;
    private EditText edittext1,edittext2;
    private String evaluateId1,evaluateId2,teacherId,issaveview,evaluateId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pj);
        /*Bundle bundle=getIntent().getExtras();
        issaveview=bundle.getString("issaveview");
        //这是保存过的评价界面

        evaluateId1=bundle.getString("evaluateId1");
        evaluateId2=bundle.getString("evaluateId2");
        teacherId=bundle.getString("teacherId");
        evaluateId=bundle.getString("evaluateId");
        if(issaveview.equals("1")){

            getSaveContent();
        }*/
        init();
        group1.setOnCheckedChangeListener(this);
        group2.setOnCheckedChangeListener(this);
        group3.setOnCheckedChangeListener(this);
        group4.setOnCheckedChangeListener(this);
        group5.setOnCheckedChangeListener(this);
        buttonsave.setOnClickListener(this);
        buttoncommit.setOnClickListener(this);
        goback.setOnClickListener(this);
        head_title.setText("具体评价");

    }


    public void init(){
        buttonsave= (Button) findViewById(R.id.buttonsave);
        buttoncommit= (Button) findViewById(R.id.buttoncommit);
        group1= (RadioGroup) findViewById(R.id.RadioGroup01);
        group2= (RadioGroup) findViewById(R.id.RadioGroup02);
        group3= (RadioGroup) findViewById(R.id.RadioGroup03);
        group4= (RadioGroup) findViewById(R.id.RadioGroup04);
        group5= (RadioGroup) findViewById(R.id.RadioGroup05);
        rbutton1=(RadioButton)findViewById(R.id.RadioButton1);
        rbutton2=(RadioButton)findViewById(R.id.RadioButton2);
        rbutton3=(RadioButton)findViewById(R.id.RadioButton3);
        rbutton4=(RadioButton)findViewById(R.id.RadioButton4);
        rbutton5=(RadioButton)findViewById(R.id.RadioButton5);
        rbutton6=(RadioButton)findViewById(R.id.RadioButton6);
        rbutton7=(RadioButton)findViewById(R.id.RadioButton7);
        rbutton8=(RadioButton)findViewById(R.id.RadioButton8);
        rbutton9=(RadioButton)findViewById(R.id.RadioButton9);
        rbutton10=(RadioButton)findViewById(R.id.RadioButton10);
        rbutton11=(RadioButton)findViewById(R.id.RadioButton11);
        rbutton12=(RadioButton)findViewById(R.id.RadioButton12);
        rbutton13=(RadioButton)findViewById(R.id.RadioButton13);
        rbutton14=(RadioButton)findViewById(R.id.RadioButton14);
        rbutton15=(RadioButton)findViewById(R.id.RadioButton15);
        rbutton16=(RadioButton)findViewById(R.id.RadioButton16);
        rbutton17=(RadioButton)findViewById(R.id.RadioButton17);
        rbutton18=(RadioButton)findViewById(R.id.RadioButton18);
        rbutton19=(RadioButton)findViewById(R.id.RadioButton19);
        rbutton20=(RadioButton)findViewById(R.id.RadioButton20);
        rbutton21=(RadioButton)findViewById(R.id.RadioButton21);
        rbutton22=(RadioButton)findViewById(R.id.RadioButton22);
        rbutton23=(RadioButton)findViewById(R.id.RadioButton23);
        rbutton24=(RadioButton)findViewById(R.id.RadioButton24);
        rbutton25=(RadioButton)findViewById(R.id.RadioButton25);
        head_title= (TextView) findViewById(R.id.head_title);
        goback= (ImageView) findViewById(R.id.goback);
        edittext1= (EditText) findViewById(R.id.edittext1);
        edittext2= (EditText) findViewById(R.id.edittext2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttoncommit:
                //提交按钮事件
                if(pj1!=null&&pj2!=null&&pj3!=null&&pj4!=null&&pj5!=null) {
                    jy1=edittext1.getText().toString();
                    jy2=edittext2.getText().toString();
                    Toast.makeText(getApplication(),pj1+pj2+pj3+pj4+pj5+jy1+jy2+"总分："+score, Toast.LENGTH_SHORT).show();
                    commit();
                }else{
                    Toast.makeText(getApplication(),"评教不完整！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.buttonsave:
                //保存按钮事件

                if(pj1!=null&&pj2!=null&&pj3!=null&&pj4!=null&&pj5!=null) {
                    jy1=edittext1.getText().toString();
                    jy2=edittext2.getText().toString();
                    Toast.makeText(getApplication(),pj1+pj2+pj3+pj4+pj5+jy1+jy2+"总分："+score, Toast.LENGTH_SHORT).show();
                    save();
                }else{
                    Toast.makeText(getApplication(),"评教不完整！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.goback:
                finish();
                break;
            default:
                ;
        }

    }

    private  void getSaveContent(){
        //获取建议内容
        OkHttpUtils.post().url(URLJY)
                .addParams("evaluateId",evaluateId )
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                        Toast.makeText(DetailPJActivity.this,"网络错误",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(DetailPJActivity.this,"保存结果"+response,Toast.LENGTH_SHORT).show();
                        Log.i("DetailPJActivity", "获取保存评教接口访问成功：" + response);
                        JSONArray jsonArray = JSON.parseArray(response);
                        List<Jynr> jynrs= JSONArray.parseArray(jsonArray.toJSONString(), Jynr.class);
                        if(jynrs.size()>=1){
                            edittext1.setText(jynrs.get(0).getJynr().toString());
                        }
                        if(jynrs.size()>=2){
                            edittext2.setText(jynrs.get(1).getJynr().toString());
                        }
                    }
                });

        //获取保存评价选项
        OkHttpUtils.post().url(URLPJ)
                .addParams("evaluateId",evaluateId )
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                        Toast.makeText(DetailPJActivity.this,"网络错误",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        // Toast.makeText(DetailPJActivity.this,"保存结果"+response,Toast.LENGTH_SHORT).show();
                        Log.i("DetailPJActivity", "获取保存评教接口访问成功：" + response);
                        JSONArray jsonArray = JSON.parseArray(response);
                        List<Pjjg> pjjgs = JSONArray.parseArray(jsonArray.toJSONString(), Pjjg.class);
                        if (pjjgs.size() >= 5) {
                            if (pjjgs.get(0).getMc().equals("很好")) {
                                rbutton1.setChecked(true);
                            } else if (pjjgs.get(0).getMc().equals("好")) {
                                rbutton2.setChecked(true);
                            } else if (pjjgs.get(0).getMc().equals("一般")) {
                                rbutton3.setChecked(true);
                            } else if (pjjgs.get(0).getMc().equals("较差")) {
                                rbutton4.setChecked(true);
                            } else if (pjjgs.get(0).getMc().equals("很差")) {
                                rbutton5.setChecked(true);
                            }

                            if (pjjgs.get(1).getMc().equals("很好")) {
                                rbutton6.setChecked(true);
                            } else if (pjjgs.get(1).getMc().equals("好")) {
                                rbutton7.setChecked(true);
                            } else if (pjjgs.get(1).getMc().equals("一般")) {
                                rbutton8.setChecked(true);
                            } else if (pjjgs.get(1).getMc().equals("较差")) {
                                rbutton9.setChecked(true);
                            } else if (pjjgs.get(1).getMc().equals("很差")) {
                                rbutton10.setChecked(true);
                            }

                            if (pjjgs.get(2).getMc().equals("很好")) {
                                rbutton11.setChecked(true);
                            } else if (pjjgs.get(2).getMc().equals("好")) {
                                rbutton12.setChecked(true);
                            } else if (pjjgs.get(2).getMc().equals("一般")) {
                                rbutton13.setChecked(true);
                            } else if (pjjgs.get(2).getMc().equals("较差")) {
                                rbutton14.setChecked(true);
                            } else if (pjjgs.get(2).getMc().equals("很差")) {
                                rbutton15.setChecked(true);
                            }

                            if (pjjgs.get(3).getMc().equals("很好")) {
                                rbutton16.setChecked(true);
                            } else if (pjjgs.get(3).getMc().equals("好")) {
                                rbutton17.setChecked(true);
                            } else if (pjjgs.get(3).getMc().equals("一般")) {
                                rbutton18.setChecked(true);
                            } else if (pjjgs.get(3).getMc().equals("较差")) {
                                rbutton19.setChecked(true);
                            } else if (pjjgs.get(3).getMc().equals("很差")) {
                                rbutton20.setChecked(true);
                            }

                            if (pjjgs.get(4).getMc().equals("很好")) {
                                rbutton21.setChecked(true);
                            } else if (pjjgs.get(4).getMc().equals("好")) {
                                rbutton22.setChecked(true);
                            } else if (pjjgs.get(4).getMc().equals("一般")) {
                                rbutton23.setChecked(true);
                            } else if (pjjgs.get(4).getMc().equals("较差")) {
                                rbutton24.setChecked(true);
                            } else if (pjjgs.get(4).getMc().equals("很差")) {
                                rbutton25.setChecked(true);
                            }

                        }
                    }
                });


    }
    private void save(){

        OkHttpUtils.post().url(URL).addParams("studentId","201501109004")
                .addParams("teacherId",teacherId )
                .addParams("evaluateId1",evaluateId1)
                .addParams("evaluateId2",evaluateId2)
                .addParams("isSave","1")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                        Toast.makeText(DetailPJActivity.this,"网络错误",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                         Toast.makeText(DetailPJActivity.this,"保存结果"+response, Toast.LENGTH_SHORT).show();
                        Log.i("DetailPJActivity", "保存评教时间接口访问成功：" + response);

                    }
                });
    }

    private  void commit(){
        OkHttpUtils.post().url(URL).addParams("studentId","201501109004")
                .addParams("teacherId",teacherId )
                .addParams("evaluateId1",evaluateId1)
                .addParams("evaluateId2",evaluateId2)
                .addParams("isSave","0")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                        Toast.makeText(DetailPJActivity.this,"网络错误",
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(DetailPJActivity.this,"保存结果"+response, Toast.LENGTH_SHORT).show();
                        Log.i("DetailPJActivity", "保存评教时间接口访问成功：" + response);

                    }
                });
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.RadioButton1:
                pj1="1";
                score+=0.2*95;
                break;
            case R.id.RadioButton2:
                pj1="2";
                score+=0.20*85;
                break;
            case R.id.RadioButton3:
                pj1="3";
                score+=0.20*75;
                break;
            case R.id.RadioButton4:
                pj1="4";
                score+=0.20*60;
                break;
            case R.id.RadioButton5:
                pj1="5";
                score+=0.20*20;
                break;
            case R.id.RadioButton6:
                pj2="1";
                score+=0.20*95;
                break;
            case R.id.RadioButton7:
                pj2="2";
                score+=0.20*85;
                break;
            case R.id.RadioButton8:
                pj2="3";
                score+=0.20*75;
                break;
            case R.id.RadioButton9:
                pj2="4";
                score+=0.20*60;
                break;
            case R.id.RadioButton10:
                pj2="5";
                score+=0.20*20;
                break;
            case R.id.RadioButton11:
                pj3="1";
                score+=0.21*95;
                break;
            case R.id.RadioButton12:
                pj3="2";
                score+=0.21*85;
                break;
            case R.id.RadioButton13:
                pj3="3";
                score+=0.21*75;
                break;
            case R.id.RadioButton14:
                pj3="4";
                score+=0.21*60;
                break;
            case R.id.RadioButton15:
                pj3="5";
                score+=0.21*20;
                break;
            case R.id.RadioButton16:
                pj4="1";
                score+=0.18*95;
                break;
            case R.id.RadioButton17:
                pj4="2";
                score+=0.18*85;
                break;
            case R.id.RadioButton18:
                pj4="3";
                score+=0.18*75;
            case R.id.RadioButton19:
                pj4="4";
                score+=0.18*60;
                break;
            case R.id.RadioButton20:
                pj4="5";
                score+=0.18*20;
                break;
            case R.id.RadioButton21:
                pj5="1";
                score+=0.21*95;
                break;
            case R.id.RadioButton22:
                pj5="2";
                score+=0.21*85;
                break;
            case R.id.RadioButton23:
                pj5="3";
                score+=0.21*75;
                break;
            case R.id.RadioButton24:
                pj5="4";
                score+=0.21*60;
            case R.id.RadioButton25:
                pj5="5";
                score+=0.21*20;
                break;
            default:
                ;

        }
    }
}

