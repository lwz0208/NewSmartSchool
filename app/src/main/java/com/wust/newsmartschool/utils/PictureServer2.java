package com.wust.newsmartschool.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.wust.newsmartschool.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;



/**
 * 图片操作类
 *
 * @author GuangT
 *
 */
public class PictureServer2 {
    /**
     * 异步下载图片，这个是这里面最为重要的方法
     *
     * @param imageView
     * @param picUrl
     *            图片的路径
     * @param cacheFile
     *            图片在sd卡上面的保存路径
     * @param type
     *            //加载图片的位置是哪，1：列表小图， 2：每页上面的的三张大图 3.订阅的图片
     */
    public static void asyncDownPic(ImageView imageView, String picUrl,
                                    File cacheFile, int type) {
        AsyncImageTask asyncImageTask = new AsyncImageTask(cacheFile,
                imageView, type);
        asyncImageTask.execute(picUrl);
    }

    /**
     * 计算bitmap的InSamplesize,用于压缩图片，减少内存的占用，避免oom错误
     *
     * @param options
     *            BitmapFactory.Options
     * @param reqWidth
     *            需求的宽
     * @param reqHeight
     *            需求的高
     * @return 图片压缩用的InSampleSize
     */

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /****************************** 各种形式的图片压缩 *************************/
    /**
     * 将sd卡中的图片进行压缩然后返回bitmap
     *

     * @return 压缩后的图片的Bitmap
     */
    public static Bitmap decodeSampledBitmapFromFile(String ImgLocalpath,
                                                     int reqWidth, int reqHeight) {
        // 第一次解析图片时，将inJustDecodeBounds设置为true，系统就不会为图片分配内存，但是可以获取图片的大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(ImgLocalpath, options);
        // 调用calculateInSampleSize计算InSampleSize的值
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        // 使用inSampleSize再次的解析图片，这个图片将是压缩后的图片
        options.inJustDecodeBounds = false; // 注意一定要这句话，不然图片是不会显示的
        Bitmap bitmap = null;
        //初始化bitmap时，为了防止程序的崩溃，一定要记得捕获异常的。
        try {
            bitmap = BitmapFactory.decodeFile(ImgLocalpath, options);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    /**
     * 将resource中的图片进行压缩然后返回bitmap
     *

     * @return 压缩后的图片的Bitmap
     */
    public static Bitmap decodeSampledBitmapFromFile(Resources res, int resId,
                                                     int reqWidth, int reqHeight) {
        // 第一次解析图片时，将inJustDecodeBounds设置为true，系统就不会为图片分配内存，但是可以获取图片的大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用calculateInSampleSize计算InSampleSize的值
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        // 使用inSampleSize再次的解析图片，这个图片将是压缩后的图片
        options.inJustDecodeBounds = false; // 注意一定要这句话，不然图片是不会显示的
        return BitmapFactory.decodeResource(res, resId, options);
    }

    /***************************************************************************/
    /******************** 从网上获取图片后的各种返回形式 ********************************/
    /**
     * 获取图片的uri
     *
     * @param url
     *            图片的路径
     * @param cacheFile
     *            图片的缓存文件
     * @return 图片在本地的路径
     */
    public static Uri getImageUri(String url, File cacheFile) throws Exception {
        MD5 md5 = new MD5();
        File localFile; // 图片在本地的存储路径
        localFile = new File(cacheFile, md5.getMD5ofStr(url)
                + url.substring(url.lastIndexOf(".")));
        if (localFile.exists()) {
            return Uri.fromFile(localFile);
        } else {
            HttpURLConnection connection = (HttpURLConnection) new URL(url)
                    .openConnection(); // 建立连接
            // 设置参数
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == 200) {
                FileOutputStream fileOutputStream = new FileOutputStream(
                        localFile);
                InputStream inputStream = connection.getInputStream();

                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, len);
                }
                inputStream.close();
                fileOutputStream.close();
                return Uri.fromFile(localFile);
            }
        }
        return null;
    }

    /**
     * 获取图片在sdk上面的路径
     *
     * @param url
     *            图片的路径
     * @param cacheFile
     *            图片的缓存文件
     * @return 图片在本地的路径
     */
    public static String getImageLocalPath(String url, File cacheFile)
            throws Exception {
        MD5 md5 = new MD5();
        File localFile; // 图片在本地的存储路径
        localFile = new File(cacheFile, md5.getMD5ofStr(url)
                + url.substring(url.lastIndexOf(".")));
        if (localFile.exists()) {
            return localFile.toString();
        } else {
            HttpURLConnection connection = (HttpURLConnection) new URL(url)
                    .openConnection(); // 建立连接
            // 设置参数
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == 200) {
                FileOutputStream fileOutputStream = new FileOutputStream(
                        localFile);
                InputStream inputStream = connection.getInputStream();

                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, len);
                }
                inputStream.close();
                fileOutputStream.close();
                return localFile.toString();
            }
        }
        return null;
    }

    /**************************************************************************************/
    private static class AsyncImageTask extends
            AsyncTask<String, android.R.integer, String> {
        private File cacheFile;
        private ImageView imageView;
        private String url;
        private int type;
        private ProgressBar progressBar = null;

        public AsyncImageTask(File cacheFile, ImageView imageView, int type) {
            this.cacheFile = cacheFile;
            this.imageView = imageView;
            this.type = type;
        }

        public AsyncImageTask(File cacheFile, ImageView imageView, int type,
                              ProgressBar progressBar) {
            this.cacheFile = cacheFile;
            this.imageView = imageView;
            this.type = type;
            this.progressBar = progressBar;
        }

        @Override
        protected String doInBackground(String... params) {
            // 下载图片
            try {
                this.url = params[0];
                String imgLocalPath = getImageLocalPath(params[0], cacheFile);
                return imgLocalPath;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null && imageView != null
                    && imageView.getTag() == this.url) {
                int reqWidth = 0;
                int reqHeight = 0;
                switch (type) {
                    // 列表小图
                    case 1:
                        reqWidth = 80;
                        reqHeight = 80;
                        break;
                    // 大图
                    case 2:
                        reqWidth = GlobalVar.deviceWidth;
                        reqHeight = 0;
                        break;
                    // 订阅的图片
                    case 3:
                        reqWidth = GlobalVar.deviceWidth / 4;
                        reqHeight = 90;
                        break;
                    //组图的图片
                    case 4:
                        reqWidth = GlobalVar.deviceWidth - 30;
                        reqHeight = 230;
                        break;
                    case 5:
                        reqWidth = GlobalVar.deviceWidth;
                        reqHeight = GlobalVar.deviceHeight;
                        break;
                    default:
                        break;
                }
                // 根据type计算不同的reqWidth和reqHeight
                Bitmap bitmap = decodeSampledBitmapFromFile(result, reqWidth,
                        reqHeight);
                if (bitmap != null){
                    imageView.setImageBitmap(bitmap);
                }
                else
                {
                    if(type == 2 || type == 4){
                        imageView.setImageResource(R.drawable.default1);
                    }
                    else if(type == 5){
                        imageView.setTag("not found");
                        imageView.setImageResource(R.drawable.reload);
                    }
                    else {

                        imageView.setImageResource(R.drawable.ic_launcher);
                    }

                }


            } else {
                if(type == 2 || type == 4){
                    imageView.setImageResource(R.drawable.default1);
                }
                else if(type == 5){
                    imageView.setTag("not found"); //设置这个，可以用来点击图片然后重新的加载
                    imageView.setImageResource(R.drawable.reload);
                }
                else {
                    imageView.setImageResource(R.drawable.ic_launcher);
                }

            }
            if(progressBar != null){
                progressBar.setVisibility(View.INVISIBLE);
            }
        }

    }

    public static void asyncDownPic(ImageView imageView, String picUrl, File cacheFile,
                                    int type, ProgressBar progressBar) {
        AsyncImageTask asyncImageTask = new AsyncImageTask(cacheFile,
                imageView, type,progressBar);
        asyncImageTask.execute(picUrl);
    }

}

