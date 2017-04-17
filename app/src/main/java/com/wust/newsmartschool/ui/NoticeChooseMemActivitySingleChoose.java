package com.wust.newsmartschool.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.ChooseMemExpandLVAdapterSingleChoose;
import com.wust.newsmartschool.domain.ALLdepartmentId1;
import com.wust.newsmartschool.domain.ALLjobClassifyId2;
import com.wust.newsmartschool.domain.ALLpartyBranchId3;
import com.wust.newsmartschool.domain.ALLpositionalTitleId4;
import com.wust.newsmartschool.domain.ChildrenItem;
import com.wust.newsmartschool.domain.Common_TypeMem;
import com.wust.newsmartschool.domain.GroupItem;
import com.wust.newsmartschool.utils.appUseUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;

public class NoticeChooseMemActivitySingleChoose extends BaseActivity {

    String TAG = "NoticeChooseMemActivitySingleChoose";
    private ExpandableListView expandableList;
    private ChooseMemExpandLVAdapterSingleChoose adapter;
    private List<GroupItem> dataList;
    private TextView allchoose;

    private ECProgressDialog progressDialog;
    //PopWindows绑定list
    private ListView title_list;
    private PopupWindow mPopWindow;
    private RelativeLayout relativeLayout1;
    //选择类别的按钮
    private LinearLayout ll_title_choosemem;
    //新建通知中的deptType参数所需要的东东
    String reCallType;
    boolean AllCheck = false;
    //四个类型对应实体
    ALLdepartmentId1 alldepartmentId1;
    ALLjobClassifyId2 alljobClassifyId2;
    ALLpartyBranchId3 allpartyBranchId3;
    ALLpositionalTitleId4 allpositionalTitleId4;
    //四个实体下面的子人员Item
    Common_TypeMem common_typeMem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_choosemem_new);
        expandableList = (ExpandableListView) findViewById(R.id.expandable_list_new);
        allchoose = (TextView) findViewById(R.id.allchoose_new);
        ll_title_choosemem = (LinearLayout) findViewById(R.id.ll_title_choosemem_new);
        dataList = new ArrayList<GroupItem>();
        common_typeMem = new Common_TypeMem();
        //4个类型实体的初始化
        alldepartmentId1 = new ALLdepartmentId1();
        alljobClassifyId2 = new ALLjobClassifyId2();
        allpartyBranchId3 = new ALLpartyBranchId3();
        allpositionalTitleId4 = new ALLpositionalTitleId4();
        progressDialog = new ECProgressDialog(NoticeChooseMemActivitySingleChoose.this);
        progressDialog.setPressText("拉取数据...");
        progressDialog.show();
        View contentView = LayoutInflater.from(NoticeChooseMemActivitySingleChoose.this).inflate(
                R.layout.title_popup, null);
        relativeLayout1 = (RelativeLayout) findViewById(R.id.rl_workflow_choose_id_new);
        mPopWindow = new PopupWindow(contentView);
        ll_title_choosemem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });
        title_list = (ListView) contentView.findViewById(R.id.title_list);
        allchoose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, AllCheck + "");
                if (!AllCheck) {
                    AllCheck = true;
                    adapter.AllCheck();
                } else {
                    AllCheck = false;
                    adapter.removeAllCheck();
                }
            }
        });

        adapter = new ChooseMemExpandLVAdapterSingleChoose(NoticeChooseMemActivitySingleChoose.this, dataList, expandableList);
        expandableList.setAdapter(adapter);
