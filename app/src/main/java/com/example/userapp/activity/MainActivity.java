package com.example.userapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.userapp.R;
import com.example.userapp.adapter.SlidingImageAdapter;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static ViewPager viewPager;
    private LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;
    private Button button_apply_tree_cutting,button_apply_branch_trimming;
    private Context mContext;
    private ImageView imageviewDashboard;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext  = MainActivity.this;
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        button_apply_tree_cutting = (Button)findViewById(R.id.button_apply_tree_cutting);
        button_apply_branch_trimming = (Button)findViewById(R.id.button_apply_branch_trimming);
        imageviewDashboard = (ImageView)findViewById(R.id.imageviewDashboard); 

        button_apply_tree_cutting.setOnClickListener(this);
        button_apply_branch_trimming.setOnClickListener(this);
        imageviewDashboard.setOnClickListener(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        sliderDotspanel = (LinearLayout) findViewById(R.id.SliderDots);
        SlidingImageAdapter slidingImageAdapter = new SlidingImageAdapter(this);
        viewPager.setAdapter(slidingImageAdapter);

        dotscount = slidingImageAdapter.getCount();
        dots = new ImageView[dotscount];

        for(int i = 0; i < dotscount; i++){

            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);

            sliderDotspanel.addView(dots[i], params);

        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i = 0; i< dotscount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.non_active_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_apply_tree_cutting:
                Bundle bundle = new Bundle();
                bundle.putInt("SERVICE",1);
                Intent intent = new Intent(this,ServiceActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.button_apply_branch_trimming:
                Bundle bundle1 = new Bundle();
                bundle1.putInt("SERVICE",2);
                Intent intent1 = new Intent(this,ServiceActivity.class);
                intent1.putExtras(bundle1);
                startActivity(intent1);
                break;
            case R.id.imageviewDashboard:
                startActivity(new Intent(mContext,NavigationActivity.class));
                break;
        }
    }
}
