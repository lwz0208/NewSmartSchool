package com.ding.chat.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ding.chat.DemoApplication;
import com.ding.chat.R;
import com.ding.chat.adapter.ChooseMemExpandLVAdapter;
import com.ding.chat.domain.ChildrenItem;
import com.ding.chat.domain.DeptArchTree;
import com.ding.chat.domain.DeptArchTree_UserInfo;
import com.ding.chat.domain.GroupItem;
import com.ding.chat.domain.Groupnameid;
import com.ding.chat.domain.PartyBranchIdEntity;
import com.ding.chat.views.ECProgressDialog;
import com.ding.easeui.Constant;
import com.ding.easeui.utils.CommonUtils;
import com.ding.easeui.utils.PreferenceManager;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.MediaType;

public class NoticeChooseMemActivity extends BaseActivity {

    String TAG = "NoticeChooseMemActivity3_Debugs";
    private ExpandableListView expandableList;
    //    private Button showBtn;
    private ChooseMemExpandLVAdapter adapter;
    private List<GroupItem> dataList;
    List<Groupnameid> departmentList;
    DeptArchTree archTree;
    private TextView allchoose;

    PartyBranchIdEntity partyBranchId;
    List<List<DeptArchTree_UserInfo>> memberList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice_choosemem_new);
        expandableList = (ExpandableListView) findViewById(R.id.expandable_list);
        allchoose = (TextView) findViewById(R.id.allchoose);
//        showBtn = (Button) findViewById(R.id.showBtn);
        ll_title_choosemem = (LinearLayout) findViewById(R.id.ll_title_choosemem);
        dataList = new ArrayList<GroupItem>();
        progressDialog = new ECProgressDialog(NoticeChooseMemActivity.this);
        progressDialog.setPressText("正在拉取全院数据");
        progressDialog.show();
        View contentView = LayoutInflater.from(NoticeChooseMemActivity.this).inflate(
                R.layout.title_popup, null);
        relativeLayout1 = (RelativeLayout) findViewById(R.id.rl_workflow_choose_id);
        mPopWindow = new PopupWindow(contentView);
        ll_title_choosemem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupWindow();
            }
        });
        title_list = (ListView) contentView.findViewById(R.id.title_list);
//        showBtn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showCheckedItems();
//            }
//        });
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

        archTree = new DeptArchTree();
        departmentList = new ArrayList<Groupnameid>();
        memberList = new ArrayList<List<DeptArchTree_UserInfo>>();
        adapter = new ChooseMemExpandLVAdapter(NoticeChooseMemActivity.this, dataList);
        expandableList.setAdapter(adapter);
