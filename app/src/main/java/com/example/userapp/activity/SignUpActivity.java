package com.example.userapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userapp.R;
import com.example.userapp.model.SignInResponse;
import com.example.userapp.model.SignUpResponse;
import com.example.userapp.services.ApiClient;
import com.example.userapp.services.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextFullName,editTextFullNameMarathi,editTextMobile,editTextEmail,editTextAddress,editTextAddressMarathi;
    private EditText editTextPassword,editTextConfirmPassword;
    private Button btnSignUp;
    private TextView textViewSignIn;
    private Context mContext;

    private String strFullName, strFullNameMarathi, strMobileNo, strEmailId, strAddress, strAddressMarathi;
    private String strUsername, strPassword, strConfirmPassword;
    private SignUpResponse signUpResponse;
    public static String TAG = "SignUpActivity";
    private ProgressDialog progressDialog;
    //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String emailPattern = "^(.+)@(.+)$";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        mContext = SignUpActivity.this;
        progressDialog = new ProgressDialog(mContext);
        editTextFullName = (EditText)findViewById(R.id.editTextFullName);
        editTextFullNameMarathi = (EditText)findViewById(R.id.editTextFullNameMarathi);
        editTextMobile = (EditText)findViewById(R.id.editTextMobile);
        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextAddress = (EditText)findViewById(R.id.editTextAddress);
        editTextAddressMarathi = (EditText)findViewById(R.id.editTextAddressMarathi);
        editTextPassword = (EditText)findViewById(R.id.editTextPassword);
        editTextConfirmPassword = (EditText)findViewById(R.id.editTextConfirmPassword);
        textViewSignIn = (TextView)findViewById(R.id.textViewSignIn);
        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        btnSignUp.setOnClickListener(this);
        textViewSignIn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btnSignUp:
                validate();
                //Toast.makeText(mContext, "SignUp", Toast.LENGTH_SHORT).show();
                break;
            case R.id.textViewSignIn:
                startActivity(new Intent(mContext,LoginActivity.class));
                finish();
                break;
        }
    }

    private void validate() {

        strFullName = editTextFullName.getText().toString();
        strFullNameMarathi = editTextFullNameMarathi.getText().toString();
        strMobileNo = editTextMobile.getText().toString();
        strEmailId = editTextEmail.getText().toString();
        strAddress = editTextAddress.getText().toString();
        strAddressMarathi = editTextAddressMarathi.getText().toString();
        strPassword = editTextPassword.getText().toString();
        strConfirmPassword = editTextConfirmPassword.getText().toString();

        if(strFullName.equalsIgnoreCase("")){
            Toast.makeText(mContext, "Enter fullname", Toast.LENGTH_SHORT).show();
            return;
        }else if(strMobileNo.equalsIgnoreCase("")){
            Toast.makeText(mContext, "Enter mobile number", Toast.LENGTH_SHORT).show();
            return;
        }else if(strEmailId.equalsIgnoreCase("")){
            Toast.makeText(mContext, "Enter emailID", Toast.LENGTH_SHORT).show();
            return;
        } else if(!strEmailId.matches(emailPattern)){
            Toast.makeText(mContext, "Enter Valid Email Address", Toast.LENGTH_SHORT).show();
        } else if(strAddress.equalsIgnoreCase("")){
            Toast.makeText(mContext, "Enter Address", Toast.LENGTH_SHORT).show();
            return;
        } else if(strPassword.equalsIgnoreCase("")){
            Toast.makeText(mContext, "Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }else if(strConfirmPassword.equalsIgnoreCase("")){
            Toast.makeText(mContext, "Confirm password", Toast.LENGTH_SHORT).show();
            return;
        }else if(!strPassword.matches(strConfirmPassword)){
            Toast.makeText(mContext, "Passwords are not matching", Toast.LENGTH_SHORT).show();
            return;
        }else {
            userSignUp(strFullName,strFullNameMarathi,strMobileNo,strEmailId,strAddress,strAddressMarathi,strPassword);
        }
    }

    private void userSignUp(String strFullName, String strFullNameMarathi, String strMobileNo, String strEmailId, String strAddress, String strAddressMarathi, String strPassword) {

        if(isOnline()){

            progressDialog.setTitle("Signing Up Please Wait..!!");
            progressDialog.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<SignUpResponse> signUpResponseCall = apiInterface.registerUser(strFullName,strFullNameMarathi,strMobileNo,strEmailId,strAddress,strAddressMarathi,strPassword);
            signUpResponseCall.enqueue(new Callback<SignUpResponse>() {
                @Override
                public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {
                    progressDialog.dismiss();
                    if(response.isSuccessful()){
                        if(response.body().getStatus().equalsIgnoreCase("success")){
                            signUpResponse = response.body();
                            Toast.makeText(mContext, signUpResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(mContext,LoginActivity.class));
                            finish();
                        }else {
                            Toast.makeText(mContext, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SignUpResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d(TAG, "onFailure: "+t.getMessage().toString());
                }
            });
        }
    }

    public boolean isOnline() {

        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
            Toast.makeText(mContext, "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
