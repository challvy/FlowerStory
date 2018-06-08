package cn.edu.nju.flowerstory.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.utils.DisplayUtil;

@SuppressLint("AppCompatCustomView")
public class OverlayerView extends ImageView {
    private static final String TAG = OverlayerView.class.getSimpleName();

    private Paint mLinePaint;
    private Paint mAreaPaint;
    private Rect mCenterRect = null;
    private Context mContext;
    private Paint paint;
    private int widthScreen, heightScreen;

    public OverlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
        mContext = context;
        Point p = DisplayUtil.getScreenMetrics(mContext);
        widthScreen = p.x;
        heightScreen = p.y;
    }

    private void initPaint() {
        // 绘制中间透明区域矩形边界的Paint
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.WHITE);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(5f);
        mLinePaint.setAlpha(255);

        // 绘制四周阴影区域 的画笔
        mAreaPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mAreaPaint.setColor(Color.BLACK);
        mAreaPaint.setStyle(Paint.Style.FILL);
        mAreaPaint.setAlpha(128);
        paint = new Paint();

    }

    @SuppressLint("DrawAllocation")
    @Override
    public void onDraw(Canvas canvas) {
        mCenterRect = new Rect(widthScreen/2-350,heightScreen/2-600,widthScreen/2+350,heightScreen/2+100);
        super.onDraw(canvas);
        if (mCenterRect == null) {
            return;
        }

        canvas.drawRect(0, 0, widthScreen, mCenterRect.top - 2, mAreaPaint);
        canvas.drawRect(0, mCenterRect.bottom + 2, widthScreen, heightScreen, mAreaPaint);
        canvas.drawRect(0, mCenterRect.top - 2, mCenterRect.left - 2, mCenterRect.bottom + 2, mAreaPaint);
        canvas.drawRect(mCenterRect.right + 2, mCenterRect.top - 2, widthScreen, mCenterRect.bottom + 2, mAreaPaint);
        paint.setColor(getResources().getColor(R.color.colorPrimary));
        paint.setAlpha(255);

        canvas.drawRect(mCenterRect.left - 10, mCenterRect.bottom, mCenterRect.left + 50, mCenterRect.bottom + 10, paint);// 左下 底部
        canvas.drawRect(mCenterRect.left - 10, mCenterRect.bottom - 50, mCenterRect.left, mCenterRect.bottom, paint);// 左下 左侧
        canvas.drawRect(mCenterRect.right - 50, mCenterRect.bottom, mCenterRect.right + 10, mCenterRect.bottom + 10, paint);// 右下 右侧
        canvas.drawRect(mCenterRect.right, mCenterRect.bottom - 50, mCenterRect.right + 10, mCenterRect.bottom, paint);// 右下 底部
        canvas.drawRect(mCenterRect.left - 10, mCenterRect.top - 10, mCenterRect.left + 50, mCenterRect.top, paint);// 左上 顶部
        canvas.drawRect(mCenterRect.left - 10, mCenterRect.top, mCenterRect.left, mCenterRect.top + 50, paint);// 左上 侧边
        canvas.drawRect(mCenterRect.right - 50, mCenterRect.top - 10, mCenterRect.right + 10, mCenterRect.top, paint);// 右上 顶部
        canvas.drawRect(mCenterRect.right, mCenterRect.top, mCenterRect.right + 10, mCenterRect.top + 50, paint);// 右上 右侧

        // 绘制目标透明区域
        canvas.drawRect(mCenterRect, mLinePaint);
    }

    public void post() {
        postInvalidate();
    }
}
