package com.example.userapp.fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userapp.R;
import com.example.userapp.constants.ConstantHandler;
import com.example.userapp.model.ApplicationDetailsResponse;
import com.example.userapp.model.ApplicationListResponse;
import com.example.userapp.services.ApiClient;
import com.example.userapp.services.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ApplicationDetailFragment extends Fragment {

    private TextView textview_appDetail_appId;
    private TextView textview_appDetail_appDate,textview_appDetail_exp_completion_appDate,textview_appDetail_serviceName;
    private TextView textview_appDetail_applicantName,textview_appDetail_applicantNameMarathi,textview_appDetail_applicantMobile,textview_appDetail_applicantEmail;
    private TextView textview_appDetail_applicantAddress,textview_appDetail_applicantAddressMarathi,textview_appDetail_treeType;
    private TextView textview_appDetail_treeTotal,textview_appDetail_reason,textview_appDetail_ward,textview_appDetail_departmentName;
    private Button button_appDetail_status;
    private ImageView imageView_appDetail_pic1,imageView_appDetail_pic2,imageView_appDetail_pic3;
    private Context mContext;
    private int appId;
    private SharedPreferences sharedpreferences;
    public static String TAG = "ApplicationDetailFragment";
    private ProgressDialog progressDialog;
    private ApplicationDetailsResponse applicationDetailsResponse;
    private ApplicationDetailsResponse.ApplicationDetail applicationDetail;

    public ApplicationDetailFragment(int appId) {
        this.appId = appId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_application_detail, container, false);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        mContext = getActivity();
        progressDialog = new ProgressDialog(mContext,R.style.MyAlertDialogStyle);
        textview_appDetail_appId = (TextView)root.findViewById(R.id.textview_appDetail_appId);
        textview_appDetail_appDate = (TextView)root.findViewById(R.id.textview_appDetail_appDate);
        textview_appDetail_exp_completion_appDate = (TextView)root.findViewById(R.id.textview_appDetail_exp_completion_appDate);
        textview_appDetail_serviceName = (TextView)root.findViewById(R.id.textview_appDetail_serviceName);
        textview_appDetail_applicantName = (TextView)root.findViewById(R.id.textview_appDetail_applicantName);
        textview_appDetail_applicantNameMarathi = (TextView)root.findViewById(R.id.textview_appDetail_applicantNameMarathi);
        textview_appDetail_applicantMobile = (TextView)root.findViewById(R.id.textview_appDetail_applicantMobile);
        textview_appDetail_applicantEmail = (TextView)root.findViewById(R.id.textview_appDetail_applicantEmail);
        textview_appDetail_applicantAddress = (TextView)root.findViewById(R.id.textview_appDetail_applicantAddress);
        textview_appDetail_applicantAddressMarathi = (TextView)root.findViewById(R.id.textview_appDetail_applicantAddressMarathi);
        textview_appDetail_treeType = (TextView)root.findViewById(R.id.textview_appDetail_treeType);
        textview_appDetail_treeTotal = (TextView)root.findViewById(R.id.textview_appDetail_treeTotal);
        textview_appDetail_reason = (TextView)root.findViewById(R.id.textview_appDetail_reason);
        textview_appDetail_ward = (TextView)root.findViewById(R.id.textview_appDetail_ward);
        textview_appDetail_departmentName = (TextView)root.findViewById(R.id.textview_appDetail_departmentName);
        button_appDetail_status = (Button) root.findViewById(R.id.button_appDetail_status);

        imageView_appDetail_pic1 = (ImageView)root.findViewById(R.id.imageview_appDetail_pic1);
        imageView_appDetail_pic2 = (ImageView)root.findViewById(R.id.imageview_appDetail_pic2);
        imageView_appDetail_pic3 = (ImageView)root.findViewById(R.id.imageview_appDetail_pic3);

        Log.d("TAG", "onCreateView: ApplicationDetail : "+mContext);

        sharedpreferences = mContext.getSharedPreferences(ConstantHandler.mypreference,
                Context.MODE_PRIVATE);

        String token = sharedpreferences.getString(ConstantHandler.USER_TOKEN, "");

        getApplicationDetail(appId,token);

        return root;
    }

    private void getApplicationDetail(int appId,String token) {

        textview_appDetail_applicantName.setText(sharedpreferences.getString(ConstantHandler.USER_NAME,""));
        textview_appDetail_applicantNameMarathi.setText(sharedpreferences.getString(ConstantHandler.USER_NAME_MARATHI,""));
        textview_appDetail_applicantMobile.setText(sharedpreferences.getString(ConstantHandler.USER_MOBILE,""));
        textview_appDetail_applicantEmail.setText(sharedpreferences.getString(ConstantHandler.USER_EMAIL,""));
        textview_appDetail_applicantAddress.setText(sharedpreferences.getString(ConstantHandler.USER_ADDRESS,""));
        textview_appDetail_applicantAddressMarathi.setText(sharedpreferences.getString(ConstantHandler.USER_ADDRESS_MARATHI,""));
        Log.d(TAG, "getApplicationDetail: Application Id: "+appId);
        Log.d(TAG, "getApplicationDetail: Token : "+token);

        if(isOnline()){

            progressDialog.setTitle("Loading.. Please Wait..!!");
            progressDialog.show();

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<ApplicationDetailsResponse> detailsResponseCall = apiInterface.getApplicationsDetailsById("Bearer "+token,appId);
            detailsResponseCall.enqueue(new Callback<ApplicationDetailsResponse>() {
                @Override
                public void onResponse(Call<ApplicationDetailsResponse> call, Response<ApplicationDetailsResponse> response) {
                    progressDialog.dismiss();
                    if(response.isSuccessful()){
                        applicationDetailsResponse = response.body();
                        Log.d(TAG, "onResponse: Message : "+applicationDetailsResponse.getMessage());
                        if(applicationDetailsResponse.getStatus().equalsIgnoreCase("success")){
                            applicationDetail = applicationDetailsResponse.getData();

                            textview_appDetail_appId.setText("#"+applicationDetail.getId().toString());
                            String appDate = applicationDetail.getCreatedAt();
                            textview_appDetail_appDate.setText(appDate.substring(0,10));
                            String expDate = applicationDetail.getExpectedCompletedDate();
                            textview_appDetail_exp_completion_appDate.setText(expDate.substring(0,10));
                            if(applicationDetail.getType().equalsIgnoreCase("tree")){
                                textview_appDetail_serviceName.setText("Tree Cutting");
                            }else if(applicationDetail.getType().equalsIgnoreCase("branch")){
                                textview_appDetail_serviceName.setText("Trimming of Branches");
                            }

                            if(applicationDetail.getApplicationStatus()==1){
                                button_appDetail_status.setText("Active");
                                button_appDetail_status.setBackgroundColor(getResources().getColor(R.color.application_progress_yellow));
                            }

                            textview_appDetail_treeType.setText(applicationDetail.getPrajati());
                            textview_appDetail_treeTotal.setText(applicationDetail.getTreeTotal());
                            textview_appDetail_reason.setText(applicationDetail.getReason());
                            textview_appDetail_ward.setText(applicationDetail.getZone());
                            textview_appDetail_departmentName.setText(applicationDetail.getDepartment());

                            Log.d(TAG, "onResponse: Type "+applicationDetail.getPrajati());
                            Log.d(TAG, "onResponse: Total "+applicationDetail.getTreeTotal());
                            Log.d(TAG, "onResponse: reason "+applicationDetail.getReason());

                        }
                    }
                }

                @Override
                public void onFailure(Call<ApplicationDetailsResponse> call, Throwable t) {
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

}
