package com.example.rsrtcs.base;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ViewDataBinding;

public abstract class BaseActivity<T> extends AppCompatActivity {

    protected T binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=getActivityBinding();
        if(binding instanceof ViewDataBinding){
            setContentView(((ViewDataBinding) binding).getRoot());
            init();
            initCtrl();
        }


    }
    protected abstract T getActivityBinding();
    protected abstract void init();
    protected abstract void initCtrl();

}
