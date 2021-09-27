package com.example.rsrtcs.ui.activity.route;


import android.content.Intent;
import android.view.View;

import com.example.rsrtcs.ui.activity.concession.ConcessionDetail;
import com.example.rsrtcs.base.BaseActivity;
import com.example.rsrtcs.databinding.ActivityPassRouteSelectionBinding;

public class PassRouteSelectionActivity extends BaseActivity<ActivityPassRouteSelectionBinding> implements View.OnClickListener {

    @Override
    protected ActivityPassRouteSelectionBinding getActivityBinding() {
        return ActivityPassRouteSelectionBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void init() {

    }

    @Override
    protected void initCtrl() {
        binding.fixedPass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(PassRouteSelectionActivity.this, ConcessionDetail.class);
        startActivity(intent);
        finish();
    }
}