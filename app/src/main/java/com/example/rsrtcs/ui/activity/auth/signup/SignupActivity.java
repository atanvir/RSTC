package com.example.rsrtcs.ui.activity.auth.signup;

import static com.example.rsrtcs.utils.CommonUtils.dismissLoadingDialog;
import static com.example.rsrtcs.utils.CommonUtils.isOnline;
import static com.example.rsrtcs.utils.CommonUtils.showLoadingDialog;
import static com.example.rsrtcs.utils.CommonUtils.showSnackBar;
import androidx.annotation.NonNull;

import android.text.Editable;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rsrtcs.R;
import com.example.rsrtcs.base.BaseActivity;
import com.example.rsrtcs.databinding.ActivitySignupBinding;
import com.example.rsrtcs.model.response.AuthModel;
import com.example.rsrtcs.model.request.SMSModel;
import com.example.rsrtcs.model.request.SignupModel;
import com.example.rsrtcs.repository.RSRTCConnection;
import com.example.rsrtcs.repository.RSRTCInterface;
import com.example.rsrtcs.utils.MultiTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends BaseActivity<ActivitySignupBinding> implements View.OnClickListener, OnCompleteListener<AuthResult>, Callback<AuthModel>, MultiTextWatcher.TextWatcherWithInstance {

    private FirebaseAuth auth;
    private PhoneAuthProvider.ForceResendingToken forceToken;
    private String otp;
    private RSRTCInterface apiInterface= new RSRTCConnection().createService();
    private RSRTCInterface smsApiInterface= new RSRTCConnection().createSMSService();


    @Override
    protected ActivitySignupBinding getActivityBinding() {
        return ActivitySignupBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        auth = FirebaseAuth.getInstance();
        binding.setData(new SignupModel("","","","",""));
    }

    @Override
    protected void initCtrl() {
        binding.tvLogin.setOnClickListener(this);
        binding.btnRegister.setOnClickListener(this);
        binding.btnOTP.setOnClickListener(this);

        new MultiTextWatcher().registerEditText(binding.tieName)
                              .registerEditText(binding.tieEmail)
                              .registerEditText(binding.tiePassword)
                              .registerEditText(binding.tieMobileNo)
                              .registerEditText(binding.tieOTP).setCallback(this);
    }




  private PhoneAuthProvider.OnVerificationStateChangedCallbacks callback=  new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            if(phoneAuthCredential!=null){
                binding.tieOTP.setText(phoneAuthCredential.getSmsCode());
                signInWithCredential(phoneAuthCredential);
            }else{
                dismissLoadingDialog();
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            dismissLoadingDialog();
            binding.tilMobileNo.setErrorEnabled(true);
            binding.tilMobileNo.setError(e.getMessage());
        }

        @Override
        public void onCodeSent(@NonNull final String code, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(code, forceResendingToken);
            otp=code;
            forceToken=forceResendingToken;
            dismissLoadingDialog();
            Toast.makeText(SignupActivity.this, "OTP send successfully!", Toast.LENGTH_SHORT).show();
        }
    };

    private void signInWithCredential(PhoneAuthCredential phoneAuthCredential) {
        auth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
            if(checkValidation()){
                showLoadingDialog(SignupActivity.this);
                binding.getData().setMobileNo("8527026788");
                signInWithCredential(PhoneAuthProvider.getCredential(otp, binding.getData().getOtp()));
            }
            break;

            case R.id.btnOTP:
            if(binding.getData().getMobileNo().trim().length()==0) showSnackBar(binding.getRoot(),"Please enter mobile number");
            else if(binding.getData().getMobileNo().trim().length()!=10) showSnackBar(binding.getRoot(),"Please enter valid mobile number");
            else sendOTP();
            break;

            case R.id.tvLogin: finish(); break;
        }
    }

    private boolean checkValidation() {
        boolean ret=true;
        if(binding.getData().getUserName().trim().length()==0){
            ret=false;
            binding.tilName.setErrorEnabled(true);
            binding.tilName.setError("Please enter name");
        }
        else if(binding.getData().getEmailId().trim().length()==0){
            ret=false;
            binding.tilEmail.setErrorEnabled(true);
            binding.tilEmail.setError("Please enter email id");

        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(binding.getData().getEmailId()).matches()){
            ret=false;
            binding.tilEmail.setErrorEnabled(true);
            binding.tilEmail.setError("Please enter valid email id");
        }
        else if(binding.getData().getPassword().trim().length()==0){
            ret=false;
            binding.tilPassword.setErrorEnabled(true);
            binding.tilPassword.setError("Please enter password");
        }
        else if(binding.getData().getMobileNo().trim().length()==0){
            ret=false;
            binding.tilMobileNo.setErrorEnabled(true);
            binding.tilMobileNo.setError("Please enter mobile no");
        }
        else if(binding.getData().getMobileNo().trim().length()!=10){
            ret=false;
            binding.tilMobileNo.setErrorEnabled(true);
            binding.tilMobileNo.setError("Please enter valid mobile no");
        }
        else if(binding.getData().getOtp().trim().length()==0){
            ret=false;
            binding.tilOTP.setErrorEnabled(true);
            binding.tilOTP.setError("Please enter otp");
        }
        else if(binding.getData().getOtp().trim().length()!=6){
            ret=false;
            binding.tilOTP.setErrorEnabled(true);
            binding.tilOTP.setError("Please enter six digit otp");
        }else if(otp==null){
            ret=false;
            showSnackBar(binding.getRoot(),"Please click on send otp button");
        }else if(!isOnline(this)){
            ret=false;
            showSnackBar(binding.getRoot(),"Please turn on internet");
        }
        return ret;

    }

    private void sendOTP() {
        showLoadingDialog(this);
        auth.setLanguageCode("en");
        PhoneAuthOptions.Builder options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+91"+binding.tieMobileNo.getText().toString().trim())
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callback);

        if(forceToken!=null) options.setForceResendingToken(forceToken);
        PhoneAuthProvider.verifyPhoneNumber(options.build());
    }


    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if(task.isSuccessful()){
          if(isOnline(SignupActivity.this)) apiInterface.signup(binding.getData()).enqueue(this);
          else showSnackBar(binding.getRoot(),"Please turn on internet");
        }else{
           dismissLoadingDialog();
           binding.tilOTP.setErrorEnabled(true);
           binding.tilOTP.setError(task.getException().getMessage());
        }
    }

    @Override
    public void onResponse(Call<AuthModel> call, Response<AuthModel> response) {
        if(response.isSuccessful()){
            if(isOnline(SignupActivity.this)){
                if(response.body().getResult().equals("Your Registration SuccessFull.")){
                    Toast.makeText(SignupActivity.this, response.body().getResult(), Toast.LENGTH_SHORT).show();
                    smsApiInterface.sendSMS(new SMSModel("Dear"+binding.getData().getUserName()+", your RFID application no 684956174295 is successfully registered with RSRTC.You will get confirmation when RSRTC Approve your application.RSRTCR",binding.getData().getMobileNo())).enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            dismissLoadingDialog();
                            if(response.isSuccessful()){
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            dismissLoadingDialog();
                            showSnackBar(binding.getRoot(),t.getMessage());
                        }
                    });
                }else{
                    binding.tilMobileNo.setErrorEnabled(true);
                    binding.tilMobileNo.setError(response.body().getResult());

                }
            }
            else showSnackBar(binding.getRoot(),"Please turn on internet");
        }
    }

    @Override
    public void onFailure(Call<AuthModel> call, Throwable t) {
        dismissLoadingDialog();
        showSnackBar(binding.getRoot(),t.getMessage());
    }

    @Override
    public void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {
        switch (editText.getId()){
            case R.id.tieName: binding.tilName.setErrorEnabled(false); break;
            case R.id.tieEmail: binding.tilEmail.setErrorEnabled(false); break;
            case R.id.tiePassword: binding.tilPassword.setErrorEnabled(false); break;
            case R.id.tieMobileNo: binding.tilMobileNo.setErrorEnabled(false); break;
            case R.id.tieOTP: binding.tilOTP.setErrorEnabled(false); break;
        }
    }

    @Override
    public void afterTextChanged(EditText editText, Editable editable) {

    }
}