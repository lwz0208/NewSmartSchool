package com.wust.newsmartschool.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 从文件夹中获取图片，如果图片不存在，就从网上下载，然后存入文件夹，然后在返回
 *
 * @author GuangT
 *
 */
public class ImageFileCache {
    private File CACHDIR; // 图片的缓存文件夹
    private static final int MB = 1024 * 1024;
    private static final int CACEHE_SIZE = 10; // 10M的sd卡的空间，因为空间越大，读取的数度越慢
    private static final int FREE_SD_SPACE_NEEDED_TO_CACHE = 10; // sd卡的可用存储空间
    private static final String WHOLESALE_CONV = ".cach";

    public ImageFileCache(File cahceFile) {
        this.CACHDIR = cahceFile;
        removeCache();
    }

    /**
     * 根据指定的宽高（防止内存溢出），从文件中读取缓存的图片
     *
     * @param url
     * @param requestWidth
     *            需求的图片的宽
     * @param requestHeight
     *            需求的图片的高
     * @return
     */
    public Bitmap getImage(String url, int requestWidth, int requestHeight) {
        File localfile = new File(CACHDIR, convertUrlToFileName(url));
        if (localfile.exists()) {// 如果文件存在
            String filepath = CACHDIR.toString() + "/"
                    + convertUrlToFileName(url);

            // 第一次解析图片时，将inJustDecodeBounds设置为true，系统就不会为图片分配内存，但是可以获取图片的大小
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filepath, options);
            // 调用calculateInSampleSize计算InSampleSize的值
            options.inSampleSize = calculateInSampleSize(options, requestWidth,
                    requestHeight);
            // 使用inSampleSize再次的解析图片，这个图片将是压缩后的图片
            options.inJustDecodeBounds = false; // 注意一定要这句话，不然图片是不会显示的
            Bitmap bitmap = null;
            // 初始化bitmap时，为了防止程序的崩溃，一定要记得捕获异常的。
            try {
                bitmap = BitmapFactory.decodeFile(filepath, options);
                if (bitmap == null) {
                    localfile.delete();
                } else {
                    updateFileTime(filepath);
                    return bitmap;
                }
            } catch (Exception e) {
                return null;
            }
        } else {// 文件不存在,从网上获取图片
            Bitmap bitmap = getImgFromHttp(url, requestWidth, requestHeight);
            return bitmap;
        }
        return null;
    }

    // 从网上获取图片，并存储在sd卡中
    private Bitmap getImgFromHttp(String url, int requestWidth,
                                  int requestHeight) {
        try {
            InputStream is = getHTTPConnectionInputStream(url);
            if (is != null) {
                saveBitmap(is, url);// 保存图片
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(is, null, options);
                options.inSampleSize = calculateInSampleSize(options,
                        requestWidth, requestHeight);
                options.inJustDecodeBounds = false;
                is.close();
                is = getHTTPConnectionInputStream(url);
                Bitmap bitmap = null;
                if (is != null)
                    bitmap = BitmapFactory.decodeStream(is, null, options);
                is.close();
                return bitmap;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 根据url得到inputstream
     *
     * @param url
     * @return
     */
    private InputStream getHTTPConnectionInputStream(String url) {
        HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == 200) {
                InputStream inputStream = connection.getInputStream();
                return inputStream;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

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

    private int calculateInSampleSize(BitmapFactory.Options options,
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

    /**
     * 将图片存入sd卡里面
     *
     * @param inputStream
     * @param 图片的路径
     */
    public void saveBitmap(InputStream inputStream, String url) {
        if (inputStream == null) {
            return;
        }
        // 判断sd卡上面的空间是否足够
        if (FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            // SD卡的空间不足
            return;
        }
        // 将图片存在sd卡上面
        File file = new File(CACHDIR.toString() + "/"
                + convertUrlToFileName(url));
        try {
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.close();
        } catch (Exception e) {
        }

    }

    private boolean removeCache() {
        File[] files = CACHDIR.listFiles();
        if (files == null) {
            return true;
        }
        if (!android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return false;
        }
        int dirsize = 0;
        for (int i = 0; i < files.length; i++) {
            if (files[i].getName().contains(WHOLESALE_CONV)) {
                dirsize += files[i].length();
            }
        }
        if (dirsize > CACEHE_SIZE * MB
                || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
            int removeFactor = (int) ((0.4 * files.length) + 1);
            Arrays.sort(files, new FileLastModifSort());
            for (int i = 0; i < removeFactor; i++) {
                if (files[i].getName().contains(WHOLESALE_CONV)) {
                    files[i].delete();
                }
            }
        }
        if (freeSpaceOnSd() <= CACEHE_SIZE) {
            return false;
        }
        return true;

    }

    private int freeSpaceOnSd() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory()
                .getPath());
        double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat
                .getBlockSize()) / MB;
        return (int) sdFreeMB;
    }

    private void updateFileTime(String filepath) {
        File newFile = new File(filepath);
        long newModifiedTime = System.currentTimeMillis();
        newFile.setLastModified(newModifiedTime);
    }

    private String convertUrlToFileName(String url) {
        String[] strs = url.split("/");
        return strs[strs.length - 1] + WHOLESALE_CONV;
    }

    /**
     * 根据文件的最后修改时间进行排序
     */
    private class FileLastModifSort implements Comparator<File> {
        public int compare(File arg0, File arg1) {
            if (arg0.lastModified() > arg1.lastModified()) {
                return 1;
            } else if (arg0.lastModified() == arg1.lastModified()) {
                return 0;
            } else {
                return -1;
            }
        }
    }
}

