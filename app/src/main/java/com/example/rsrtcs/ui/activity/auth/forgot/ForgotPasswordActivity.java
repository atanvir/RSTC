package com.example.rsrtcs.ui.activity.auth.forgot;

import static com.example.rsrtcs.utils.CommonUtils.dismissLoadingDialog;
import static com.example.rsrtcs.utils.CommonUtils.isOnline;
import static com.example.rsrtcs.utils.CommonUtils.showLoadingDialog;
import static com.example.rsrtcs.utils.CommonUtils.showSnackBar;

import android.text.Editable;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rsrtcs.R;
import com.example.rsrtcs.base.BaseActivity;
import com.example.rsrtcs.databinding.ActivityForgotPasswordBinding;
import com.example.rsrtcs.model.request.ForgotModel;
import com.example.rsrtcs.model.response.AuthModel;
import com.example.rsrtcs.repository.remote.RSRTCConnection;
import com.example.rsrtcs.repository.remote.RSRTCInterface;
import com.example.rsrtcs.utils.MultiTextWatcher;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends BaseActivity<ActivityForgotPasswordBinding> implements View.OnClickListener, MultiTextWatcher.TextWatcherWithInstance, Callback<List<AuthModel>> {
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
        if(binding.getData().getMobileNo().length()==0){
            ret=false;
            binding.tilEmail.setErrorEnabled(true);
            binding.tilEmail.setError("Please enter mobile no");
        } if(binding.getData().getMobileNo().length()!=10){
            ret=false;
            binding.tilEmail.setErrorEnabled(true);
            binding.tilEmail.setError("Please enter valid mobile no");
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
    public void onResponse(Call<List<AuthModel>> call, Response<List<AuthModel>> response) {
        dismissLoadingDialog();
        if(response.isSuccessful()) {
         if(response.body().size()>0){
             for(AuthModel model:response.body()){
                 Toast.makeText(ForgotPasswordActivity.this, model.getMsg(), Toast.LENGTH_SHORT).show();
                 finish();
                 break;
             }
         } else{
             binding.tilEmail.setErrorEnabled(true);
             binding.tilEmail.setError("Mobile Number is not yet register, try signup");
         }
        }
        else showSnackBar(binding.getRoot(),getString(R.string.internal_server_error));
    }

    @Override
    public void onFailure(Call<List<AuthModel>> call, Throwable t) {
        dismissLoadingDialog();
        showSnackBar(binding.getRoot(),t.getMessage());

    }
}