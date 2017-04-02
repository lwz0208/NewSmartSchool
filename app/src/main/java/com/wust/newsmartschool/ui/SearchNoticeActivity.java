package com.wust.newsmartschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.NoticeListAdapter;
import com.wust.newsmartschool.domain.NoticeEntity;
import com.wust.newsmartschool.domain.Notice_OutEntity;
import com.wust.newsmartschool.views.ECProgressDialog;
import com.wust.easeui.Constant;
import com.wust.easeui.utils.CommonUtils;
import com.wust.easeui.utils.PreferenceManager;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android.widget.TextView.OnEditorActionListener;

import okhttp3.Call;
import okhttp3.MediaType;

public class SearchNoticeActivity extends BaseActivity implements OnEditorActionListener {

    String TAG = "SearchNoticeActivity_Debugs";
    private ListView lv_searchnotice;
    private TextView tv_tips_noticesearch;
    private NoticeListAdapter searchnotice_Adapter;
    List<NoticeEntity> searchnoticeList;
    private EditText query_notice;
    private ImageButton clearSearch;
    private ECProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_notice);
        lv_searchnotice = (ListView) findViewById(R.id.lv_searchnotice);
        searchnoticeList = new ArrayList<>();
        clearSearch = (ImageButton) findViewById(R.id.search_clear_notice);
        tv_tips_noticesearch = (TextView) findViewById(R.id.tv_tips_noticesearch);
        pd = new ECProgressDialog(SearchNoticeActivity.this);
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query_notice.getText().clear();
            }
        });
        pd.setPressText("正在搜索...");
        query_notice = (EditText) findViewById(R.id.query_notice);
        query_notice.addTextChangedListener(new watcher());
        query_notice.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        query_notice.setOnEditorActionListener(this);
        searchnotice_Adapter = new NoticeListAdapter(this, searchnoticeList);
        lv_searchnotice.setAdapter(searchnotice_Adapter);
        lv_searchnotice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoticeEntity clickNoticeEntity = new NoticeEntity();
                clickNoticeEntity = (NoticeEntity) searchnotice_Adapter
                        .getItem(position);
                startActivity(new Intent(SearchNoticeActivity.this,
                        NoticeDetailActivity.class).putExtra(
                        "KEY_CLICK_NOTICE", clickNoticeEntity));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    overridePendingTransition(
                            R.anim.slide_in_from_right,
                            R.anim.slide_out_to_left);
                }
            }
        });

    }
    // 模拟本地数据，真实数据的时候就要删掉哦

    private void initLocalData() throws JSONException {
        JSONObject noticeJson = new JSONObject();
        noticeJson.put("page", "1");
        noticeJson.put("pageSize", "100");
        noticeJson.put("searchString", query_notice.getText().toString());
        noticeJson.put("accept", PreferenceManager.getInstance()
                .getCurrentUserId());
        CommonUtils.setCommonJson(SearchNoticeActivity.this, noticeJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.i(TAG, noticeJson.toString());

        OkHttpUtils.postString().url(Constant.GETNOTICELIST_URL)
                .content(noticeJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        Log.e(TAG, arg0);
                        tv_tips_noticesearch.setVisibility(View.GONE);
                        lv_searchnotice.setVisibility(View.VISIBLE);
                        pd.dismiss();
                        try {
                            JSONObject temp = new JSONObject(arg0);
                            if (temp.getInt("code") == 1) {
                                Notice_OutEntity templist = new Notice_OutEntity();
                                templist = new Gson().fromJson(arg0,
                                        Notice_OutEntity.class);
                                Log.e(TAG, templist.getData().get(0).getUserRealname());
                                searchnoticeList.clear();
                                searchnoticeList.addAll(templist.getData());
                                searchnotice_Adapter.notifyDataSetChanged();
                            } else if (temp.getInt("code") == 2) {
                                showToastShort("暂无数据");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        showToastShort("搜索失败");
                        pd.dismiss();
                    }
                });

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_SEARCH:
                pd.show();
                try {
                    initLocalData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return true;
    }


    class watcher implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            if (s.length() > 0) {
                clearSearch.setVisibility(View.VISIBLE);
            } else {
                clearSearch.setVisibility(View.INVISIBLE);
            }

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
