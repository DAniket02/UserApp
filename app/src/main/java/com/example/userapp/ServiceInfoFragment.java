package com.example.userapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.userapp.adapter.ApplicationListAdapter;
import com.example.userapp.adapter.ServiceInfoAdapter;
import com.example.userapp.constants.ConstantHandler;


/**
 * A simple {@link Fragment} subclass.
 */
public class ServiceInfoFragment extends Fragment {


    private RecyclerView recyclerServiceInfo;
    private Context mContext;
    private SharedPreferences sharedpreferences;
    private ProgressDialog progressDialog;
    private ServiceInfoAdapter serviceInfoAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_service_info, container, false);
        mContext = getActivity();

        progressDialog = new ProgressDialog(mContext,R.style.MyAlertDialogStyle);
        sharedpreferences = mContext.getSharedPreferences(ConstantHandler.mypreference,
                Context.MODE_PRIVATE);
        String token = sharedpreferences.getString(ConstantHandler.USER_TOKEN, "");
        //getServiceInfo(token);
        recyclerServiceInfo = (RecyclerView)view.findViewById(R.id.recyclerServiceInfo);
        recyclerServiceInfo.setLayoutManager(new LinearLayoutManager(mContext));
        serviceInfoAdapter= new ServiceInfoAdapter(mContext);
        recyclerServiceInfo.setAdapter(serviceInfoAdapter);

        return view;
    }

    private void getServiceInfo(String token) {

    }

}
