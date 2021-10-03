package com.example.rsrtcs.ui.activity.billdesk;

import static com.example.rsrtcs.repository.cache.PrefrenceKeyConstant.BDSKUATY;
import static com.example.rsrtcs.repository.cache.PrefrenceKeyConstant.bdskuaty;
import static com.example.rsrtcs.utils.CommonUtils.dismissLoadingDialog;
import static com.example.rsrtcs.utils.CommonUtils.showLoadingDialog;
import static com.example.rsrtcs.utils.CommonUtils.showSnackBar;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.billdesk.sdk.PaymentOptions;
import com.example.rsrtcs.R;
import com.example.rsrtcs.base.BaseActivity;
import com.example.rsrtcs.databinding.ActivityPaymentBinding;
import com.example.rsrtcs.model.request.BillDeskRequestModel;
import com.example.rsrtcs.model.response.BillDeskModel;
import com.example.rsrtcs.repository.cache.PrefrenceHelper;
import com.example.rsrtcs.repository.cache.PrefrenceKeyConstant;
import com.example.rsrtcs.repository.remote.RSRTCConnection;
import com.example.rsrtcs.repository.remote.RSRTCInterface;
import com.example.rsrtcs.utils.CommonUtils;
import com.example.rsrtcs.utils.RegisterationDataHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends BaseActivity<ActivityPaymentBinding> implements View.OnClickListener {

    // Payment SDK
    private String payload = BDSKUATY;
    private String payloadRest = "|NA|NA|NA|INR|NA|R|"+bdskuaty+"|NA|NA|F|NA|NA|NA|NA|NA|NA|NA|https://rsrtcrfidsystem.co.in/pg_dump.aspx";
    private String FEES=PrefrenceKeyConstant.FEES;

    private RSRTCInterface apiInterface= new RSRTCConnection().createService();

    @Override
    protected ActivityPaymentBinding getActivityBinding() {
        return ActivityPaymentBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        generatePayloadBillDesk();
        if(getIntent().getStringExtra("amount")!=null){
            FEES=getIntent().getStringExtra("amount");
        }
    }

    private void generatePayloadBillDesk() {
        payload = payload + "|" + new SimpleDateFormat("yyMMddHHmmssms").format(new Date()) + "|" + "NA" + "|" + FEES + payloadRest;
        payload = payload + "|" + CommonUtils.HmacSHA256(payload, getString(R.string.check_sum_key));
    }

    @Override
    protected void initCtrl() {
        binding.btnPayNow.setOnClickListener(this);
    }

    private void billDeskRequest() {
        showLoadingDialog(this);
        apiInterface.billDeskRequest(new BillDeskRequestModel(RegisterationDataHelper.getInstance().getApplicationData().getApplicantID(),PrefrenceHelper.getPrefrenceStringValue(this,PrefrenceKeyConstant.EMAIL_ID),payload)).enqueue(new Callback<List<BillDeskModel>>() {
            @Override
            public void onResponse(Call<List<BillDeskModel>> call, Response<List<BillDeskModel>> response) {
                dismissLoadingDialog();
                Intent sdkIntent = new Intent(PaymentActivity.this, PaymentOptions.class);
                sdkIntent.putExtra("msg", payload);
                sdkIntent.putExtra("user-email", PrefrenceHelper.getPrefrenceStringValue(PaymentActivity.this, PrefrenceKeyConstant.EMAIL_ID));
                sdkIntent.putExtra("user-mobile",PrefrenceHelper.getPrefrenceStringValue(PaymentActivity.this, PrefrenceKeyConstant.PHONE_NO));
                sdkIntent.putExtra("callback",new SampleCallBack());
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPayNow: billDeskRequest(); break;
        }
    }
}