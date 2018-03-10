package com.example.administrator.webviewdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Administrator on 2017-12-29.
 */

public class FloatMonkService extends Service implements FloatCallBack{

    private static final String TAG = "FloatMonkService";
//    private HomeWatcherReceiver mHomeKeyReceiver;
    @Override
    public void onCreate() {
        super.onCreate();
        FloatActionController.getInstance().registerCallLittleMonk(this);
//        //注册广播接收者
//        mHomeKeyReceiver = new HomeWatcherReceiver();
//        final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
//        registerReceiver(mHomeKeyReceiver, homeFilter);
        //初始化悬浮窗UI
        initWindowData();
        Log.d(TAG, "onCreate: ");
    }

    private void initWindowData() {
        FloatWindowManager.createFloatWindow(this.getApplicationContext());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
            if (intent.getAction().equals("play")) {
                if (!TextUtils.isEmpty(intent.getStringExtra("url"))) {
                    FloatWindowManager.load(intent.getStringExtra("url"));
                }
            } else if (intent.getAction().equals("small")) {
                FloatWindowManager.willSmall(this);
            } else if (intent.getAction().equals("big")) {
                FloatWindowManager.willBig(this);
            }
        }
        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //移除悬浮窗
        FloatWindowManager.removeFloatWindowManager();
        Log.d(TAG, "onDestroy: ");
        //注销广播接收者
//        if (null != mHomeKeyReceiver) {
//            unregisterReceiver(mHomeKeyReceiver);
//        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void show() {
        FloatWindowManager.show();
    }

    @Override
    public void hide() {
        FloatWindowManager.hide();
    }
}
