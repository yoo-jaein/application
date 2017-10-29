package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.myapplication.Fragment.Help1Fragment;
import com.example.myapplication.Fragment.Help2Fragment;
import com.example.myapplication.Fragment.Help3Fragment;
import com.example.myapplication.PhysicalArchitecture.ClientController;
import com.example.myapplication.R;

public class IntroActivity extends AppCompatActivity {

    ClientController client;

    private Handler handler;


    ViewPager vp;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    };

    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            vp.setCurrentItem(1);
        }
    };

    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            vp.setCurrentItem(2);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        init();

        client = ClientController.getClientControl();
        client.client.start();

        handler.postDelayed(runnable1,4000);
        handler.postDelayed(runnable2,8000);
        handler.postDelayed(runnable, 14000);

        vp = (ViewPager)findViewById(R.id.vp);
        vp.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);


    }


    public void init() {
        handler = new Handler();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        handler.removeCallbacks(runnable);
    }

}

class pagerAdapter extends FragmentStatePagerAdapter
{
    public pagerAdapter(android.support.v4.app.FragmentManager fm)
    {
        super(fm);
    }
    @Override
    public android.support.v4.app.Fragment getItem(int position)
    {
        switch(position)
        {
            case 0:
                return new Help1Fragment();
            case 1:
                return new Help2Fragment();
            case 2:
                return new Help3Fragment();
            default:
                return null;
        }
    }
    @Override
    public int getCount()
    {
        return 3;
    }
}

