package com.example.rstc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    Context context;
    List<pojoClass> list = new ArrayList<>();

    public CustomAdapter(Context context, List<pojoClass> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomAdapter.ViewHolder holder, int position) {

        holder.routeName.setText(list.get(position).getRoutename());
        holder.rootNo.setText(list.get(position).getRouteno());
        holder.kmm.setText(list.get(position).getKm());
        holder.depo.setText(list.get(position).getDepotcd());


        holder.vv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle b = new Bundle();
                b.putSerializable("rootData",list.get(position));
              //  Intent i = new Intent(context,FareCalculation.class);
                Intent i = new Intent(context,FixPassFareCalculation.class);
                i.putExtras(b);
                v.getContext().startActivity(i);
              //  ((Activity)context).finish();

               // v.getContext().startActivity(new Intent(context,FareCalculation.class));

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView routeName,rootNo,kmm,depo;
        View vv;
        public ViewHolder( View itemView) {
            super(itemView);

            routeName = itemView.findViewById(R.id.route_name);
            rootNo = itemView.findViewById(R.id.route_no);
            kmm = itemView.findViewById(R.id.km);
            depo = itemView.findViewById(R.id.depot);

            vv = itemView;
        }
    }
}
