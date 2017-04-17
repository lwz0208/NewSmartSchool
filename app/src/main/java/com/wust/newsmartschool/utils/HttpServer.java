package com.wust.newsmartschool.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.wust.newsmartschool.domain.BookItem;
import com.wust.newsmartschool.domain.BuildingItem;
import com.wust.newsmartschool.domain.CourseItem;
import com.wust.newsmartschool.domain.DateItem;
import com.wust.newsmartschool.domain.DishOrderItem;
import com.wust.newsmartschool.domain.EnableSelectSemsterItem;
import com.wust.newsmartschool.domain.GetJob;
import com.wust.newsmartschool.domain.GradeItem;
import com.wust.newsmartschool.domain.HelpItem;
import com.wust.newsmartschool.domain.HjhBusItem;
import com.wust.newsmartschool.domain.IM_DepartMent_Item;
import com.wust.newsmartschool.domain.JobDeatils;
import com.wust.newsmartschool.domain.JobItem;
import com.wust.newsmartschool.domain.MealItem;
import com.wust.newsmartschool.domain.News;
import com.wust.newsmartschool.domain.Pic;
import com.wust.newsmartschool.domain.PsyRecordItem;
import com.wust.newsmartschool.domain.PsychologistInfoItem;
import com.wust.newsmartschool.domain.RMessage;
import com.wust.newsmartschool.domain.RecordMeal;
import com.wust.newsmartschool.domain.RecordMeal1;
import com.wust.newsmartschool.domain.RemarkDetail;
import com.wust.newsmartschool.domain.RepairItem;
import com.wust.newsmartschool.domain.TimeTableItem;
import com.wust.newsmartschool.domain.TrainningItem;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HttpServer
{

    private static String TAG = "NET";

    /**
     * 根据urlString从服务器得到json类型的字符串
     *
     * @param urlString
     *            服务器地址
     * @return json类型字符串
     */
    public String getData(String urlString)
    {
        try
        {
            URL url = new URL(urlString);
            HttpURLConnection connection;
            connection = (HttpURLConnection) url.openConnection(); // 打开连接
            connection.setConnectTimeout(5000);// 设置超时时间
            connection.connect(); // 连接
            if (connection.getResponseCode() == 200)
            {// 请求成功
                String currentUrlString = connection.getURL().toString();
                if (!urlString.equals(currentUrlString))
                {// 防止cmcc_edu形的网络
                    return null;
                }
                InputStream is = connection.getInputStream();
                Reader reader = new InputStreamReader(is, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(reader);
                String str = null;
                StringBuffer jsonStr = new StringBuffer();
                while ((str = bufferedReader.readLine()) != null)
                {
                    jsonStr.append(str);
                }
                is.close();
                reader.close();
                bufferedReader.close();
                connection.disconnect();
                return jsonStr.toString();
            }
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * http post请求
     *
     * @param urlString
     * @param paraList
     * @return
     */
    public String httppost(String urlString, List<NameValuePair> paraList)
    {
        // BasicNameValuePair param = new BasicNameValuePair("name","GuangT");
        // paraList.add(param);

        try
        {
            String result = "";
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(urlString);
            // 设置参数
            post.setEntity(new UrlEncodedFormEntity(paraList, HTTP.UTF_8));
            // 发送httppost请求
            HttpResponse httpResponse = httpClient.execute(post);
            if (httpResponse.getStatusLine().getStatusCode() == 200)
            {
                result = EntityUtils.toString(httpResponse.getEntity());
            }
            return result;
        }
        catch (Exception e)
        {
            return null;
        }

    }

    public ArrayList<RMessage> getRMessagelist(String jsonString)
    {
        try
        {
            ArrayList<RMessage> rMessageList = new ArrayList<RMessage>();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                RMessage rMessage = new RMessage();
                rMessage.setMessageId(jsonObject.getString("id"));
                rMessage.setMessageTitle(jsonObject.getString("title"));
                rMessage.setClickNum(jsonObject.getInt("clicknum"));
                rMessage.setMessageContent(jsonObject.getString("newscontent"));
                if (jsonObject.getString("newsimg") != null
                        && !(jsonObject.getString("newsimg").equals(""))
                        && !(jsonObject.getString("newsimg").equals("null")))
                {
                    if (jsonObject.getString("newsimg").contains("http"))
                        rMessage.setMessagePic(jsonObject.getString("newsimg"));
                    else
                        rMessage.setMessagePic(GlobalVar.serviceip
                                + jsonObject.getString("newsimg"));
                }
                rMessage.setMessageTime(jsonObject.getString("newstime"));
                rMessageList.add(rMessage);
            }
            return rMessageList;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * 得到图片列表
     *
     * @param jsonStr
     * @return
     */
    public ArrayList<Pic> getPic(String jsonStr)
    {
        ArrayList<Pic> pics = new ArrayList<Pic>();
        try
        {
            JSONArray picArray = new JSONArray(jsonStr);
            for (int i = 0; i < picArray.length(); i++)
            {
                Pic pic = new Pic();
                JSONObject jsonObject = picArray.optJSONObject(i);
                JSONArray cell = jsonObject.getJSONArray("cell");
                pic.setPicId(cell.optString(0));
                pic.setPicTitle(cell.optString(2));
                pic.setPicUrl(cell.optString(1));
                pics.add(pic);
            }
            return pics;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /********************************************/

    /**
     * 得到图片新闻对应的所有图片的列表
     *
     * @param jsonStr
     * @return
     */
    public ArrayList<Pic> getPic2(String jsonStr)
    {
        ArrayList<Pic> pics = new ArrayList<Pic>();
        try
        {
            JSONArray picArray = new JSONArray(jsonStr);
            for (int i = 0; i < picArray.length(); i++)
            {
                Pic pic = new Pic();
                JSONObject jsonObject = picArray.optJSONObject(i);
                JSONArray cell = jsonObject.getJSONArray("cell");
                pic.setPicId(String.valueOf(i));
                pic.setPicTitle(cell.optString(1));
                pic.setPicUrl(cell.optString(0));
                pics.add(pic);
            }
            return pics;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<GetJob> getJoblist(String jsonString)
    {
        try
        {
            ArrayList<GetJob> getJobList = new ArrayList<GetJob>();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                GetJob getJob = new GetJob();
                getJob.set_getjob_xwbh(jsonObject.getString("xwbh"));
                getJob.set_getjob_xwbt(jsonObject.getString("xwbt"));
                getJob.set_getjob_fbsj(jsonObject.getString("fbsj"));

                getJobList.add(getJob);
            }
            return getJobList;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    //获取校车时间列表
    public ArrayList<HjhBusItem> getBuslist(String jsonString)
    {
        try
        {
            ArrayList<HjhBusItem> getBusList = new ArrayList<HjhBusItem>();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                HjhBusItem hjhBusItem = new HjhBusItem();
                hjhBusItem.setDepartment(jsonObject.getString("title"));
                hjhBusItem.setTime(jsonObject.getString("time"));
                hjhBusItem.setContent(jsonObject.getString("xxnr"));

                getBusList.add(hjhBusItem);
            }
            return getBusList;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public ArrayList<BuildingItem> getBuildingList(String jsonString)
    {
        ArrayList<BuildingItem> buildingList = new ArrayList<BuildingItem>();
        try
        {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                BuildingItem item = new BuildingItem();

                if (!jsonObject.optString("jzwmc").equals("教十楼")
                        && !jsonObject.optString("jzwmc").equals("教七楼"))
                {
                    item.setJzwid(jsonObject.optString("jzwid"));
                    item.setJzwmc(jsonObject.optString("jzwmc"));
                    buildingList.add(item);
                }

            }
            return buildingList;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public ArrayList<DateItem> getDateList(String jsonString)
    {
        ArrayList<DateItem> dateList = new ArrayList<DateItem>();
        try
        {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                DateItem item = new DateItem();
                item.setDate(jsonObject.optString("date"));
                item.setNum(jsonObject.optString("num"));
                item.setWeek(jsonObject.optString("week"));
                dateList.add(item);
            }
            return dateList;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public ArrayList<String> getJcList(String jsonString)
    {
        ArrayList<String> jcList = new ArrayList<String>();
        try
        {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                jcList.add(jsonObject.optString("jc"));
            }
            return jcList;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public ArrayList<String> getRoomsList(String jsonString)
    {
        ArrayList<String> roomList = new ArrayList<String>();
        try
        {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                roomList.add(jsonObject.optString("jsid"));
            }
            return roomList;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public ArrayList<News> getNewsList(String jsonString)
    {
        ArrayList<News> newsList = new ArrayList<News>();
        try
        {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                News news = new News();
                news.setTITLE(jsonObject.optString("Xwbh"));
                news.setTITLE(jsonObject.optString("Xwbt"));
                news.setNEWSADDRESS(jsonObject.optString("Fbsj"));
                newsList.add(news);
            }

            return newsList;
        }
        catch (Exception e)
        {
            return null;
        }
    }

	/*public News getNews(String jsonString)
	{
		News news = new News();
		try
		{
			JSONObject jsonObject = new JSONObject(jsonString);
			//JSONArray jsonArray = new JSONArray(jsonString);
			//JSONObject jsonObject = jsonArray.optJSONObject(0);
			news.setNewsTitle(jsonObject.optString("Xwbt"));
			news.setNewsContent(jsonObject.optString("Xwnr"));
			news.setNewsTime(jsonObject.optString("Fbsj"));
			news.setClickNum(jsonObject.optString("Djsl"));
			news.setNewsLj(jsonObject.optString("Xwdz"));
			return news;
		}
		catch (Exception e)
		{
			return null;
		}
	}*/

    // 返回“咨询师信息列表”
    public ArrayList<PsychologistInfoItem> getExpertInfoList(String jsonString)
    {
        ArrayList<PsychologistInfoItem> newsList = new ArrayList<PsychologistInfoItem>();
        try
        {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                PsychologistInfoItem news = new PsychologistInfoItem();
                news.setName(jsonObject.optString("name"));
                news.setId(jsonObject.optString("id"));
                news.setIntroduction(jsonObject.optString("introduction"));
                news.setImgurl(jsonObject.optString("head_img"));
                newsList.add(news);
            }
            return newsList;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    // 返回“个人预约记录信息列表”
    public ArrayList<PsyRecordItem> getRecodeList(String jsonString)
    {
        ArrayList<PsyRecordItem> newsList = new ArrayList<PsyRecordItem>();
        try
        {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                PsyRecordItem news = new PsyRecordItem();
                news.setExpertName(jsonObject.optString("dname"));
                news.setDate(jsonObject.optString("date"));
                news.setType(jsonObject.optString("tname"));
                news.setStatus(jsonObject.optString("status"));
                news.setTime(jsonObject.optString("time"));
                // news.setExpertPic(jsonObject.optString("head_img"));
                newsList.add(news);
            }
            return newsList;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    // 返回“后勤报修的预约表”
    public ArrayList<RepairItem> getLogisticList(String jsonString)
    {
        ArrayList<RepairItem> newsList = new ArrayList<RepairItem>();
        try
        {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                RepairItem news = new RepairItem();
                news.setId(Integer.parseInt(jsonObject.optString("id")));
                news.setSname(jsonObject.optString("sname"));
                news.setSid(jsonObject.optString("sid"));
                news.setStel(jsonObject.optString("stel"));
                news.setSdormitory(jsonObject.optString("sdormitory"));
                news.setDate(jsonObject.optString("date") + " " + jsonObject.optString("time"));
                news.setDordetail(jsonObject.optString("dordetail"));
                news.setPubdetail(jsonObject.optString("pubdetail"));
                news.setWid(Integer.parseInt(jsonObject.optString("wid")));
                news.setStatus(Integer.parseInt(jsonObject.optString("status")));
                news.setCampus(jsonObject.optString("campus"));
                news.setRefuse(jsonObject.optString("refuse"));
                newsList.add(news);
            }
            return newsList;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /*
     *  判断是否有网络
     */
    public static boolean isNetworkAvailable(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
        {
            return false;
        }
        else
        {
            // 打印所有的网络状态
            NetworkInfo[] infos = cm.getAllNetworkInfo();
            if (infos != null)
            {
                for (int i = 0; i < infos.length; i++)
                {
                    // Log.d(TAG, "isNetworkAvailable - info: " +
                    // infos[i].toString());
                    if (infos[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        Log.d(TAG, "isNetworkAvailable -  I " + i);
                    }
                }
            }

            // 如果仅仅是用来判断网络连接
            // 则可以使用cm.getActiveNetworkInfo().isAvailable();
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null)
            {
                Log.d(TAG,
                        "isNetworkAvailable - 是否有网络"
                                + networkInfo.isAvailable());
            }
            else
            {
                Log.d(TAG, "isNetworkAvailable - 完成没有网络");
                return false;
            }

            // 1、判断是否有3G网络
            if (networkInfo != null
                    && networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
            {
                Log.d(TAG, "isNetworkAvailable - 有3G网络");
                return true;
            }
            else
            {
                Log.d(TAG, "isNetworkAvailable - 没有3G网络");
            }

            // 2、判断是否有wifi连接
            if (networkInfo != null
                    && networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
            {
                Log.d(TAG, "isNetworkAvailable - 有wifi连接");
                return true;
            }
            else
            {
                Log.d(TAG, "isNetworkAvailable -  没有wifi连接");
            }
        }
        return false;
    }

    // 素质拓展中使用
	/*public News getQualityDetail(String jsonString)
	{
		Log.d("sss", jsonString);
		News newsItem = new News();
		JSONArray jsonArray;
		try
		{
			jsonArray = new JSONArray(jsonString);
			JSONObject jsonObject = jsonArray.optJSONObject(0);
			newsItem.setNewsContent(jsonObject.optString("content"));
			newsItem.setClickNum(jsonObject.optString("clicked"));
			newsItem.setNewsTime(jsonObject.optString("submit_time"));
			newsItem.setNewsTitle(jsonObject.optString("title"));
		}
		catch (JSONException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return newsItem;
	}*/

    // 返回“素质拓展模块列表”
    public List<News> getQualityList(String jsonString)
    {
        List<News> newsList = new ArrayList<>();
        Log.d("进入", jsonString);
        JSONArray jsonArray;

        jsonString = jsonString;
        try
        {
            jsonArray = new JSONArray(jsonString);

            // Log.d("数", jsonString);
            // Log.d("数据", jsonArray.toString());
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                News news = new News();
                news.setTITLE(jsonObject.optString("title"));
                news.setTITLE(jsonObject.optString("id"));
                news.setTITLE(jsonObject.optString("title"));
                news.setCREATETIME(jsonObject.optString("submit_time"));
                //news.setClickNum(jsonObject.optString("clicked"));
                //newsList.getData().add(news.getData());
            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            Log.d("数", jsonString);
            e.printStackTrace();
            return null;
        }

        return newsList;

    }

    // 个人成绩
    public ArrayList<GradeItem> getGradeItems(String jsonString)
    {
        ArrayList<GradeItem> gradeList = new ArrayList<GradeItem>();
        try
        {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                GradeItem item = new GradeItem();
                item.setJd(jsonObject.optString("jd"));
                item.setKcmc(jsonObject.optString("kcmc"));
                item.setKcsx(jsonObject.optString("kcsx"));
                item.setKcxz(jsonObject.optString("kcxz"));
                // item.setKkdw(jsonObject.optString("kkdw"));
                // item.setKkxq(jsonObject.optString("kkxq"));
                // item.setKsxz(jsonObject.optString("ksxz"));
                item.setXf(jsonObject.optString("xf"));
                // item.setXm(jsonObject.optString("xm"));
                item.setZcj(jsonObject.optString("zcj"));
                item.setZxs(jsonObject.optString("zxs"));
                gradeList.add(item);
            }
            return gradeList;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    // 个人课表
    public ArrayList<TimeTableItem> getTimeTableItems(String jsonString)
    {
        ArrayList<TimeTableItem> timeTableItems = new ArrayList<TimeTableItem>();
        try
        {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                TimeTableItem item = new TimeTableItem();
                item.setBjmc(jsonObject.optString("bjmc"));
                item.setJsmc(jsonObject.optString("jsmc"));
                item.setJsxm(jsonObject.optString("jsxm"));
                item.setJxl(jsonObject.optString("jxl"));
                item.setKcmc(jsonObject.optString("kcmc"));
                item.setKcsj(jsonObject.optString("kcsj"));
                item.setKkzc(jsonObject.optString("kkzc"));
                timeTableItems.add(item);
            }
            return timeTableItems;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    // 学期列表
    public ArrayList<String> getSemList(String jsonString)
    {
        ArrayList<String> semList = new ArrayList<String>();
        try
        {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                semList.add(jsonObject.optString("xnxq"));
            }
            return semList;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    // 培养方案
    public ArrayList<TrainningItem> getTrainningItems(String jsonString)
    {
        ArrayList<TrainningItem> trainningItems = new ArrayList<TrainningItem>();
        try
        {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                TrainningItem trainningItem = new TrainningItem();
                trainningItem.setSKYX(jsonObject.optString("SKYX"));
                trainningItem.setSKZY(jsonObject.optString("SKZY"));
                trainningItem.setKCXZ(jsonObject.optString("KCXZ"));
                trainningItem.setKCH(jsonObject.optString("KCH"));//课程号
                trainningItem.setKCMC(jsonObject.optString("KCMC"));//课程名称
                trainningItem.setZXS(jsonObject.optString("ZXS"));//总学时
                trainningItem.setXF(jsonObject.optString("XF"));//学分
                trainningItem.setJKXS(jsonObject.optString("JKXS"));
                trainningItem.setSYXS(jsonObject.optString("SYXS"));
                trainningItem.setSJXS(jsonObject.optString("SJXS"));
                trainningItem.setKSXQ(jsonObject.optString("KSXQ"));
                trainningItem.setKKYX(jsonObject.optString("KKYX"));
                trainningItem.setFXMC(jsonObject.optString("FXMC"));// 所属方向
                trainningItem.setHKFS(jsonObject.optString("HKFS"));// 考核方式
                trainningItem.setZHXS(jsonObject.optString("ZHXS"));// 周学时
                trainningItem.setSHZT(jsonObject.optString("SHZT"));// 审核状态
                trainningItems.add(trainningItem);
            }
            return trainningItems;
        }
        catch (Exception e)
        {
            // TODO: handle exception
            return null;
        }
    }

    // 在借图书
    public ArrayList<BookItem> getBookItems(String jsonString)
    {
        ArrayList<BookItem> list = new ArrayList<BookItem>();
        try
        {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                BookItem item = new BookItem();
                item.setM_title(jsonObject.optString("m_title"));
                item.setM_author(jsonObject.optString("m_author"));
                item.setNorm_ret_date(jsonObject.optString("norm_ret_date"));
                item.setRedr_cert_id(jsonObject.optString("redr_cert_id"));
                item.setProp_no_f(jsonObject.optString("prop_no_f"));
                item.setCall_no(jsonObject.optString("call_no"));
                item.setLend_date(jsonObject.optString("lend_date"));
                item.setName(jsonObject.optString("name"));
                item.setRet_date(jsonObject.optString("ret_date"));
                item.setBook_lend_flag(jsonObject.optString("book_lend_flag"));
                item.setM_publisher(jsonObject.optString("m_publisher"));
                item.setM_isbn(jsonObject.optString("m_isbn"));
                item.setLocation_dept(jsonObject.optString("location_dept"));
                item.setLocation_name(jsonObject.optString("location_name"));

                list.add(item);
            }
        }
        catch (Exception e)
        {
        }
        return list;
    }

    // 获得帮助列表
    public ArrayList<HelpItem> getHelpItems(String jsonString)
    {
        ArrayList<HelpItem> list = new ArrayList<HelpItem>();
        try
        {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                HelpItem item = new HelpItem();
                item.setTitle(jsonObject.optString("title"));
                item.setRoute(jsonObject.optString("route"));
                item.setContent(jsonObject.optString("content"));

                list.add(item);
            }
        }
        catch (Exception e)
        {
        }
        return list;
    }

    // 获得班车评价的班次
    public ArrayList<String> getBustimes(String jsonString)
    {
        ArrayList<String> timeStrings = new ArrayList<String>();
        try
        {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                timeStrings.add(jsonObject.optString("time"));
            }
            return timeStrings;
        }
        catch (Exception e)
        {
            return null;
        }
    }
    //订餐获取套餐列表
    public ArrayList<MealItem> getMealList(String jsonString)
    {
        ArrayList<MealItem> mealList = new ArrayList<MealItem>();
        try
        {
            JSONObject jsonObject1=new JSONObject(jsonString);
            JSONArray jsonArray=jsonObject1.optJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                MealItem mealitem = new MealItem();
                mealitem.setDescripation(jsonObject.optString("Dish_Description"));
                mealitem.setImgUrl(jsonObject.optString("Dish_ImageURL"));
                mealitem.setName(jsonObject.optString("Dish_Name"));
                mealitem.setPrice(jsonObject.optString("Dish_Price"));
                mealitem.setMealId(jsonObject.optString("Dish_ID"));
                mealitem.setSaleNum(100);
                mealitem.setPrePrice("12.00");
                mealList.add(mealitem);
            }
            return mealList;
        }
        catch (Exception e)
        {
            return null;
        }
    }
    //创建餐单历史记录
    public ArrayList<RecordMeal> getMealRecordList(String jsonString)
    {
        ArrayList<RecordMeal> RecordlList = new ArrayList<RecordMeal>();
        try
        {

            //JSONObject jsonObject1=new JSONObject(jsonString);
            JSONArray jsonArray= new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                Log.e("dddddd","3333333----"+ String.valueOf(jsonArray.length()));
                JSONObject jsonObject = jsonArray.optJSONObject(i);

                JSONArray jsonDishList=jsonObject.optJSONArray("dish_lists");
//				     for(int j=0;j<jsonDishList.length();j++)
//				     {
//
//				     }
                JSONObject jsonDish = jsonDishList.optJSONObject(0);
                RecordMeal recordlitem = new RecordMeal();
                //mealitem.setDescripation(jsonObject.optString("Dish_Description"));
                recordlitem.setDishName(jsonObject.optString("Order_UserName"));
                recordlitem.setDishType(jsonDish.optString("Dish_Type"));
                recordlitem.setImgUrl(jsonDish.optString("Dish_ImageURL"));///
                recordlitem.setOrderTime(jsonObject.optString("Order_SubmitTime"));
                recordlitem.setPrice(jsonObject.optString("Order_TotalPrice"));
                recordlitem.setDishStatus(jsonObject.optString("Order_Status"));
                recordlitem.setMealOrderId(jsonObject.optString("Order_OrderID"));
                RecordlList.add(recordlitem);
                Log.e("列表中订单id ","3333333----"+jsonObject.optString("Order_OrderID"));
            }
            return RecordlList;
        }
        catch (Exception e)
        {
            return null;
        }
    }
    //获取查看详单里面的菜的名称，份数和单价
    public ArrayList<RecordMeal1> getCheckMealRecordList(String jsonString)
    {
        // TODO Auto-generated method stub
        ArrayList<RecordMeal1> RecordMeallList= new ArrayList<RecordMeal1>();
        try
        {
            JSONArray jsonArray= new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.optJSONObject(i);


                JSONArray jsonDishList=jsonObject.optJSONArray("dish_lists");
                for (int j = 0; j < jsonDishList.length(); j++)
                {

                    JSONObject jsonDish = jsonDishList.optJSONObject(j);

                    RecordMeal1 recorditem = new RecordMeal1();
                    recorditem.setMealCount(jsonDish.optString("Order_Num"));
                    recorditem.setMealName(jsonDish.optString("Dish_Name"));
                    recorditem.setMealPrice(jsonDish.optString("Dish_Price"));
                    recorditem.setMealId(jsonDish.optString("Order_DishID"));
                    RecordMeallList.add(recorditem);
                    Log.e("朱大洋武科大11111111111","3333333----"+ RecordMeallList.get(0).getMealCount());
                }

                JSONObject jsonSender=jsonObject.optJSONObject("sender");
                if(jsonSender!=null)
                {
                    Log.e("sender_____", "$$$$$"+jsonSender);
                    if(RecordMeallList.size()>0)
                    {
                        RecordMeallList.get(0).setStuName(jsonSender.optString("name"));
                        Log.e("sender_____", "$$$$$"+jsonSender.optString("name"));
                        RecordMeallList.get(0).setStuTel(jsonSender.optString("tel"));//送餐人电话号码
                        RecordMeallList.get(0).setStuId(jsonSender.optString("stuid"));
                    }
//					RecordMeallList.get(0).setStuStatus(jsonObject.optString("Order_Status"));
//					Log.e("鍗庝腑绉戞妧澶у璁＄畻鏈虹瀛﹀闄�",jsonObject.optString("Order_Status"));
//					RecordMeallList.get(0).setStuArriveTime(jsonObject.optString("Order_sendTime"));//閫侀鍒拌揪鏃堕棿
//					RecordMeallList.get(0).setStuAcceptTime(jsonObject.optString("Order_acceptTime"));
                    //RecordMeallList.get(0).setStuTotalMoney(jsonObject.optString("Order_TotalPrice"));//鎬讳环閽�
                }
                RecordMeallList.get(0).setStuStatus(jsonObject.optString("Order_Status"));
                Log.e("华中科技大学计算机科学学院",jsonObject.optString("Order_Status"));
                RecordMeallList.get(0).setStuArriveTime(jsonObject.optString("Order_sendTime"));//送餐到达时间
                RecordMeallList.get(0).setStuAcceptTime(jsonObject.optString("Order_acceptTime"));

                RecordMeallList.get(0).setStuTotalMoney(jsonObject.optString("Order_TotalPrice"));//总价钱
                RecordMeallList.get(0).setStuSummitTime(jsonObject.optString("Order_SubmitTime"));//用户下单时间
                RecordMeallList.get(0).setStuCancelTime(jsonObject.optString("Order_cancelTime"));//用户取消时间
                RecordMeallList.get(0).setBuyerId(jsonObject.optString("Order_BuyerID"));
            }

            return RecordMeallList;
        }

        catch (Exception e)
        {
            return null;
        }

    }
    /**
     * 2015-05-23朱大洋
     * 获取每个菜的评价详情
     *
     */
    public ArrayList<RemarkDetail> getRemarkDetailList(String jsonString)
    {
        // TODO Auto-generated method stub
        ArrayList<RemarkDetail>  remarkList=new ArrayList<RemarkDetail>();
        try
        {

            JSONArray jsonArray=new JSONArray(jsonString);
            for(int i=0;i<jsonArray.length();i++)
            {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                RemarkDetail mealitem=new RemarkDetail();
                mealitem.setRemarkContent(jsonObject.optString("Review_Content"));
                Log.e("中哈哈哈哈哈评论列表参数", jsonObject.optString("Review_Content"));
                mealitem.setRemarkId(jsonObject.optString("Review_ID"));///
                mealitem.setRemarkTime(jsonObject.optString("Review_Date"));
                mealitem.setRemarkStar(jsonObject.optString("Review_Dishstar"));
                //Log.e("涓搱鍝堝搱鍝堝搱璇勮鍒楄〃鍙傛暟锛嶏紞锛嶏紞锛嶏紞锛�", "22222222"+jsonObject.optString("Review_Dishstar"));
                remarkList.add(mealitem);

            }
            return remarkList;
        }
        catch(Exception e)
        {
            return null;
        }

    }



    public ArrayList<JobItem> getjobList(String jsonString)
    {
        ArrayList<JobItem> jobList = new ArrayList<JobItem>();
        try
        {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                JobItem job = new JobItem();
                job.setId(jsonObject.optString("id"));
                job.setTitle(jsonObject.optString("title"));

                jobList.add(job);
            }
            return jobList;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public JobDeatils getRecruitmentMessageDeatil(String jsonString)
    {
        JobDeatils details=new JobDeatils();
        try
        {
            JSONObject jsonObject=new JSONObject(jsonString);
            details.setJobDeatilTitle(jsonObject.optString("title"));
            details.setJobDeatilContent(jsonObject.optString("content"));
            return details;

        }
        catch (Exception e)
        {
            return null;
        }

    }
    /**
     * 订餐手机后台
     * @param url
     * @return
     */
    public List<DishOrderItem> getMealOrderList(String url)
    {
        String jsonString = getData(url);
        List<DishOrderItem> dishOrderItems = new ArrayList<DishOrderItem>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.optJSONObject(i);
                DishOrderItem item = new DishOrderItem();
                item.setOrderId(jsonObject.optString("Order_OrderID"));
                item.setUserSummitTime(jsonObject.optString("Order_SubmitTime"));
                item.setArriveTime(jsonObject.optString("Order_ArriveTime"));
                item.setAddress(jsonObject.optString("Order_Address"));
                item.setUserName(jsonObject.optString("Order_UserName"));
                item.setUserTel(jsonObject.optString("Order_UserTelephone"));
                item.setTotalMoney(jsonObject.optString("Order_TotalPrice"));
                List<DishOrderItem.DishItem> dishItems = new ArrayList<DishOrderItem.DishItem>();
                JSONArray jsonArray2 = jsonObject.getJSONArray("dish_lists");
                for (int j = 0; j < jsonArray2.length(); j++) {
                    JSONObject jsonObject2 = jsonArray2.optJSONObject(j);
                    DishOrderItem.DishItem item2 = new DishOrderItem.DishItem();
                    item2.setDishName(jsonObject2.optString("Dish_Name"));
                    item2.setDishNum(jsonObject2.optString("Order_Num"));

                    dishItems.add(item2);
                }
                item.setDishes(dishItems);

                item.setAcceptTime(jsonObject.optString("Order_acceptTime"));

                dishOrderItems.add(item);
            }

            return dishOrderItems;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 我要配送以及已配送按钮发出的动作
     * @param url
     * @return 动作成功返回ture，否则返回false
     */
    public boolean getActionResult(String url) {
        String jsonString = getData(url);
        if (jsonString.equals("1"))
            return true;
        else
            return false;
    }


    public ArrayList<IM_DepartMent_Item> getDepartMent_Items(String jsonString)
    {

        try
        {
            ArrayList<IM_DepartMent_Item> items = new ArrayList<IM_DepartMent_Item>();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject object = jsonArray.optJSONObject(i);
                IM_DepartMent_Item item = new IM_DepartMent_Item();
                item.setCardNumber(object.optString("cardnumber"));
                String departname = object.optString("departmentname");
                item.setDepartmentName(departname.substring(0, departname.length()-2));
                String content = object.optString("content");
                Log.d("IM_content_Old", content);

                content = content.replaceAll("<br>", "\n");
                Log.d("IM_content_New", content);
                item.setContent(content);


                Log.d("IM_item", item.toString());

                items.add(item);
            }
            return items;

        } catch (Exception e)
        {
            // TODO: handle exception
            return null;
        }
    }

    //	public GuideGallery getAdver(String jsonString)
//	{
//		// TODO Auto-generated method stub
//		try
//		{
//			JSONArray jsonArray = new JSONArray(jsonString);
//			for(int i=0;i<jsonArray.length();i++)
//			{
//
//			}
//			return ;
//		}
//		catch(Exception e)
//		{
//			return null;
//		}
//
//	}
    public List<EnableSelectSemsterItem> getEnableSelectSemsterItems(
            String jsonString) {
        List<EnableSelectSemsterItem> SemsterItems = new ArrayList<EnableSelectSemsterItem>();
        try {
            SemsterItems = JSON.parseArray(jsonString,
                    EnableSelectSemsterItem.class);
        } catch (Exception e) {
            Log.i("getCourseItems", jsonString);
            e.printStackTrace();
            return null;
        }
        return SemsterItems;
    }
    public List<CourseItem> getCourseItems(String jsonString) {
        List<CourseItem> courseList = new ArrayList<CourseItem>();
        // try {
        // JSONArray jsonArray = new JSONArray(jsonString);
        // for (int i = 0; i < jsonArray.length(); i++) {
        // JSONObject jsonObject = jsonArray.optJSONObject(i);
        // CourseItem item = new CourseItem();
        //
        // item.setKcmc(jsonObject.optString("kcmc"));
        //
        // courseList.add(item);
        // }
        // return courseList;
        // } catch (Exception e) {
        // return null;
        // }
        try {
            courseList = JSON.parseArray(jsonString, CourseItem.class);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.i("getCourseItems", jsonString);
            e.printStackTrace();
            return null;
        }
        return courseList;
    }



}

