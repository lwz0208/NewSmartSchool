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
package com.ding.chat.ui;

import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.ding.chat.R;
import com.ding.chat.adapter.NewFriendsMsgAdapter;
import com.ding.chat.db.InviteMessgeDao;
import com.ding.chat.domain.InviteMessage;

/**
 * 申请与通知
 */
public class NewFriendsMsgActivity extends BaseActivity {
    private ListView listView;
    private ImageView add_newfriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_new_friends_msg);
        add_newfriends = (ImageView) findViewById(R.id.add_newfriends);
        listView = (ListView) findViewById(R.id.list);
        add_newfriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewFriendsMsgActivity.this, AddContactActivity.class));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    NewFriendsMsgActivity.this.overridePendingTransition(
                            R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                }
            }
        });
        InviteMessgeDao dao = new InviteMessgeDao(this);
        List<InviteMessage> msgs = dao.getMessagesList();
        //倒叙一下
        Collections.reverse(msgs);
        // 设置adapter
        NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(this, 1, msgs);
        listView.setAdapter(adapter);
        dao.saveUnreadMessageCount(0);

    }

    public void back(View view) {
        finish();
        if (android.os.Build.VERSION.SDK_INT > 5) {
            overridePendingTransition(
                    R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
    }

}
