package com.example.rsrtcs.ui.activity.billdesk;

import static com.example.rsrtcs.utils.CommonUtils.dismissLoadingDialog;
import static com.example.rsrtcs.utils.CommonUtils.showLoadingDialog;
import static com.example.rsrtcs.utils.CommonUtils.showSnackBar;


import android.content.Intent;
import android.widget.Toast;

import com.example.rsrtcs.R;
import com.example.rsrtcs.base.BaseActivity;
import com.example.rsrtcs.databinding.ActivityStatusBinding;
import com.example.rsrtcs.model.request.BillDeskResponseModel;
import com.example.rsrtcs.model.response.BillDeskModel;
import com.example.rsrtcs.repository.remote.RSRTCConnection;
import com.example.rsrtcs.repository.remote.RSRTCInterface;
import com.example.rsrtcs.ui.activity.main.MainActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentStatusActivity extends BaseActivity<ActivityStatusBinding> {

    private RSRTCInterface apiInterface= new RSRTCConnection().createService();
    private String status;

    @Override
    protected ActivityStatusBinding getActivityBinding() {
        return ActivityStatusBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        status = getIntent().getExtras().getString("status");
        if(status.contains("0300")){
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
            binding.status.setText("Payment Successful");
            billDeskResponseApi();
        }else{
            Toast.makeText(PaymentStatusActivity.this, "Payment Failure", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PaymentStatusActivity.this, MainActivity.class));
        }
    }

    @Override
    protected void initCtrl() {

    }


    private void billDeskResponseApi() {
        showLoadingDialog(this);
        apiInterface.billDeskResponse(new BillDeskResponseModel(status)).enqueue(new Callback<List<BillDeskModel>>() {
            @Override
            public void onResponse(Call<List<BillDeskModel>> call, Response<List<BillDeskModel>> response) {
                dismissLoadingDialog();
                if(response.isSuccessful()) startActivity(new Intent(PaymentStatusActivity.this, MainActivity.class));
                else showSnackBar(binding.getRoot(),getString(R.string.internal_server_error));
            }

            @Override
            public void onFailure(Call<List<BillDeskModel>> call, Throwable t) {
                dismissLoadingDialog();
                showSnackBar(binding.getRoot(),t.getMessage());
            }
        });
    }

}