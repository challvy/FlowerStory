package cn.edu.nju.flowerstory.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import cn.edu.nju.flowerstory.app.FloatWindowApp;

public class ScreenSizeUtil {

    private static DisplayMetrics displayMetrics = getDisplayMetrics();

    public static int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
    }

    public static int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
    }

    public static int getScreenWidth() {
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeight() {
        return displayMetrics.heightPixels;
    }

    private static DisplayMetrics getDisplayMetrics() {
        WindowManager wm = (WindowManager) FloatWindowApp.getAppContext().getSystemService(Context.WINDOW_SERVICE);
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

}
