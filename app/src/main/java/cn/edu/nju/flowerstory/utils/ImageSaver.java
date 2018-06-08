package cn.edu.nju.flowerstory.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Handler;
import android.os.Message;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static cn.edu.nju.flowerstory.app.Constants.CAMERA_RESULT;
import static cn.edu.nju.flowerstory.utils.BitmapUtil.cropImage;
import static cn.edu.nju.flowerstory.utils.BitmapUtil.sizeBitmap;

public class ImageSaver implements Runnable {

    private final Image mImage;
    private File mFileUpload;
    private Handler mUIHandler;
    private double zoom;

    public ImageSaver(Image image, File file, Handler handler, double zoom) {
        mImage = image;
        mFileUpload = file;
        mUIHandler = handler;
        this.zoom = zoom;
    }

    @Override
    public void run() {
        ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(mFileUpload);
            BitmapFactory.Options ontain = new BitmapFactory.Options();
            Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, ontain);

            int width = bm.getWidth();
            int height = bm.getHeight();

            // 设置想要的大小, 0.6为取景框的缩放补偿系数,非精确值
            int newWidth = (int)(width/(zoom+0.6));
            int newHeight = (int)(height/(zoom+0.6));

            Bitmap newbm = Bitmap.createBitmap(bm, (width-newWidth)/2, (height-newHeight)/2, newWidth, newHeight);
            Bitmap newbm1 = cropImage(newbm);
            Bitmap newbm2 = sizeBitmap(newbm1, 300, 300);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            newbm2.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            Message.obtain(mUIHandler, CAMERA_RESULT).sendToTarget();
            //Message.obtain(mUIHandler, RESULT, newbm2).sendToTarget();

            output.write(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mImage.close();
            if (null != output) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
