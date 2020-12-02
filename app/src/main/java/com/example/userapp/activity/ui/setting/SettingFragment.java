package com.example.userapp.activity.ui.setting;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.userapp.R;
import com.example.userapp.constants.ConstantHandler;
import com.example.userapp.model.ChangePasswordResponse;
import com.example.userapp.model.ForgotPasswordResponse;
import com.example.userapp.services.ApiClient;
import com.example.userapp.services.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment implements View.OnClickListener {

    private EditText editTextUsername_setting,editTextPassword_setting,editTextConfirmPassword_setting;
    private Button btnChangePassword;
    private String strUsername, strPassword, strConfirmPassword;
    private Context mContext;
    private String token;
    private SharedPreferences sharedpreferences;
    private ProgressDialog progressDialog;
    private ChangePasswordResponse changePasswordResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        mContext = getActivity();
        progressDialog = new ProgressDialog(mContext);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        editTextUsername_setting = (EditText)root.findViewById(R.id.editTextFullName_setting);
        editTextPassword_setting = (EditText)root.findViewById(R.id.editTextPassword_setting);
        editTextConfirmPassword_setting = (EditText)root.findViewById(R.id.editTextConfirmPassword_setting);
        btnChangePassword = (Button)root.findViewById(R.id.btnChangePassword);

        sharedpreferences = mContext.getSharedPreferences(ConstantHandler.mypreference,
                Context.MODE_PRIVATE);

        btnChangePassword.setOnClickListener(this);
        token = sharedpreferences.getString(ConstantHandler.USER_TOKEN, "");
        if(sharedpreferences.contains(ConstantHandler.USER_NAME)){
            editTextUsername_setting.setText(sharedpreferences.getString(ConstantHandler.USER_NAME,""));
        }

        return root;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnChangePassword:
                validate();
               // Toast.makeText(getActivity(), "Change password", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void validate() {

        strUsername = editTextUsername_setting.getText().toString();
        strPassword = editTextPassword_setting.getText().toString();
        strConfirmPassword = editTextConfirmPassword_setting.getText().toString();

        if(strUsername.equalsIgnoreCase("")){
            editTextUsername_setting.setError("Enter Username");
            return;
        }else if(strPassword.equalsIgnoreCase("")){
            editTextPassword_setting.setError("Enter Password");
            return;
        }else if(strConfirmPassword.equalsIgnoreCase("")){
            editTextConfirmPassword_setting.setError("Confirm password");
            return;
        }else if(!strPassword.matches(strConfirmPassword)){
            editTextConfirmPassword_setting.setError("Passwords are not matching");
            return;
        }else {
            changePassword(token,strConfirmPassword);
        }
    }

    private void changePassword(String token, final String strConfirmPassword) {

        if(isOnline()){

            progressDialog.setTitle("Updating Password Please Wait..!!");
            progressDialog.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<ChangePasswordResponse> changePasswordResponseCall = apiInterface.updatePassword("Bearer "+token,strConfirmPassword);
            changePasswordResponseCall.enqueue(new Callback<ChangePasswordResponse>() {
                @Override
                public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                    progressDialog.dismiss();
                    if(response.isSuccessful()){
                        if(response.body().getStatus().equalsIgnoreCase("success")){
                            changePasswordResponse = response.body();
                            Toast.makeText(mContext, changePasswordResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            editTextPassword_setting.setText("");
                            editTextConfirmPassword_setting.setText("");
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(ConstantHandler.USER_PASSWORD,strConfirmPassword);
                            editor.commit();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
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
