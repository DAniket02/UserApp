package com.example.userapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.userapp.R;
import com.example.userapp.activity.ui.dashboard.DashboardFragment;
import com.example.userapp.activity.ui.myapplication.MyApplicationFragment;
import com.example.userapp.activity.ui.myprofile.MyProfileFragment;
import com.example.userapp.activity.ui.setting.SettingFragment;
import com.example.userapp.constants.ConstantHandler;
import com.example.userapp.fragment.ApplicationDetailFragment;
import com.example.userapp.model.ApplicationListResponse;
import com.example.userapp.model.DownloadCertificateResponse;
import com.example.userapp.model.ViewReceiptResponse;
import com.example.userapp.services.ApiClient;
import com.example.userapp.services.ApiInterface;
import com.example.userapp.util.Contents;
import com.example.userapp.util.QRCodeEncoder;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigationActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    boolean doubleBackToExitPressedOnce = false;
    private Context mContext;
    private SharedPreferences sharedpreferences;
    private String name,email;
    private ApplicationListResponse applicationListResponse;
    private ProgressDialog progressDialog;
    private String token;
    private ViewReceiptResponse viewReceiptResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        mContext = NavigationActivity.this;
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //loading the default fragment
        progressDialog = new ProgressDialog(mContext, R.style.MyAlertDialogStyle);
        toolbar.setTitle("Dashboard");
        sharedpreferences = getSharedPreferences(ConstantHandler.mypreference,
                Context.MODE_PRIVATE);
        if(sharedpreferences.contains(ConstantHandler.USER_NAME) && sharedpreferences.contains(ConstantHandler.USER_EMAIL)){
             name = sharedpreferences.getString(ConstantHandler.USER_NAME,"");
             email = sharedpreferences.getString(ConstantHandler.USER_EMAIL,"");
            loadFragment(new DashboardFragment(mContext,name,email));
        }else {
            loadFragment(new DashboardFragment(mContext));
        }

        //getting bottom navigation view and attaching the listener
        FrameLayout container = (FrameLayout)findViewById(R.id.fragment_container);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_profile:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
        if(backStackCount >= 1){
            getSupportFragmentManager().popBackStack();
            // Change to hamburger icon if at bottom of stack
            if(backStackCount == 1){
                finish();
            }
        }else {
            super.onBackPressed();
        }


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.navigation_dashboard:
                fragment = new DashboardFragment(mContext, name, email);
                toolbar.setTitle("Dashboard");
                getSupportFragmentManager().popBackStack();
                break;

            case R.id.navigation_my_application:
                fragment = new MyApplicationFragment();
                toolbar.setTitle("My Applications");
                getSupportFragmentManager().popBackStack();
                break;

            case R.id.navigation_setting:
                fragment = new SettingFragment();
                toolbar.setTitle("Setting");
                getSupportFragmentManager().popBackStack();
                break;

            case R.id.navigation_profile:
                fragment = new MyProfileFragment();
                toolbar.setTitle("Profile");
                getSupportFragmentManager().popBackStack();
                break;

            case R.id.navigation_logout:
                showAlertDialog();
                break;
        }

        return loadFragment(fragment);
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setMessage("Do you want to log out ?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        sharedpreferences.edit().remove(ConstantHandler.USER_PASSWORD).apply();
                        startActivity(new Intent(mContext,LoginActivity.class));
                        finish();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack("fragment")
                    .commit();
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void openApplicationDetail(Long appId) {
        Toast.makeText(this, ""+appId, Toast.LENGTH_SHORT).show();
        Fragment fragment = new ApplicationDetailFragment(Math.toIntExact(appId));
        loadFragment(fragment);
    }

    public void downloadApplicationCertificate(String appId) {
        //Toast.makeText(this, "Certificate will get download", Toast.LENGTH_SHORT).show();
        //downloadCertificate(appId);
        showDownloadDialog(appId);
    }

    private void downloadCertificate(String appId) {

        if(isOnline()){

            progressDialog.setTitle("Getting Receipt.. Please Wait..!!");
            progressDialog.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<DownloadCertificateResponse> certificateResponseCall = apiInterface.downloadCertificate("Bearer "+token,appId);
            certificateResponseCall.enqueue(new Callback<DownloadCertificateResponse>() {
                @Override
                public void onResponse(Call<DownloadCertificateResponse> call, Response<DownloadCertificateResponse> response) {
                    progressDialog.dismiss();
                    if(response.isSuccessful()){
                        //showDownloadDialog(appId);
                    }
                }

                @Override
                public void onFailure(Call<DownloadCertificateResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(mContext, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void showDownloadDialog(String appId) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
        builder1.setTitle("Download Certificate "+appId);
        builder1.setMessage("Do you want to Download Certificate ?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void viewApplicationReceipt(String appId) {
        Toast.makeText(this, appId, Toast.LENGTH_SHORT).show();
        showReceipt();
        /*if(isOnline()){

            progressDialog.setTitle("Fetching Details.. Please Wait..!!");
            progressDialog.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<ViewReceiptResponse> viewReceiptResponseCall = apiInterface.getReceiptDetails("Bearer " + token,appId);
            viewReceiptResponseCall.enqueue(new Callback<ViewReceiptResponse>() {
                @Override
                public void onResponse(Call<ViewReceiptResponse> call, Response<ViewReceiptResponse> response) {
                    progressDialog.dismiss();
                    if(response.isSuccessful()){

                    }
                }

                @Override
                public void onFailure(Call<ViewReceiptResponse> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(mContext, t.toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }*/


    }

    private void showReceipt() {
        String strTest = "https://cdattechnologies.com/";
        LayoutInflater li = LayoutInflater.from(mContext);
        View promptsView = li.inflate(R.layout.receipt_layout, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setView(promptsView);
        final TextView textview_receipt_number = (TextView)promptsView.findViewById(R.id.textview_receipt_number);
        final TextView textview_receipt_appid = (TextView)promptsView.findViewById(R.id.textview_receipt_appid);
        final TextView textview_receipt_date = (TextView)promptsView.findViewById(R.id.textview_receipt_date);
        final TextView textview_receipt_payplace = (TextView)promptsView.findViewById(R.id.textview_receipt_payplace);
        final TextView textview_receipt_appNo = (TextView)promptsView.findViewById(R.id.textview_receipt_appNo);
        final TextView textview_receipt_paymode = (TextView)promptsView.findViewById(R.id.textview_receipt_paymode);
        final TextView textview_receipt_nocopies = (TextView)promptsView.findViewById(R.id.textview_receipt_nocopies);
        final TextView textview_receipt_percopy = (TextView)promptsView.findViewById(R.id.textview_receipt_percopy);
        final TextView textview_receipt_amount = (TextView)promptsView.findViewById(R.id.textview_receipt_amount);
        final TextView textview_receipt_latefee = (TextView)promptsView.findViewById(R.id.textview_receipt_latefee);
        final TextView textview_receipt_gstno = (TextView)promptsView.findViewById(R.id.textview_receipt_gstno);

        final ImageView imageviewCloseReceipt = (ImageView)promptsView.findViewById(R.id.imageviewCloseReceipt);
        final ImageView imageViewQR = (ImageView) promptsView.findViewById(R.id.receipt_imageview_qr);

        //Find screen size
        /*WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3/4;

        //Encode with a QR Code image
        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(strTest,
                null,
                Contents.Type.TEXT,
                BarcodeFormat.QR_CODE.toString(),
                smallerDimension);
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            imageViewQR.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }*/

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        imageviewCloseReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }

    public void goToHome() {
        startActivity(new Intent(mContext,MainActivity.class));
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
