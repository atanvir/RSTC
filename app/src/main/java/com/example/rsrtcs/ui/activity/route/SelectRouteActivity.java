package com.example.rsrtcs.ui.activity.route;

import static com.example.rsrtcs.utils.CommonUtils.dismissLoadingDialog;
import static com.example.rsrtcs.utils.CommonUtils.showLoadingDialog;
import static com.example.rsrtcs.utils.CommonUtils.showSnackBar;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import com.example.rsrtcs.R;
import com.example.rsrtcs.base.BaseActivity;
import com.example.rsrtcs.databinding.ActivitySelectRouteBinding;
import com.example.rsrtcs.model.response.RouteModel;
import com.example.rsrtcs.repository.remote.RSRTCConnection;
import com.example.rsrtcs.repository.remote.RSRTCInterface;
import com.example.rsrtcs.ui.adapter.RouteAdapter;
import com.example.rsrtcs.utils.RegisterationDataHelper;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectRouteActivity extends BaseActivity<ActivitySelectRouteBinding> implements Callback<List<RouteModel>> {
    private RSRTCInterface apiInterface= new RSRTCConnection().createServiceRoute();

    @Override
    protected ActivitySelectRouteBinding getActivityBinding() {
        return ActivitySelectRouteBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {
        showLoadingDialog(this);
        apiInterface.getRouteDetailApi(new com.example.rsrtcs.model.request.RouteModel(RegisterationDataHelper.getInstance().getApplicationData().getFromStop(), RegisterationDataHelper.getInstance().getApplicationData().getTillStop())).enqueue(this);
    }

    @Override
    protected void initCtrl() {

    }


    @Override
    public void onResponse(Call<List<RouteModel>> call, Response<List<RouteModel>> response) {
        dismissLoadingDialog();
        if(response.isSuccessful()){
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(SelectRouteActivity.this));
            binding.recyclerView.setAdapter(new RouteAdapter(SelectRouteActivity.this,response.body()));
        }else{
            showSnackBar(binding.getRoot(),getString(R.string.internal_server_error));
        }
    }

    @Override
    public void onFailure(Call<List<RouteModel>> call, Throwable t) {
        dismissLoadingDialog();
        showSnackBar(binding.getRoot(),t.getMessage());
    }
}