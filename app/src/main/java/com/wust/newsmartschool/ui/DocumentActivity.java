package com.wust.newsmartschool.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.DocumentsListAdapter;
import com.wust.newsmartschool.domain.DocumentsList;
import com.wust.newsmartschool.domain.RMessage;
import com.wust.newsmartschool.utils.DocumentServer;
import com.wust.newsmartschool.utils.GlobalVar;
import com.wust.newsmartschool.utils.HttpServer;
import com.wust.newsmartschool.utils.MyListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 人大客户端通知公告模块
 *
 * @author GuangT
 *
 */
// 校园公告
public class DocumentActivity extends Activity implements MyListView.IXListViewListener {
    /*private SlideHolder mSlideHolder; // 侧边栏
    private LinearLayout userlogout; // 注销
    private LinearLayout userlogon; // 登入*/
    private ArrayList<RMessage> messagelist;
    private MyListView listView;
    private DocumentsListAdapter adapter;
    private ProgressBar progressBar;
    private List<DocumentsList> documentsLists = new ArrayList<DocumentsList>();
    private int pagenum = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                adapter = new DocumentsListAdapter(getApplicationContext(),
                        documentsLists);
                listView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }
            if (msg.what == 100) {
                messagelist = (ArrayList<RMessage>) (msg.obj);

                pagenum = 2;
            } else if (msg.what == 102) { // 加载更多的存在
                ArrayList<RMessage> messagelist3 = (ArrayList<RMessage>) (msg.obj);
                if (messagelist3.size() == 0) {
                    Toast.makeText(getApplicationContext(), "已是最后一条",
                            Toast.LENGTH_SHORT).show();
                } else {
                    messagelist.addAll(messagelist3);
                    adapter.notifyDataSetChanged();
                    pagenum++;
                }
            } else if (msg.what == 101) {
                Toast.makeText(getApplicationContext(), "网速不给力",
                        Toast.LENGTH_SHORT).show();
            } else if (msg.what == 108) {
                Toast.makeText(getApplicationContext(), "签到成功",
                        Toast.LENGTH_SHORT).show();
            }

            listView.stopRefresh();
            listView.stopLoadMore();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_document);
        initView();
        getNotice(pagenum);

        ImageView backImageView = (ImageView) findViewById(R.id.back);
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        listView = (MyListView) findViewById(R.id.documentList);
        listView.setPullLoadEnable(true); // 使能下拉加载跟多
        listView.setXListViewListener(this);
