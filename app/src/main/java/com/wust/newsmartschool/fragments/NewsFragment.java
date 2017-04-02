package com.wust.newsmartschool.fragments;

import java.util.ArrayList;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.wust.newsmartschool.R;
import com.wust.newsmartschool.adapter.NewsListAdapter;
import com.wust.newsmartschool.domain.CommonNews;
import com.wust.newsmartschool.ui.NewsDetailActivity;
import com.wust.easeui.utils.CommonUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class NewsFragment extends Fragment implements AdapterView.OnItemClickListener {
    String TAG = "NewsFragment";
    private PtrClassicFrameLayout ptrClassicFrameLayout;
    private ListView mListView;

    private NewsListAdapter newsListAdapter;
    private ArrayList<CommonNews> newsList;
    private int page = 1;
    private int newsnum = 0;
    ArrayList<CommonNews> mList = new ArrayList<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    ArrayList<CommonNews> tmp = (ArrayList<CommonNews>) msg.obj;
                    //如果page是2那就说明是刷新
                    if (page == 2) {
                        newsList.clear();
                        newsList.addAll(tmp);
                        newsListAdapter.notifyDataSetChanged();
                        ptrClassicFrameLayout.refreshComplete();
                        if (tmp.size() >= 10) {
                            ptrClassicFrameLayout.setLoadMoreEnable(true);
                        }
                    } else if (page > 2) {
                        //否则就是上拉加载
                        newsList.addAll(tmp);
                        newsListAdapter.notifyDataSetChanged();
                        ptrClassicFrameLayout.loadMoreComplete(true);
                        if (tmp.size() < 10) {
                            ptrClassicFrameLayout.setLoadMoreEnable(false);
                            Toast.makeText(getContext(), "没有更多数据", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case 200:
                    if (ptrClassicFrameLayout.isRefreshing()) {
                        ptrClassicFrameLayout.refreshComplete();
                    } else if (ptrClassicFrameLayout.isLoadingMore()) {
                        ptrClassicFrameLayout.loadMoreComplete(true);
                    }
                    Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ding, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ptrClassicFrameLayout = (PtrClassicFrameLayout) getView().findViewById(R.id.news_list_frame);
        mListView = (ListView) getView().findViewById(R.id.news_list);

        initView();
        initData();
    }

    private void initView() {
        newsList = new ArrayList<CommonNews>();
        newsListAdapter = new NewsListAdapter(newsList, getActivity());
        mListView.setAdapter(newsListAdapter);
        mListView.setOnItemClickListener(this);
        ptrClassicFrameLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 1;
                newsnum = 0;
                initData();
            }
        });

        ptrClassicFrameLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void loadMore() {
                page++;
                initData();
            }
        });
    }

    private void initData() {
        if (!CommonUtils.isNetWorkConnected(getActivity())) {
            handler.sendEmptyMessage(200);
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                mList.clear();
                getData();

            }
        }).start();
    }

    private void getData() {
        try {
            Document doc = Jsoup.connect("http://www.wh5yy.com/index.php/index/show/tid/8/p/" + page + ".html").timeout(200000).get();
            Elements newslines = doc.select("div.ny_news").select("li");
            String num = doc.select("div.ny_news").select("div.fenye").text().trim().split(" ")[0];
            if (newsnum == Integer.parseInt(num)) {
                return;
            }
            for (int i = 0; i < newslines.size(); i++) {
                String detail = newslines.get(i).select("a").attr("href");
                String img = newslines.get(i).select("img").attr("src");
                String title = newslines.get(i).select("div.p2_r_con").select("p.title").text();
                String content = newslines.get(i).select("div.p2_r_con").select("p").select("a[href]").text();
                CommonNews CommonNews = new CommonNews();
                CommonNews.setTitle(title);
                CommonNews.setImageurl1(img);
                CommonNews.setContent(content);
                CommonNews.setDetailUrl(detail);
                mList.add(CommonNews);
            }
            newsnum += newslines.size();
            if (page % 2 != 0) {
                page++;
                getData();
                handler.sendMessage(handler.obtainMessage(100, mList));
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (page != 1) page--;
            handler.sendEmptyMessage(200);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CommonNews news = (CommonNews) newsListAdapter.getItem(position);
        startActivity(new Intent(getActivity(), NewsDetailActivity.class)
                .putExtra("url", news.getDetailUrl()));
        if (android.os.Build.VERSION.SDK_INT > 5) {
            getActivity().overridePendingTransition(R.anim.slide_in_from_right,
                    R.anim.slide_out_to_left);
        }
    }

    public void back(View view) {
        getActivity().finish();
    }
}

