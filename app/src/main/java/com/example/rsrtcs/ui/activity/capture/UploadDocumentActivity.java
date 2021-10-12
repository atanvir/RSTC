package com.example.rsrtcs.ui.activity.capture;

import static com.example.rsrtcs.repository.cache.PrefrenceKeyConstant.BDSKUATY;
import static com.example.rsrtcs.repository.cache.PrefrenceKeyConstant.BILL_DESK_DUMP_URL;
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

import com.billdesk.sdk.PaymentOptions;
import com.bumptech.glide.Glide;
import com.example.rsrtcs.BuildConfig;
import com.example.rsrtcs.R;
import com.example.rsrtcs.base.BaseActivity;

import com.example.rsrtcs.model.request.BillDeskRequestModel;
import com.example.rsrtcs.model.request.BilldeskRequestPayloadModel;
import com.example.rsrtcs.model.response.BillDeskModel;
import com.example.rsrtcs.ui.activity.billdesk.BilldeskCallBack;
import com.example.rsrtcs.databinding.ActivityUploadDocumentsBinding;
import com.example.rsrtcs.model.request.ApplicationModel;
import com.example.rsrtcs.model.request.SMSModel;
import com.example.rsrtcs.model.request.SpinnerDataModel;
import com.example.rsrtcs.model.request.SpinnerRequestModel;
import com.example.rsrtcs.model.response.RegistrationModel;
import com.example.rsrtcs.repository.cache.PrefrenceHelper;
import com.example.rsrtcs.repository.cache.PrefrenceKeyConstant;
import com.example.rsrtcs.repository.remote.RSRTCConnection;
import com.example.rsrtcs.repository.remote.RSRTCInterface;
import com.example.rsrtcs.utils.CommonUtils;
import com.example.rsrtcs.utils.FilePath;
import com.example.rsrtcs.utils.ImageUtil;
import com.example.rsrtcs.utils.RegisterationDataHelper;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
    private List<SpinnerDataModel> mainList= new ArrayList<>();
    private List<SpinnerDataModel> concessionList= new ArrayList<>();

    private String payload = BDSKUATY;
    private String payloadRest = "|NA|NA|NA|INR|NA|R|"+BDSKUATY.toLowerCase(Locale.ROOT)+"|NA|NA|F|NA|NA|NA|NA|NA|NA|NA|"+BILL_DESK_DUMP_URL;
    private String FEES=PrefrenceKeyConstant.FEES;
    
    @Override
    protected ActivityUploadDocumentsBinding getActivityBinding() {
        return ActivityUploadDocumentsBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        if(getIntent().getStringExtra("amount")!=null) FEES=getIntent().getStringExtra("amount");
        generatePayloadBillDesk();
    }

    private void generatePayloadBillDesk() {
        showLoadingDialog(this);
        payload = payload + "|" + new SimpleDateFormat("yyMMddHHmmssms").format(new Date()) + "|" + "NA" + "|" + FEES + payloadRest;
        apiInterface.getCheckSum(new BilldeskRequestPayloadModel(payload)).enqueue(new Callback<List<BillDeskModel>>() {
            @Override
            public void onResponse(Call<List<BillDeskModel>> call, Response<List<BillDeskModel>> response) {
                if(response.isSuccessful()) {
                    for(int i=0;i<response.body().size();i++){
                        payload= payload +  "|" + response.body().get(i).getMsg().toUpperCase(Locale.ROOT) ;
                        Log.e("response",response.body().get(i).getMsg().toUpperCase(Locale.ROOT));
                        break;
                    }
                    apiInterface.getDocumentType(new SpinnerRequestModel("MTP")).enqueue(new Callback<List<SpinnerDataModel>>() {
                        @Override
                        public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                            if(response.isSuccessful()){
                                concessionList=response.body();
                                List<String> concessionListClone=new ArrayList<>();
                                concessionListClone.add(0,"Select Concession Proof");
                                for(int i=0;i<response.body().size();i++){
                                    concessionListClone.add(response.body().get(i).getDocumentName());
                                }

                                apiInterface.getConcessionDoc(new SpinnerRequestModel("")).enqueue(new Callback<List<SpinnerDataModel>>() {
                                    @Override
                                    public void onResponse(Call<List<SpinnerDataModel>> call, Response<List<SpinnerDataModel>> response) {
                                        dismissLoadingDialog();
                                        if(response.isSuccessful()) {
                                            mainList=response.body();
                                            List<String> addressList=new ArrayList<>();
                                            List<String> proofList=new ArrayList<>();
                                            addressList.add(0,"Select Address Proof");
                                            proofList.add(0,"Select Proof ID");
                                            for(int i=0;i<response.body().size();i++){
                                                addressList.add(response.body().get(i).getDocumentName());
                                                proofList.add(response.body().get(i).getDocumentName());
                                            }

                                            CommonUtils.setSpinner(binding.spinnerConcession,concessionListClone);
                                            CommonUtils.setSpinner(binding.spinnerPhotoId,proofList);
                                            CommonUtils.setSpinner(binding.spinnerAddressProof,addressList);
                                        }
                                        else showSnackBar(binding.getRoot(),getString(R.string.internal_server_error));
                                    }

                                    @Override
                                    public void onFailure(Call<List<SpinnerDataModel>> call, Throwable t) {
                                        dismissLoadingDialog();
                                        showSnackBar(binding.getRoot(),t.getMessage());
                                    }
                                });
                            }else {
                                dismissLoadingDialog();
                                showSnackBar(binding.getRoot(),getString(R.string.internal_server_error));
                            }
                        }

                        @Override
                        public void onFailure(Call<List<SpinnerDataModel>> call, Throwable t) {
                            dismissLoadingDialog();
                            showSnackBar(binding.getRoot(),t.getMessage());
                        }
                    });
                }else{
                    dismissLoadingDialog();
                    showSnackBar(binding.getRoot(),getString(R.string.internal_server_error)); }
            }

            @Override
            public void onFailure(Call<List<BillDeskModel>> call, Throwable t) {
                dismissLoadingDialog();
                showSnackBar(binding.getRoot(),t.getMessage());
            }
        });
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
        case R.id.spinner_photo_id : if(position>0) RegisterationDataHelper.getInstance().getApplicationData().setPhotoIDProofID(mainList.get(position-1).getSrNo()); break;
        case R.id.spinner_concession : if(position>0) RegisterationDataHelper.getInstance().getApplicationData().setConcessionApplicableDocumentProofID(concessionList.get(position-1).getSrNo()); break;
        case R.id.spinner_addressProof : if(position>0) RegisterationDataHelper.getInstance().getApplicationData().setAddressProofID(mainList.get(position-1).getSrNo()); break;
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
            }else{ billDeskRequest(); }
            break;
        }
    }

    private void billDeskRequest() {
        showLoadingDialog(this);
        apiInterface.billDeskRequest(new BillDeskRequestModel(RegisterationDataHelper.getInstance().getApplicationData().getApplicantID(),PrefrenceHelper.getPrefrenceStringValue(this,PrefrenceKeyConstant.EMAIL_ID),payload)).enqueue(new Callback<List<BillDeskModel>>() {
            @Override
            public void onResponse(Call<List<BillDeskModel>> call, Response<List<BillDeskModel>> response) {
                dismissLoadingDialog();
                Intent sdkIntent = new Intent(UploadDocumentActivity.this, PaymentOptions.class);
                sdkIntent.putExtra("msg", payload.toUpperCase(Locale.ROOT));
                sdkIntent.putExtra("user-email", PrefrenceHelper.getPrefrenceStringValue(UploadDocumentActivity.this, PrefrenceKeyConstant.EMAIL_ID));
                sdkIntent.putExtra("user-mobile",PrefrenceHelper.getPrefrenceStringValue(UploadDocumentActivity.this, PrefrenceKeyConstant.PHONE_NO));
                sdkIntent.putExtra("callback",new BilldeskCallBack());
                startActivity(sdkIntent);
                finish();
            }

            @Override
            public void onFailure(Call<List<BillDeskModel>> call, Throwable t) {
                dismissLoadingDialog();
                showSnackBar(binding.getRoot(),t.getMessage());
            }
        });

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

                smsApiInterface.sendSMS(new SMSModel("Dear "+ PrefrenceHelper.getPrefrenceStringValue(this, PrefrenceKeyConstant.FULL_NAME) +", your RFID application no "+response.body().get(i).getAppId()+" is successfully registered with RSRTC.You will get confirmation when RSRTC Approve your application.RSRTCR",PrefrenceHelper.getPrefrenceStringValue(this,PrefrenceKeyConstant.PHONE_NO))).enqueue(new Callback<String>() {
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
                .setCancelable(false)
                .setMessage("Your Application id is "+appId)
                .setPositiveButton("Pay Now", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        binding.btnRegister.setText("Pay Now");
                        RegisterationDataHelper.getInstance().setApplicationData(new ApplicationModel("","1",appId,"","","","","","",PrefrenceHelper.getPrefrenceStringValue(UploadDocumentActivity.this, PrefrenceKeyConstant.PHONE_NO),PrefrenceHelper.getPrefrenceStringValue(UploadDocumentActivity.this, PrefrenceKeyConstant.EMAIL_ID),"","","","","","","","","","","","","","","","","","","","","","","","","","","","0","0","0","","","0","0","0","0","0","0","","","","","I","","","000000000000"));
                        billDeskRequest();
                    }
                })
                .show();
    }
}



