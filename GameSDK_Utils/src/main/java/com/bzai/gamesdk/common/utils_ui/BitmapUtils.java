package com.bzai.gamesdk.common.utils_ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;

import com.bzai.gamesdk.common.utils_base.cache.ApplicationCache;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Created by bzai on 2018/5/26.
 * <p>
 * Desc:
 *
 *  处理图片Bitmap
 */

public class BitmapUtils {

    /**
     * 获取网络图片
     */
    public static Bitmap getImageBitmap(String imageUrl) {
        if (null == imageUrl) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream inputStream = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 保存图片到私有内存
     * @param bm
     * @param fileName
     * @return
     */
    public static String savePicture(Bitmap bm, String fileName){

        try {
            File file = ApplicationCache.getInstance().getApplicationContext().getExternalCacheDir(); //SDCard/Android/data/应用包名/cache/
            File pictureFile = new File(file.getPath(), fileName);
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(pictureFile));

            //处理格式：
            if (fileName.endsWith(".png") ) {
                bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
            }else if (fileName.endsWith(".jpg")){
                bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            }

            bos.flush();
            bos.close();
            return pictureFile.getAbsolutePath();

        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 读取私有目录下的指定文件
     * @return
     */
    public static Bitmap getPicture(String fileName){

        String path = ApplicationCache.getInstance().getApplicationContext().getExternalCacheDir().getPath();
        File file = new File(path,fileName);

        Bitmap bitmap = null;
        InputStream is = null;
        try {

            is = new FileInputStream(file);
            if (is != null){
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bitmap;
    }

    /**
     * 图片的缩放方法
     *
     * @param bitmap  ：源图片资源
     * @param maxSize ：图片允许最大空间  单位:KB
     * @return
     */
    public static Bitmap getZoomImage(Bitmap bitmap, double maxSize) {
        if (null == bitmap) {
            return null;
        }
        if (bitmap.isRecycled()) {
            return null;
        }

        // 单位：从 Byte 换算成 KB
        double currentSize = bitmapToByteArray(bitmap, false).length / 1024;
        // 判断bitmap占用空间是否大于允许最大空间,如果大于则压缩,小于则不压缩
        while (currentSize > maxSize) {
            // 计算bitmap的大小是maxSize的多少倍
            double multiple = currentSize / maxSize;
            // 开始压缩：将宽带和高度压缩掉对应的平方根倍
            // 1.保持新的宽度和高度，与bitmap原来的宽高比率一致
            // 2.压缩后达到了最大大小对应的新bitmap，显示效果最好
            bitmap = getZoomImage(bitmap, bitmap.getWidth() / Math.sqrt(multiple), bitmap.getHeight() / Math.sqrt(multiple));
            currentSize = bitmapToByteArray(bitmap, false).length / 1024;
        }
        return bitmap;
    }

    /**
     * 图片的缩放方法
     *
     * @param orgBitmap ：源图片资源
     * @param newWidth  ：缩放后宽度
     * @param newHeight ：缩放后高度
     * @return
     */
    public static Bitmap getZoomImage(Bitmap orgBitmap, double newWidth, double newHeight) {
        if (null == orgBitmap) {
            return null;
        }
        if (orgBitmap.isRecycled()) {
            return null;
        }
        if (newWidth <= 0 || newHeight <= 0) {
            return null;
        }

        // 获取图片的宽和高
        float width = orgBitmap.getWidth();
        float height = orgBitmap.getHeight();
        // 创建操作图片的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(orgBitmap, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

    /**
     * bitmap转换成byte数组
     *
     * @param bitmap
     * @param needRecycle
     * @return
     */
    public static byte[] bitmapToByteArray(Bitmap bitmap, boolean needRecycle) {
        if (null == bitmap) {
            return null;
        }
        if (bitmap.isRecycled()) {
            return null;
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bitmap.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.toString();
        }
        return result;
    }

    /**
     * bitmap转化为string
     * @param bitmap
     * @return
     */
    public static String bitmapToString(Bitmap bitmap){

        if (null == bitmap) {
            return null;
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);

        byte[] result = output.toByteArray();
        String temp = Base64.encodeToString(result, Base64.DEFAULT);
        try {
            output.close();
        } catch (Exception e) {
            e.toString();
        }

        return temp;

    }


    /**
     * string转成bitmap
     *
     * @param st
     */
    public static Bitmap stringToBitmap(String st)
    {

        Bitmap bitmap = null;
        try
        {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);
            return bitmap;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * 图片的大小
     * @param size
     * @return
     */
    public String getReadableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
