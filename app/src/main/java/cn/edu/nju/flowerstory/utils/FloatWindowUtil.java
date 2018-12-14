package cn.edu.nju.flowerstory.utils;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

import cn.edu.nju.flowerstory.activity.CameraActivity;
import cn.edu.nju.flowerstory.view.FloatWindowView;

/**
 * Created by AwenZeng on 2016/12/30.
 */
public class FloatWindowUtil {

    private FloatWindowView mFloatWindowView;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private Context mContext;
    private Fragment mFragment;
    private Activity mActivity;
    private ValueAnimator valueAnimator;
    private int direction;
    private final int LEFT = 0;
    private final int RIGHT = 1;

    public FloatWindowUtil(Context context, Fragment fragment, Activity activity) {
        mContext = context;
        mFragment = fragment;
        mActivity = activity;
        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    }

    @SuppressLint({"ClickableViewAccessibility", "RtlHardcoded"})
    public void showContactView() {
        mFloatWindowView = new FloatWindowView(mContext);
        mFloatWindowView.setVisibility(View.VISIBLE);
        if (layoutParams == null) {
            layoutParams = new WindowManager.LayoutParams();
            layoutParams.width = mFloatWindowView.width;
            layoutParams.height = mFloatWindowView.height;
            layoutParams.x += ScreenSizeUtil.getScreenWidth() - ScreenSizeUtil.dp2px(72);
            layoutParams.y += ScreenSizeUtil.getScreenHeight() - ScreenSizeUtil.dp2px(105);
            layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
            if (Build.VERSION.SDK_INT < 23) {
                layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            } else {
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
            }
            layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
            layoutParams.format = PixelFormat.RGBA_8888;
        }
        mFloatWindowView.setOnTouchListener(touchListener);
        mFloatWindowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, CameraActivity.class);
                mFragment.startActivity(intent);
            }
        });
        windowManager.addView(mFloatWindowView, layoutParams);
    }

    public void hideContactView() {
        if (mFloatWindowView != null) {
            mFloatWindowView.setVisibility(View.INVISIBLE);
            windowManager.removeView(mFloatWindowView);
            mFloatWindowView = null;
            stopAnim();
        }
    }

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        float startX;
        float startY;
        float moveX;
        float moveY;
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getRawX();
                    startY = event.getRawY();
                    moveX = event.getRawX();
                    moveY = event.getRawY();
                    break;
                    /*
                case MotionEvent.ACTION_MOVE:
                    float x = event.getRawX() - moveX;
                    float y = event.getRawY() - moveY;
                    layoutParams.x += x;
                    layoutParams.y += y;
                    if(layoutParams.y<150){
                        layoutParams.y=150;
                    } else if (layoutParams.y>ScreenSizeUtil.getScreenHeight() - ScreenSizeUtil.dp2px(134)){
                        layoutParams.y = ScreenSizeUtil.getScreenHeight() - ScreenSizeUtil.dp2px(134);
                    }
                    windowManager.updateViewLayout(mFloatWindowView, layoutParams);
                    moveX = event.getRawX();
                    moveY = event.getRawY();
                    break;*/
                case MotionEvent.ACTION_UP:
                    float endX = event.getRawX();
                    if (endX < ScreenSizeUtil.getScreenWidth() / 2) {
                        direction = LEFT;
                        endX = 0;
                    } else {
                        direction = RIGHT;
                        endX = ScreenSizeUtil.getScreenWidth() - mFloatWindowView.width;
                    }
                    if(moveX != startX){
                        starAnim((int) moveX, (int) endX, direction);
                    }
                    //如果初始落点与松手落点的坐标差值超过4个像素，则拦截该点击事件
                    //否则继续传递，将事件交给OnClickListener函数处理
                    if (Math.abs(startX - moveX) > 4) {
                        return true;
                    }
                    break;
            }
            return false;
        }
    };

    private void starAnim(int startX, int endX,final int direction) {
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator = null;
        }
        valueAnimator = ValueAnimator.ofInt(startX, endX);
        valueAnimator.setDuration(250);
        valueAnimator.setRepeatCount(0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if(direction == LEFT){
                    layoutParams.x = (int) animation.getAnimatedValue()- mFloatWindowView.width/2;
                }else if (direction == RIGHT){
                    layoutParams.x = (int) animation.getAnimatedValue();
                }
                if (mFloatWindowView != null) {
                    windowManager.updateViewLayout(mFloatWindowView, layoutParams);
                }
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }

    private void stopAnim() {
        if (valueAnimator != null) {
            valueAnimator.cancel();
            valueAnimator = null;
        }
    }
}
