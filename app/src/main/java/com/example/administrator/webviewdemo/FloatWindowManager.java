package com.example.administrator.webviewdemo;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by Administrator on 2017-12-29.
 */

public class FloatWindowManager {
    private static FloatLayout mFloatLayout;
    private static WindowManager mWindowManager;
    private static WindowManager.LayoutParams wmParams;
    private static boolean mHasShown;
    private static int screenWidth;
    private static int screenHeight;


    public static void createFloatWindow(Context context) {
        wmParams = new WindowManager.LayoutParams();
        WindowManager windowManager = getWindowManager(context);
        mFloatLayout = new FloatLayout(context);
//        if (Build.VERSION.SDK_INT >= 24) {
//            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
//        } else {
//            String packname = context.getPackageName();
//            PackageManager pm = context.getPackageManager();
//            boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.SYSTEM_ALERT_WINDOW", packname));
//            if (permission) {
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
//            } else {
//                wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
//            }
//        }

        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGB_565;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.START | Gravity.TOP;

        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        //窗口的宽度
        screenWidth = dm.widthPixels;
        //窗口高度
        screenHeight = dm.heightPixels;

        //设置悬浮窗口长宽数据
        wmParams.width = screenWidth - 200;
        wmParams.height = screenHeight -300;

        //以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 100;
        wmParams.y = 150;
        mFloatLayout.setParams(wmParams);
        windowManager.addView(mFloatLayout, wmParams);
        mHasShown = true;
    }

    /**
     * 返回当前已创建的WindowManager。
     */
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    /**
     * 移除悬浮窗
     */
    public static void removeFloatWindowManager() {
        //移除悬浮窗口
        boolean isAttach = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            isAttach = mFloatLayout.isAttachedToWindow();
        }
        if (mHasShown && isAttach && mWindowManager != null)
            mWindowManager.removeView(mFloatLayout);
    }

    public static void hide() {
        if (mHasShown)
            mWindowManager.removeViewImmediate(mFloatLayout);
        mHasShown = false;
    }

    public static void show() {
        if (!mHasShown)
            mWindowManager.addView(mFloatLayout, wmParams);
        mHasShown = true;
    }

    public static void load(String url){
        mFloatLayout.loadUrl(url);
    }

    public static void willSmall(Context context) {
        wmParams.width = 550;
        wmParams.height = 400;
        mFloatLayout.setParams(wmParams);
        getWindowManager(context).updateViewLayout(mFloatLayout, wmParams);
        mFloatLayout.willSmall();
        mHasShown = true;
    }

    public static void willBig(Context context) {
        //设置悬浮窗口长宽数据
        wmParams.width = screenWidth - 200;
        wmParams.height = screenHeight -300;

        //以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 100;
        wmParams.y = 150;
        mFloatLayout.setParams(wmParams);
        getWindowManager(context).updateViewLayout(mFloatLayout, wmParams);
        mFloatLayout.willBig();
        mHasShown = true;
    }

    public static void fullSrceen(Context context){
        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmParams.height = WindowManager.LayoutParams.MATCH_PARENT;
//        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
//        wmParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
        mFloatLayout.setParams(wmParams);
        getWindowManager(context).updateViewLayout(mFloatLayout, wmParams);
        mHasShown = true;
    }
}
