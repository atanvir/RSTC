package com.example.rstc;

import static com.example.rstc.utils.ImageUtil.checkImageSize;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.rstc.utils.CommonUtils;
import com.example.rstc.utils.ImageUtil;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImageCaptureActivity extends AppCompatActivity {

    Button btnCapture, btnBrowse, mNext,btnPrev,btnClear;
    ImageView imageView;

    public static final int CAPTURE_IMAGE = 1;
    public static final int PICK_IMAGE = 2;

    boolean isImageAdded = false;

    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    Uri imageUri2 = null;

    ProgressBar progressBar;
    String uriImage1,hex1;
    Bitmap bitmap2;

    byte[] bmImage;
    private String image="";
    private  SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_capture);


        btnCapture = findViewById(R.id.capture);
        btnBrowse = findViewById(R.id.browse);
      //  btnPrev = findViewById(R.id.prev);
        btnClear = findViewById(R.id.clear);
        mNext = findViewById(R.id.next);
        imageView = findViewById(R.id.image);
        progressBar = findViewById(R.id.simpleProgressBar);





//        pref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
//        String name = pref.getString("name", "No name defined");//"No name defined" is the default value.
//        String idName = pref.getString("middle_name", "middle_name");

//        Log.e("name : ", name);
//        Log.e("idName : ", idName);


        if (ContextCompat.checkSelfPermission(ImageCaptureActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(ImageCaptureActivity.this,new String[]{
                    Manifest.permission.CAMERA
            },100);
        }


        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageView.setImageDrawable(null);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,CAPTURE_IMAGE);
            }
        });

        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageView.setImageDrawable(null);

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);

//                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(galleryIntent,  PICK_IMAGE);
            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);


                editor.putString("hex1", image);
              //  editor.putString("uriImage",uriImage);
                editor.commit();

                if (isImageAdded){
                    Intent intent = new Intent(ImageCaptureActivity.this,PassRouteSelection.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(ImageCaptureActivity.this, "Please Select The Image!", Toast.LENGTH_SHORT).show();

                }


            }
        });

//        btnPrev.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(ImageCaptureActivity.this,MainActivity.class);
//                startActivity(intent);
//            }
//        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageView.setImageDrawable(null);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_IMAGE){
           // Uri imageUri = data.getData();

            Bitmap bitmap1 = (Bitmap) data.getExtras().get("data");
            if(checkImageSize(bitmap1)){
                imageView.setImageBitmap(bitmap1);
                image=ImageUtil.convertBaseString(bitmap1);
                isImageAdded = true;
            }else{
                Toast.makeText(ImageCaptureActivity.this, "Please upload photo upto of 2 MB", Toast.LENGTH_SHORT).show();
            }


        }else if (requestCode == PICK_IMAGE){

            Uri imageUri = data.getData();
            if (imageUri != null){
                try{
                    Bitmap bitmap=ImageUtil.getBitmapFromUri(this,imageUri);
                    if(checkImageSize(bitmap)){
                        imageView.setImageBitmap(bitmap);
                        image=ImageUtil.convertBaseString(bitmap);
                        isImageAdded = true;
                    }else{
                        Toast.makeText(ImageCaptureActivity.this, "Please upload photo upto of 2 MB", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){

                }
            }
        }
    }

    private void bitmap1ToHex(Bitmap bitmap1) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
           // bmImage = byteArray;
           // bitmap.recycle();

            StringBuilder hex = new StringBuilder(byteArray.length * 2);
            for (byte b : byteArray)
                hex.append(String.format("%02x", b));

           String uriImage = String.valueOf(hex);
            //   return hex.ToString();
        uriImage1 = uriImage;

            Log.e("hex1="+ "", String.valueOf(hex));
            Log.e("Image1 ="+ "", uriImage);
            // bitmap.recycle();

            Log.e("bitmapImageToByteArray="+ "", String.valueOf(byteArray));

            imageView.setImageBitmap(bitmap1);
         bitmap2 = bitmap1;
       // isImageAdded = true;

    }

    private void uriToHex(Uri imageUri) {

        InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.JPEG, 90, stream);
                byte[] byteArray = stream.toByteArray();
                  bmImage = byteArray;


                    float lengthbmp = byteArray.length;
                    float lengthbmp1 = lengthbmp / 1024;
                    Log.e("lengthbmp1","" + lengthbmp);
                    Log.e("lengthbmp1","" + lengthbmp1);

                    String str = String.valueOf(lengthbmp1);
                    Log.e("str = ","" + str);
                    float ff = (float) 1024.0000;
                    if (lengthbmp1 < 1024){
                        Log.e("valid1 = ","" + lengthbmp1);
                        imageView.setImageURI(imageUri);
                        new AsyncCaller().execute();

                    }else {
                        Log.e("Invalid1 = ","" + lengthbmp1);
                        Toast.makeText(this, "Please Select Image upto 1MB!", Toast.LENGTH_SHORT).show();
                    }



//                StringBuilder hex = new StringBuilder(byteArray.length * 2);
//                for (byte b : byteArray)
//                    hex.append(String.format("%02x", b));
//
//               String uriImage = String.valueOf(hex);
//                //   return hex.ToString();
//                uriImage1 = uriImage;
//
//                    Log.e("hex2="+ "", String.valueOf(hex));
//                    Log.e("uriImage="+ "", uriImage);

                }catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        progressBar.setVisibility(View.GONE);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedpreferences.edit();

        if(!sharedpreferences.getString("hex1","").equals("")){
            imageView.setImageBitmap(ImageUtil.convertBitmap(sharedpreferences.getString("hex1","")));
        }

    }

    @SuppressLint("StaticFieldLeak")
    private class AsyncCaller extends AsyncTask<Void, Void, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(ImageCaptureActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }
        @Override
        protected String doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here

            StringBuilder hex = new StringBuilder(bmImage.length * 2);
            for (byte b : bmImage)
                hex.append(String.format("%02x", b));

           String hexxx1 = String.valueOf(hex);
            hex1 = hexxx1;
            //   return hex.ToString();

         //   Log.e("hex1="+ "", String.valueOf(hex));
            Log.e("hexxx1="+ "", hexxx1);
            return hexxx1.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pdLoading.dismiss();
            //this method will be running on UI thread
            Toast.makeText(ImageCaptureActivity.this, "Data Loaded!", Toast.LENGTH_SHORT).show();
            // Toast.makeText(MainActivity.this, hexxx1, Toast.LENGTH_SHORT).show();
           // Log.e("hex111111111111111="+ "", String.valueOf(hexxx1));

        }

    }
}