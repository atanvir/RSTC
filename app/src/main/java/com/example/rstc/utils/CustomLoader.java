package com.example.rstc.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.example.rstc.R;

public class CustomLoader extends Dialog {

    public CustomLoader(@NonNull Context context, int theme) {
        super(context);
    }

    public static CustomLoader show(Context context, boolean cancelable) {
        final CustomLoader dialog = new CustomLoader(context, android.R.style.Theme_Black);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.layout_loader);
        dialog.setCancelable(cancelable);
        dialog.show();
        return dialog;
    }
}