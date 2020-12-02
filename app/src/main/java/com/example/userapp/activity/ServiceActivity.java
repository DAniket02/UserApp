package com.example.userapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.userapp.R;
import com.example.userapp.ServiceInfoFragment;
import com.example.userapp.fragment.ApplyServiceFragment;

public class ServiceActivity extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout relative_apply_for_service,relative_service_info;
    FrameLayout frame_service_container;
    private Toolbar toolbar;
    private Context mContext;
    private Bundle bundle;
    private TextView textviewServiceInfo,textviewApplyService;
    private String textType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        mContext = ServiceActivity.this;
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bundle = getIntent().getExtras();
        int id = bundle.getInt("SERVICE");
        if(id==1){
            getSupportActionBar().setTitle(getString(R.string.text_service_1));
            textType = "tree";
        }else if(id==2){
            getSupportActionBar().setTitle(getString(R.string.text_service_2));
            textType = "trim";

        }

        loadFragment(new ApplyServiceFragment(textType,ServiceActivity.this));
        relative_apply_for_service = (RelativeLayout)findViewById(R.id.relative_apply_for_service);
        relative_service_info = (RelativeLayout)findViewById(R.id.relative_service_info);
        frame_service_container = (FrameLayout) findViewById(R.id.frame_service_container);
        relative_apply_for_service.setOnClickListener(this);
        relative_service_info.setOnClickListener(this);

        textviewServiceInfo = (TextView)findViewById(R.id.textviewServiceInfo);
        textviewApplyService = (TextView)findViewById(R.id.textviewApplyService);


    }

    private void loadFragment(Fragment fragment) {

        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_service_container, fragment)
                    .commit();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.relative_apply_for_service:
                relative_apply_for_service.setBackgroundColor(getResources().getColor(R.color.red_font1));
                textviewApplyService.setTextColor(getResources().getColor(R.color.white_color));
                textviewServiceInfo.setTextColor(getResources().getColor(R.color.black_font1));
                relative_service_info.setBackgroundColor(getResources().getColor(R.color.inactive_tab));
                loadFragment(new ApplyServiceFragment(textType, ServiceActivity.this));
                break;
            case R.id.relative_service_info:
                relative_service_info.setBackgroundColor(getResources().getColor(R.color.red_font1));
                textviewServiceInfo.setTextColor(getResources().getColor(R.color.white_color));
                textviewApplyService.setTextColor(getResources().getColor(R.color.black_font1));
                relative_apply_for_service.setBackgroundColor(getResources().getColor(R.color.inactive_tab));
                loadFragment(new ServiceInfoFragment());
                break;
        }
    }
}