//判断缓存是否存在树形结构，并且显示之
        checkExistmAche("departmentId");
        reCallType = "departmentId";

    }

    //先判断缓存，若没有则访问接口。此函数带数据校验以及访问网络加显示一体；
    private void checkExistmAche(String type) {
        switch (type) {
            case "departmentId"://部门
                archTree = (DeptArchTree) DemoApplication.getInstance().mCache.getAsObject(Constant.MY_KEY_ARCHTREE);
                if (archTree == null) {
                    try {
                        getDeptArch();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    progressDialog.dismiss();
                    dealDeptData();
                }
                break;
            case "partyBranchId"://党支部3
                partyBranchId = (PartyBranchIdEntity) DemoApplication.getInstance().mCache.getAsObject(Constant.MY_KEY_PARTY);
                if (partyBranchId == null) {
                    try {
                        changeData("partyBranchId");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    progressDialog.dismiss();
                    dealTypeData();
                }
                break;
            case "jobClassifyId"://按职务
                partyBranchId = (PartyBranchIdEntity) DemoApplication.getInstance().mCache.getAsObject(Constant.MY_KEY_JOB);
                if (partyBranchId == null) {
                    try {
                        changeData("jobClassifyId");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    progressDialog.dismiss();
                    dealTypeData();
                }
                break;
            case "positionalTitleId"://按职称
                partyBranchId = (PartyBranchIdEntity) DemoApplication.getInstance().mCache.getAsObject(Constant.MY_KEY_POSITION);
                if (partyBranchId == null) {
                    try {
                        changeData("positionalTitleId");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    progressDialog.dismiss();
                    dealTypeData();
                }
                break;
        }

    }

    private void getDeptArch() throws JSONException {
        JSONObject DeptArchJson = new JSONObject();
        CommonUtils.setCommonJson(NoticeChooseMemActivity.this, DeptArchJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.e("archTree", "getDeptArch调用了");
        Log.e("archTree", DeptArchJson.toString());
        OkHttpUtils.postString().url(Constant.GETDEPTARCH_URL)
                .mediaType(MediaType.parse("application/json"))
                .content(DeptArchJson.toString()).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        progressDialog.dismiss();
                        Log.e("archTree_arg0Before", arg0.toString());
                        arg0 = arg0.replaceAll("\"usersInfo\":\"\"", "\"usersInfo\":[]");
                        Log.e("archTree_arg0After", arg0.toString());
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            if (jObject.getInt("code") == 1) {
                                archTree = new Gson().fromJson(
                                        jObject.getString("data"),
                                        DeptArchTree.class);
                                //好不容易得到的，缓存一下;
                                DemoApplication.getInstance().mCache.put(Constant.MY_KEY_ARCHTREE, archTree);
                                dealDeptData();

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

    //改变数据源，按照党类别划分
    public void changeData(final String typeId) throws JSONException {
        JSONObject typeJson = new JSONObject();
        typeJson.put("typeId", typeId);
        CommonUtils.setCommonJson(NoticeChooseMemActivity.this, typeJson, PreferenceManager.getInstance().getCurrentUserFlowSId());
        Log.i("typeId", typeId.toString());
        OkHttpUtils.postString().url(Constant.GETMEMBYTYPEBYID_URL)
                .content(typeJson.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onResponse(String arg0) {
                        progressDialog.dismiss();
                        JSONObject jObject;
                        try {
                            jObject = new JSONObject(arg0);
                            if (jObject.getInt("code") == 1) {
                                partyBranchId = new Gson().fromJson(arg0, PartyBranchIdEntity.class);
                                DemoApplication.getInstance().mCache.put(typeId, partyBranchId);
                                dealTypeData();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        Log.e(TAG, arg0.toString());
                        progressDialog.dismiss();
                    }
                });
    }

    // 递归上面archTree属性结构的数据，使他变为两级菜单
    private void handlerOut(DeptArchTree archTree) {
        Groupnameid handerOut = new Groupnameid();
        handerOut.setName(archTree.getDeptName());
        handerOut.setId(String.valueOf(archTree.getDeptId()));
        departmentList.add(handerOut);
        memberList.add(archTree.getUsersInfo());
        handlerDeep(archTree.getChildren());
    }

    private void handlerDeep(List<DeptArchTree> childrenList) {
        for (int i = 0; i < childrenList.size(); i++) {
            Groupnameid handlerDeep = new Groupnameid();
            handlerDeep.setName(childrenList.get(i).getDeptName());
            handlerDeep.setId(String.valueOf(childrenList.get(i).getDeptId()));
            departmentList.add(handlerDeep);
            memberList.add(childrenList.get(i).getUsersInfo());
            handlerDeep(childrenList.get(i).getChildren());

        }


    }

    public void dealTypeData() {
        departmentList.clear();
        memberList.clear();
        dataList.clear();
        //先处理一下原始原始数据源partyBranchId
        for (int i = 0; i < partyBranchId.getData().get(0).getData().size(); i++) {
            List<DeptArchTree_UserInfo> temp_List = new ArrayList<>();
            Groupnameid tempnameid = new Groupnameid();
            tempnameid.setId(String.valueOf(partyBranchId.getData().get(0).getData().get(i).getId()));
            tempnameid.setName(partyBranchId.getData().get(0).getData().get(i).getName());
            departmentList.add(tempnameid);
            for (int j = 0; j < partyBranchId.getData().get(0).getData().get(i).getUsersInfo().size(); j++) {
                //将新数据源填充到老的类型的数据原来，避免了换Adapter
                DeptArchTree_UserInfo temp = new DeptArchTree_UserInfo();
                temp.setUserRealname(partyBranchId.getData().get(0).getData().get(i).getUsersInfo().get(j).getUserRealname());
                temp.setUserId(partyBranchId.getData().get(0).getData().get(i).getUsersInfo().get(j).getUserId());
                temp.setJobtitleName(partyBranchId.getData().get(0).getData().get(i).getUsersInfo().get(j).getJobtitleName());
                Log.e(TAG + "_temp", temp.getUserRealname());
                temp_List.add(j, temp);
                Log.e(TAG + "_temp_List", temp_List.size() + "");
                Log.e(TAG + "_temp_List", temp_List.get(j).getUserRealname());
            }
            memberList.add(i, temp_List);
            Log.e(TAG + "size()", memberList.get(i).size() + "");
        }
//再处理一下上面处理的departmentList
        for (int i = 0; i < departmentList.size(); i++) {
            List<ChildrenItem> tempChildList = new ArrayList<ChildrenItem>();
            GroupItem tempParent = new GroupItem();
            tempParent.setId(departmentList.get(i).getId());
            tempParent.setName(departmentList.get(i).getName());
            for (int j = 0; j < memberList.get(i).size(); j++) {
                //将新数据源填充到老的类型的数据原来，避免了换Adapter
                ChildrenItem tempChild = new ChildrenItem();
                tempChild.setName(memberList.get(i).get(j).getUserRealname());
                tempChild.setId(memberList.get(i).get(j).getUserId() + "");
                tempChild.setJobtitleName(partyBranchId.getData().get(0).getData().get(i).getUsersInfo().get(j).getJobtitleName());
                tempChildList.add(tempChild);
            }
            tempParent.setChildrenItems(tempChildList);
            dataList.add(tempParent);
        }
        adapter = new ChooseMemExpandLVAdapter(NoticeChooseMemActivity.this, dataList);
        expandableList.setAdapter(adapter);
    }

    public void dealDeptData() {
        //得到树形结构的实体数据，执行对他的数据处理操作;
        departmentList.clear();
        memberList.clear();
        dataList.clear();
        handlerOut(archTree);
        for (int i = 0; i < departmentList.size(); i++) {
            List<ChildrenItem> tempChildList = new ArrayList<ChildrenItem>();
            GroupItem tempParent = new GroupItem();
            tempParent.setId(departmentList.get(i).getId());
            tempParent.setName(departmentList.get(i).getName());
            for (int j = 0; j < memberList.get(i).size(); j++) {
                //将新数据源填充到老的类型的数据原来，避免了换Adapter
                ChildrenItem tempChild = new ChildrenItem();
                tempChild.setName(memberList.get(i).get(j).getUserRealname());
                tempChild.setId(memberList.get(i).get(j).getUserId() + "");
                tempChild.setJobtitleName(memberList.get(i).get(j).getJobtitleName() + "");
                tempChildList.add(tempChild);
            }
            tempParent.setChildrenItems(tempChildList);
            dataList.add(tempParent);
        }
        adapter = new ChooseMemExpandLVAdapter(NoticeChooseMemActivity.this, dataList);
        expandableList.setAdapter(adapter);
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
//                                                          progressDialog.show();
                                                          checkExistmAche("partyBranchId");
//                                                          try {
//                                                              changeData("partyBranchId");
//                                                          } catch (JSONException e) {
//                                                              e.printStackTrace();
//                                                          }
                                                          break;
                                                      case 1://按职务分类
                                                          reCallType = "jobClassifyId";
                                                          checkExistmAche("jobClassifyId");
//                                                          progressDialog.show();
//                                                          try {
//                                                              changeData("jobClassifyId");
//                                                          } catch (JSONException e) {
//                                                              e.printStackTrace();
//                                                          }
                                                          break;
                                                      case 2://按职称分类
                                                          reCallType = "positionalTitleId";
//                                                          progressDialog.show();
                                                          checkExistmAche("positionalTitleId");
//                                                          try {
//                                                              changeData("positionalTitleId");
//                                                          } catch (JSONException e) {
//                                                              e.printStackTrace();
//                                                          }
                                                          break;
                                                      case 3://按科室分类
                                                          reCallType = "departmentId";
//                                                          progressDialog.show();
                                                          checkExistmAche("departmentId");
//                                                          try {
//                                                              getDeptArch();
//                                                          } catch (JSONException e) {
//                                                              e.printStackTrace();
//                                                          }
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

        finish();

    }

    // 将Android的dp转换为屏幕的px
    public int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}
