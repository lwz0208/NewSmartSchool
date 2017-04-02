package com.ding.chat.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.bumptech.glide.Glide;
import com.ding.chat.R;
import com.ding.chat.domain.JFlowTrackEntity_Track;
import com.ding.easeui.Constant;
import com.ding.easeui.widget.GlideCircleImage;
import com.ding.easeui.widget.GlideRoundTransform;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class JFlowInfoAdapter extends BaseAdapter {
    List<JFlowTrackEntity_Track> acceptcenterentity_data = new ArrayList<>();
    private LayoutInflater mInflater;
    private ViewHolder vh;
    Context mCon;

    public JFlowInfoAdapter(Context con, List<JFlowTrackEntity_Track> objects) {
        mInflater = LayoutInflater.from(con);
        acceptcenterentity_data.addAll(objects);
        mCon = con;
    }

    @Override
    public int getCount() {
        return acceptcenterentity_data.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return acceptcenterentity_data.get(position);
    }


    private static class ViewHolder {
        private TextView jflowtrack_content;
        private TextView jflowend_tips;
        private ImageView head_jflow;
        private ImageView jflowtrack_titleimg;
        private TextView jflowtrack_title;
        private TextView jflowtrack_time;
        private TextView status_jflow;
        private TextView jflowtrack_nexttips;
        private LinearLayout ll_jflowtrack;
        private LinearLayout ll_jflowtrack_status;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_visit, null);
            vh.jflowtrack_content = (TextView) convertView
                    .findViewById(R.id.jflowtrack_content);
            vh.ll_jflowtrack = (LinearLayout) convertView
                    .findViewById(R.id.ll_jflowtrack);
            vh.jflowtrack_titleimg = (ImageView) convertView.findViewById(R.id.jflowtrack_titleimg);
            vh.ll_jflowtrack_status = (LinearLayout) convertView
                    .findViewById(R.id.ll_jflowtrack_status);
            vh.status_jflow = (TextView) convertView.findViewById(R.id.status_jflow);
            vh.jflowend_tips = (TextView) convertView.findViewById(R.id.jflowend_tips);
            vh.jflowtrack_nexttips = (TextView) convertView.findViewById(R.id.jflowtrack_nexttips);
            vh.jflowtrack_time = (TextView) convertView
                    .findViewById(R.id.jflowtrack_time);
            vh.head_jflow = (ImageView) convertView.findViewById(R.id.head_jflow);
            vh.jflowtrack_title = (TextView) convertView
                    .findViewById(R.id.jflowtrack_title);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        if (position >= 0 && position < getCount() - 1) {
            vh.jflowend_tips.setVisibility(View.GONE);
            vh.ll_jflowtrack.setVisibility(View.VISIBLE);
            vh.ll_jflowtrack_status.setVisibility(View.VISIBLE);
            vh.status_jflow.setVisibility(View.VISIBLE);
            vh.jflowtrack_time.setVisibility(View.VISIBLE);
            vh.jflowtrack_content.setVisibility(View.VISIBLE);
            vh.jflowtrack_nexttips.setVisibility(View.GONE);
            Glide.with(mCon)
                    .load(Constant.GETHEADIMAG_URL
                            + acceptcenterentity_data.get(position).getEmpfrom()
                            + ".png").transform(new GlideRoundTransform(mCon)).placeholder(R.drawable.ease_default_avatar)
                    .into(vh.head_jflow);
            vh.jflowtrack_titleimg.setImageResource(R.drawable.working_finished_check);
            try {
                vh.jflowtrack_time.setText(acceptcenterentity_data.get(position).getRdt());

            } catch (Exception e) {
                vh.jflowtrack_time.setText("");
            }
            try {
                vh.jflowtrack_content.setText(acceptcenterentity_data.get(position)
                        .getMsg());

            } catch (Exception e) {
                vh.jflowtrack_content.setText("");
            }
            try {
                vh.jflowtrack_title.setText(acceptcenterentity_data.get(position)
                        .getEmpfromt());

            } catch (Exception e) {
                vh.jflowtrack_title.setText("");
            }
            vh.status_jflow.setText(acceptcenterentity_data.get(position).getNdfromt());
        }
        if (position != 0) {
            if (position == (getCount() - 1)) {
                Log.e("JFlowInfoAdapter", getCount() + "");
                if (acceptcenterentity_data.get(getCount() - 2).getActiontype() == 8) {
                    //为8表示已经完成了
                    vh.head_jflow.setBackgroundResource(R.drawable.em_unread_dot);
                    vh.ll_jflowtrack.setVisibility(View.GONE);
                    vh.ll_jflowtrack_status.setVisibility(View.GONE);
                    vh.status_jflow.setVisibility(View.GONE);
                    vh.jflowend_tips.setVisibility(View.VISIBLE);
                    vh.jflowend_tips.setText("该流程已结束");
                } else if (acceptcenterentity_data.get(getCount() - 2).getActiontype() == 1) {
                    //为1表示是正在进行
                    vh.jflowtrack_titleimg.setImageResource(R.drawable.working_flow_ing);
                    Glide.with(mCon)
                            .load(Constant.GETHEADIMAG_URL
                                    + acceptcenterentity_data.get(getCount() - 2).getEmpto()
                                    + ".png").transform(new GlideRoundTransform(mCon)).placeholder(R.drawable.ease_default_avatar)
                            .into(vh.head_jflow);
                    vh.jflowtrack_time.setVisibility(View.GONE);
                    vh.jflowtrack_content.setVisibility(View.GONE);
                    vh.jflowtrack_nexttips.setVisibility(View.VISIBLE);
                    try {
                        vh.jflowtrack_title.setText(acceptcenterentity_data.get(getCount() - 2)
                                .getEmptot());
                    } catch (Exception e) {
                        vh.jflowtrack_title.setText("");
                    }
                    vh.status_jflow.setText(acceptcenterentity_data.get(getCount() - 2).getNdtot());
                } else if (acceptcenterentity_data.get(getCount() - 2).getActiontype() == 5) {
                    //为5表示是被撤销了，GG怪我咯！~
                    vh.head_jflow.setBackgroundResource(R.drawable.em_unread_dot);
                    vh.ll_jflowtrack.setVisibility(View.GONE);
                    vh.status_jflow.setVisibility(View.GONE);
                    vh.ll_jflowtrack_status.setVisibility(View.GONE);
                    vh.jflowend_tips.setVisibility(View.VISIBLE);
                    vh.jflowend_tips.setText("该流程已被撤销");
                } else if (acceptcenterentity_data.get(getCount() - 2).getActiontype() == 2) {
                    //为2表示是被驳回了，GG怪我咯！~
                    vh.head_jflow.setBackgroundResource(R.drawable.em_unread_dot);
                    vh.ll_jflowtrack.setVisibility(View.GONE);
                    vh.status_jflow.setVisibility(View.GONE);
                    vh.ll_jflowtrack_status.setVisibility(View.GONE);
                    vh.jflowend_tips.setVisibility(View.VISIBLE);
                    vh.jflowend_tips.setText("该流程已被驳回");
                }

            }
        }
        return convertView;
    }

}