package cn.edu.nju.flowerstory.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

import static cn.edu.nju.flowerstory.app.Constants.BITMAP_SCALE;

public class BitmapUtil {

    private static final String TAG = "BitmapUtil";

    public static File saveBitmap(Bitmap b, String absolutePath, int quality) {
        return saveBitmap(b, absolutePath, quality, Bitmap.CompressFormat.JPEG);
    }

    public static File saveBitmap(Bitmap b, String absolutePath, int quality, Bitmap.CompressFormat format) {
        File f = new File(absolutePath);
        try {
            FileOutputStream fOut = new FileOutputStream(f);
            b.compress(format, quality, fOut);
            fOut.close();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return f;
    }

    public static Bitmap createCaptureBitmap(String filepath) {
        int scale = 1;
        try {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filepath, options);
            int IMAGE_MAX_SIZE = 800;
            if (options.outWidth > IMAGE_MAX_SIZE || options.outHeight > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(options.outHeight, options.outWidth)) / Math.log(0.5)));
            }
            Options opt = new Options();
            opt.inSampleSize = scale;
            return BitmapFactory.decodeFile(filepath, opt);
        } catch (OutOfMemoryError e) {
            Log.e("memory", "createCaptureBitmap out of memory");
            scale = scale * 2;
            try {
                Options opt = new Options();
                opt.inSampleSize = scale;
                return BitmapFactory.decodeFile(filepath, opt);
            } catch (OutOfMemoryError oe) {
                Log.e("memory", "createCaptureBitmap out of memory second");
                return null;
            }
        }
    }

    /**
     * 从中心点   按正方形裁切图片  边长为小边
     */
    public static Bitmap cropImage(Bitmap bitmap) {
        int w = bitmap.getWidth(); // 得到图片的宽，高
        int h = bitmap.getHeight();
        int wh = w > h ? h : w;// 裁切后所取的正方形区域边长
        int centerX = w / 2;
        int centerY = h / 2;
        Bitmap outBitmap = Bitmap.createBitmap(bitmap, centerX - wh / 2, centerY - wh / 2, wh, wh);
        bitmap.recycle();
        return outBitmap;
    }

    public static Bitmap sizeBitmap(Bitmap origin, int newWidth, int newHeight) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        return newBM;
    }

    /**
     * 模糊图片的具体方法
     *
     * @param context 上下文对象
     * @param image   需要模糊的图片
     * @return 模糊处理后的图片
     */
    public static Bitmap blurBitmap(Context context, Bitmap image, float blurRadius) {
        // 计算图片缩小后的长宽
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        // 将缩小后的图片做为预渲染的图片
        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        // 创建一张渲染后的输出图片
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        // 创建RenderScript内核对象
        RenderScript rs = RenderScript.create(context);
        // 创建一个模糊效果的RenderScript的工具对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        // 设置渲染的模糊程度, 25f是最大模糊度
        blurScript.setRadius(blurRadius);
        // 设置blurScript对象的输入内存
        blurScript.setInput(tmpIn);
        // 将输出数据保存到输出内存中
        blurScript.forEach(tmpOut);

        // 将数据填充到Allocation中
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

}
