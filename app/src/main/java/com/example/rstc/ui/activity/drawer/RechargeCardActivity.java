package com.example.rstc.ui.activity.drawer;

import static com.example.rstc.FixPassFareCalculation.MyPREFERENCES;
import static com.example.rstc.utils.CommonUtils.dismissLoadingDialog;
import static com.example.rstc.utils.CommonUtils.showLoadingDialog;
import static com.example.rstc.utils.CommonUtils.showSnackBar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.rstc.PaymentActivity;
import com.example.rstc.R;
import com.example.rstc.base.BaseActivity;
import com.example.rstc.databinding.ActivityRechargeCardBinding;
import com.example.rstc.model.request.CardDataModel;
import com.example.rstc.model.response.CardModel;
import com.example.rstc.repository.RSRTCConnection;
import com.example.rstc.repository.RSRTCInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RechargeCardActivity extends BaseActivity<ActivityRechargeCardBinding> implements View.OnClickListener, TextWatcher, Callback<CardModel> {

    private RSRTCInterface apiInterface= new RSRTCConnection().createService();

    @Override
    protected ActivityRechargeCardBinding getActivityBinding() {
        return ActivityRechargeCardBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        binding.setData(new CardDataModel(""));
        binding.btnForgot.setOnClickListener(this);

    }

    @Override
    protected void initCtrl() {
        binding.tieEmail.addTextChangedListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnForgot:
            if(binding.getData().getCardNo().length()==0){
                binding.tilEmail.setErrorEnabled(true);
                binding.tilEmail.setError("Please enter card number");
            }else{
                showLoadingDialog(this);
                apiInterface.getCardData(binding.getData()).enqueue(this);
            }
            break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        binding.tilEmail.setErrorEnabled(false);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onResponse(Call<CardModel> call, Response<CardModel> response) {
        if(response.isSuccessful()){
            getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE).edit().putString("cardNo", binding.tieEmail.getText().toString()).apply();
            dismissLoadingDialog();
            startActivity(new Intent(RechargeCardActivity.this,PaymentActivity.class).putExtra("amount", response.body().getAmount()));

        }
    }

    @Override
    public void onFailure(Call<CardModel> call, Throwable t) {
        dismissLoadingDialog();
        showSnackBar(binding.getRoot(),t.getMessage());
    }
}