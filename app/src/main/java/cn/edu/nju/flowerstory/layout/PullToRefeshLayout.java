package cn.edu.nju.flowerstory.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Roy on 2016/12/28.
 * desc:
 */
public class PullToRefeshLayout extends LinearLayout {
    private int mHeaderOffsetTop = 0;
    private int mHeaderOffsetBottom = 0;

    private float mStratY;

    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public PullToRefeshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mHeaderOffsetTop = dip2px(context, 100);
        mHeaderOffsetBottom = dip2px(context, 100);
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
                if ((mCurrentY-mStratY<0 && tmpCur<=-670) || (mCurrentY-mStratY>=0 && tmpCur>=90)){
                    return true;
                }
                getChildAt(0).setTranslationY(cur + delta);
                getChildAt(1).setTranslationY(cur + delta);
                return true;
            case MotionEvent.ACTION_UP:
                cur += (mCurrentY - mStratY);
                if(cur<-670){
                    cur = -670;
                    getChildAt(0).setTranslationY(cur);
                    getChildAt(1).setTranslationY(cur);
                } else if (cur>0){
                    cur = 0;
                    getChildAt(0).animate().translationY(cur);
                    getChildAt(1).animate().translationY(cur);
                }
                return true;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

}
