package com.example.rstc.ui.activity.auth.forgot;

import static com.example.rstc.utils.CommonUtils.dismissLoadingDialog;
import static com.example.rstc.utils.CommonUtils.isOnline;
import static com.example.rstc.utils.CommonUtils.showLoadingDialog;
import static com.example.rstc.utils.CommonUtils.showSnackBar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rstc.R;
import com.example.rstc.base.BaseActivity;
import com.example.rstc.databinding.ActivityForgotPasswordBinding;
import com.example.rstc.model.request.ForgotModel;
import com.example.rstc.model.response.AuthModel;
import com.example.rstc.repository.RSRTCConnection;
import com.example.rstc.repository.RSRTCInterface;
import com.example.rstc.utils.MultiTextWatcher;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends BaseActivity<ActivityForgotPasswordBinding> implements View.OnClickListener, MultiTextWatcher.TextWatcherWithInstance, Callback<AuthModel> {
    private RSRTCInterface apiInterface= new RSRTCConnection().createService();


    @Override
    protected ActivityForgotPasswordBinding getActivityBinding() {
        return ActivityForgotPasswordBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        binding.setData(new ForgotModel(""));
    }

    @Override
    protected void initCtrl() {
        binding.btnForgot.setOnClickListener(this);
        new MultiTextWatcher().registerEditText(binding.tieEmail).setCallback(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnForgot:
            if(checkValidation()) {
                showLoadingDialog(this);
                apiInterface.forgotPassword(binding.getData()).enqueue(this);
            }
            break;
        }
    }

    private boolean checkValidation() {
        boolean ret=true;
        if(binding.getData().getEmailId().length()==0){
            ret=false;
            binding.tilEmail.setErrorEnabled(true);
            binding.tilEmail.setError("Please enter email id");
        }

        else if(!Patterns.EMAIL_ADDRESS.matcher(binding.getData().getEmailId()).matches()){
            ret=false;
            binding.tilEmail.setErrorEnabled(true);
            binding.tilEmail.setError("Please enter valid email id");
        }
        else if(!isOnline(this)){
            ret=false;
            showSnackBar(binding.getRoot(),"Please turn on internet");
        }


        return ret;
    }

    @Override
    public void beforeTextChanged(EditText editText, CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(EditText editText, CharSequence s, int start, int before, int count) {
        switch (editText.getId()){
            case R.id.tieEmail: binding.tilEmail.setErrorEnabled(false); break;
        }
    }

    @Override
    public void afterTextChanged(EditText editText, Editable editable) {

    }

    @Override
    public void onResponse(Call<AuthModel> call, Response<AuthModel> response) {
        dismissLoadingDialog();
        if(response.isSuccessful()){
            Toast.makeText(ForgotPasswordActivity.this, "You will receive an email shortly", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onFailure(Call<AuthModel> call, Throwable t) {
        dismissLoadingDialog();
        showSnackBar(binding.getRoot(),t.getMessage());

    }
}