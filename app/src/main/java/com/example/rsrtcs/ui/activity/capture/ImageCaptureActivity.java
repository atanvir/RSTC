package com.example.rsrtcs.ui.activity.capture;

import static com.example.rsrtcs.utils.CommonUtils.showSnackBar;
import static com.example.rsrtcs.utils.ImageUtil.checkImageSize;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.os.Build;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.rsrtcs.PassRouteSelection;
import com.example.rsrtcs.R;
import com.example.rsrtcs.base.BaseActivity;
import com.example.rsrtcs.databinding.ActivityImageCaptureBinding;
import com.example.rsrtcs.repository.cache.PrefrenceHelper;
import com.example.rsrtcs.utils.ImageUtil;

import java.io.IOException;

public class ImageCaptureActivity extends BaseActivity<ActivityImageCaptureBinding> implements View.OnClickListener {
    public static final int CAPTURE_IMAGE = 100,PICK_IMAGE=221;
    private String image="";

    @Override
    protected ActivityImageCaptureBinding getActivityBinding() {
        return ActivityImageCaptureBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
    }

    @Override
    protected void initCtrl() {
        binding.clear.setOnClickListener(this);
        binding.next.setOnClickListener(this);
        binding.capture.setOnClickListener(this);
        binding.browse.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CAPTURE_IMAGE: loadImage((Bitmap) data.getExtras().get("data")); break;
            case PICK_IMAGE: try { loadImage(ImageUtil.getBitmapFromUri(this,data.getData())); } catch (IOException e) { e.printStackTrace(); } break;
        }
    }

    private void loadImage(Bitmap bitmap) {
        if(checkImageSize(bitmap)){
            binding.image.setImageBitmap(bitmap);
            image=ImageUtil.convertBaseString(bitmap);
        }else{ Toast.makeText(ImageCaptureActivity.this, "Please upload photo upto of 2 MB", Toast.LENGTH_SHORT).show(); }

    }


    @Override
    protected void onResume() {
        super.onResume();
        image=PrefrenceHelper.getPrefrenceStringValue(this,"hex1");
        if(!image.equals("")) {
            binding.image.setImageBitmap(ImageUtil.convertBitmap(image));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.capture:
            binding.image.setImageDrawable(null);
            if(checkPermission(CAPTURE_IMAGE)) startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),CAPTURE_IMAGE);
            break;

            case R.id.browse:
            binding.image.setImageDrawable(null);
            if(checkPermission(PICK_IMAGE)){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);}
            break;

            case R.id.clear:  binding.image.setImageDrawable(null); break;

            case R.id.next:
            if (image!=null){
                PrefrenceHelper.writePrefrencesValue(this).putString("hex1",image).apply();
                startActivity(new Intent(ImageCaptureActivity.this, PassRouteSelection.class));
            }
            else Toast.makeText(ImageCaptureActivity.this, "Please Select The Image!", Toast.LENGTH_SHORT).show();
            break;
        }
    }

    private boolean checkPermission(int requestCode) {
        boolean ret=true;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ) {
                ret = false;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, requestCode);
            }
        }

        return  ret;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean isPermissionDenied=false;
        for(int i=0;i<grantResults.length;i++)
        {
            if(!(grantResults[i]==PackageManager.PERMISSION_GRANTED))
            {isPermissionDenied=true;
                break;
            }
        }
        if(!isPermissionDenied)
        {
            switch (requestCode){
                case CAPTURE_IMAGE: binding.capture.performClick(); break;
                case PICK_IMAGE: binding.browse.performClick(); break;
            }
        }else{
            showSnackBar(binding.getRoot(),"Please allow permission");
        }
    }


}