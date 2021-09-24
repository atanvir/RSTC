package com.example.rstc.ui.activity.report;

import static com.example.rstc.utils.CommonUtils.dismissLoadingDialog;
import static com.example.rstc.utils.CommonUtils.showLoadingDialog;

import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.rstc.R;
import com.example.rstc.base.BaseActivity;
import com.example.rstc.databinding.ActivityReportBinding;
import com.example.rstc.model.request.ReportModel;
import com.example.rstc.model.response.CardModel;
import com.example.rstc.repository.RSRTCConnection;
import com.example.rstc.repository.RSRTCInterface;
import com.example.rstc.ui.ReportAdapter;

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