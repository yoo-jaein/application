package com.example.myapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.BackPressCloseHandler;
import com.example.myapplication.Fragment.LikeListFragment;
import com.example.myapplication.Fragment.MyPageFragment;
import com.example.myapplication.Fragment.TimeLineFragment;
import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity implements Animation.AnimationListener, View.OnClickListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private BackPressCloseHandler backPressCloseHandler;
    private ViewPager mViewPager;

    boolean option = false;

    private FloatingActionButton goToWritingButton;

    private ImageButton mSettingButton;

    private ImageButton optionButton;
    private ImageButton timeOrderButton;
    private ImageButton distanceOrderButton;
    private ImageButton likeOrderButton;

    private FrameLayout frameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckAppFirstExecute();
/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        backPressCloseHandler = new BackPressCloseHandler(this);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListView timeline=(ListView)findViewById(R.id.timeline);
                timeline.smoothScrollToPosition(0);
            }
        });

        goToWritingButton = (FloatingActionButton)findViewById(R.id.goToWritingButton);

        goToWritingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test","goToWritingbutton onclick listener :");

                Intent intent = new Intent(getApplicationContext(), WritingNewPostActivity.class);
                startActivity(intent);
            }
        });

        timeOrderButton = (ImageButton)findViewById(R.id.timeOrderButton);
        timeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        distanceOrderButton = (ImageButton)findViewById(R.id.distanceOrderButton);
        distanceOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        likeOrderButton = (ImageButton)findViewById(R.id.likeOrderButton);
        likeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        optionButton=(ImageButton)findViewById(R.id.mainoptionButton);
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test","MainActivity : optionButton OnClickListner");

                if(!option) {
                    showOptionAnim();
                }
                else{
                    hideOptionAnim();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(option){
            hideOptionAnim();
        }
    }

    public void showOptionAnim(){
        option = true;

        timeOrderButton.setVisibility(View.VISIBLE);
        distanceOrderButton.setVisibility(View.VISIBLE);
        likeOrderButton.setVisibility(View.VISIBLE);

        TranslateAnimation animTime = new TranslateAnimation(optionButton.getX(), optionButton.getX(), optionButton.getY(), optionButton.getY() + 200);
        animTime.setFillAfter(true);
        animTime.setDuration(1000);

        TranslateAnimation animDistance = new TranslateAnimation(optionButton.getX(), optionButton.getX(), optionButton.getY(), optionButton.getY() + 400);
        animDistance.setFillAfter(true);
        animDistance.setDuration(1000);

        TranslateAnimation animLike = new TranslateAnimation(optionButton.getX(), optionButton.getX(), optionButton.getY(), optionButton.getY() + 600);
        animLike.setFillAfter(true);
        animLike.setDuration(1000);

        long time = AnimationUtils.currentAnimationTimeMillis();

        timeOrderButton.invalidate();
        distanceOrderButton.invalidate();
        likeOrderButton.invalidate();

        animTime.setStartTime(time);
        animDistance.setStartTime(time);
        animLike.setStartTime(time);

        timeOrderButton.setAnimation(animTime);
        distanceOrderButton.setAnimation(animDistance);
        likeOrderButton.setAnimation(animLike);
    }

    public void hideOptionAnim(){
        option = false;

        TranslateAnimation animTime = new TranslateAnimation(optionButton.getX(), optionButton.getX(), timeOrderButton.getY(), optionButton.getY());
        animTime.setFillAfter(true);
        animTime.setDuration(1000);

        TranslateAnimation animDistance = new TranslateAnimation(optionButton.getX(), optionButton.getX(), distanceOrderButton.getY(), optionButton.getY());
        animDistance.setFillAfter(true);
        animDistance.setDuration(1000);

        TranslateAnimation animLike = new TranslateAnimation(optionButton.getX(), optionButton.getX(), likeOrderButton.getY(), optionButton.getY());
        animLike.setFillAfter(true);
        animLike.setDuration(1000);

        long time = AnimationUtils.currentAnimationTimeMillis();

        timeOrderButton.invalidate();
        distanceOrderButton.invalidate();
        likeOrderButton.invalidate();

        animTime.setStartTime(time);
        animDistance.setStartTime(time);
        animLike.setStartTime(time);

        timeOrderButton.setAnimation(animTime);
        distanceOrderButton.setAnimation(animDistance);
        likeOrderButton.setAnimation(animLike);

        timeOrderButton.setVisibility(View.GONE);
        distanceOrderButton.setVisibility(View.GONE);
        likeOrderButton.setVisibility(View.GONE);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

        mSettingButton=(ImageButton)findViewById(R.id.mainmenuButton);
        mSettingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

    //앱최초실행확인 (true - 최초실행)
    public boolean CheckAppFirstExecute(){
        SharedPreferences pref = getSharedPreferences("IsFirst" , Activity.MODE_PRIVATE);
        boolean isFirst = pref.getBoolean("isFirst", false);
        if(!isFirst){ //if first run, go to HelpActivity
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFirst", true);
            editor.commit();

            Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
            startActivity(intent);
        }
        return !isFirst;
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return new TimeLineFragment();
                case 1: return new LikeListFragment();
                case 2: return new MyPageFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
          return "";
        }
    }
}
