package com.example.rsrtcs.ui.activity.auth.login;

import static com.example.rsrtcs.repository.cache.PrefrenceHelper.saveUserData;
import static com.example.rsrtcs.utils.CommonUtils.dismissLoadingDialog;
import static com.example.rsrtcs.utils.CommonUtils.isOnline;
import static com.example.rsrtcs.utils.CommonUtils.showLoadingDialog;
import static com.example.rsrtcs.utils.CommonUtils.showSnackBar;

import android.content.Intent;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rsrtcs.ui.activity.main.MainActivity;
import com.example.rsrtcs.R;
import com.example.rsrtcs.base.BaseActivity;
import com.example.rsrtcs.databinding.ActivityLoginBinding;
import com.example.rsrtcs.model.response.AuthModel;
import com.example.rsrtcs.model.request.LoginModel;
import com.example.rsrtcs.repository.remote.RSRTCConnection;
import com.example.rsrtcs.repository.remote.RSRTCInterface;
import com.example.rsrtcs.ui.activity.auth.forgot.ForgotPasswordActivity;
import com.example.rsrtcs.ui.activity.auth.signup.SignupActivity;
import com.example.rsrtcs.utils.MultiTextWatcher;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> implements View.OnClickListener, Callback<List<AuthModel>>, MultiTextWatcher.TextWatcherWithInstance {
    private RSRTCInterface apiInterface= new RSRTCConnection().createService();

    @Override
    protected ActivityLoginBinding getActivityBinding() {
        return ActivityLoginBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        binding.setModel(new LoginModel("",""));
    }

    @Override
    protected void initCtrl() {
        binding.btnLogin.setOnClickListener(this);
        binding.tvRegister.setOnClickListener(this);
        binding.tvForgotPassword.setOnClickListener(this);

        new MultiTextWatcher().registerEditText(binding.tieMobileNo).registerEditText(binding.tiePassword).setCallback(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogin:
            if(checkValidation()) {
            showLoadingDialog(this);
            apiInterface.login(binding.getModel()).enqueue(this); }
            break;
            case R.id.tvRegister: startActivity(new Intent(this, SignupActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK)); break;
            case R.id.tvForgotPassword: startActivity(new Intent(this, ForgotPasswordActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK)); break;
        }
    }

    private boolean checkValidation() {
        boolean ret=true;
        if(binding.getModel().getMobileNo().length()==0){
            ret=false;
            binding.tilMobileNo.setErrorEnabled(true);
            binding.tilMobileNo.setError("Please enter mobile number");
        }
        else if(binding.getModel().getMobileNo().length()!=10){
            ret=false;
            binding.tilMobileNo.setErrorEnabled(true);
            binding.tilMobileNo.setError("Please enter valid mobile number");
        }

        else if(binding.getModel().getPassword().length()==0){
            ret=false;
            binding.tilPassword.setErrorEnabled(true);
            binding.tilPassword.setError("Please enter password");
        }else if(!isOnline(this)){
            ret=false;
            showSnackBar(binding.getRoot(),"Please turn on internet");
        }

        return ret;
    }

    @Override
    public void onResponse(Call<List<AuthModel>> call, Response<List<AuthModel>> response) {
        dismissLoadingDialog();
        if(response.isSuccessful()){
            if(response.body().size()>0){
            for(int i=0;i<response.body().size();i++){

               if(binding.getModel().getMobileNo().equals(response.body().get(i).getMobileNo())){
                   if(binding.getModel().getPassword().equals(response.body().get(i).getPassword())){
                       saveUserData(LoginActivity.this,response.body().get(i));
                       startActivity(new Intent(LoginActivity.this, MainActivity.class));
                       break;
                   }else{
                       binding.tilPassword.setErrorEnabled(true);
                       binding.tilPassword.setError("Password does not match");
                       break;
                   }
               }else{
                   binding.tilMobileNo.setErrorEnabled(true);
                   binding.tilMobileNo.setError("Mobile number not yet register");
               }
            }}
            else {
                binding.tilMobileNo.setErrorEnabled(true);
                binding.tilMobileNo.setError("Mobile number not yet register");
            }

        }
        else Toast.makeText(LoginActivity.this, getString(R.string.internal_server_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(Call<List<AuthModel>> call, Throwable t) {
        dismissLoadingDialog();
        showSnackBar(binding.getRoot(),t.getMessage());

    }

    @Override
    public void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {
    switch (editText.getId()){
        case R.id.tieMobileNo: binding.tilMobileNo.setErrorEnabled(false); break;
        case R.id.tiePassword: binding.tilPassword.setErrorEnabled(false); break;
    }
    }

    @Override
    public void afterTextChanged(EditText editText, Editable editable) {

    }
}