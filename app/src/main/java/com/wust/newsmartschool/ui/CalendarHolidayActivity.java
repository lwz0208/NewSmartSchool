package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wust.newsmartschool.R;

/**
 * 节日放假调休安排Activity
 * @author Yorek Liu
 */
public class CalendarHolidayActivity extends Activity
{
    private ImageView goback;
    private TextView headTitle;
    private TextView tv_information;
    private TextView tv_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_calendar_holiday);

        goback = (ImageView) findViewById(R.id.goback);
        tv_information = (TextView) findViewById(R.id.tv_introduction);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        headTitle = (TextView) findViewById(R.id.head_title);

        tv_phone.setText(R.string.calendar_exchange_class_phone_number);
        setchaining();

        goback.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        switch (getIntent().getIntExtra("holiday", 0))
        {
            case 5:
                headTitle.setText("清明节放假调休安排");
                tv_information.setText("4月5日放假，4月6日（星期一）停课、补休。");
                break;
            case 6:
                headTitle.setText("清明节放假调休安排");
                tv_information.setText("4月5日放假，4月6日（星期一）停课、补休。");
                break;
            case 10:
                headTitle.setText("清明节放假调休安排");
                tv_information.setText("4月5日放假，4月6日（星期一）停课、补休。");
                break;

            case 13:
                headTitle.setText("劳动节放假调休安排");
                tv_information.setText("5月1日放假，与周末连休；5月1日停课。");
                break;
            case 14:
                headTitle.setText("劳动节放假调休安排");
                tv_information.setText("5月1日放假，与周末连休；5月1日停课。");
                break;
            case 9:
                headTitle.setText("劳动节放假调休安排");
                tv_information.setText("5月1日放假，与周末连休；5月1日停课。");
                break;

            case 16:
                headTitle.setText("端午节放假调休安排");
                tv_information.setText("6月20日放假，6月22日（星期一）停课、补休。");
                break;
            case 17:
                headTitle.setText("端午节放假调休安排");
                tv_information.setText("6月20日放假，6月22日（星期一）停课、补休。");
                break;
            case 21:
                headTitle.setText("端午节放假调休安排");
                tv_information.setText("6月20日放假，6月22日（星期一）停课、补休。");
                break;

            case 23:
                headTitle.setText("暑假放假安排");
                tv_information.setText("从7月4日放到8月29日。");
                break;

            default:
                break;
        }
    }

    private void setchaining(){
        tv_phone.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text=tv_phone.getText();
        if(text instanceof Spannable){
            int end =text.length();
            Spannable sp=(Spannable) tv_phone.getText();
            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
            SpannableStringBuilder style = new SpannableStringBuilder(text);
            style.clearSpans();
            for (URLSpan url : urls) {

                MyURLSpan myURLSpan = new MyURLSpan(url.getURL(), this);
                style.setSpan(myURLSpan, sp.getSpanStart(url),
                        sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            }
            tv_phone.setText(style);
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
}

