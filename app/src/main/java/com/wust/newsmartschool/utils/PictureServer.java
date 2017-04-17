package com.wust.newsmartschool.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;

public class PictureServer
{
    private Context context;
    private File cacheFile;
    public PictureServer(Context context,File cacheFile)
    {
        this.context = context;
        this.cacheFile = cacheFile;
    }

    public void asyncDownPic(ImageView imageView, String picUrl,
                             File cacheFile, int requestWidth, int requestHeigth,
                             Drawable drawable, ProgressBar progressBar)
    {
        AsyncImageTask asyncImageTask = new AsyncImageTask(cacheFile,
                imageView, requestWidth, requestHeigth, drawable, progressBar);
        asyncImageTask.execute(picUrl);
    }

    public void asyncDownPic2(ImageView imageView, String picUrl,
                              File cacheFile, int requestWidth, int requestHeigth,
                              Drawable drawable, ProgressBar progressBar,ImageView.ScaleType type)
    {
        AsyncImageTask asyncImageTask = new AsyncImageTask(cacheFile,
                imageView, requestWidth, requestHeigth, drawable, progressBar,type);
        asyncImageTask.execute(picUrl);
    }

    public Bitmap getImage(String url, int requestWidth,
                           int requestHeigth)
    {
        ImageMemoryCache memoryCache = new ImageMemoryCache(context);
        ImageFileCache fileCache = new ImageFileCache(this.cacheFile);
        //从内存中获取图片
        Bitmap bitmap = memoryCache.getBitmapFromCache(url,requestWidth,requestHeigth);
        if(bitmap == null)
        {
            //从文件缓存中获取图片
            bitmap = fileCache.getImage(url, requestWidth, requestHeigth);
            if(bitmap != null)
                memoryCache.addBitmapToCache(url, bitmap,requestWidth,requestHeigth);
        }
        return bitmap;
    }

    /**************************************************************************************/
    private class AsyncImageTask extends AsyncTask<String, android.R.integer, Bitmap>
    {
        private File cacheFile;
        private ImageView imageView;
        private String url;
        private int requestWidth;
        private int requestHeigth;
        private ProgressBar progressBar;
        private Drawable drawable;
        private ImageView.ScaleType type;

        public AsyncImageTask(File cacheFile, ImageView imageView,
                              int requestWidth, int requestHeigth, Drawable drawable,
                              ProgressBar progressBar)
        {
            this.cacheFile = cacheFile;
            this.imageView = imageView;
            this.requestHeigth = requestHeigth;
            this.requestWidth = requestWidth;
            this.drawable = drawable;
            this.progressBar = progressBar;
        }
        public AsyncImageTask(File cacheFile, ImageView imageView,
                              int requestWidth, int requestHeigth, Drawable drawable,
                              ProgressBar progressBar,ImageView.ScaleType type)
        {
            this.cacheFile = cacheFile;
            this.imageView = imageView;
            this.requestHeigth = requestHeigth;
            this.requestWidth = requestWidth;
            this.drawable = drawable;
            this.progressBar = progressBar;
            this.type = type;
        }

        @Override
        protected Bitmap doInBackground(String... params)
        {
            // 下载图片
            try
            {
                this.url = params[0];
                Bitmap bitmap = getImage(params[0], this.requestWidth,
                        this.requestHeigth);
                return bitmap;
            }
            catch (Exception e)
            {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap)
        {
            if (bitmap != null && imageView != null
                    && imageView.getTag().toString().equals(this.url))
            {
                if(this.type != null)
                {
                    imageView.setScaleType(type);
                }
                imageView.setImageBitmap(bitmap);
                if (this.progressBar != null)
                {
                    this.progressBar.setVisibility(View.INVISIBLE);
                }

            }
            // else 图片下载没有成功，有默认的图片就加载默认的图片，有progressBar就隐藏
            else
            {
                if (this.drawable != null)
                {
                    imageView.setImageDrawable(this.drawable);
                }
                if (this.progressBar != null)
                {
                    this.progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }

    }
}

