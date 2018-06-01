package cn.edu.nju.flowerstory.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.Image;
import android.os.Handler;
import android.os.Message;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ImageSaver implements Runnable {

    private final Image mImage;
    private final File mFile;
    private Handler mUIHandler;
    private double zoom;
    private static final int SETIMAGE = 1;

    public ImageSaver(Image image, File file, Handler handler, double zoom) {
        mImage = image;
        mFile = file;
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
            output = new FileOutputStream(mFile);
            BitmapFactory.Options ontain = new BitmapFactory.Options();
            Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, ontain);

            int width = bm.getWidth();
            int height = bm.getHeight();

            // 设置想要的大小
            int newWidth = (int)(width/zoom);
            int newHeight = (int)(height/zoom);

            // 取得想要缩放的matrix参数
            Matrix matrix = new Matrix();

            //matrix.postScale(1/(float)zoom, 1/(float)zoom);

            Bitmap newbm = Bitmap.createBitmap(bm, (width-newWidth)/2, (height-newHeight)/2, newWidth, newHeight, matrix, true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            newbm.compress(Bitmap.CompressFormat.JPEG, 80, baos);

            Message.obtain(mUIHandler, SETIMAGE, newbm).sendToTarget();

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
