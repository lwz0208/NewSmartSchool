package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.wust.newsmartschool.R;

public class SchoolHistoryActivity extends Activity {
    private TextView textView170;
    private TextView textView160;
    private TextView textView150;
    private TextView textView140;
    private TextView textView130;
    private TextView textView120;
    private TextView textView110;
    private TextView textView100;
    private TextView textView90;
    private TextView textView9;
    private TextView textView10;
    private TextView textView11;
    private TextView textView12;
    private TextView textView13;
    private TextView textView14;
    private TextView textView15;
    private TextView textView16;
    private TextView textView17;
    private TextView textView18;
    private TextView textView19;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_school_history);

        ImageView backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textView90 = (TextView)this.findViewById(R.id.history9);
        textView9 = (TextView)this.findViewById(R.id.history9a);
        textView100 = (TextView)this.findViewById(R.id.history10);
        textView10 = (TextView)this.findViewById(R.id.history10a);
        textView110 = (TextView)this.findViewById(R.id.history11);
        textView11 = (TextView)this.findViewById(R.id.history11a);
        textView120 = (TextView)this.findViewById(R.id.history12);
        textView12 = (TextView)this.findViewById(R.id.history12a);
        textView130 = (TextView)this.findViewById(R.id.history13);
        textView13 = (TextView)this.findViewById(R.id.history13a);
        textView140 = (TextView)this.findViewById(R.id.history14);
        textView14 = (TextView)this.findViewById(R.id.history14a);
        textView150 = (TextView)this.findViewById(R.id.history15);
        textView15 = (TextView)this.findViewById(R.id.history15a);
        textView160 = (TextView)this.findViewById(R.id.history16);
        textView16 = (TextView)this.findViewById(R.id.history16a);
        textView170 = (TextView)this.findViewById(R.id.history17);
        textView17 = (TextView)this.findViewById(R.id.history17a);
        textView18 = (TextView)this.findViewById(R.id.history18a);
        textView19 = (TextView)this.findViewById(R.id.history19a);

        textView90.setText("1954年:\n");
        textView9.setText("武昌钢铁工业学校\n"+"武昌建筑工业学校");
        textView100.setText("1958年:\n");
        textView10.setText("武汉钢铁学院\n"+"湖北冶金工业专科学校");
        textView110.setText("\n1960年:\n");
        textView11.setText("武汉钢铁学院\n"+"湖北冶金工业专科学校\n"+"武钢医学院");
        textView120.setText("\n1961年:\n");
        textView12.setText("武汉钢铁学院\n"+"湖北冶金工业专科学校\n"+"武钢医学院");
        textView130.setText("\n1963年:\n");
        textView13.setText("武汉钢铁学院\n"+"武汉钢铁学校\n"+"武汉冶金卫生学校");
        textView140.setText("\n1965年:\n");
        textView14.setText("武汉钢铁学院\n"+"武汉钢铁学校\n"+"武汉冶金医学专科学校");
        textView150.setText("\n1979年:\n");
        textView15.setText("武汉钢铁学院\n"+"解放军基建工程兵第二技术学校\n"+"武汉冶金医学专科学校");
        textView160.setText("\n1983年:\n");
        textView16.setText("武汉钢铁学院\n"+"武汉冶金建筑专科学校\n"+"武汉冶金医学专科学校");
        textView170.setText("\n1993年:\n");
        textView17.setText("武汉钢铁学院\n"+"武汉建筑高等专科学校\n"+"武汉冶金医学高等专科学校");
        textView18.setText("武 汉 冶 金 科 技 大 学 ");
        textView19.setText("武 汉 科 技 大 学 ");








    }


}

