package com.example.administrator.webviewdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2017-12-29.
 */

public class FloatLayout extends FrameLayout {

    private WindowManager mWindowManager;
    private long startTime;
    private float mTouchStartX;
    private float mTouchStartY;
    private boolean isclick;
    private VideoEnabledWebView webView;
    private WindowManager.LayoutParams mWmParams;
    private Context mContext;
    private long endTime;
    private RelativeLayout teb;
    private String state;
    private View mask;
    private Handler mHandler;
    private RelativeLayout fullScreenLayout;
    private VideoEnabledWebChromeClient chromeClient;
    private LinearLayout bigLayout;
    private static final String BIG_STATE = "big_state";
    private static final String SMALL_STATE = "small_state";
    private static final String FULL_STATE = "full_state";
    private static int time = 5000;

    public FloatLayout(@NonNull Context context) {
        this(context,null);
    }

    public FloatLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public FloatLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWindow(context);
        initHandler();
        mHandler.sendEmptyMessageDelayed(1,time);
    }


    private void initHandler() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 1){
                    autoSmall();
                }
                return true;
            }
        });
    }

    private void autoSmall() {
        FloatWindowManager.willSmall(mContext);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWindow(@NonNull final Context context) {
        mContext = context;
        state = BIG_STATE;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.activity_web_view,this);
        fullScreenLayout = findViewById(R.id.videoLayout);
        webView = findViewById(R.id.web_view);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        bigLayout = findViewById(R.id.big_web_view_layout);
        View loadingView = LayoutInflater.from(context).inflate(R.layout.view_loading_video,null);
//        chromeClient = new VideoEnabledWebChromeClient(bigLayout,fullScreenLayout,loadingView,webView);
//        chromeClient.setOnToggledFullscreen(new VideoEnabledWebChromeClient.ToggledFullscreenCallback() {
//            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//            @Override
//            public void toggledFullscreen(boolean fullscreen) {
//                if (mHandler != null) {
//                    mHandler.removeMessages(1);
//                }
//                // Your code to handle the full-screen change, for example showing and hiding the title bar. Example:
//                if (fullscreen) {
//                    WindowManager.LayoutParams attrs = (WindowManager.LayoutParams) getLayoutParams();
//                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
//                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
//                    attrs.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
//                    setParams(attrs);
//                    setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
//                    setSystemUiVisibility(View.INVISIBLE);
//                    FloatWindowManager.fullSrceen(context);
//                    state = FULL_STATE;
//                }
//                else {
//                    WindowManager.LayoutParams attrs = (WindowManager.LayoutParams) getLayoutParams();
//                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
//                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
//                    attrs.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
//                    setParams(attrs);
//                    FloatWindowManager.willBig(context);
//                    state = BIG_STATE;
//                }
//
//            }
//        });

//        webView.setWebViewClient(new WebViewClient(){
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                view.loadUrl(String.valueOf(request.getUrl()));
//                return true;
//            }
//        });

        Button close = findViewById(R.id.close_float);
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FloatMonkService.class);
                context.stopService(intent);
//                FloatWindowManager.hide();
            }
        });

        teb = findViewById(R.id.teb);
        mask = findViewById(R.id.mask);
        mask.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatWindowManager.willBig(mContext);
            }
        });

        findViewById(R.id.go_small).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatWindowManager.willSmall(mContext);
            }
        });
//        webView.setWebChromeClient(chromeClient);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获取相对屏幕的坐标，即以屏幕左上角为原点
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        //下面的这些事件，跟图标的移动无关，为了区分开拖动和点击事件
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //图标移动的逻辑在这里
                float mMoveStartX = event.getX();
                float mMoveStartY = event.getY();
                // 如果移动量大于3才移动
                if (Math.abs(mTouchStartX - mMoveStartX) > 3
                        && Math.abs(mTouchStartY - mMoveStartY) > 3) {
                    // 更新浮动窗口位置参数
                    mWmParams.x = (int) (x - mTouchStartX);
                    mWmParams.y = (int) (y - mTouchStartY - getStateHeight(mContext));
                    mWindowManager.updateViewLayout(this, mWmParams);
                    return true;
                }
            case MotionEvent.ACTION_UP:
                endTime = System.currentTimeMillis();
                //当从点击到弹起小于半秒的时候,则判断为点击,如果超过则不响应点击事件
                if ((endTime - startTime) > 0.1 * 1000L) {
                    isclick = false;
                } else {
                    isclick = true;
                }
                break;
        }
        return true;
    }

    public void setParams(WindowManager.LayoutParams params) {
        mWmParams = params;
    }

    public void loadUrl(String url){
        if (webView != null){
            webView.loadUrl(url);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (state.equals(BIG_STATE) && mHandler != null) {
            mHandler.removeMessages(1);
            mHandler.sendEmptyMessageDelayed(1, time);
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE){
            if (BIG_STATE.equals(state)){
                if (ev.getY() <= 40) {
                    return true;
                } else {
                    return false;
                }
            } else if (SMALL_STATE.equals(state)){
                float mMoveStartX = ev.getX();
                float mMoveStartY = ev.getY();
                if (Math.abs(mTouchStartX - mMoveStartX) > 3
                        && Math.abs(mTouchStartY - mMoveStartY) > 3) {
                    return true;
                }
            }
        } else if (ev.getAction() == MotionEvent.ACTION_DOWN){
            mTouchStartX = ev.getX();
            mTouchStartY = ev.getY();
            return false;
        }
        return false;
    }

    public static int getStateHeight(Context context) {
        int statusBarHeight1 = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight1 = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public void willSmall() {
//        webView.loadUrl("javascript:scrollTo(0,46)");
        if (BIG_STATE.equals(state)) {
            webView.scrollTo(0, 265);
            teb.setVisibility(GONE);
            mask.setVisibility(VISIBLE);
            mask.setClickable(true);
            state = SMALL_STATE;
        }
    }

    public void willBig() {

        if (mHandler != null) {
            mHandler.removeMessages(1);
            mHandler.sendEmptyMessageDelayed(1, time);
        }
        if (SMALL_STATE.equals(state)) {
            teb.setVisibility(VISIBLE);
            mask.setVisibility(GONE);
            mask.setClickable(false);
            state = BIG_STATE;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mHandler != null) {
            mHandler.removeMessages(1);
            mHandler = null;
        }
    }
}
