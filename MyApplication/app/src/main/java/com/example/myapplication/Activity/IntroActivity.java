package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.myapplication.Fragment.Help1Fragment;
import com.example.myapplication.Fragment.Help2Fragment;
import com.example.myapplication.Fragment.Help3Fragment;
import com.example.myapplication.PhysicalArchitecture.ClientController;
import com.example.myapplication.R;

public class IntroActivity extends AppCompatActivity {

    ClientController client;

    private Handler handlers;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
           Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        init();

        client = ClientController.getClientControl();
        client.client.start();

        handlers.postDelayed(runnable, 3000);

    }


    public void init() {
        handlers = new Handler();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        handlers.removeCallbacks(runnable);
    }

}