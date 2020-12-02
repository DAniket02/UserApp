package com.example.userapp.activity.ui.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapp.R;
import com.example.userapp.activity.NavigationActivity;
import com.example.userapp.adapter.ApplicationListAdapter;
import com.example.userapp.constants.ConstantHandler;
import com.example.userapp.model.ApplicationListResponse;
import com.example.userapp.services.ApiClient;
import com.example.userapp.services.ApiInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyApplicationFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerApplicationList;
    private Context mContext;
    private ApplicationListAdapter applicationListAdapter;
    private ApplicationListResponse applicationListResponse;
    private SharedPreferences sharedpreferences;
    private ProgressDialog progressDialog;
    public static String TAG = "MyApplicationFragment";
    private ArrayList<ApplicationListResponse.RequestList> requestListArrayList ;
    private FloatingActionButton fabDashboard;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_myapplication, container, false);
        mContext = getActivity();
        progressDialog = new ProgressDialog(mContext,R.style.MyAlertDialogStyle);
        sharedpreferences = mContext.getSharedPreferences(ConstantHandler.mypreference,
                Context.MODE_PRIVATE);

        Log.d(TAG, "onCreate: Username "+sharedpreferences.getString(ConstantHandler.USER_TOKEN, ""));
        String token = sharedpreferences.getString(ConstantHandler.USER_TOKEN, "");
        getApplicationLists(token);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        recyclerApplicationList = (RecyclerView)root.findViewById(R.id.recyclerApplicationList);
        fabDashboard = (FloatingActionButton)root.findViewById(R.id.fab_dashboard);
        fabDashboard.setOnClickListener(this);

        return root;
    }

    private void getApplicationLists(String token) {

        if(isOnline()){

            progressDialog.setTitle("Loading.. Please Wait..!!");
            progressDialog.show();

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<ApplicationListResponse> listResponseCall = apiInterface.getApplicationsList("Bearer "+token);
            listResponseCall.enqueue(new Callback<ApplicationListResponse>() {
                @Override
                public void onResponse(Call<ApplicationListResponse> call, Response<ApplicationListResponse> response) {
                    progressDialog.dismiss();
                    if(response.isSuccessful()){
                        applicationListResponse = response.body();
                        if(applicationListResponse.getStatus().equalsIgnoreCase("success")){
                            requestListArrayList = new ArrayList<>();
                            for (int i = 0; i < applicationListResponse.getRequestList().size(); i++) {
                                requestListArrayList.add(applicationListResponse.getRequestList().get(i));
                            }
                            Collections.sort(requestListArrayList, new Comparator<ApplicationListResponse.RequestList>() {
                                @Override
                                public int compare(ApplicationListResponse.RequestList t1, ApplicationListResponse.RequestList t2) {
                                    return t2.getId().compareTo(t1.getId());
                                }
                            });
                            recyclerApplicationList.setLayoutManager(new LinearLayoutManager(mContext));
                            applicationListAdapter= new ApplicationListAdapter(mContext,requestListArrayList);
                            recyclerApplicationList.setAdapter(applicationListAdapter);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ApplicationListResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(mContext, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public boolean isOnline() {

        ConnectivityManager conMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            Toast.makeText(mContext, "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab_dashboard:
                if (mContext instanceof NavigationActivity) {
                    ((NavigationActivity)mContext).goToHome();
                }
                break;
        }
    }

}