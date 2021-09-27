package com.example.rsrtcs.ui.activity.splash;

import android.content.Intent;
import android.os.Handler;
import android.view.WindowManager;

import com.example.rsrtcs.UploadDocuments;
import com.example.rsrtcs.ui.activity.auth.login.LoginActivity;
import com.example.rsrtcs.base.BaseActivity;
import com.example.rsrtcs.databinding.ActivitySplashBinding;

public class SplashActivity extends BaseActivity<ActivitySplashBinding> {

    @Override
    protected ActivitySplashBinding getActivityBinding() {
        return ActivitySplashBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loadIntent();
    }

    @Override
    protected void initCtrl() { }


    private void loadIntent() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        },2500);
    }
}