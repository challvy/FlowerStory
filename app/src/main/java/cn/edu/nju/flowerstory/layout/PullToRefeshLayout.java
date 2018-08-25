package cn.edu.nju.flowerstory.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import cn.edu.nju.flowerstory.utils.ScreenUtil;

/**
 * Created by Roy on 2016/12/28.
 * desc:
 */

public class PullToRefeshLayout extends LinearLayout {
    private static final String TAG = "PullToRefeshLayout";
    private int mHeaderOffsetTop = 0;
    private int mHeaderOffsetBottom = 0;

    private float mStratY;

    public PullToRefeshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHeaderOffsetTop = ScreenUtil.dip2px(context, 100);
        mHeaderOffsetBottom = ScreenUtil.dip2px(context, 100);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(sizeWidth, sizeHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int count = getChildCount();
        if (count != 2) {
            throw new RuntimeException("should have 2 child views!");
        }
        int headHeight = 0;
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            if (i == 0) {
                headHeight = childView.getMeasuredHeight();
                childView.layout(0, -mHeaderOffsetTop, childView.getMeasuredWidth(), headHeight - mHeaderOffsetTop);
            } else {
                childView.layout(0, headHeight - mHeaderOffsetTop - mHeaderOffsetBottom, childView.getMeasuredWidth(), headHeight - mHeaderOffsetTop - mHeaderOffsetBottom + childView.getMeasuredHeight());
            }
        }
    }

    float delta = 0.0f;
    float cur = 0.0f;
    float tmpCur = 0.0f;
    float mCurrentY=0.0f;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStratY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE:
                mCurrentY = event.getY();
                delta = mCurrentY - mStratY;
                tmpCur = cur + delta;
                if (mCurrentY - mStratY < 0) {
                    if(tmpCur<=-670){
                        return true;
                    }
                    getChildAt(0).setTranslationY(delta);
                    getChildAt(1).setTranslationY(delta);
                    return true;
                } else {
                    if(tmpCur>=80){
                        return true;
                    }
                    getChildAt(0).setTranslationY(cur + mCurrentY - mStratY);
                    getChildAt(1).setTranslationY(cur + mCurrentY - mStratY);
                }
                return true;
            case MotionEvent.ACTION_UP:
                cur += (mCurrentY - mStratY);
                if(cur<-670){
                    cur = -670;
                } else if (cur>0){
                    cur = 0;
                }
                getChildAt(0).animate().translationY(cur);
                getChildAt(1).animate().translationY(cur);
                return true;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

}
