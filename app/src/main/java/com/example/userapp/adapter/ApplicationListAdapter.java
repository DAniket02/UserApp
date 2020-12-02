package com.example.userapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapp.R;
import com.example.userapp.activity.NavigationActivity;
import com.example.userapp.model.ApplicationListResponse;

import java.util.ArrayList;

public class ApplicationListAdapter extends RecyclerView.Adapter<ApplicationListAdapter.ApplicationViewHolder> {

    Context mContext;
    Fragment fragment = null;
    ArrayList<ApplicationListResponse.RequestList> requestListArrayList;

    public ApplicationListAdapter(Context mContext, ArrayList<ApplicationListResponse.RequestList> requestListArrayList) {
        this.mContext = mContext;
        this.requestListArrayList = requestListArrayList;
    }

    @NonNull
    @Override
    public ApplicationListAdapter.ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_application_list_item, parent, false);
        return new ApplicationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ApplicationListAdapter.ApplicationViewHolder holder, final int position) {

        if(requestListArrayList !=null){

            holder.textViewApplicationNumber.setText("#"+requestListArrayList.get(position).getId().toString());
            String appDate = requestListArrayList.get(position).getCreatedAt();
            holder.textViewApplicationDate.setText(appDate.substring(0,10));
            String expDate = requestListArrayList.get(position).getUpdatedAt();
            holder.textViewApplicationCompletionDate.setText(expDate.substring(0,10));
            if(requestListArrayList.get(position).getType().equalsIgnoreCase("tree")){
                holder.textViewApplicationName.setText("Tree Cutting");
            }else if(requestListArrayList.get(position).getType().equalsIgnoreCase("branch")){
                holder.textViewApplicationName.setText("Trimming of Branches");
            }

            if(requestListArrayList.get(position).getApplicationStatus() == 1){
                holder.btnStatus.setText("Active");
                holder.btnStatus.setBackgroundColor(holder.btnStatus.getContext().getResources().getColor(R.color.application_progress_yellow));
                holder.btnDownloadCertificate.setVisibility(View.GONE);
            }

            holder.btnViewRecipt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mContext instanceof NavigationActivity) {
                        ((NavigationActivity)mContext).viewApplicationReceipt(requestListArrayList.get(position).getId().toString());
                    }
                }
            });

            holder.btnDownloadCertificate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mContext instanceof NavigationActivity) {
                        ((NavigationActivity)mContext).downloadApplicationCertificate(requestListArrayList.get(position).getId().toString());
                    }
                }
            });

            holder.cardviewApplication.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mContext instanceof NavigationActivity) {
                        ((NavigationActivity)mContext).openApplicationDetail(requestListArrayList.get(position).getId());
                    }
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return requestListArrayList.size();
    }

    public class ApplicationViewHolder extends RecyclerView.ViewHolder  {

        Button btnStatus, btnViewRecipt,btnDownloadCertificate;
        TextView textViewApplicationDate,textViewApplicationCompletionDate,textViewApplicationName,textViewApplicationNumber;
        CardView cardviewApplication;

        public ApplicationViewHolder(@NonNull final View itemView) {
            super(itemView);

            btnStatus = (Button)itemView.findViewById(R.id.btn_applicationStatus);
            btnViewRecipt = (Button)itemView.findViewById(R.id.btn_applicationViewReceipt);
            btnDownloadCertificate = (Button)itemView.findViewById(R.id.btn_applicationDownloadCertificate);

            textViewApplicationDate  = (TextView)itemView.findViewById(R.id.textView_applicationDate);
            textViewApplicationCompletionDate  = (TextView)itemView.findViewById(R.id.textView_applicationCompletedDate);
            textViewApplicationName  = (TextView)itemView.findViewById(R.id.textView_applicationName);
            textViewApplicationNumber  = (TextView)itemView.findViewById(R.id.textView_applicationId);

            cardviewApplication = (CardView)itemView.findViewById(R.id.cardview_application);

        }
    }
}
