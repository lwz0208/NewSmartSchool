package com.wust.newsmartschool.utils;

import android.util.Log;

import com.wust.newsmartschool.domain.MealAdd;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


@SuppressWarnings("deprecation")
public class HttpTools {
    private static HttpClient client = new DefaultHttpClient();
    public String getJsonString(String url) {
        String result = "";
        String line = "";
        InputStream is = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
            httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
            HttpResponse response = httpclient.execute(new HttpGet(url));
            is = response.getEntity().getContent();
            if (is != null) {
                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(is));
                while ((line = rd.readLine()) != null) {
                    result += line;
                }
                is.close();
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String doPost(String url,ArrayList<BasicNameValuePair> data)
    {

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(data, HTTP.UTF_8);
            String result = "";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            post.setEntity(entity);
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(httpResponse.getEntity());
            }

            return result;
        } catch (Exception e) {
            Log.i("post_exception",e.toString());
            return null;
        }
    }

    public ArrayList<MealAdd> getAds(String jsonStr)
    {
        try
        {
            ArrayList<MealAdd> result = new ArrayList<MealAdd>();
            JSONArray jArray = new JSONArray(jsonStr);
            JSONObject jsonObj;
            for(int i = 0; i < jArray.length() ; i++)
            {
                MealAdd add = new MealAdd();
                jsonObj = jArray.getJSONObject(i);
                add.setTitle(jsonObj.optString("title"));
                add.setImgUrl(jsonObj.optString("url"));
                add.setContent(jsonObj.optString("content"));
                result.add(add);
            }
            return result;
        }
        catch(Exception e)
        {
            return null;
        }
    }


}

