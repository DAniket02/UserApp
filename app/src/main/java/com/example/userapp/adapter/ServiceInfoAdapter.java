package com.example.userapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapp.R;

public class ServiceInfoAdapter extends RecyclerView.Adapter<ServiceInfoAdapter.InfoViewHolder> {

    Context mContext;

    public ServiceInfoAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public InfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.service_info_item_layout, parent, false);
        return new InfoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InfoViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10 ;
    }

    public class InfoViewHolder extends RecyclerView.ViewHolder {

        TextView textViewServiceInfoPoint;

        public InfoViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewServiceInfoPoint = (TextView)itemView.findViewById(R.id.textViewServiceInfoPoint);
        }
    }
}
