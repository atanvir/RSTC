package com.example.rsrtcs.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rsrtcs.ui.activity.calculation.FixPassFareCalculation;
import com.example.rsrtcs.R;
import com.example.rsrtcs.databinding.AdapterRouteBinding;
import com.example.rsrtcs.model.response.RouteModel;
import com.example.rsrtcs.utils.RegisterationDataHelper;

import java.util.ArrayList;
import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {
    private Context context;
    private List<RouteModel> list = new ArrayList<>();

    public RouteAdapter(Context context, List<RouteModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RouteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(AdapterRouteBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(RouteAdapter.ViewHolder holder, int position) {
        holder.binding.setData(list.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        AdapterRouteBinding binding;

        public ViewHolder(@NonNull AdapterRouteBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
            binding.cvMain.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.cvMain:
                Bundle b = new Bundle();
                RegisterationDataHelper.getInstance().getApplicationData().setRouteNo(list.get(getAdapterPosition()).getRouteNo());
                RegisterationDataHelper.getInstance().getApplicationData().setRouteName(list.get(getAdapterPosition()).getRouteName());
                RegisterationDataHelper.getInstance().getApplicationData().setKm(list.get(getAdapterPosition()).getKm());
                RegisterationDataHelper.getInstance().getApplicationData().setDepot(list.get(getAdapterPosition()).getDepotCd());

                b.putSerializable("rootData",list.get(getAdapterPosition()));
                Intent i = new Intent(context, FixPassFareCalculation.class);
                i.putExtras(b);
                v.getContext().startActivity(i);
                break;
            }
        }
    }
}