//判断缓存是否存在树形结构，并且显示之
        try {
            getAllMembersTypeDept();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        expandableList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                progressDialog.show();
                try {
                    getChildData(reCallType, dataList.get(groupPosition).getId(), groupPosition);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        //不知道为啥没有响应
        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                dataList.get(groupPosition).getChildrenItems().get(childPosition).getId();
                Log.e(TAG, "onChildClick: " + dataList.get(groupPosition).getChildrenItems().get(childPosition).getId());
                return true;
            }
        });

    }

    private void getChildData(String reCallType, String groupId, final int groupPosition) throws JSONException {
        JSONObject getChildDataJson = new JSONObject();
        getChildDataJson.put("typeId", reCallType);
        getChildDataJson.put("id", groupId);
        Log.i(TAG, getChildDataJson.toString());
        CommonUtils.setCommonJson(NoticeChooseMemActivitySingleChoose.this, getChildDataJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        OkHttpUtils.postString().url(Constant.GETMEMBYTYPE_URL)
                .content(getChildDataJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                             @Override
                             public void onResponse(String arg0) {
                                 Log.e(TAG, arg0);
                                 progressDialog.dismiss();
                                 common_typeMem = new Gson().fromJson(arg0,
                                         Common_TypeMem.class);
                                 if (common_typeMem.getData().size() != 0) {
                                     List<ChildrenItem> tempchildrenItems = new ArrayList<ChildrenItem>();
                                     for (int i = 0; i < common_typeMem.getData().size(); i++) {
                                         ChildrenItem tempchildrenItem = new ChildrenItem();
                                         tempchildrenItem.setId(common_typeMem.getData().get(i).getId() + "");
//                                         tempchildrenItem.setJobtitleName(common_typeMem.getData().get(i).getJobtitleName());
                                         tempchildrenItem.setName(common_typeMem.getData().get(i).getName());
//                                         tempchildrenItem.setRoleId(common_typeMem.getData().get(i).getRoleId());
//                                         tempchildrenItem.setDepartmentName(common_typeMem.getData().get(i).getDepartmentName());
                                         tempchildrenItems.add(tempchildrenItem);
                                     }
                                     dataList.get(groupPosition).setChildrenItems(tempchildrenItems);
                                     GroupItem groupItem = dataList.get(groupPosition);
                                     if (appUseUtils.checkedGroupList.contains(groupItem)) {
                                         appUseUtils.checkedChildrenList.addAll(tempchildrenItems);
                                     } else {
                                         appUseUtils.checkedChildrenList.clear();
                                     }
                                     adapter.notifyDataSetChanged();
                                 } else {
                                     showToastShort("暂无人员");
                                 }
                             }

                             @Override
                             public void onError(Call arg0, Exception arg1) {
                                 showToastShort("获取人员列表失败");
                                 progressDialog.dismiss();
                             }
                         }
                );
    }


    private void getAllMembersTypeDept() throws JSONException {
        JSONObject DeptArchJson = new JSONObject();
        CommonUtils.setCommonJson(NoticeChooseMemActivitySingleChoose.this, DeptArchJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.e(TAG, DeptArchJson.toString());
        OkHttpUtils.postString().url(Constant.GETALLTYPEDEPT_URL)
                .mediaType(MediaType.parse("application/json"))
                .content(DeptArchJson.toString()).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        progressDialog.dismiss();
                        Log.e(TAG, arg0.toString());
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            if (jObject.getInt("code") == 1) {
                                ((Button) findViewById(R.id.btn_yes_finish_new)).setVisibility(View.VISIBLE);
                                ((View) findViewById(R.id.btn_yes_finish_view_new)).setVisibility(View.VISIBLE);
                                alldepartmentId1 = new Gson().fromJson(jObject.getJSONArray("data").get(0).toString(), ALLdepartmentId1.class);
                                alljobClassifyId2 = new Gson().fromJson(jObject.getJSONArray("data").get(1).toString(), ALLjobClassifyId2.class);
                                allpartyBranchId3 = new Gson().fromJson(jObject.getJSONArray("data").get(2).toString(), ALLpartyBranchId3.class);
                                allpositionalTitleId4 = new Gson().fromJson(jObject.getJSONArray("data").get(3).toString(), ALLpositionalTitleId4.class);
                                checkExistmAche("departmentId");
                                reCallType = "departmentId";
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        progressDialog.dismiss();
                        showToastShort("网络错误");
                    }
                });
    }

    //处理四个父节点的函数  //先判断缓存，若没有则访问接口。此函数带数据校验以及访问网络加显示一体；
    private void checkExistmAche(String typeId) {
        dataList.clear();
        switch (typeId) {
            case "departmentId"://部门
                for (int i = 0; i < alldepartmentId1.getData().get(0).getChildren().size(); i++) {
                    GroupItem tempgroupItem = new GroupItem();
                    tempgroupItem.setName(alldepartmentId1.getData().get(0).getChildren().get(i).getName());
                    tempgroupItem.setId(String.valueOf(alldepartmentId1.getData().get(0).getChildren().get(i).getId()));
                    dataList.add(tempgroupItem);
                }
                break;
            case "jobClassifyId"://按职务
                for (int i = 0; i < alljobClassifyId2.getData().size(); i++) {
                    GroupItem tempgroupItem = new GroupItem();
                    tempgroupItem.setName(alljobClassifyId2.getData().get(i).getName());
                    tempgroupItem.setId(String.valueOf(alljobClassifyId2.getData().get(i).getId()));
                    dataList.add(tempgroupItem);
                }
                break;
            case "partyBranchId"://党支部3
                for (int i = 0; i < allpartyBranchId3.getData().size(); i++) {
                    GroupItem tempgroupItem = new GroupItem();
                    tempgroupItem.setName(allpartyBranchId3.getData().get(i).getName());
                    tempgroupItem.setId(String.valueOf(allpartyBranchId3.getData().get(i).getId()));
                    dataList.add(tempgroupItem);
                }
                break;
            case "positionalTitleId"://按职称
                for (int i = 0; i < allpositionalTitleId4.getData().size(); i++) {
                    GroupItem tempgroupItem = new GroupItem();
                    tempgroupItem.setName(allpositionalTitleId4.getData().get(i).getName());
                    tempgroupItem.setId(String.valueOf(allpositionalTitleId4.getData().get(i).getId()));
                    dataList.add(tempgroupItem);
                }
                break;
        }
        adapter.notifyDataSetChanged();

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
        title_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                              @Override
                                              public void onItemClick(AdapterView<?> parent, View view,
                                                                      int position, long id) {
                                                  // TODO Auto-generated method stub
                                                  mPopWindow.dismiss();
                                                  switch (position) {
                                                      case 0://按党支部分类
                                                          reCallType = "partyBranchId";
                                                          checkExistmAche("partyBranchId");
                                                          break;
                                                      case 1://按职务分类
                                                          reCallType = "jobClassifyId";
                                                          checkExistmAche("jobClassifyId");
                                                          break;
                                                      case 2://按职称分类
                                                          reCallType = "positionalTitleId";
                                                          checkExistmAche("positionalTitleId");
                                                          break;
                                                      case 3://按科室分类
                                                          reCallType = "departmentId";
                                                          checkExistmAche("departmentId");
                                                          break;
                                                  }


                                              }
                                          }

        );

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
        mPopWindow.showAtLocation(ll_title_choosemem, Gravity.CLIP_HORIZONTAL | Gravity.TOP,
                Dp2Px(this, 5f),
                frame.top + relativeLayout1.getHeight());
    }

    public void onOkClick(View view) {
        if (adapter.getCheckedChildren() != null) {
            String members_userid = "";
            String members_name = "";
            String group_userid = "";
            String group_name = "";

            for (int i = 0; i < adapter.getCheckedChildren().size(); i++) {
                members_userid = adapter.getCheckedChildren().get(i).getId() + "," + members_userid;
                members_name = adapter.getCheckedChildren().get(i).getName() + "," + members_name;
            }
            for (int i = 0; i < adapter.getCheckedGroup().size(); i++) {
                group_userid = adapter.getCheckedGroup().get(i).getId() + "," + group_userid;
                group_name = adapter.getCheckedGroup().get(i).getName() + "," + group_name;
            }
            setResult(
                    RESULT_OK,
                    new Intent()
                            .putExtra("members_userid",
                                    members_userid)
                            .putExtra("members_name",
                                    members_name).putExtra("group_userid", group_userid).putExtra("group_name", group_name).putExtra("deptType", reCallType));
        } else {
            setResult(
                    RESULT_CANCELED,
                    null);
        }
        appUseUtils.checkedChildrenList.clear();
        appUseUtils.checkedGroupList.clear();
        finish();

    }

    // 将Android的dp转换为屏幕的px
    public int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}
