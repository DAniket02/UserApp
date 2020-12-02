package com.example.userapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userapp.R;
import com.example.userapp.model.ForgotPasswordResponse;
import com.example.userapp.services.ApiClient;
import com.example.userapp.services.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText editTextFPEmail;
    TextView textviewFPViewSignIn;
    Button btnFPSendLink;
    Context mContext;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressDialog progressDialog;
    ForgotPasswordResponse forgotPasswordResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot_password);
        mContext = ForgotPasswordActivity.this;
        progressDialog = new ProgressDialog(mContext);
        editTextFPEmail = (EditText)findViewById(R.id.editTextFPEmail);
        textviewFPViewSignIn = (TextView)findViewById(R.id.textviewFPViewSignIn);
        btnFPSendLink = (Button)findViewById(R.id.btnFPSendLink);
        
        btnFPSendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextFPEmail.getText().toString();
                
                if(email.equalsIgnoreCase("")){
                    Toast.makeText(mContext, "Enter Email Id", Toast.LENGTH_SHORT).show();
                }else if(!email.matches(emailPattern)){
                    Toast.makeText(mContext, "Enter Valid Email Id", Toast.LENGTH_SHORT).show();
                }else {
                    sendPasswordResetLink(email);
                }
            }
        });

        textviewFPViewSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void sendPasswordResetLink(String email) {

        if(isOnline()){

            progressDialog.setTitle("Sending Link Please Wait..!!");
            progressDialog.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<ForgotPasswordResponse> forgotPasswordResponseCall = apiInterface.sendPasswordResetLink(email);
            forgotPasswordResponseCall.enqueue(new Callback<ForgotPasswordResponse>() {
                @Override
                public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {
                    progressDialog.dismiss();
                    if(response.isSuccessful()){
                        if(response.body().getStatus().equalsIgnoreCase("success")){
                            forgotPasswordResponse = response.body();
                            Toast.makeText(mContext, forgotPasswordResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                }

                @Override
                public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(mContext, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
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
