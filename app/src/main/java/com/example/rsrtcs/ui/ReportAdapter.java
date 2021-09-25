package com.example.rsrtcs.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rsrtcs.databinding.AdapterReportBinding;
import com.example.rsrtcs.model.response.CardModel;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder>{
    private Context context;
    private List<CardModel> list;

    public ReportAdapter(Context context,List<CardModel> list){
        this.context=context;
        this.list=list;
    }


    @NonNull
    @Override
    public ReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReportAdapter.MyViewHolder(AdapterReportBinding.inflate(LayoutInflater.from(context),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapter.MyViewHolder holder, int position) {
        holder.binding.setModel(list.get(position));
        holder.binding.executePendingBindings();

    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{
        AdapterReportBinding binding;

        public MyViewHolder(@NonNull AdapterReportBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
