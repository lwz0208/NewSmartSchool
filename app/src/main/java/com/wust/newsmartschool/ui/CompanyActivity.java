package com.wust.newsmartschool.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.UplistviewGCD_Adapter;
import com.wust.newsmartschool.adapter.Uplistview_Adapter;
import com.wust.newsmartschool.domain.CompanyEntity;
import com.wust.newsmartschool.domain.Company_Dep;
import com.wust.newsmartschool.domain.Type_GCD;
import com.wust.easeui.utils.CommonUtils;
import com.wust.easeui.utils.PreferenceManager;
import com.wust.newsmartschool.views.ListViewForScrollView;
import com.wust.easeui.Constant;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.MediaType;

public class CompanyActivity extends BaseActivity {
    String TAG = "CompanyActivity_Debugs";
    private TextView company_title;
    private ListView title_list;
    private RelativeLayout relativeLayout1;
    private ListViewForScrollView up_listview;
    private Uplistview_Adapter uplistview_adapter;
    private UplistviewGCD_Adapter uplistviewgcd_adapter;
    private PopupWindow mPopWindow;
    //点击标题的标题
    private LinearLayout ll_title;
    CompanyEntity companyentity;
    Type_GCD type_gcd;
    // 搜索的框框的控件
    private EditText query;
    private ImageButton clearSearch;
    List<Company_Dep> data;
    String typeName = "departmentId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company);
        Intent intent = getIntent();
        companyentity = (CompanyEntity) intent
                .getSerializableExtra("companyentity");
        ll_title = (LinearLayout) findViewById(R.id.ll_title);
        type_gcd = new Type_GCD();
        View contentView = LayoutInflater.from(CompanyActivity.this).inflate(
                R.layout.title_popup, null);
        mPopWindow = new PopupWindow(contentView);
        relativeLayout1 = (RelativeLayout) findViewById(R.id.relativeLayout1);
        data = new ArrayList<Company_Dep>();
        data.addAll(companyentity.getData());
        //需求原因，对数据排序一下。总部放最前面
        Company_Dep zongbuDept = new Company_Dep(); //保存总部的实体
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getDepName().equals("总部")) {
                zongbuDept = data.get(i);
                data.remove(i);
                break;
            }
        }
        try {
            if (!zongbuDept.getDepName().equals("")) {
                data.add(0, zongbuDept);
            }
        } catch (Exception e) {
            //就不做任何操作
        }
        // 搜索框
        query = (EditText) findViewById(R.id.query);
        query.addTextChangedListener(new watcher());
        // 搜索框中清除button
        clearSearch = (ImageButton) findViewById(R.id.search_clear);
        title_list = (ListView) contentView.findViewById(R.id.title_list);
        company_title = (TextView) findViewById(R.id.company_title);
        company_title.setText(companyentity.getCompanyName());
        up_listview = (ListViewForScrollView) findViewById(R.id.up_listview);
        uplistview_adapter = new Uplistview_Adapter(this, data);
        up_listview.setAdapter(uplistview_adapter);
        up_listview.setOnItemClickListener(listener);
        clearSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
            }
        });

        ll_title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });
    }

    private void showPopupWindow() {
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        Map<String, Object> value = new HashMap<String, Object>();
//        value.put("icon", R.drawable.partybranchid);
        value.put("text", "按党支部分类");
        data.add(value);
        value = new HashMap<String, Object>();
//        value.put("icon", R.drawable.jobtitleid);
        value.put("text", "按职务分类");
        data.add(value);
        value = new HashMap<String, Object>();
//        value.put("icon", R.drawable.positionaltitleid);
        value.put("text", "按职称分类");
        data.add(value);
        value = new HashMap<String, Object>();
//        value.put("icon", R.drawable.departmentid);
        value.put("text", "按科室分类");
        data.add(value);

        SimpleAdapter adapter = new SimpleAdapter(this, data,
                R.layout.title_popup_item, new String[]{"icon", "text"},
                new int[]{R.id.iv_icon, R.id.tv_text});
        title_list.setAdapter(adapter);
        title_list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                mPopWindow.dismiss();
                switch (position) {
                    case 0://按党支部分类
                        company_title.setText("党支部");
                        try {
                            changeDataByGCD();
                            typeName = "partyBranchId";
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 1://按职务分类
                        company_title.setText("职务");
                        try {
                            changeDataByPosition();
                            typeName = "jobTitleId";
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2://按职称分类
                        company_title.setText("职称");
                        try {
                            changeDataByJob();
                            typeName = "positionalTitleId";
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 3://按科室分类
                        company_title.setText("科室");
                        up_listview.setAdapter(uplistview_adapter);
                        typeName = "departmentId";
                        break;
                }
            }
        });

        // 获得listview的宽度，使popwindow设置为自适应的宽度
        title_list.measure(View.MeasureSpec.UNSPECIFIED,
                View.MeasureSpec.UNSPECIFIED);
        mPopWindow.setWidth(title_list.getMeasuredWidth());
        mPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.setFocusable(true);
        // 获取状态栏高度
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        mPopWindow.showAtLocation(ll_title, Gravity.CLIP_HORIZONTAL | Gravity.TOP,
                Dp2Px(this, 5f),
                frame.top + relativeLayout1.getHeight());
    }

    //改变数据源，按照职称类别划分
    public void changeDataByJob() throws JSONException {
        JSONObject typeJson = new JSONObject();
        CommonUtils.setCommonJson(CompanyActivity.this, typeJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.i("userId", typeJson.toString());

        OkHttpUtils.postString().url(Constant.GETTYPEBYJOB_URL)
                .content(typeJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String arg0) {
                        Log.i(TAG, arg0.toString());
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            if (jObject.getInt("code") == 1) {
                                type_gcd = new Gson().fromJson(arg0, Type_GCD.class);
                                Log.i(TAG, type_gcd.getData().get(0).getName());
                                uplistviewgcd_adapter = new UplistviewGCD_Adapter(CompanyActivity.this, type_gcd);
                                up_listview.setAdapter(uplistviewgcd_adapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            showToastShort("请求失败");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        Log.i(TAG, arg0.toString());
                    }
                });
    }

    //改变数据源，按照职务类别划分
    public void changeDataByPosition() throws JSONException {
        JSONObject typeJson = new JSONObject();
        CommonUtils.setCommonJson(CompanyActivity.this, typeJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.i("userId", typeJson.toString());

        OkHttpUtils.postString().url(Constant.GETTYPEBYPOSITION_URL)
                .content(typeJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {

                    @Override
                    public void onResponse(String arg0) {
                        Log.i(TAG, arg0.toString());
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            if (jObject.getInt("code") == 1) {
                                type_gcd = new Gson().fromJson(arg0, Type_GCD.class);
                                Log.i(TAG, type_gcd.getData().get(0).getName());
                                uplistviewgcd_adapter = new UplistviewGCD_Adapter(CompanyActivity.this, type_gcd);
                                up_listview.setAdapter(uplistviewgcd_adapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            showToastShort("请求失败");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        Log.i(TAG, arg0.toString());
                    }
                });
    }

    //改变数据源，按照党类别划分
    public void changeDataByGCD() throws JSONException {
        JSONObject typeJson = new JSONObject();
        CommonUtils.setCommonJson(CompanyActivity.this, typeJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.i("userId", typeJson.toString());

        OkHttpUtils.postString().url(Constant.GETTYPEBYGCD_URL)
                .content(typeJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        Log.i(TAG, arg0.toString());
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            if (jObject.getInt("code") == 1) {
                                type_gcd = new Gson().fromJson(arg0, Type_GCD.class);
                                Log.i(TAG, type_gcd.getData().get(0).getName());
                                uplistviewgcd_adapter = new UplistviewGCD_Adapter(CompanyActivity.this, type_gcd);
                                up_listview.setAdapter(uplistviewgcd_adapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            showToastShort("请求失败");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        Log.i(TAG, arg0.toString());
                    }
                });
    }

    // 将Android的dp转换为屏幕的px
    public int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    class watcher implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
            // TODO Auto-generated method stub

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
            switch (typeName) {
                case "departmentId"://按科室
                    List<Company_Dep> we = new ArrayList<Company_Dep>();
                    for (int i = 0; i < data.size(); i++) {
                        Company_Dep pp = data.get(i);
                        Matcher matcher = p.matcher(pp.getDepName().toString());
                        if (matcher.find()) {
                            we.add(pp);
                        }
                    }
                    uplistview_adapter = new Uplistview_Adapter(CompanyActivity.this,
                            we);
                    up_listview.setAdapter(uplistview_adapter);
                    break;
                case "partyBranchId"://按照党
//                    type_gcd
                    Type_GCD type_temp_party = new Type_GCD();
                    List<Type_GCD.DataBean> temp_party = new ArrayList<>();
                    for (int i = 0; i < type_gcd.getData().size(); i++) {
                        Type_GCD.DataBean pp = type_gcd.getData().get(i);
                        Matcher matcher = p.matcher(pp.getName().toString());
                        if (matcher.find()) {
                            temp_party.add(pp);
                        }
                    }
                    type_temp_party.setCode(1);
                    type_temp_party.setMsg("请求成功");
                    type_temp_party.setData(temp_party);
                    uplistviewgcd_adapter = new UplistviewGCD_Adapter(CompanyActivity.this, type_temp_party);
                    up_listview.setAdapter(uplistviewgcd_adapter);

                    break;
                case "jobTitleId"://按照职务
                    Type_GCD type_temp_job = new Type_GCD();
                    List<Type_GCD.DataBean> temp_job = new ArrayList<>();
                    for (int i = 0; i < type_gcd.getData().size(); i++) {
                        Type_GCD.DataBean pp = type_gcd.getData().get(i);
                        Matcher matcher = p.matcher(pp.getName().toString());
                        if (matcher.find()) {
                            temp_job.add(pp);
                        }
                    }
                    type_temp_job.setCode(1);
                    type_temp_job.setMsg("请求成功");
                    type_temp_job.setData(temp_job);
                    uplistviewgcd_adapter = new UplistviewGCD_Adapter(CompanyActivity.this, type_temp_job);
                    up_listview.setAdapter(uplistviewgcd_adapter);

                    break;
                case "positionalTitleId"://按职称
                    Type_GCD type_temp_posi = new Type_GCD();
                    List<Type_GCD.DataBean> temp_posi = new ArrayList<>();
                    for (int i = 0; i < type_gcd.getData().size(); i++) {
                        Type_GCD.DataBean pp = type_gcd.getData().get(i);
                        Matcher matcher = p.matcher(pp.getName().toString());
                        if (matcher.find()) {
                            temp_posi.add(pp);
                        }
                    }
                    type_temp_posi.setCode(1);
                    type_temp_posi.setMsg("请求成功");
                    type_temp_posi.setData(temp_posi);
                    uplistviewgcd_adapter = new UplistviewGCD_Adapter(CompanyActivity.this, type_temp_posi);
                    up_listview.setAdapter(uplistviewgcd_adapter);

                    break;
            }
        }

    }

    OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            switch (typeName) {
                case "departmentId"://部门
                    Company_Dep company_Dep = (Company_Dep) uplistview_adapter.getItem(position);
                    startActivity(new Intent(getApplicationContext(),
                            DeptMembersActivity.class).putExtra("company_Dep",
                            company_Dep).putExtra("type", typeName));
                    if (android.os.Build.VERSION.SDK_INT > 5) {
                        overridePendingTransition(R.anim.slide_in_from_right,
                                R.anim.slide_out_to_left);
                    }
                    break;
                case "partyBranchId"://党支部
                    String title_party = ((Type_GCD.DataBean) uplistviewgcd_adapter.getItem(position)).getName();
                    startActivity(new Intent(getApplicationContext(),
                            DeptMembersActivity.class).putExtra("type_gcd",
                            type_gcd.getData().get(position).getId()).putExtra("type", typeName).putExtra("title", title_party));
                    if (android.os.Build.VERSION.SDK_INT > 5) {
                        overridePendingTransition(R.anim.slide_in_from_right,
                                R.anim.slide_out_to_left);
                    }
                    break;
                case "jobTitleId"://按职务
                    String title_job = ((Type_GCD.DataBean) uplistviewgcd_adapter.getItem(position)).getName();
                    startActivity(new Intent(getApplicationContext(),
                            DeptMembersActivity.class).putExtra("type_gcd",
                            type_gcd.getData().get(position).getId()).putExtra("type", typeName).putExtra("title", title_job));
                    if (android.os.Build.VERSION.SDK_INT > 5) {
                        overridePendingTransition(R.anim.slide_in_from_right,
                                R.anim.slide_out_to_left);
                    }
                    break;
                case "positionalTitleId"://按职称
                    String title_posit = ((Type_GCD.DataBean) uplistviewgcd_adapter.getItem(position)).getName();
                    startActivity(new Intent(getApplicationContext(),
                            DeptMembersActivity.class).putExtra("type_gcd",
                            type_gcd.getData().get(position).getId()).putExtra("type", typeName).putExtra("title", title_posit));
                    if (android.os.Build.VERSION.SDK_INT > 5) {
                        overridePendingTransition(R.anim.slide_in_from_right,
                                R.anim.slide_out_to_left);
                    }
                    break;
            }
        }
    };

//    //每次返回或者启动该页面的时候，搜索框置空
//    @Override
//    protected void onResume() {
//        super.onResume();
//        query.setText("");
//    }

    public void back(View view) {
        finish();
        if (android.os.Build.VERSION.SDK_INT > 5) {
            overridePendingTransition(
                    R.anim.slide_in_from_left, R.anim.slide_out_to_right);
        }
    }

}
