package com.example.administrator.webviewdemo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.start_webview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //有对应权限或者系统版本小于7.0
                    //开启悬浮窗
                boolean isPermission = FloatPermissionManager.getInstance().applyFloatWindow(MainActivity.this);
                //有对应权限或者系统版本小于7.0
                if (isPermission || Build.VERSION.SDK_INT < 24) {
                    //开启悬浮窗
                    FloatActionController.getInstance().startMonkServer(MainActivity.this);
                }
            }
        });

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatActionController.getInstance().stopMonkServer(MainActivity.this);
            }
        });

        findViewById(R.id.small).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatActionController.getInstance().smallServer(MainActivity.this);
            }
        });

        findViewById(R.id.big).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatActionController.getInstance().bigServer(MainActivity.this);
            }
        });

        findViewById(R.id.activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,WebViewActivity.class);
                startActivity(intent);
            }
        });
    }
}
