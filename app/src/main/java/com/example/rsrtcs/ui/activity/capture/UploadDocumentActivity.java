package com.example.rsrtcs.ui.activity.capture;

import static com.example.rsrtcs.utils.CommonUtils.dismissLoadingDialog;
import static com.example.rsrtcs.utils.CommonUtils.showLoadingDialog;
import static com.example.rsrtcs.utils.CommonUtils.showSnackBar;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import com.bumptech.glide.Glide;
import com.example.rsrtcs.BuildConfig;
import com.example.rsrtcs.R;
import com.example.rsrtcs.base.BaseActivity;

import com.example.rsrtcs.databinding.ActivityUploadDocumentsBinding;
import com.example.rsrtcs.model.request.SMSModel;
import com.example.rsrtcs.model.request.SpinnerDataModel;
import com.example.rsrtcs.model.response.RegistrationModel;
import com.example.rsrtcs.repository.cache.PrefrenceHelper;
import com.example.rsrtcs.repository.cache.PrefrenceKeyConstant;
import com.example.rsrtcs.repository.remote.RSRTCConnection;
import com.example.rsrtcs.repository.remote.RSRTCInterface;
import com.example.rsrtcs.ui.activity.main.MainActivity;
import com.example.rsrtcs.utils.CommonUtils;
import com.example.rsrtcs.utils.FilePath;
import com.example.rsrtcs.utils.ImageUtil;
import com.example.rsrtcs.utils.RegisterationDataHelper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadDocumentActivity extends BaseActivity<ActivityUploadDocumentsBinding> implements AdapterView.OnItemSelectedListener, View.OnClickListener, Callback<List<RegistrationModel>> {

    private RSRTCInterface apiInterface= new RSRTCConnection().createService();
    private RSRTCInterface smsApiInterface= new RSRTCConnection().createSMSService();

    private final int PROOF_ID_CODE=1,CONCESSION_CODE=2,ADDRESS_PROOF_CODE=3;
    private String path;

    @Override
    protected ActivityUploadDocumentsBinding getActivityBinding() {
        return ActivityUploadDocumentsBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        CommonUtils.setSpinner(binding.spinnerPhotoId,R.array.Concession_Proof);
        CommonUtils.setSpinner(binding.spinnerConcession,R.array.Concession_Proof);
        CommonUtils.setSpinner(binding.spinnerAddressProof,R.array.Addresss_Proof);
    }

    @Override
    protected void initCtrl() {
        binding.spinnerPhotoId.setOnItemSelectedListener(this);
        binding.spinnerConcession.setOnItemSelectedListener(this);
        binding.spinnerAddressProof.setOnItemSelectedListener(this);
        binding.textView1.setOnClickListener(this);
        binding.textView2.setOnClickListener(this);
        binding.textView3.setOnClickListener(this);
        binding.btnRegister.setOnClickListener(this);
        binding.btnBack.setOnClickListener(this);
        binding.btnClear.setOnClickListener(this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
        if (data != null) path = FilePath.getPath(this, Uri.parse(data.getDataString()));
        Bitmap bitmap= Compressor.getDefault(this).compressToBitmap(new File(path));

        switch (requestCode) {
            case PROOF_ID_CODE:
                if (ImageUtil.checkImageSize(bitmap)) {
                    RegisterationDataHelper.getInstance().getApplicationData().setPhotoIDProofData(ImageUtil.convertBaseString(bitmap));
                    binding.textView1.setText("Selected");
                    Glide.with(UploadDocumentActivity.this).load(bitmap).into(binding.ivOne);
                    binding.ivOne.setVisibility(View.VISIBLE);
                } else {
                    showSnackBar(binding.getRoot(), "Please upload photo upto 2 MB");
                }
                break;
            case CONCESSION_CODE:
                if (ImageUtil.checkImageSize(bitmap)) {
                    RegisterationDataHelper.getInstance().getApplicationData().setConcessionApplicableDocumentProofFileData(ImageUtil.convertBaseString(bitmap));
                    binding.textView2.setText("Selected");
                    Glide.with(UploadDocumentActivity.this).load(bitmap).into(binding.ivTwo);
                    binding.ivTwo.setVisibility(View.VISIBLE);
                } else {
                    showSnackBar(binding.getRoot(), "Please upload photo upto 2 MB");
                }
                break;
            case ADDRESS_PROOF_CODE:
                if (ImageUtil.checkImageSize(bitmap)) {
                    RegisterationDataHelper.getInstance().getApplicationData().setAddressProofFileData(ImageUtil.convertBaseString(bitmap));
                    binding.textView3.setText("Selected");
                    Glide.with(UploadDocumentActivity.this).load(bitmap).into(binding.ivThree);
                    binding.ivThree.setVisibility(View.VISIBLE);
                } else {
                    showSnackBar(binding.getRoot(), "Please upload photo upto 2 MB");
                }
                break;
        }
        }

    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
        case R.id.spinner_photo_id : RegisterationDataHelper.getInstance().getApplicationData().setPhotoIDProofID(""+position); break;
        case R.id.spinner_concession : RegisterationDataHelper.getInstance().getApplicationData().setConcessionApplicableDocumentProofID(""+position); break;
        case R.id.spinner_addressProof : RegisterationDataHelper.getInstance().getApplicationData().setAddressProofID(""+position); break;
      }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_view1:
            if (binding.spinnerPhotoId.getSelectedItemPosition() == 0) showSnackBar(binding.getRoot(),"Please Select Proof ID!");
            else { if(checkPermission(PROOF_ID_CODE)) takePhotoIntent(PROOF_ID_CODE); }
            break;
            case R.id.text_view2:
            if (binding.spinnerConcession.getSelectedItemPosition() == 0) showSnackBar(binding.getRoot(),"Please Select Concession Proof!");
            else { if(checkPermission(CONCESSION_CODE)) takePhotoIntent(CONCESSION_CODE); }
            break;
            case R.id.text_view3:
            if (binding.spinnerAddressProof.getSelectedItemPosition() == 0) showSnackBar(binding.getRoot(),"Please Select Address Proof!");
            else { if(checkPermission(ADDRESS_PROOF_CODE)) takePhotoIntent(ADDRESS_PROOF_CODE); }
            break;

            case R.id.btn_back: onBackPressed(); break;
            case R.id.btn_clear:
            binding.textView1.setText("Choose File");
            binding.textView2.setText("Choose File");
            binding.textView3.setText("Choose File");
            binding.btnRegister.setText("Register");
            binding.ivOne.setVisibility(View.GONE);
            binding.ivTwo.setVisibility(View.GONE);
            binding.ivThree.setVisibility(View.GONE);
            RegisterationDataHelper.getInstance().getApplicationData().setConcessionApplicableDocumentProofFileData("");
            RegisterationDataHelper.getInstance().getApplicationData().setAddressProofFileData("");
            RegisterationDataHelper.getInstance().getApplicationData().setPhotoIDProofData("");
            break;


            case R.id.btn_register:
            if(binding.btnRegister.getText().toString().equals("Register")){
                if (binding.spinnerPhotoId.getSelectedItemPosition() > 0 &&
                    binding.spinnerConcession.getSelectedItemPosition() > 0 &&
                    binding.spinnerAddressProof.getSelectedItemPosition() > 0 &&
                    !RegisterationDataHelper.getInstance().getApplicationData().getAddressProofFileData().equals("") &&
                    !RegisterationDataHelper.getInstance().getApplicationData().getConcessionApplicableDocumentProofFileData().equals("") &&
                    !RegisterationDataHelper.getInstance().getApplicationData().getPhotoIDProofData().equals(""))
                {
                    showLoadingDialog(this);

                    apiInterface.saveRegistration(RegisterationDataHelper.getInstance().getApplicationData()).enqueue(this);
                }

                else {showSnackBar(binding.getRoot(),"Please Select Proof Details!");}
            }
            break;
        }
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
            takePhotoIntent(requestCode);
        }
        }


    private void takePhotoIntent(int code) {
        String capture_dir= Environment.getExternalStorageDirectory() + "/RSRTC/Images/";
        File file = new File(capture_dir);
        if (!file.exists()) file.mkdirs();
        path = capture_dir + System.currentTimeMillis() + ".jpg";
        Uri imageFileUri = FileProvider.getUriForFile(Objects.requireNonNull(this.getApplicationContext()), BuildConfig.APPLICATION_ID + ".provider", new File(path));
        Intent intent = new CommonUtils().getPickIntent(this,imageFileUri);
        startActivityForResult(intent, code);
    }

    @Override
    public void onResponse(Call<List<RegistrationModel>> call, Response<List<RegistrationModel>> response) {
        dismissLoadingDialog();
        if(response.isSuccessful()){
            for(int i=0;i<response.body().size();i++){
                String outMsg=response.body().get(i).getOutMsg();
                String appId=response.body().get(i).getAppId();

                smsApiInterface.sendSMS(new SMSModel("Dear"+ PrefrenceHelper.getPrefrenceStringValue(this, PrefrenceKeyConstant.FULL_NAME) +", your RFID application no "+response.body().get(i).getAppId()+" is successfully registered with RSRTC.You will get confirmation when RSRTC Approve your application.RSRTCR",PrefrenceHelper.getPrefrenceStringValue(this,PrefrenceKeyConstant.PHONE_NO))).enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        dismissLoadingDialog();
                        if(response.isSuccessful()) showDialog(outMsg,appId);
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        dismissLoadingDialog();
                        showSnackBar(binding.getRoot(),t.getMessage());
                    }
                });
                break;
            }

        }else{
            showSnackBar(binding.getRoot(),getString(R.string.internal_server_error));
        }
    }

    @Override
    public void onFailure(Call<List<RegistrationModel>> call, Throwable t) {
        dismissLoadingDialog();
        showSnackBar(binding.getRoot(),t.getMessage());
    }

    private void showDialog(String message,String appId){
        new AlertDialog.Builder(this)
                .setTitle(message)
                .setMessage("Your Application id is "+appId)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(new Intent(UploadDocumentActivity.this, MainActivity.class));
                    }
                })
                .show();
    }
}