/*		mSlideHolder = (SlideHolder) findViewById(R.id.slideHolder);
		userlogout = (LinearLayout) findViewById(R.id.userlogout);
		userlogon = (LinearLayout) findViewById(R.id.userlogon);
		ImageView userImageView = (ImageView) findViewById(R.id.user);

		mSlideHolder.setEnabled(false);
		userImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (GlobalVar.userid == null || GlobalVar.userid.equals("")) {
					Intent intent = new Intent(DocumentActivity.this,
							LoginActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(DocumentActivity.this,
							UserCenterActivity.class);
					startActivity(intent);
				}
			}
		});
		ImageView moreImageView = (ImageView) findViewById(R.id.more);
		moreImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSlideHolder.toggle();
			}
		});*/
        // initnav();
        // initSlibeNav();
        loadCache();
        getMessageList(1);
    }
    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
    }
    /**
     *
     * 获取公告
     */
    private void getNotice(final int pageNum){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    String url = "http://202.114.255.75:88/SchoolData/GetSDocumentsList?pagenum="
                            + pageNum;
                    documentsLists = DocumentServer
                            .parseNewsJSON(documentsLists,
                                    url,
                                    DocumentActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(1);
            }
        };
        new Thread(runnable).start();

    }
    private void loadCache() {
        SharedPreferences sharedPreferences = getSharedPreferences("messages",
                Context.MODE_APPEND);
        String jsonString = sharedPreferences.getString(
                "E91AD70B-B38B-4C19-AAA3-513B93EBF26B", "");
        if (!(jsonString.equals(""))) { // 有缓存
            HttpServer httpServer = new HttpServer();
            ArrayList<RMessage> messages2 = httpServer
                    .getRMessagelist(jsonString);
            handler.sendMessage(handler.obtainMessage(100, messages2));
        }
    }

    private void getMessageList(final int pagenum) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                HttpServer httpServer = new HttpServer();
                String urlString = GlobalVar.serverClient
                        + "getmyList?folderId=E91AD70B-B38B-4C19-AAA3-513B93EBF26B&page_num="
                        + pagenum + "&companyId=" + GlobalVar.companyid;
                String jsonString = httpServer.getData(urlString);
                if (jsonString != null) {
                    ArrayList<RMessage> messages2 = httpServer
                            .getRMessagelist(jsonString);

                    if (messages2 != null) {
                        if (pagenum == 1) { // 第一页的数据记得加上缓存的
                            SharedPreferences sharedPreferences = getSharedPreferences(
                                    "messages", Context.MODE_APPEND);
                            String cacheString = sharedPreferences.getString(
                                    "E91AD70B-B38B-4C19-AAA3-513B93EBF26B", "");
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            if (!(cacheString.equals(""))) {// 有缓存的
                                editor.remove("E91AD70B-B38B-4C19-AAA3-513B93EBF26B");
                            }
                            editor.putString(
                                    "E91AD70B-B38B-4C19-AAA3-513B93EBF26B",
                                    jsonString);
                            editor.commit();
                            handler.sendMessage(handler.obtainMessage(100,
                                    messages2));
                        } else { // 加载更多的存在
                            handler.sendMessage(handler.obtainMessage(102,
                                    messages2));
                        }

                    }

                } else {
                    handler.sendMessage(handler.obtainMessage(101));
                }

            }
        };
        new Thread(runnable).start();
    }

    @Override
    public void onRefresh() {
        getMessageList(1);
    }

    @Override
    public void onLoadMore() {
        //getMessageList(pagenum);
        getNotice(pagenum);
    }

    // 生成导航条
	/*
	 * private void initnav() { final TextView navnews = (TextView)
	 * findViewById(R.id.navnews); final TextView navinformation = (TextView)
	 * findViewById(R.id.navinformation); final TextView navsuggestion =
	 * (TextView) findViewById(R.id.navsuggestion); final TextView navrepresent
	 * = (TextView) findViewById(R.id.navrepresent); final TextView navhome =
	 * (TextView) findViewById(R.id.navhome); final TextView navassist =
	 * (TextView) findViewById(R.id.navassist);
	 *
	 * navinformation.setBackgroundResource(R.color.maroon);
	 *
	 * navnews.setOnClickListener(new OnClickListener() {
	 *
	 * @Override public void onClick(View v) {
	 * navinformation.setBackgroundResource(R.drawable.icon_bg); Intent intent =
	 * new Intent(MessageActivity.this, StartActivity.class);
	 * startActivity(intent); MessageActivity.this.finish(); } });
	 * navsuggestion.setOnClickListener(new OnClickListener() {
	 *
	 * @Override public void onClick(View v) {
	 * navinformation.setBackgroundResource(R.drawable.icon_bg); Intent intent =
	 * new Intent(MessageActivity.this, ApplicationCenterActivity.class);
	 * startActivity(intent); MessageActivity.this.finish(); } });
	 * navrepresent.setOnClickListener(new OnClickListener() {
	 *
	 * @Override public void onClick(View v) {
	 * navinformation.setBackgroundResource(R.drawable.icon_bg); Intent intent =
	 * new Intent(MessageActivity.this, PictureActivity.class);
	 * startActivity(intent); MessageActivity.this.finish(); } });
	 * navhome.setOnClickListener(new OnClickListener() {
	 *
	 * @Override public void onClick(View v) {
	 * navnews.setBackgroundResource(R.drawable.icon_bg); Intent intent = new
	 * Intent(MessageActivity.this, CampusOverviewActivity.class);
	 * startActivity(intent); } }); navassist.setOnClickListener(new
	 * OnClickListener() {
	 *
	 * @Override public void onClick(View v) {
	 *
	 * Intent intent = new Intent(MessageActivity.this,
	 * CampusCultureActivity2.class); startActivity(intent);
	 * MessageActivity.this.finish();
	 *
	 * } });
	 *
	 * }
	 */
    // 初始化侧边栏
    // private void initSlibeNav(){
    //
    // if (GlobalVar.userid == null || GlobalVar.userid.equals("")) {
    // userlogout.setVisibility(View.GONE);
    // userlogon.setVisibility(View.VISIBLE);
    // userlogon.setOnClickListener(new OnClickListener() {
    // @Override
    // public void onClick(View v) {
    // Intent intent = new Intent(MessageActivity.this,
    // LoginActivity.class);
    // startActivity(intent);
    // }
    // });
    // } else {
    // TextView username2 = (TextView)findViewById(R.id.username2);
    // username2.setText(GlobalVar.username);
    // userlogon.setVisibility(View.GONE);
    // userlogout.setVisibility(View.VISIBLE);
    // userlogout.setOnClickListener(new OnClickListener() {
    // @Override
    // public void onClick(View v) {
    // Intent intent = new Intent(MessageActivity.this,
    // UserCenterActivity.class);
    // startActivity(intent);
    // }
    // });
    // }
    //
    // final TextView slidenavintroduction = (TextView)
    // findViewById(R.id.slidenavintroduction);
    // slidenavintroduction.setSelected(true);
    // final TextView slidenavEdu = (TextView) findViewById(R.id.slidenavEdu);
    // final TextView slidenavrecruit = (TextView)
    // findViewById(R.id.slidenavrecruit);
    // final TextView slidenavTeacher = (TextView)
    // findViewById(R.id.slidenavTeacher);
    // final TextView slidenavCulture = (TextView)
    // findViewById(R.id.slidenavCulture);
    // slidenavEdu.setOnClickListener(new OnClickListener() {
    // @Override
    // public void onClick(View v) {
    // //mSlideHolder.close();
    // Intent intent = new Intent(MessageActivity.this,SchoolEduActivity.class);
    // startActivity(intent);
    // }
    // });
    // slidenavrecruit.setOnClickListener(new OnClickListener() {
    // @Override
    // public void onClick(View v) {
    // //mSlideHolder.close();
    // Intent intent = new
    // Intent(MessageActivity.this,SchoolRecruitActivity.class);
    // startActivity(intent);
    // }
    // });
    // slidenavTeacher.setOnClickListener(new OnClickListener() {
    // @Override
    // public void onClick(View v) {
    // //mSlideHolder.close();
    // Intent intent = new
    // Intent(MessageActivity.this,SchoolTeacherActivity.class);
    // startActivity(intent);
    // }
    // });
    // slidenavCulture.setOnClickListener(new OnClickListener() {
    // @Override
    // public void onClick(View v) {
    // //mSlideHolder.close();
    // Intent intent = new
    // Intent(MessageActivity.this,SchoolCutureActivity.class);
    // startActivity(intent);
    // }
    // });
    // }

    @Override
    protected void onResume() {
        // initSlibeNav();
        // TextView navinformation = (TextView)
        // findViewById(R.id.navinformation);
        // if (navinformation != null)
        // navinformation.setBackgroundResource(R.color.maroon);
        super.onResume();
    }
}

