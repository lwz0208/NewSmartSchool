package com.wust.newsmartschool.ui;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.fragments.ContactListFragment;

import android.os.Bundle;

public class FriendsListActivity extends BaseActivity {
    private ContactListFragment contactListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        contactListFragment = new ContactListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.friends_fragment, contactListFragment)
                .show(contactListFragment).commit();

    }
}
