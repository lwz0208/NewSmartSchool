package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wust.newsmartschool.R;

public class FeedbackActivity extends Activity implements OnClickListener {
	private TextView fbTipTextView;
	private EditText cardNumEditText;
	private EditText stuNameEditText;
	private EditText phoneEditText;
	private EditText fbEditText;
	private Button fbCommitButton;
	
	private static final int COMMIT_SUCCESS = 1;
	private static final int COMMIT_FAILED = 0;
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case COMMIT_FAILED:
				Toast.makeText(FeedbackActivity.this, "提交失败！请检查网络", Toast.LENGTH_LONG).show();
				break;

			case COMMIT_SUCCESS:
				Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_LONG).show();
				FeedbackActivity.this.finish();
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);

	initView();


	}

	private void initView() {
		/*((ImageView) findViewById(R.id.goback)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FeedbackActivity.this.finish();
				if (android.os.Build.VERSION.SDK_INT > 5) {
					overridePendingTransition(
							R.anim.slide_in_from_left, R.anim.slide_out_to_right);
				}
			}
		});*/

		fbTipTextView = (TextView) findViewById(R.id.tv_feedback_tip);
		String messageText = fbTipTextView.getText().toString();
		int start = messageText.indexOf("*");
		int end = start + "*".length();
		SpannableStringBuilder builder = new SpannableStringBuilder(messageText);
		ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.RED);
		builder.setSpan(blueSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		fbTipTextView.setText(builder);
		
		cardNumEditText = (EditText) findViewById(R.id.tv_card_number);
		stuNameEditText = (EditText) findViewById(R.id.tv_name);
		phoneEditText = (EditText) findViewById(R.id.tv_phone_number);
		fbEditText = (EditText) findViewById(R.id.tv_feedback);
		fbCommitButton = (Button) findViewById(R.id.btn_feedback_commit);
		
		/*	cardNumEditText.setText(GlobalVar.userid);
		if (!TextUtils.isEmpty(GlobalVar.username))
			stuNameEditText.setText(GlobalVar.username);
		*/
		fbCommitButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		final String userid = cardNumEditText.getText().toString();
		final String username = stuNameEditText.getText().toString();
		final String method = phoneEditText.getText().toString();
		final String suggestion = fbEditText.getText().toString();
		
		if (TextUtils.isEmpty(suggestion)) {
			Toast.makeText(FeedbackActivity.this, "反馈意见不能为空！", Toast.LENGTH_SHORT).show();
			return ;
		}
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				/*try {
					String urlString = "http://202.114.242.198:8090/insert_suggestion.php?userid="
							+ userid + "&username="+ URLEncoder.encode(username, "UTF-8").toString() + "&suggestion=" + URLEncoder.encode(suggestion, "UTF-8").toString() + "&method=" + method;
					String result = new HttpServer().getData(urlString);
					if (TextUtils.isEmpty(result)) {
						handler.sendEmptyMessage(FeedbackActivity.COMMIT_FAILED);
						return ;
					}
					if (result.equals("1"))
						handler.sendEmptyMessage(FeedbackActivity.COMMIT_SUCCESS);
				} catch (Exception e) {
					handler.sendEmptyMessage(FeedbackActivity.COMMIT_FAILED);
				}*/
			}
		}).start();
	}
	public void back(View view) {
		finish();
		if (android.os.Build.VERSION.SDK_INT > 5) {
			overridePendingTransition(
					R.anim.slide_in_from_left, R.anim.slide_out_to_right);
		}
	}
}
