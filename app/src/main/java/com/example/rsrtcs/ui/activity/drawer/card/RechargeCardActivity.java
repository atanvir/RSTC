package com.example.rsrtcs.ui.activity.drawer.card;

import static com.example.rsrtcs.utils.CommonUtils.dismissLoadingDialog;
import static com.example.rsrtcs.utils.CommonUtils.showLoadingDialog;
import static com.example.rsrtcs.utils.CommonUtils.showSnackBar;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.rsrtcs.ui.activity.billdesk.PaymentActivity;
import com.example.rsrtcs.R;
import com.example.rsrtcs.base.BaseActivity;
import com.example.rsrtcs.databinding.ActivityRechargeCardBinding;
import com.example.rsrtcs.model.request.CardDataModel;
import com.example.rsrtcs.model.response.CardModel;
import com.example.rsrtcs.repository.cache.PrefrenceKeyConstant;
import com.example.rsrtcs.repository.remote.RSRTCConnection;
import com.example.rsrtcs.repository.remote.RSRTCInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RechargeCardActivity extends BaseActivity<ActivityRechargeCardBinding> implements View.OnClickListener, TextWatcher, Callback<List<CardModel>> {

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
    public void onResponse(Call<List<CardModel>> call, Response<List<CardModel>> response) {
        if(response.isSuccessful()){
            for(int i=0;i<response.body().size();i++){
                if(binding.getData().getCardNo().equalsIgnoreCase(response.body().get(i).getCardNo())){
                    getSharedPreferences(PrefrenceKeyConstant.PREF_NAME, Context.MODE_PRIVATE).edit().putString("cardNo", binding.tieEmail.getText().toString()).apply();
                    dismissLoadingDialog();
                    startActivity(new Intent(RechargeCardActivity.this,PaymentActivity.class).putExtra("amount", response.body().get(i).getAmount()));
                }
            }

        }
    }

    @Override
    public void onFailure(Call<List<CardModel>> call, Throwable t) {
        dismissLoadingDialog();
        showSnackBar(binding.getRoot(),t.getMessage());
    }
}