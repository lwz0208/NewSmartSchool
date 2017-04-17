package com.wust.newsmartschool.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wust.newsmartschool.R;
import com.wust.newsmartschool.domain.DocumentsList;
import com.wust.newsmartschool.ui.DocumentDetailActivity;

import java.text.SimpleDateFormat;
import java.util.List;

public class DocumentsListAdapter extends BaseAdapter {
    private static final String TAG = "DocumentsListAdapter";
    private List<DocumentsList> documentsLists;
    private Context context;

    public DocumentsListAdapter(Context context, List<DocumentsList> documentsLists) {
        this.context = context;
        this.documentsLists = documentsLists;
    }

    @Override
    public int getCount() {
        return documentsLists.size();
    }

    @Override
    public Object getItem(int position) {
        return documentsLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 第一步目的是得到newsImg，newsTitle，newsContent，newsComment
        ImageView messageImg;
        TextView messageTitle;
        TextView messageTime;
        TextView messageContent;

        // 如果是第一屏的数据，没有缓存
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.message_item, null);
            messageImg = (ImageView) convertView.findViewById(R.id.messageImg);
            messageTitle = (TextView) convertView
                    .findViewById(R.id.messageTitle);
            messageTime = (TextView) convertView.findViewById(R.id.messageTime);
            ViewBundle viewBundle = new ViewBundle(messageImg, messageTitle,
                    messageTime);
            convertView.setTag(viewBundle);
        } else {
            ViewBundle viewBundle = (ViewBundle) convertView.getTag();
            messageImg = viewBundle.messageImg;
            messageTitle = viewBundle.messageTitle;
            messageTime = viewBundle.messageTime;
            //messageContent = viewBundle.messageContent;
        }

        // messageTitle.setTypeface(GlobalVar.tf);

        // 第二步目的是得到数据，并填充
        // 得到position位置的news信息
        DocumentsList documentsList = (DocumentsList) getItem(position);
        messageTitle.setText(documentsList.getXwbh());
        String str = toTime(documentsList.getXwbt());
        messageTime.setText("发布日期：" + str);
        // 异步加载图片
        // if (rMessage.getMessagePic()==null ||
        // rMessage.getMessagePic().equals("")) {
        // messageImg.setVisibility(View.GONE);
        // } else {
        messageImg.setVisibility(View.VISIBLE);
        // 为imageView设置tag，可以防止图片的错位
        messageImg.setImageDrawable(context.getResources().getDrawable(
                (R.drawable.ic_launcher2)));
		/*
		 * PictureServer pictureServer = new PictureServer(context,
		 * GlobalVar.cacheFile);
		 * pictureServer.asyncDownPic(messageImg,rMessage.getMessagePic(),
		 * GlobalVar.cacheFile, 90, 90, null, null);
		 */
        // }
        // 为convertView设置点击事件，不知道为什么listview的onitemclick事件无法响应？？？？？
        convertView.setOnClickListener(new NewsClickListener(documentsList.getFbsj()));
        return convertView;
    }

    private String toTime(String xwbt) {
        String str = xwbt.substring(6, xwbt.length() - 2);
        String time = new SimpleDateFormat("yyyy年MM月dd日").format(Long
                .parseLong(str));

        return time;
    }

    private class ViewBundle {
        private ImageView messageImg;
        private TextView messageTitle;
        private TextView messageTime;
        //private TextView messageContent;

        public ViewBundle(ImageView messageImg, TextView messageTitle,
                          TextView messageTime) {
            this.messageImg = messageImg;
            this.messageTitle = messageTitle;
            this.messageTime = messageTime;
        }
    }

    // 新闻的点击事件
    private class NewsClickListener implements View.OnClickListener {

        private String newsId; // 新闻的id;

        public NewsClickListener(String newsId) {
            this.newsId = newsId;
        }

        @Override
        public void onClick(View v) {
            // 跳转到详细新闻页面
            Intent intent = new Intent(context, DocumentDetailActivity.class);
            intent.putExtra("newsid", newsId);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

    }
}

