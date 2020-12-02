package com.example.userapp.activity.ui.dashboard;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.userapp.R;
import com.example.userapp.activity.NavigationActivity;
import com.example.userapp.constants.ConstantHandler;
import com.example.userapp.model.DashboardResponse;
import com.example.userapp.services.ApiClient;
import com.example.userapp.services.ApiInterface;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    private CircleImageView dashboard_profile_imageview;
    private TextView textView_dashboard_name,textView_dashboard_email,textView_dashboard_totalapplication,textView_dashboard_totalinprocess;
    private TextView textView_dashboard_totaldueforpayment,textView_dashboard_totalinpending,textView_dashboard_totalcompleted;
    private TextView textView_dashboard_tcp_totalinprocess,textView_dashboard_tcp_totaldueforpayment,textView_dashboard_tcp_totalinpending;
    private TextView textView_dashboard_tbp_totalinprocess, textView_dashboard_tbp_totaldueforpayment,textView_dashboard_tbp_totalinpending;
    private FloatingActionButton fabDashboard;
    private Context mContext;
    String name, email;
    private ProgressDialog progressDialog;
    private String token;
    private SharedPreferences sharedpreferences;
    private String strProfilePic;
    private static String TAG ="DashboardFragment" ;

    public DashboardFragment(Context mContext, String name, String email) {
        this.mContext = mContext;
        this.name = name;
        this.email = email;
    }

    public DashboardFragment(Context mContext) {
        this.mContext = mContext;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        progressDialog = new ProgressDialog(mContext, R.style.MyAlertDialogStyle);
        sharedpreferences = mContext.getSharedPreferences(ConstantHandler.mypreference,
                Context.MODE_PRIVATE);
        token = sharedpreferences.getString(ConstantHandler.USER_TOKEN, "");
        strProfilePic = sharedpreferences.getString(ConstantHandler.USER_PROFILE_PIC,"");

        dashboard_profile_imageview = (CircleImageView) root.findViewById(R.id.dashboard_profile_imageview);

        textView_dashboard_name = (TextView) root.findViewById(R.id.textView_dashboard_name);
        textView_dashboard_email = (TextView) root.findViewById(R.id.textView_dashboard_email);
        textView_dashboard_totalapplication = (TextView) root.findViewById(R.id.textView_dashboard_totalapplication);
        textView_dashboard_totalinprocess = (TextView) root.findViewById(R.id.textView_dashboard_totalinprocess);

        textView_dashboard_totaldueforpayment = (TextView) root.findViewById(R.id.textView_dashboard_totaldueforpayment);
        textView_dashboard_totalinpending = (TextView) root.findViewById(R.id.textView_dashboard_totalinpending);
        textView_dashboard_totalcompleted = (TextView) root.findViewById(R.id.textView_dashboard_totalcompleted);

        textView_dashboard_tcp_totalinprocess = (TextView) root.findViewById(R.id.textView_dashboard_tcp_totalinprocess);
        textView_dashboard_tcp_totaldueforpayment = (TextView) root.findViewById(R.id.textView_dashboard_tcp_totaldueforpayment);
        textView_dashboard_tcp_totalinpending = (TextView) root.findViewById(R.id.textView_dashboard_tcp_totalinpending);

        textView_dashboard_tbp_totalinprocess = (TextView) root.findViewById(R.id.textView_dashboard_tbp_totalinprocess);
        textView_dashboard_tbp_totaldueforpayment = (TextView) root.findViewById(R.id.textView_dashboard_tbp_totaldueforpayment);
        textView_dashboard_tbp_totalinpending = (TextView) root.findViewById(R.id.textView_dashboard_tbp_totalinpending);

        getLocalData();



        //getDashboardData(token);

        return root;
    }

    private void getLocalData() {
        name = sharedpreferences.getString(ConstantHandler.USER_NAME,"");
        email = sharedpreferences.getString(ConstantHandler.USER_EMAIL,"");
        strProfilePic = sharedpreferences.getString(ConstantHandler.USER_PROFILE_PIC,"");
        if(name != null && email !=null){
            textView_dashboard_name.setText(name);
            textView_dashboard_email.setText(email);
        }
        Log.d(TAG, "onCreateView: Image "+strProfilePic);
        if(!strProfilePic.equalsIgnoreCase("")){
            Picasso.get().load(strProfilePic).into(dashboard_profile_imageview);
        }

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onResume() {
        getLocalData();
        super.onResume();
    }

    private void getDashboardData(String token) {

        if(isOnline()){

            progressDialog.setTitle("Getting Receipt.. Please Wait..!!");
            progressDialog.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<DashboardResponse> dashboardResponseCall = apiInterface.getDashboardDetails("Bearer "+token);
            dashboardResponseCall.enqueue(new Callback<DashboardResponse>() {
                @Override
                public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                    progressDialog.dismiss();
                    if(response.isSuccessful()){

                    }
                }

                @Override
                public void onFailure(Call<DashboardResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(mContext, t.toString(), Toast.LENGTH_SHORT).show();
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
}