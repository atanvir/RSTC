package com.example.rsrtcs.ui.activity.report;

import static com.example.rsrtcs.utils.CommonUtils.dismissLoadingDialog;
import static com.example.rsrtcs.utils.CommonUtils.showLoadingDialog;

import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.rsrtcs.base.BaseActivity;
import com.example.rsrtcs.databinding.ActivityReportBinding;
import com.example.rsrtcs.model.request.ReportModel;
import com.example.rsrtcs.model.response.CardModel;
import com.example.rsrtcs.repository.RSRTCConnection;
import com.example.rsrtcs.repository.RSRTCInterface;
import com.example.rsrtcs.ui.ReportAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends BaseActivity<ActivityReportBinding> {

    private RSRTCInterface apiInterface= new RSRTCConnection().createService();


    @Override
    protected ActivityReportBinding getActivityBinding() {
        return ActivityReportBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        showLoadingDialog(this);
        apiInterface.report(new ReportModel(getSharedPreferences("abc",MODE_PRIVATE).getString("emailId",""))).enqueue(new Callback<List<CardModel>>() {
            @Override
            public void onResponse(Call<List<CardModel>> call, Response<List<CardModel>> response) {
                dismissLoadingDialog();
                binding.rvItem.setLayoutManager(new LinearLayoutManager(ReportActivity.this));
                binding.rvItem.setAdapter(new ReportAdapter(ReportActivity.this,response.body()));
            }

            @Override
            public void onFailure(Call<List<CardModel>> call, Throwable t) {
                dismissLoadingDialog();
                Toast.makeText(ReportActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void initCtrl() {

    }
}