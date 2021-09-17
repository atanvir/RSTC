package com.example.rstc.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class CommonUtils {
    public static CustomLoader customProgressBar;
    public static boolean networkConnectionCheck(final Context context) {


        boolean isConnected = isOnline(context);
        if (!isConnected) {
            Toast.makeText(context, "Please turn on internet", Toast.LENGTH_SHORT).show();
        }
        return isConnected;


    }

    public static void showLoadingDialog(Activity activity){
        if(customProgressBar==null)
            customProgressBar = CustomLoader.show(activity, true);

        try {
            customProgressBar.setCancelable(false);
            customProgressBar.show();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public static void dismissLoadingDialog(){
        try
        {
            if (null != customProgressBar && customProgressBar.isShowing()) {
                customProgressBar.dismiss();
                customProgressBar=null;
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void showSnackBar(View view, String message){
        Snackbar.make(view,message,Snackbar.LENGTH_LONG).show();
    }


    public static boolean isOnline(Context context) {
        try {

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobile_info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifi_info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mobile_info != null) {
                if (mobile_info.isConnectedOrConnecting() || wifi_info.isConnectedOrConnecting()) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (wifi_info.isConnectedOrConnecting()) {
                    return true;
                } else {
                    return false;
                }
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println("" + e);
            return false;
        }
    }

    public Intent getPickIntent(Context context, Uri cameraOutputUri) {
        final List<Intent> intents = new ArrayList<Intent>();

        if (true) {
            intents.add(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
        }

        if (true) {
            setCameraIntents(context,intents, cameraOutputUri);
        }

        if (intents.isEmpty()) return null;
        Intent result = Intent.createChooser(intents.remove(0), null);
        if (!intents.isEmpty()) {
            result.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toArray(new Parcelable[] {}));
        }
        return result;


    }

    public  void setCameraIntents(Context context,List<Intent> cameraIntents, Uri output) {
        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = context.getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
            intent.putExtra("uri",output);
            cameraIntents.add(intent);
        }
    }

}
