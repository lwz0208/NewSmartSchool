package com.wust.newsmartschool.ui;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.NoticeAttach_Adapter;
import com.wust.newsmartschool.domain.NoticeDetailEntity;
import com.wust.newsmartschool.domain.UpdataonResponse;
import com.wust.newsmartschool.domain.myTaskMajor;
import com.wust.newsmartschool.domain.myTaskType;
import com.wust.newsmartschool.views.ECProgressDialog;
import com.wust.newsmartschool.views.ListViewForScrollView;
import com.wust.easeui.Constant;
import com.wust.easeui.utils.CommonUtils;
import com.wust.easeui.utils.PreferenceManager;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Request;

public class NewTaskActivity extends BaseActivity {
    String TAG = "NewTaskActivity_Debugs";
    private String CREATTASK_URL = Constant.CREATTASK_URL;
    private final static String URL_TASK_TYPE = Constant.GETTASKTYPE_URL;

    private RelativeLayout rl_deadline;
    private RelativeLayout rl_taskType;
    private RelativeLayout rl_taskMember;
    private RelativeLayout rl_taskChargePeople;
    private RelativeLayout rl_taskMajor;
    private TextView tv_deadline;
    private TextView tv_taskType;
    private TextView tv_finish;
    private TextView iv_taskMember;
    private TextView tv_taskChargePeople;
    //上传附件的控件
    private ImageView task_updatafile;
    private ListViewForScrollView newtask_attachlist;
    //新建任务选择的附件
    File upnoticefile;
    UpdataonResponse updataonResponse;
    List<File> Files;
    List<NoticeDetailEntity.FileListBean> AttachListEntitytoadapter;
    NoticeAttach_Adapter newnotice_attachadapter;

    //新建任务参数里的fileids
    org.json.JSONArray noticeAttachList;

    private EditText et_taskTitle;
    private EditText et_taskContent;
    private EditText et_taskDescribe;

