package com.wust.newsmartschool.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

/**
 * 图片在内存中的缓存类
 *
 * @author GuangT
 *
 */
public class ImageMemoryCache {
    /**
     * 从内存读取数据速度是最快的，为了更大限度使用内存，这里使用了两层缓存。 硬引用缓存不会轻易被回收，用来保存常用数据，不常用的转入软引用缓存。
     */
    private static final int SOFT_CACHE_SIZE = 15; // 软引用的缓存容量
    private static LruCache<String, Bitmap> mLruCache; // 硬缓存
    private static LinkedHashMap<String, SoftReference<Bitmap>> mSoftCache; // 软引用缓存

    public ImageMemoryCache(Context context) {
        int memClass = ((ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass(); //得到apk的运行内存
        int cacheSize = 1024*1024*memClass/4 ; //硬缓存容量，为系统可用内存的1/4
        mLruCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected void entryRemoved(boolean evicted, String key,
                                        Bitmap oldValue, Bitmap newValue) {
                //硬缓存满了，根据lru算法，将最近没有使用的图片放入软缓存中
                mSoftCache.put(key, new SoftReference<Bitmap>(oldValue));
            }

            @Override
            protected int sizeOf(String key, Bitmap value) {
                if(value != null){
                    return value.getRowBytes() * value.getHeight();
                }
                else
                    return 0;
            }

        };
        mSoftCache = new LinkedHashMap<String, SoftReference<Bitmap>>(SOFT_CACHE_SIZE,0.75f,true){
            private static final long serialVersionUID = 6040103833179403725L;
            @Override
            protected boolean removeEldestEntry(
                    Entry<String, SoftReference<Bitmap>> eldest) {
                if(size() > SOFT_CACHE_SIZE){
                    return true;
                }
                return false;
            }

        };
    }
    //从缓存中获取图片
    public Bitmap getBitmapFromCache(String url, int requestWidth, int requestHeigth){
        Bitmap bitmap;
        url = url+"_"+requestWidth+"_"+requestWidth; //不同宽高的同一张图片算作是不同的图片
        synchronized (mLruCache) {
            bitmap = mLruCache.get(url);
            if(bitmap != null){
                //如果找到了，把元素移到LruCache的最前面，保证其在lru算法中最后的被删除
                mLruCache.remove(url);
                mLruCache.put(url, bitmap);
                return bitmap;
            }
        }
        //如果硬缓存中找不到，就到软缓存中去找
        synchronized (mSoftCache) {
            SoftReference<Bitmap> bitmapSoftReference = mSoftCache.get(url);
            if(bitmapSoftReference != null){
                bitmap = bitmapSoftReference.get();
                if(bitmap != null){
                    //将图片移回硬缓存
                    mLruCache.put(url, bitmap);
                    mSoftCache.remove(url);
                    return bitmap;
                }
                else{
                    mSoftCache.remove(url);
                }
            }
        }
        return null;
    }
    /**
     * 添加图片缓存
     * @param url 图片的路径
     * @param bitmap 位图
     * @param requestHeigth
     * @param requestWidth
     */
    public void addBitmapToCache(String url,Bitmap bitmap, int requestWidth, int requestHeigth){
        url = url+"_"+requestWidth+"_"+requestWidth; //不同宽高的同一张图片算作是不同的图片
        if(bitmap != null){
            synchronized (mLruCache) {
                mLruCache.put(url, bitmap);
            }
        }
    }
    public void clearCache(){
        mSoftCache.clear();
    }
}

