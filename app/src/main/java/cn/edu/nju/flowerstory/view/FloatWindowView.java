package cn.edu.nju.flowerstory.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.utils.ScreenSizeUtil;


public class FloatWindowView extends View {

    public int width;

    public int height;

    private Bitmap bitmap;

    public FloatWindowView(Context context) {
        super(context);
        init();
    }

    public FloatWindowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FloatWindowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        Bitmap src = BitmapFactory.decodeResource(getResources(), R.mipmap.camera_icon);
        // 将图片裁剪到指定大小
        width = height = ScreenSizeUtil.dp2px(50.0f);
        bitmap = Bitmap.createScaledBitmap(src, width, height, true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, null);
    }
}
