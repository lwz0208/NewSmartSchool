/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wust.newsmartschool.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wust.newsmartschool.DemoHelper;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.AddFriends_Adapter;
import com.wust.newsmartschool.domain.SearchFriendsEntity;
import com.wust.newsmartschool.views.ECProgressDialog;
import com.wust.easeui.Constant;
import com.wust.easeui.utils.CommonUtils;
import com.wust.easeui.utils.PreferenceManager;
import com.wust.easeui.widget.EaseAlertDialog;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;

public class AddContactActivity extends BaseActivity {
    String TAG = "AddContactActivity_Debugs";
    private EditText editText;
    private LinearLayout searchedUserLayout;
    private TextView nameText, mTextView;
    private Button searchBtn;
    private ImageView avatar;
    private InputMethodManager inputMethodManager;
    private String toAddUsername;
    private ProgressDialog progressDialog;
    private ListView lv_friends_add;
    private AddFriends_Adapter search_adapter;
    List<SearchFriendsEntity.DataBean> SearchPersons;
    ECProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_add_contact);
        mTextView = (TextView) findViewById(R.id.add_list_friends);
        editText = (EditText) findViewById(R.id.edit_note);
        lv_friends_add = (ListView) findViewById(R.id.lv_friends_add);
        pd = new ECProgressDialog(AddContactActivity.this);
        SearchPersons = new ArrayList<>();
        String strAdd = getResources().getString(R.string.add_friend);
        mTextView.setText(strAdd);
        String strUserName = getResources().getString(R.string.user_name);
        editText.setHint("请输入姓名");
        searchedUserLayout = (LinearLayout) findViewById(R.id.ll_user);
        nameText = (TextView) findViewById(R.id.name);
        searchBtn = (Button) findViewById(R.id.search);
        avatar = (ImageView) findViewById(R.id.avatar);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        lv_friends_add.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String userId = String
                        .valueOf(((SearchFriendsEntity.DataBean) search_adapter
                                .getItem(position)).getId());
                startActivity(new Intent(getApplicationContext(),
                        DeptMemInfoActivity.class).putExtra("userId",
                        userId));
            }
        });
        search_adapter = new AddFriends_Adapter(AddContactActivity.this,
                SearchPersons);
        lv_friends_add.setAdapter(search_adapter);
    }

    /**
     * 查找contact
     *
     * @param v
     */
    public void searchContact(View v) throws JSONException {
        String name = editText.getText().toString();
        String saveText = searchBtn.getText().toString();
        toAddUsername = name;
        if (TextUtils.isEmpty(name)) {
            new EaseAlertDialog(this, "请输入查找姓名")
                    .show();
            return;
        }
        pd.setPressText("正在搜索中...");
        pd.show();
        JSONObject search = new JSONObject();
        search.put("searchString", name);
        CommonUtils.setCommonJson(AddContactActivity.this, search, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.e(TAG, search.toString());
        OkHttpUtils.postString().url(Constant.ADDFRIENDS_URL)
                .content(search.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {
                        pd.dismiss();
                        showToastShort("搜索失败");
                        Log.e(TAG, call.toString() + e.toString());
                    }

                    @Override
                    public void onResponse(String s) {
                        pd.dismiss();
                        Log.e(TAG, s.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("code") == 1) {
                                SearchFriendsEntity searchFriendsEntity = new Gson().fromJson(s, SearchFriendsEntity.class);
                                SearchPersons.clear();
                                if (searchFriendsEntity.getData().size() != 0) {
                                    SearchPersons.addAll(searchFriendsEntity.getData());
                                    search_adapter.notifyDataSetChanged();
                                } else {
                                    showToastShort("查无此人，请检查输入");
                                }

                            }
                        } catch (JSONException e) {
                            showToastShort("搜索失败");
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            showToastShort("搜索失败");
                            e.printStackTrace();
                        }


                    }
                });

        // 服务器存在此用户，显示此用户和添加按钮
//            searchedUserLayout.setVisibility(View.VISIBLE);
//            nameText.setText(toAddUsername);

    }

    /**
     * 添加contact
     *
     * @param view
     */
    public void addContact(View view) {
        if (EMClient.getInstance().getCurrentUser()
                .equals(nameText.getText().toString())) {
            new EaseAlertDialog(this, R.string.not_add_myself).show();
            return;
        }

        if (DemoHelper.getInstance().getContactList()
                .containsKey(nameText.getText().toString())) {
            // 提示已在好友列表中(在黑名单列表里)，无需添加
            if (EMClient.getInstance().contactManager().getBlackListUsernames()
                    .contains(nameText.getText().toString())) {
                new EaseAlertDialog(this, R.string.user_already_in_contactlist)
                        .show();
                return;
            }
            new EaseAlertDialog(this, R.string.This_user_is_already_your_friend)
                    .show();
            return;
        }

        progressDialog = new ProgressDialog(this);
        String stri = getResources().getString(R.string.Is_sending_a_request);
        progressDialog.setMessage(stri);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        new Thread(new Runnable() {
            public void run() {

                try {
                    // demo写死了个reason，实际应该让用户手动填入
                    String s = getResources().getString(R.string.Add_a_friend);
                    EMClient.getInstance().contactManager()
                            .addContact(toAddUsername, s);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s1 = getResources().getString(
                                    R.string.send_successful);
                            Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s2 = getResources().getString(
                                    R.string.Request_add_buddy_failure);
                            Toast.makeText(getApplicationContext(),
                                    s2 + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    public void back(View v) {
        finish();
        if (android.os.Build.VERSION.SDK_INT > 5) {
            overridePendingTransition(
                    R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
    }
}
