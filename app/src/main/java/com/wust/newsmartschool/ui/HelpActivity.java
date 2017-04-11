package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.wust.newsmartschool.R;

public class HelpActivity extends Activity {
	private ImageView backImageView;
	private TextView content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_help);

		/*backImageView=(ImageView)findViewById(R.id.goback);
		backImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				if (android.os.Build.VERSION.SDK_INT > 5) {
					overridePendingTransition(
							R.anim.slide_in_from_left, R.anim.slide_out_to_right);
				}
			}
		});*/
		
		content=(TextView)findViewById(R.id.content);
		content.setTextSize(17);
		content.setText("手机客户端登录方式：\n        手机客户端用户名和密码与学校电脑版信息门户网站（http://portal.wust.edu.cn/）用户名和密码相同，学生登录帐号为学号，教工登录帐号为工号，初始密码为8位个人出生日期，如用户已修改过学校电脑版信息门户网站登录密码，则以新密码登录手机客户端。为了用户信息安全，建议用户先登录电脑版信息门户网站修改初始密码。\n        如有账号登录问题，请联系现代教育信息中心:support@wust.edu.cn。");

	}
	public void back(View view) {
		finish();
		if (android.os.Build.VERSION.SDK_INT > 5) {
			overridePendingTransition(
					R.anim.slide_in_from_left, R.anim.slide_out_to_right);
		}
	}



}
