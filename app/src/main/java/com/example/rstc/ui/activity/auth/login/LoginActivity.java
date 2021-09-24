package com.example.rstc.ui.activity.auth.login;

import static com.example.rstc.utils.CommonUtils.dismissLoadingDialog;
import static com.example.rstc.utils.CommonUtils.isOnline;
import static com.example.rstc.utils.CommonUtils.showLoadingDialog;
import static com.example.rstc.utils.CommonUtils.showSnackBar;

import android.content.Intent;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.example.rstc.MainActivity;
import com.example.rstc.R;
import com.example.rstc.base.BaseActivity;
import com.example.rstc.databinding.ActivityLoginBinding;
import com.example.rstc.model.response.AuthModel;
import com.example.rstc.model.request.LoginModel;
import com.example.rstc.repository.RSRTCConnection;
import com.example.rstc.repository.RSRTCInterface;
import com.example.rstc.ui.activity.auth.forgot.ForgotPasswordActivity;
import com.example.rstc.ui.activity.auth.signup.SignupActivity;
import com.example.rstc.utils.MultiTextWatcher;

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
                       getSharedPreferences("abc",MODE_PRIVATE).edit().putString("emailId",response.body().get(i).getEmailId()).apply();
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