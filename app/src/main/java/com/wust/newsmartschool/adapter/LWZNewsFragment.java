package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.News;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LWZNewsFragment extends Fragment implements MyNewsItemRecyclerViewAdapter.OnItemClickLitener {

    private static final String ARG_PAGE = "column-count";
    private int mPage;
    private int page = 1;
    private List<News> newsList = new ArrayList<>();
    private List<News> mList = new ArrayList<>();
    private MyNewsItemRecyclerViewAdapter mAdapter;
    private PtrClassicFrameLayout ptrClassicFrameLayout;
    private String Url;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    ArrayList<News> tmp = (ArrayList<News>) msg.obj;
                    Log.i("resulttmp", tmp.toString());
                    if (page == 1) {
                        //page=1是下拉刷新
                        newsList.clear();
                        newsList.addAll(tmp);
                        mAdapter.notifyDataSetChanged();
                        ptrClassicFrameLayout.refreshComplete();
                        if (tmp.size() < 30) {
                            ptrClassicFrameLayout.setLoadMoreEnable(false);
                        } else {
                            page++;
                        }
                    } else {
                        //否则就是上拉加载
                        newsList.addAll(tmp);
                        mAdapter.notifyDataSetChanged();
                        ptrClassicFrameLayout.loadMoreComplete(true);
                        if (tmp.size() < 30) {
                            ptrClassicFrameLayout.setLoadMoreEnable(false);
                            Snackbar.make(getView(), "没有更多数据了", Snackbar.LENGTH_LONG).setAction("", null).show();
                        } else {
                            page++;
                        }
                    }
                    break;
                case 200:
                    if (ptrClassicFrameLayout.isRefreshing()) {
                        ptrClassicFrameLayout.refreshComplete();
                    } else if (ptrClassicFrameLayout.isLoadingMore()) {
                        ptrClassicFrameLayout.loadMoreComplete(true);
                    }
                    Snackbar.make(getView(), "网络异常", Snackbar.LENGTH_LONG).setAction("", null).show();
                    break;
            }
        }
    };

    public LWZNewsFragment() {
    }

    @SuppressWarnings("unused")
    public static LWZNewsFragment newInstance(int mPage) {
        LWZNewsFragment fragment = new LWZNewsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, mPage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        // Set the adapter
        Context context = view.getContext();
        mAdapter = new MyNewsItemRecyclerViewAdapter(newsList);
        mAdapter.setOnItemClickLitener(this);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mAdapter);

        ptrClassicFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.test_list_view_frame);
        ptrClassicFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrClassicFrameLayout.autoRefresh(true);
            }
        }, 150);

        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        initNewsList(mPage);
                    }
                }, 1500);
            }
        });

        ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        initNewsList(mPage);
                    }
                }, 1000);
            }
        });
        initNewsList(mPage);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initNewsList(int pos) {
        switch (pos) {
            case 0:
                //要闻
                Url = "http://202.114.242.85/type/00500112.html";
                break;
            case 1:
                //综合
                Url = "http://202.114.242.85/type/00500104.html";
                break;
            case 2:
                //院系
                Url = "http://202.114.242.85/type/00500105.html";
                break;
            case 3:
                //学术
                Url = "http://202.114.242.85/type/00500106.html";
                break;
            case 4:
                //人物
                Url = "http://202.114.242.85/type/00500107.html";
                break;
            case 5:
                //声音
                Url = "http://202.114.242.85/type/00500108.html";
                break;
            case 6:
                //深度
                Url = "http://202.114.242.85/type/00500109.html";
                break;
            case 7:
                //聚焦
                Url = "http://202.114.242.85/type/00500110.html";
                break;
            case 8:
                //媒体
                Url = "http://202.114.242.85/type/00500111.html";
                break;
        }

        Log.i("resultUrl", Url);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mList.clear();
                try {
                    Document doc = Jsoup.connect(Url).get();
                    Elements newslines = doc.select("div.typelist").select("li");
                    for(int i = 0; i < newslines.size(); i++) {
                        String detail = newslines.get(i).select("a").attr("href");
                        String time = newslines.get(i).select("span.time").text();
                        String title = newslines.get(i).select("a[href]").text();

                        News news = new News();
                        news.setNEWSADDRESS(detail);
                        news.setCREATETIME(time);
                        news.setTITLE(title);
                        mList.add(news);
                    }
                    handler.sendMessage(handler.obtainMessage(100, mList));
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(200);
                }
            }
        }).start();
    }

    @Override
    public void onItemClick(View view, int position) {
        Snackbar.make(view, "ItemPressed" + newsList.get(position).getCREATETIME() + "--" + newsList.get(position).getTITLE(), Snackbar.LENGTH_LONG).setAction("", null).show();
    }

    @Override
    public void onItemLongClick(View view, int position) {
        Snackbar.make(view, "ItemLongPressed" + newsList.get(position).getCREATETIME() + "--" + newsList.get(position).getTITLE(), Snackbar.LENGTH_LONG).setAction("", null).show();
    }
}

