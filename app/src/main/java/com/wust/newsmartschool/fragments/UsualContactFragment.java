package com.wust.newsmartschool.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wust.easeui.Constant;
import com.wust.easeui.utils.PreferenceManager;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.TypeMembers_Adapter;
import com.wust.newsmartschool.domain.Common_TypeMem;
import com.wust.newsmartschool.domain.CompanyEntity;
import com.wust.newsmartschool.ui.AddContactActivity;
import com.wust.newsmartschool.ui.CompanyActivity;
import com.wust.newsmartschool.ui.DeptMemInfoActivity;
import com.wust.newsmartschool.ui.FriendsListActivity;
import com.wust.newsmartschool.ui.GroupsActivity;
import com.wust.newsmartschool.ui.NewFriendsMsgActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;

@SuppressWarnings("deprecation")
public class UsualContactFragment extends Fragment implements OnClickListener {
    String TAG = "UsualContactFragment_Debugs";
    private LinearLayout ll_phonelist;
    private LinearLayout ll_friends;
    private LinearLayout ll_mygroup;
    private LinearLayout ll_company;
    private LinearLayout ll_my_deptname;
    private TextView company;
    private TextView tip_my_deptname;
    private ImageView add_newfriends;
    //电视下面本科室人员的一些参数
    private ListView listview_mydeptmembers;
    List<Common_TypeMem.DataBean> data;
    private TypeMembers_Adapter typememb_adapter;

    // 部门实体类列表需要传到companyactivity
    CompanyEntity companyentity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_usualcontact, container,
                false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        companyentity = new CompanyEntity();
        data = new ArrayList<>();
        add_newfriends = (ImageView) getView()
                .findViewById(R.id.add_newfriends);
        listview_mydeptmembers = (ListView) getView()
                .findViewById(R.id.listview_mydeptmembers);
        LayoutInflater infla = getActivity().getLayoutInflater();
        View headerView = infla.inflate(R.layout.lvheader, null);
        listview_mydeptmembers.addHeaderView(headerView);
        ll_phonelist = (LinearLayout) headerView.findViewById(R.id.ll_phonelist);
        ll_friends = (LinearLayout) headerView.findViewById(R.id.ll_friends);
        ll_mygroup = (LinearLayout) headerView.findViewById(R.id.ll_mygroup);
        ll_company = (LinearLayout) headerView.findViewById(R.id.ll_company);
        ll_my_deptname = (LinearLayout) headerView.findViewById(R.id.ll_my_deptname);

        company = (TextView) headerView.findViewById(R.id.company);
        tip_my_deptname = (TextView) headerView.findViewById(R.id.tip_my_deptname);
        try {
            tip_my_deptname.setText(PreferenceManager.getInstance().getCurrentUserClassName());
        } catch (Exception e) {
            ll_my_deptname.setVisibility(View.GONE);
            e.printStackTrace();
        }
        typememb_adapter = new TypeMembers_Adapter(getActivity(), data);
        listview_mydeptmembers.setAdapter(typememb_adapter);

        ll_phonelist.setOnClickListener(this);
        add_newfriends.setOnClickListener(this);
        ll_friends.setOnClickListener(this);
        ll_mygroup.setOnClickListener(this);
        ll_company.setOnClickListener(this);

        try {
            //获得本科室成员的接口
            geMyClassMembers();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        listview_mydeptmembers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String userId;
                    userId = String
                            .valueOf(((Common_TypeMem.DataBean) typememb_adapter.getItem(position - 1)).getId());
                    Log.e(TAG, userId);
                    startActivity(new Intent(getActivity(),
                            DeptMemInfoActivity.class).putExtra("userId",
                            userId));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_phonelist:
                // 进入申请与通知页面
                startActivity(new Intent(getActivity(), NewFriendsMsgActivity.class));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    getActivity().overridePendingTransition(
                            R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                }
                break;
            case R.id.ll_friends:
                startActivity(new Intent(getContext(), FriendsListActivity.class));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    getActivity().overridePendingTransition(
                            R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                }
                break;
            case R.id.ll_mygroup:
                startActivity(new Intent(getContext(), GroupsActivity.class));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    getActivity().overridePendingTransition(
                            R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                }
                break;
            case R.id.ll_company:
                startActivity(new Intent(getContext(), CompanyActivity.class));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    getActivity().overridePendingTransition(
                            R.anim.slide_in_from_right,
                            R.anim.slide_out_to_left);
                }
                break;
            case R.id.add_newfriends:
                startActivity(new Intent(getActivity(), AddContactActivity.class));
                if (android.os.Build.VERSION.SDK_INT > 5) {
                    getActivity().overridePendingTransition(
                            R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                }
                break;
            default:
                break;
        }
    }


    private void geMyClassMembers() throws JSONException {
        JSONObject myclass = new JSONObject();
        myclass.put("id", PreferenceManager.getInstance().getCurrentUserClassId());
        Log.i(TAG, myclass.toString());
        OkHttpUtils.postString().url(Constant.GETMYCLASSTYPE_URL)
                .content(myclass.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        Log.e(TAG, arg0);
                        try {
                            JSONObject json = new JSONObject(arg0);
                            if (json.getInt("code") == 1) {
                                Common_TypeMem common_typeMem = new Gson().fromJson(arg0,
                                        Common_TypeMem.class);
                                if (common_typeMem.getData().size() != 0) {
                                    ll_my_deptname.setVisibility(View.VISIBLE);
                                    data.addAll(common_typeMem.getData());
                                    typememb_adapter.notifyDataSetChanged();
                                } else {
                                    ll_my_deptname.setVisibility(View.GONE);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        Log.e(TAG, arg0.toString() + "/*/" + arg1.toString());
                    }
                });

    }


}
