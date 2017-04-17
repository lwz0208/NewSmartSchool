package com.wust.newsmartschool.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.utils.ImageRotation;


public class Security_Department_detail_Activity extends AppCompatActivity {
    // 标题控件
    private Toolbar toolbar;
    private TextView msgTextView;
    private Context context;
    private int id = 0;

    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security__department_detail_);
        context=getApplicationContext();
        id = getIntent().getIntExtra("id", 0);
        InitView();
    }

    private void InitView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        msgTextView = (TextView) findViewById(R.id.tv_introduction);

        // 根据id来填充数据
        switch (id) {
            case 1: // 使用activity_security_idcard.xml布局文件填充
                setContentView(R.layout.activity_security_idcard);
                Bitmap bitmap1 = new ImageRotation(Security_Department_detail_Activity.this, R.drawable.xlistview_arrow, 45).getBitmap();
                Bitmap bitmap2 = new ImageRotation(Security_Department_detail_Activity.this, R.drawable.xlistview_arrow, -45).getBitmap();
                ((ImageView) findViewById(R.id.bwc_a1)).setImageBitmap(bitmap1);
                ((ImageView) findViewById(R.id.bwc_a2)).setImageBitmap(bitmap2);
                ((ImageView) findViewById(R.id.bwc_a3)).setImageBitmap(bitmap2);
                ((ImageView) findViewById(R.id.bwc_a4)).setImageBitmap(bitmap1);
                ((ImageView) findViewById(R.id.bwc_a5)).setImageBitmap(bitmap1);
                ((ImageView) findViewById(R.id.bwc_a6)).setImageBitmap(bitmap2);
                ((ImageView) findViewById(R.id.bwc_a7)).setImageBitmap(bitmap1);
                setchaining((TextView) findViewById(R.id.tv_bwc_id));
                toolbar = (Toolbar) findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                break;
            case 2:
                toolbar.setTitle("新生户口异动情况说明");
                msgTextView.setText(R.string.security_hukou_2);
                break;
            case 3:
                toolbar.setTitle("毕业生户口迁移须知");
                msgTextView.setText(R.string.security_hukou_3);
                break;
            case 4:
                toolbar.setTitle("各校区报警电话");
                msgTextView.setText(R.string.security_baojing);
                setchaining(msgTextView);
                break;
            case 5:
                toolbar.setTitle("通行授权申办程序表");
                msgTextView.setText("");
                msgTextView.setBackgroundResource(R.drawable.security_cheliang_tx1);
                break;
            case 6:
                toolbar.setTitle("门禁系统缴费说明");
                msgTextView.setText(R.string.security_che2);
                break;
            case 7:
                toolbar.setTitle("机动车管理办法");
                msgTextView.setText(R.string.security_che3);
                break;
            case 8:
                toolbar.setTitle("夜间出入须知");
                msgTextView.setText(R.string.security_sushe1);
                setchaining(msgTextView);
                break;
            case 9:
                toolbar.setTitle("备用钥匙借用须知");
                msgTextView.setText(R.string.security_sushe2);
                break;
            default:
                break;
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setchaining(TextView msgTextView){
        msgTextView.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text=msgTextView.getText();
        if(text instanceof Spannable){
            int end =text.length();
            Spannable sp=(Spannable) msgTextView.getText();
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.clearSpans();
            for (URLSpan url : urls) {

                MyURLSpan myURLSpan = new MyURLSpan(url.getURL(),context);
                style.setSpan(myURLSpan, sp.getSpanStart(url),
                        sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
            msgTextView.setText(style);
        }
    }

    private static class MyURLSpan extends ClickableSpan {

        private String mUrl;
        private Context context;

        public MyURLSpan(String url,Context context) {

            mUrl =url;
            this.context=context;
        }

        public void onClick(View widget) {

            try {
                Intent phoneIntent=new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+mUrl));
                phoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(phoneIntent);
            } catch (Exception e) {
                Toast.makeText(context, "拨打电话不成功", Toast.LENGTH_LONG).show();
            }
        }
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
