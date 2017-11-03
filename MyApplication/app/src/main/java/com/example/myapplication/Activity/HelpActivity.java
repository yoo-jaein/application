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
import com.example.myapplication.R;

public class HelpActivity extends AppCompatActivity {

    private Handler handlers;

    ViewPager vp;

    ImageView select1;
    ImageView select2;
    ImageView select3;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                Intent intent = new Intent(HelpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }catch (Exception e) {
            }
        }
    };

    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            vp.setCurrentItem(1);
            select1.setImageResource(R.drawable.guideunselect);
            select2.setImageResource(R.drawable.guideselect);
        }
    };

    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            vp.setCurrentItem(2);
            select2.setImageResource(R.drawable.guideunselect);
            select3.setImageResource(R.drawable.guideselect);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        init();

        Button skip=(Button)findViewById(R.id.guideskipButton);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test","HelpActivity : skip Onclick Listener");
                handlers.removeMessages(0);
                Intent intent = new Intent(HelpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        select1=(ImageView)findViewById(R.id.guideselect1);
        select2=(ImageView)findViewById(R.id.guideselect2);
        select3=(ImageView)findViewById(R.id.guideselect3);

        select1.setImageResource(R.drawable.guideselect);

        handlers.postDelayed(runnable1,4000);
        handlers.postDelayed(runnable2,8000);
        handlers.postDelayed(runnable, 14000);

        vp = (ViewPager)findViewById(R.id.vp);
        vp.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);


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