    private List<myTaskType> taskList;
    private List<myTaskMajor> taskmajorList;
    private int selectTaskTypeID;
    private int selectTaskMajorID;
    private String[] items = null;
    String membersuserid;
    String newmembers;
    //上面两个逗号隔开的数据化为数组
    String memberNameArray[];
    String memberIdArray[];
    int headUserId;
    //父id（如果有）
    int fatherid;
    ECProgressDialog pd;
    //来源标识以及数据
    String from = "";
    JSONObject json;
    //任务id，如果是从修改的地方过来的才有
    int id = -1;
    //上传附件的pd
    //上传附件的bar
    ProgressDialog mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        taskList = new ArrayList<>();
        taskmajorList = new ArrayList<>();
        Files = new ArrayList<>();
        noticeAttachList = new org.json.JSONArray();
        AttachListEntitytoadapter = new ArrayList<>();
        //初始化上传附件的那个bar
        mProgressBar = new ProgressDialog(NewTaskActivity.this);
        mProgressBar.setMessage("正在上传附件文件...");
        mProgressBar.setCancelable(true);
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressBar.setProgress(0);
        mProgressBar.setMax(100);
        task_updatafile = (ImageView) findViewById(R.id.task_updatafile);
        newtask_attachlist = (ListViewForScrollView) findViewById(R.id.newtask_attachlist);
        rl_deadline = (RelativeLayout) findViewById(R.id.rl_deadline);
        rl_taskType = (RelativeLayout) findViewById(R.id.rl_taskType);
        rl_taskChargePeople = (RelativeLayout) findViewById(R.id.rl_taskChargePeople);
        rl_taskMember = (RelativeLayout) findViewById(R.id.rl_taskMember);
        rl_taskMajor = (RelativeLayout) findViewById(R.id.rl_taskMajor);
        tv_deadline = (TextView) findViewById(R.id.tv_deadline);
        tv_taskType = (TextView) findViewById(R.id.tv_taskType);
        tv_finish = (TextView) findViewById(R.id.tv_finish);
        iv_taskMember = (TextView) findViewById(R.id.iv_taskMember);
        tv_taskChargePeople = (TextView) findViewById(R.id.tv_taskChargePeople);
        et_taskTitle = (EditText) findViewById(R.id.et_taskTitle);
        et_taskContent = (EditText) findViewById(R.id.et_taskContent);
        et_taskDescribe = (EditText) findViewById(R.id.et_taskDescribe);
        from = getIntent().getStringExtra("from");
        pd = new ECProgressDialog(NewTaskActivity.this);
        //上传附件
        newnotice_attachadapter = new NoticeAttach_Adapter(NewTaskActivity.this, AttachListEntitytoadapter);
        newtask_attachlist.setAdapter(newnotice_attachadapter);
        initTaskType();
        initTaskMajor();
        if (from != null) {
            if (from.equals("TaskDetailActivity_NewChildTask")) {
                CREATTASK_URL = Constant.CREATTASK_URL;
                fatherid = getIntent().getIntExtra("fathertask", -1);
            } else if (from.equals("TaskDetailActivity_ChangeTask")) {
                CREATTASK_URL = Constant.CHANGETASKDETAIL_URL;
                String data = getIntent().getStringExtra("data");
                json = JSON.parseObject(data);

                tv_taskType.setText(json.getJSONObject("data").getString("type"));
                if (json.getJSONObject("data").getString("type").equals("普通任务")) {
                    selectTaskTypeID = 1;
                } else if (json.getJSONObject("data").getString("type").equals("院办会议任务")) {
                    selectTaskTypeID = 2;
                }
                id = json.getJSONObject("data").getInteger("id");
                ((TextView) findViewById(R.id.tv_taskMajor)).setText(json.getJSONObject("data").getString("priorityName"));
                selectTaskMajorID = json.getJSONObject("data").getInteger("priorityId");
                et_taskTitle.setText(json.getJSONObject("data").getString("title"));
                et_taskContent.setText(json.getJSONObject("data").getString("content"));
                et_taskDescribe.setText(json.getJSONObject("data").getString("descri"));
                tv_deadline.setText(json.getJSONObject("data").getString("deadline"));
                try {
                    tv_taskChargePeople.setText(json.getJSONObject("data").getString("headUserName"));
                    headUserId = json.getJSONObject("data").getInteger("headUserId");
                } catch (Exception e) {
                    tv_taskChargePeople.setText(PreferenceManager.getInstance().getCurrentRealName());
                    headUserId = Integer.valueOf(PreferenceManager.getInstance().getCurrentUserId());
                    e.printStackTrace();
                }
                //处理一下人员信息并且显示和同步
                String frommembersname = "";
                String frommembersuserid = "";
                for (int i = 0; i < json.getJSONArray("member").size(); i++) {
                    frommembersname = ((JSONObject) (json.getJSONArray("member").get(i))).getString("userName") + "," + frommembersname;
                    frommembersuserid = ((JSONObject) (json.getJSONArray("member").get(i))).getInteger("userId") + "," + frommembersuserid;
                }
                iv_taskMember.setText(frommembersname);
                memberNameArray = frommembersname.split(",");
                memberIdArray = frommembersuserid.split(",");
            }
        }
        newtask_attachlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File beclickfile = Files.get(position);
                CommonUtils.openFileUnknowType(NewTaskActivity.this, beclickfile);
            }
        });
        task_updatafile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),
                            Constant.FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(NewTaskActivity.this, "请安装文件管理器", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        rl_deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(NewTaskActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // TODO Auto-generated method stub
                                tv_deadline.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, calendar.get(Calendar.YEAR), calendar
                        .get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        rl_taskMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult((new Intent(NewTaskActivity.this,
                                NoticeChooseMemActivitySingleChooseForTask.class)),
                        Constant.CHOOSE_NOTICEPEOPLE);
            }
        });

        rl_taskType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        NewTaskActivity.this);
                // 指定下拉列表的显示数据
                if (taskList.size() == 0) {
                    showToastShort("获取任务类型失败");
                } else {
                    if (items == null) {
                        items = new String[taskList.size()];
                        for (int i = 0; i < taskList.size(); i++) {
                            items[i] = taskList.get(i).getTaskName();
                        }
                    }
                    builder.setTitle("请选择任务类型");
                    // 设置一个下拉的列表选择项
                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tv_taskType.setText(items[which]);
                            selectTaskTypeID = which + 1;
                        }
                    });
                    builder.show();
                }
            }
        });

        rl_taskMajor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskmajorList.size() == 0) {
                    showToastShort("获取任务优先级失败");
                } else {
                    final String MajorArray[] = new String[taskmajorList.size()];
                    for (int i = 0; i < taskmajorList.size(); i++) {
                        MajorArray[i] = taskmajorList.get(i).getName();
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewTaskActivity.this);
                    builder.setTitle("请选择任务优先级");
                    builder.setItems(MajorArray, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ((TextView) findViewById(R.id.tv_taskMajor)).setText(MajorArray[which]);
                            selectTaskMajorID = taskmajorList.get(which).getId();
                        }
                    });
                    builder.show();
                }
            }
        });

        tv_finish.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             if (isCanCommit()) {
                                                 pd.show();
                                                 org.json.JSONObject jObject = new org.json.JSONObject();
                                                 org.json.JSONArray jsonArray = new org.json.JSONArray();
                                                 int membersInt[] = new int[memberIdArray.length];
                                                 for (int i = 0; i < memberIdArray.length; i++) {
                                                     membersInt[i] = Integer.parseInt(memberIdArray[memberIdArray.length - i - 1]);
                                                     try {
                                                         jsonArray.put(i, membersInt[i]);
                                                     } catch (JSONException e) {
                                                         e.printStackTrace();
                                                     }
                                                 }
                                                 try {
                                                     jObject.put("title", et_taskTitle.getText().toString());
                                                     jObject.put("content", et_taskContent.getText().toString());
                                                     jObject.put("typeId", selectTaskTypeID);
                                                     jObject.put("deadline", tv_deadline.getText().toString());
                                                     jObject.put("descri", et_taskDescribe.getText().toString());
                                                     jObject.put("headUserId", headUserId);
                                                     jObject.put("source", PreferenceManager.getInstance().getCurrentRealName());
                                                     jObject.put("member", jsonArray);
                                                     jObject.put("priorityId", selectTaskMajorID);
                                                     jObject.put("fileIds", noticeAttachList);
                                                     if (from != null) {
                                                         if (from.equals("TaskDetailActivity_NewChildTask")) {
                                                             jObject.put("createUserId", Integer.parseInt(PreferenceManager.getInstance().getCurrentUserId()));
                                                             //如果不为-1，则有父任务，必须带上
                                                             if (fatherid != -1) {
                                                                 jObject.put("pTaskId", fatherid);
                                                             }
                                                         } else if (from.equals("TaskDetailActivity_ChangeTask")) {
                                                             //如果有id则加上id
                                                             if (id != -1) {
                                                                 jObject.put("id", id);
                                                             }
                                                             jObject.put("toUsers", jsonArray);
                                                         }
                                                     } else {
                                                         jObject.put("createUserId", Integer.parseInt(PreferenceManager.getInstance().getCurrentUserId()));
                                                     }
                                                     CommonUtils.setCommonJson(NewTaskActivity.this, jObject, PreferenceManager.getInstance().getCurrentUserFlowSId());
                                                 } catch (JSONException e) {
                                                     // TODO Auto-generated catch block
                                                     e.printStackTrace();
                                                 }
                                                 Log.i("task", jObject.toString());
                                                 OkHttpUtils.postString().url(CREATTASK_URL).content(jObject.toString())
                                                         .mediaType(MediaType.parse("application/json")).build()
                                                         .execute(new StringCallback() {
                                                                      @Override
                                                                      public void onResponse(String arg0) {
                                                                          Log.e(TAG, arg0);
                                                                          pd.dismiss();
                                                                          // 这里就解析返回的arg0，注意是String类型的需要类型转换。
                                                                          JSONObject json = JSON.parseObject(arg0);
                                                                          if (json.getIntValue("code") == 1) {
                                                                              showToastShort(json.getString("msg"));
                                                                              //如果这个补位-1，表示是从任务详情的新建子任务传过来的，需要回调一下刷新。
                                                                              if (fatherid != -1 || from.equals("TaskDetailActivity_ChangeTask")) {
                                                                                  setResult(
                                                                                          RESULT_OK,
                                                                                          new Intent()
                                                                                                  .putExtra("needfreshchildtask",
                                                                                                          1));
                                                                              }

                                                                              finish();
                                                                          } else if (json.getIntValue("code") == 5) {
                                                                              Toast.makeText(getApplicationContext(), R.string.common_no_power, Toast.LENGTH_SHORT).show();
                                                                          } else {
                                                                              showToastShort(json.getString("msg"));
                                                                          }
                                                                      }

                                                                      @Override
                                                                      public void onError(Call arg0, Exception arg1) {
                                                                          pd.dismiss();
                                                                          showToastShort("请求失败");
                                                                          Log.i("task", "新建任务接口失败访问失败返回结果：" + arg0.toString() + "////////////////" + arg1.toString());
                                                                      }
                                                                  }

                                                         );

                                             }
                                         }
                                     }

        );

        rl_taskChargePeople.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if (!tv_taskChargePeople.getText().equals("")) {
                                                           AlertDialog.Builder builder = new AlertDialog.Builder(NewTaskActivity.this);
                                                           builder.setTitle("选择负责人");
                                                           builder.setSingleChoiceItems(memberNameArray, 0, new DialogInterface.OnClickListener() {
                                                               @Override
                                                               public void onClick(DialogInterface dialog, int which) {
                                                                   tv_taskChargePeople.setText(memberNameArray[which]);
                                                                   headUserId = Integer.valueOf(memberIdArray[which]);
                                                                   dialog.cancel();
                                                               }
                                                           });

                                                           builder.show();
                                                       } else {
                                                           showToastShort("请先选择任务成员");
                                                       }
                                                   }
                                               }

        );

    }


    public void back(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void initTaskType() {
        org.json.JSONObject jObject = new org.json.JSONObject();
        try {
            CommonUtils.setCommonJson(NewTaskActivity.this, jObject, PreferenceManager.getInstance().getCurrentUserFlowSId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils.postString().url(URL_TASK_TYPE).content(jObject.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        Log.i("task", "获取任务类型接口失败返回结果：" + arg0.toString() + " :    " + arg1.toString());
                    }

                    @Override
                    public void onResponse(String arg0) {
                        Log.i("task", "获取任务类型接口成功返回结果：" + arg0);
                        JSONObject json = JSON.parseObject(arg0);
                        if (json.getIntValue("code") == 1) {
                            if (!json.getString("msg").equals("无数据")) {
                                taskList = new ArrayList<myTaskType>();
                                JSONArray array = json.getJSONArray("data");
                                for (int i = 0; i < array.size(); i++) {
                                    myTaskType task = new myTaskType(
                                            array.getJSONObject(i).getIntValue(
                                                    "id"), array.getJSONObject(
                                            i).getString("name"));
                                    taskList.add(task);
                                }
                            }
                        }
                    }
                });
    }

    private void initTaskMajor() {
        org.json.JSONObject jObject = new org.json.JSONObject();
        try {
            CommonUtils.setCommonJson(NewTaskActivity.this, jObject, PreferenceManager.getInstance().getCurrentUserFlowSId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils.postString().url(Constant.GETTASKMAJOR_URL).content(jObject.toString())
                .mediaType(MediaType.parse("application/json")).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call arg0, Exception arg1) {
                        Log.i("task", "获取任务类型接口失败返回结果：" + arg0.toString() + " :    " + arg1.toString());
                    }

                    @Override
                    public void onResponse(String arg0) {
                        JSONObject json = JSON.parseObject(arg0);
                        Log.e(TAG, arg0);
                        if (json.getIntValue("code") == 1) {
                            JSONArray TaskMajordata = json.getJSONArray("data");
                            for (int i = 0; i < TaskMajordata.size(); i++) {
                                myTaskMajor temp = new myTaskMajor();
                                temp.setName(TaskMajordata.getJSONObject(i).getString("name"));
                                temp.setId(TaskMajordata.getJSONObject(i).getInteger("id"));
                                taskmajorList.add(temp);
                            }
                        }
                    }
                });
    }

    public void UpdataAttach(File upmeetingfile) throws JSONException {
        OkHttpUtils.post().url(Constant.UPDATAATTACH_URL).addParams("userId", PreferenceManager.getInstance().getCurrentUserId().toString()).addFile("file1", upmeetingfile.getName(), upmeetingfile).build()
                .connTimeOut(20000)
                .readTimeOut(20000)
                .writeTimeOut(20000).execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Log.i("UpdataAttach", call.toString() + "/*/" + e.toString());
                showToastShort("上传附件失败");
                pd.dismiss();
            }

            @Override
            public void onResponse(String s) {
                pd.dismiss();
                Log.i("UpdataAttach", s);
                try {
                    org.json.JSONObject response = new org.json.JSONObject(s);
                    if (response.getInt("code") == 1) {
                        showToastShort("上传附件成功");
                        updataonResponse = new Gson().fromJson(s, UpdataonResponse.class);
                        String temp = updataonResponse.getData().getFilesList().get(0).getFileName();
                        Log.e(TAG, temp);
                        //存在接口里需要的noticeAttachList
                        noticeAttachList.put(Integer.valueOf(updataonResponse.getData().getFilesIds().replaceAll(",", "")));
                        NoticeDetailEntity.FileListBean tempattach = new NoticeDetailEntity.FileListBean();
                        tempattach.setFileName(temp);
                        AttachListEntitytoadapter.add(tempattach);
                        newnotice_attachadapter.notifyDataSetChanged();
                        //上传成功并且listview更新之后再加到这个List里面去，保证了上传了的一定在这个List里面
                        Files.add(upnoticefile);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return;
                } catch (NullPointerException e) {
                    showToastShort("请求失败");
                    e.printStackTrace();
                    return;
                } catch (Exception e) {
                    showToastShort("请求失败");
                    e.printStackTrace();
                }

            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                pd.dismiss();
            }

            @Override
            public void inProgress(float progress) {
                super.inProgress(progress);
                Log.e(TAG, progress + "");
                mProgressBar.show();
                mProgressBar.setProgress((int) (progress * 100));
            }

            @Override
            public void onAfter() {
                super.onAfter();
                mProgressBar.dismiss();

            }
        });

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.FILE_SELECT_CODE:
                    Uri uri = data.getData();
                    try {
                        upnoticefile = new File(CommonUtils.FileUtilsgetPathUp4p4(NewTaskActivity.this, uri));
                        UpdataAttach(upnoticefile);
                        pd.show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        showToastShort("该文件不存在");
                    }
                    break;
                case Constant.CHOOSE_NOTICEPEOPLE:// 添加群成员
                    String newmembers = data.getStringExtra("members_name");
                    // 有的接口要电话，有的接口要id，所以都返回回来了。全局变量的自取所需。
                    membersuserid = data.getStringExtra("members_userid");
                    if (!membersuserid.equals("")) {
                        iv_taskMember.setText(newmembers);
                        memberNameArray = newmembers.split(",");
                        memberIdArray = membersuserid.split(",");
                        tv_taskChargePeople.setText(memberNameArray[0]);
                        headUserId = Integer.valueOf(memberIdArray[0]);
                        Log.i(TAG, newmembers + "-----" + membersuserid);
                    }
                    break;
            }
        }
    }

    private boolean isCanCommit() {
        if (et_taskTitle.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "请填写任务标题", Toast.LENGTH_SHORT).show();
            return false;
        } else if (et_taskContent.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "请填写任务内容", Toast.LENGTH_SHORT).show();
            return false;
        } else if (et_taskDescribe.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "请填写任务描述", Toast.LENGTH_SHORT).show();
            return false;
        } else if (iv_taskMember.getText().equals("选择成员")) {
            Toast.makeText(getApplicationContext(), "请选择任务成员", Toast.LENGTH_SHORT).show();
            return false;
        } else if (tv_deadline.getText().equals("请选择日期")) {
            Toast.makeText(getApplicationContext(), "请选择任务日期", Toast.LENGTH_SHORT).show();
            return false;
        } else if (tv_taskType.getText().equals("请选择类型")) {
            Toast.makeText(getApplicationContext(), "请选择任务类型", Toast.LENGTH_SHORT).show();
            return false;
        } else if (((TextView) findViewById(R.id.tv_taskMajor)).getText().equals("请选择优先级")) {
            Toast.makeText(getApplicationContext(), "请选择任务优先级", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
