package com.wust.newsmartschool.utils;

import android.content.Context;
import android.util.Log;

import com.wust.newsmartschool.domain.DocumentDetail;
import com.wust.newsmartschool.domain.DocumentsList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;


public class DocumentServer {
    private static final String TAG = "DocumentServer";

    public static List<DocumentsList> parseNewsJSON(List<DocumentsList> SNoticeLists, String url, Context context)
            throws IOException {
        String json = "";
        if (Helper.checkConnection(context)) {
            try {
                json = Helper.getStringFromUrl(url);

            } catch (IOException e) {
                Log.e("IOException is : ", e.toString());
                e.printStackTrace();
                return SNoticeLists;
            }
        }

        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                DocumentsList SNoticeList = new DocumentsList();
                SNoticeList.setFbsj((jsonObject.isNull("xwbh") ? ""
                        : jsonObject.getString("xwbh")));
                SNoticeList.setXwbh((jsonObject.isNull("xwbt") ? ""
                        : jsonObject.getString("xwbt")));
                SNoticeList.setXwbt((jsonObject.isNull("fbsj") ? ""
                        : jsonObject.getString("fbsj")));
                SNoticeLists.add(SNoticeList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return SNoticeLists;
    }
    public static DocumentDetail parseDetailJSON(String url, Context context)
            throws IOException {
        DocumentDetail sNoticesDetail = new DocumentDetail();
        String json = "";
        if (Helper.checkConnection(context)) {
            try {
                json = Helper.getStringFromUrl(url);

            } catch (IOException e) {
                Log.e("IOException is : ", e.toString());
                e.printStackTrace();
                return sNoticesDetail;
            }
        }
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                sNoticesDetail.setDjsl((jsonObject.isNull("djsl") ? ""
                        : jsonObject.getString("djsl")));
                sNoticesDetail.setFbsj((jsonObject.isNull("fbsj") ? ""
                        : jsonObject.getString("fbsj")));
                sNoticesDetail.setXwbt((jsonObject.isNull("xwbt") ? ""
                        : jsonObject.getString("xwbt")));
                sNoticesDetail.setXwnr((jsonObject.isNull("xwnr") ? ""
                        : jsonObject.getString("xwnr")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sNoticesDetail;
    }
}

