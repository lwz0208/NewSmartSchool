package com.wust.newsmartschool.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.GroupMemsEntity;
import com.wust.newsmartschool.views.ListViewForScrollView;
import com.wust.newsmartschool.adapter.ChooseMembers_Adapter;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.MediaType;

public class ChooseAtPersonActivity extends BaseActivity {
    String TAG = "ChooseAtPersonActivity_Debugs";
    ListViewForScrollView lv_groupmems;
    ChooseMembers_Adapter deptMembers_adapter;
    String groupid;
    private EditText query;
    private ImageButton clearSearch;
    List<GroupMemsEntity.DataBean.DataBean1.AffiliationsBean> groupMemsEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_at_person);
        lv_groupmems = (ListViewForScrollView) findViewById(R.id.lv_groupmems);
        groupid = getIntent().getStringExtra("groupid");
        query = (EditText) findViewById(R.id.query);
        query.addTextChangedListener(new watcher());
        clearSearch = (ImageButton) findViewById(R.id.search_clear);
        clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
            }
        });
        try {
            initData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        lv_groupmems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String realname = ((TextView) view.findViewById(com.wust.easeui.R.id.friend_name)).getText().toString();
                String userId;
                if (!((GroupMemsEntity.DataBean.DataBean1.AffiliationsBean) deptMembers_adapter.getItem(position)).getMember().toString().equals("")) {
                    userId = ((GroupMemsEntity.DataBean.DataBean1.AffiliationsBean) deptMembers_adapter.getItem(position)).getMember().toString();
                } else {
                    userId = ((GroupMemsEntity.DataBean.DataBean1.AffiliationsBean) deptMembers_adapter.getItem(position)).getOwner().toString();
                }
                if (!TextUtils.isEmpty(realname) && !TextUtils.isEmpty(userId)) {
                    setResult(
                            RESULT_OK,
                            new Intent()
                                    .putExtra("choosename", realname).putExtra("chooseuserId", userId));
                } else {
                    setResult(
                            RESULT_CANCELED,
                            null);
                }
                finish();
            }
        });
    }

    public void initData() throws JSONException {
        JSONObject loginJson = new JSONObject();
        loginJson.put("groupId", groupid);
        Log.i(TAG, loginJson.toString());
        CommonUtils.setCommonJson(ChooseAtPersonActivity.this, loginJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        OkHttpUtils.postString().url(Constant.GETGROUPMEMS_URL)
                .content(loginJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        Log.e(TAG, arg0);
                        try {
                            JSONObject temp = new JSONObject(arg0);
                            if (temp.getJSONObject("data").getJSONObject("data").getInt("affiliations_count") > 0) {
                                GroupMemsEntity grouporignal = new GroupMemsEntity();
                                grouporignal = new Gson().fromJson(arg0, GroupMemsEntity.class);
                                List<GroupMemsEntity.DataBean.DataBean1.AffiliationsBean> temp_list = new ArrayList<>();
                                temp_list.addAll(grouporignal.getData().getData().getAffiliations());
                                for (int i = 0; i < temp_list.size(); i++) {
                                    if (temp_list.get(i).getName().equals(PreferenceManager.getInstance().getCurrentRealName().toString())) {
                                        temp_list.remove(i);
                                    }
                                }
                                groupMemsEntity = temp_list;
                                deptMembers_adapter = new ChooseMembers_Adapter(ChooseAtPersonActivity.this, temp_list);
                                lv_groupmems.setAdapter(deptMembers_adapter);
                                deptMembers_adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            showToastShort("成员获取失败");
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                    }
                });

    }

    class watcher implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub
            Pattern p = Pattern.compile(s.toString());
            if (s.length() > 0) {
                clearSearch.setVisibility(View.VISIBLE);
            } else {
                clearSearch.setVisibility(View.INVISIBLE);
            }

            List<GroupMemsEntity.DataBean.DataBean1.AffiliationsBean> selectored = new ArrayList<>();
            for (int i = 0; i < groupMemsEntity.size(); i++) {
                GroupMemsEntity.DataBean.DataBean1.AffiliationsBean pp = groupMemsEntity.get(i);
                Matcher matcher = p.matcher(pp.getName().toString());
                if (matcher.find()) {
                    selectored.add(pp);
                }
            }
            deptMembers_adapter = new ChooseMembers_Adapter(ChooseAtPersonActivity.this, selectored);
            lv_groupmems.setAdapter(deptMembers_adapter);
        }

    }

    public void back(View view) {

        finish();
    }

}
