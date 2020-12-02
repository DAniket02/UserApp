package com.example.userapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userapp.R;
import com.example.userapp.constants.ConstantHandler;
import com.example.userapp.model.SignInResponse;
import com.example.userapp.services.ApiClient;
import com.example.userapp.services.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private int PERMISSION_ALL = 1;
    private String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static String TAG = "LoginActivity";
    private EditText edittextUsername,edittextPassword;
    private Button btnLogin;
    private TextView textviewForgot,textviewSignUp;
    private String strToken;
    private HashMap<String,String> userdataMap = new HashMap<>();
    private SignInResponse signInResponseModel;
    ArrayList<SignInResponse.UserDetails> userDetailsArrayList = new ArrayList<>();
    private String strUserId, strUserName, strUserNameMarathi,strUserEmail,strUserMobile, strUserUsername, strUserAddress,strUserAddressMarathi,strProfilePic;

    private SharedPreferences sharedpreferences;
    private ProgressDialog progressDialog;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        mContext = LoginActivity.this;
        progressDialog = new ProgressDialog(mContext,R.style.MyAlertDialogStyle);
        sharedpreferences = getSharedPreferences(ConstantHandler.mypreference,
                Context.MODE_PRIVATE);

        Log.d(TAG, "onCreate: Username "+sharedpreferences.getString(ConstantHandler.USER_TOKEN, ""));

        edittextUsername = (EditText)findViewById(R.id.editText_username);
        edittextPassword = (EditText)findViewById(R.id.editText_password);
        textviewForgot = (TextView) findViewById(R.id.textView_forgot_password);
        textviewSignUp = (TextView)findViewById(R.id.textView_sign_up);
        btnLogin = (Button)findViewById(R.id.btnLogin);

        textviewForgot.setOnClickListener(this);
        textviewSignUp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

        if(sharedpreferences.contains(ConstantHandler.USER_EMAIL)){
            edittextUsername.setText(sharedpreferences.getString(ConstantHandler.USER_EMAIL, ""));
        }
        if(sharedpreferences.contains(ConstantHandler.USER_PASSWORD)){
            edittextPassword.setText(sharedpreferences.getString(ConstantHandler.USER_PASSWORD,""));
        }

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    private boolean hasPermissions(LoginActivity loginActivity, String[] permissions) {
        if (mContext != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.textView_forgot_password:
                startActivity(new Intent(mContext,ForgotPasswordActivity.class));
                break;
            case R.id.textView_sign_up:
                startActivity(new Intent(mContext,SignUpActivity.class));
                break;
            case R.id.btnLogin:
                validateAndSignIn();
                break;
        }
    }

    private void validateAndSignIn() {

        if(isOnline()){

            String username = edittextUsername.getText().toString();
            final String password = edittextPassword.getText().toString();

            if(username.equalsIgnoreCase("")){
                Toast.makeText(mContext, "Enter Username", Toast.LENGTH_SHORT).show();
                return;
            }else if(password.equalsIgnoreCase("")){
                Toast.makeText(mContext, "Enter Password", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog.setTitle("Signing In Please Wait..!!");
            progressDialog.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<SignInResponse> signInResponseCall = apiInterface.login_user(username,password);
            signInResponseCall.enqueue(new Callback<SignInResponse>() {
                @Override
                public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                    progressDialog.dismiss();
                    if(response.isSuccessful()){
                        signInResponseModel = response.body();
                        if(signInResponseModel.getStatus().equalsIgnoreCase("success")){

                            strToken = signInResponseModel.getToken().toString();
                            strUserId = signInResponseModel.getUserDetails().getId().toString();
                            strUserName = signInResponseModel.getUserDetails().getName().toString();
                            strUserEmail = signInResponseModel.getUserDetails().getEmail().toString();
                            strUserMobile = signInResponseModel.getUserDetails().getMobileNumber().toString();
                            strUserUsername = signInResponseModel.getUserDetails().getUsername().toString();
                            strUserNameMarathi = signInResponseModel.getUserDetails().getMarathiName().toString();
                            strUserAddress = signInResponseModel.getUserDetails().getAddress().toString();
                            strUserAddressMarathi = signInResponseModel.getUserDetails().getMarathiAddress().toString();
                            strProfilePic = signInResponseModel.getUserDetails().getProfilePic().toString();
                            startActivity(new Intent(mContext,NavigationActivity.class));
                            edittextPassword.setText("");
                            saveUserDetails(strToken,strUserId,strUserName,strUserUsername,strUserMobile,strUserEmail,strUserNameMarathi,strUserAddressMarathi,strUserAddress,password,strProfilePic);
                        }else {
                            Toast.makeText(mContext, signInResponseModel.getMessage() , Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<SignInResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.d(TAG, "onFailure: "+t.getMessage());
                    Toast.makeText(mContext, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    private void saveUserDetails(String strToken, String strUserId, String strUserName, String strUserUsername, String strUserMobile, String strUserEmail, String strUserNameMarathi, String strUserAddressMarathi, String strUserAddress, String password, String strProfilePic) {
        //save data into SharedPref
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(ConstantHandler.USER_TOKEN,strToken);
        editor.putString(ConstantHandler.USER_ID,strUserId);
        editor.putString(ConstantHandler.USER_NAME,strUserName);
        editor.putString(ConstantHandler.USER_UNAME,strUserUsername);
        editor.putString(ConstantHandler.USER_MOBILE,strUserMobile);
        editor.putString(ConstantHandler.USER_EMAIL,strUserEmail);
        editor.putString(ConstantHandler.USER_NAME_MARATHI,strUserNameMarathi);
        editor.putString(ConstantHandler.USER_ADDRESS_MARATHI,strUserAddressMarathi);
        editor.putString(ConstantHandler.USER_ADDRESS,strUserAddress);
        editor.putString(ConstantHandler.USER_PASSWORD,password);
        editor.putString(ConstantHandler.USER_PROFILE_PIC,strProfilePic);
        editor.commit();
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

    @Override
    protected void onResume() {
        if(sharedpreferences.contains(ConstantHandler.USER_PASSWORD)){
            edittextPassword.setText(sharedpreferences.getString(ConstantHandler.USER_PASSWORD,""));
        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
