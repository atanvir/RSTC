package com.example.rsrtcs.ui.activity.calculation;


import android.content.Intent;
import android.view.View;

import com.example.rsrtcs.R;
import com.example.rsrtcs.ui.activity.capture.UploadDocumentActivity;
import com.example.rsrtcs.base.BaseActivity;
import com.example.rsrtcs.databinding.ActivityFixPassFareCalculationBinding;
import com.example.rsrtcs.repository.cache.PrefrenceHelper;
import com.example.rsrtcs.repository.cache.PrefrenceKeyConstant;
import com.example.rsrtcs.utils.RegisterationDataHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class FixPassFareCalculation extends BaseActivity<ActivityFixPassFareCalculationBinding> implements View.OnClickListener {

    // Current Date
    private Date date = Calendar.getInstance().getTime();
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");

    @Override
    protected ActivityFixPassFareCalculationBinding getActivityBinding() {
        return ActivityFixPassFareCalculationBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        RegisterationDataHelper.getInstance().getApplicationData().setTransactionDate(format.format(date));
        RegisterationDataHelper.getInstance().getApplicationData().setStartDate(format.format(date));
        RegisterationDataHelper.getInstance().getApplicationData().setCreatedOn(format.format(date));
        RegisterationDataHelper.getInstance().getApplicationData().setCreatedBy(PrefrenceHelper.getPrefrenceStringValue(this,PrefrenceKeyConstant.PHONE_NO));
        binding.transDate.setText(RegisterationDataHelper.getInstance().getApplicationData().getTransactionDate());
        binding.expDate.setText(RegisterationDataHelper.getInstance().getApplicationData().getExpiryDate());
        binding.cardFees.setText(PrefrenceKeyConstant.FEES);
    }

    @Override
    protected void initCtrl() {
        binding.btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save:
            startActivity(new Intent(FixPassFareCalculation.this, UploadDocumentActivity.class));
            break;
        }
    }
}