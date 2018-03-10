package com.example.administrator.webviewdemo;

import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2017-12-29.
 */

public class FloatActionController {
    private FloatActionController() {
    }

    public static FloatActionController getInstance() {
        return LittleMonkProviderHolder.sInstance;
    }


    // 静态内部类
    private static class LittleMonkProviderHolder {
        private static final FloatActionController sInstance = new FloatActionController();
    }

    private FloatCallBack mFloatCallBack;

    /**
     * 开启服务悬浮窗
     */
    public void startMonkServer(Context context) {
        Intent intent = new Intent(context, FloatMonkService.class);
        intent.setAction("play");
        intent.putExtra("url","https://www.youtube.com/channel/UC-9-kyTW8ZkZNDHQJ6FgpwQ");
        intent.putExtra("url","https://www.youtube.com/");
//        intent.putExtra("url","https://m.youtube.com/watch?v=bTT4lmzlFuE");
        context.startService(intent);
    }

    /**
     * 关闭悬浮窗
     */
    public void stopMonkServer(Context context) {
        Intent intent = new Intent(context, FloatMonkService.class);
        context.stopService(intent);
    }

    /**
     * 缩小悬浮窗
     */
    public void smallServer(Context context) {
        Intent intent = new Intent(context, FloatMonkService.class);
        intent.setAction("small");
        context.startService(intent);
    }

    /**
     * 放大悬浮窗
     */
    public void bigServer(Context context) {
        Intent intent = new Intent(context, FloatMonkService.class);
        intent.setAction("big");
        context.startService(intent);
    }

    /**
     * 注册监听
     */
    public void registerCallLittleMonk(FloatCallBack callLittleMonk) {
        mFloatCallBack = callLittleMonk;
    }


    /**
     * 悬浮窗的显示
     */
    public void show() {
        if (mFloatCallBack == null) return;
        mFloatCallBack.show();
    }

    /**
     * 悬浮窗的隐藏
     */
    public void hide() {
        if (mFloatCallBack == null) return;
        mFloatCallBack.hide();
    }
}
