package com.example.myapplication.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.BackPressCloseHandler;
import com.example.myapplication.Fragment.LikeListFragment;
import com.example.myapplication.Fragment.MyPageFragment;
import com.example.myapplication.Fragment.TimeLineFragment;
import com.example.myapplication.PhysicalArchitecture.ClientController;
import com.example.myapplication.ProblemDomain.Constants;
import com.example.myapplication.R;
import com.example.myapplication.Service.GPSInfo;

public class MainActivity extends AppCompatActivity{

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

    private GPSInfo gpsInfo;
    private ClientController client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 //////////////////////////////////////////////////////////////////       CheckAppFirstExecute();
/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        client = ClientController.getClientControl();

        gpsInfo = new GPSInfo(MainActivity.this);
        // GPS 사용유무 가져오기
        if (gpsInfo.isGetLocation()) {

            double latitude = gpsInfo.getLatitude();
            double longitude = gpsInfo.getLongitude();

        } else {
            // GPS 를 사용할수 없으므로
            gpsInfo.showSettingsAlert();
        }

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
        //timeOrderButton.setVisibility(View.INVISIBLE);
        timeOrderButton.setAlpha(0f);
        timeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test", "timeorder button click");
                client.setTimeLineOrder(Constants.TIME);
                client.setHandler(((TimeLineFragment)(mSectionsPagerAdapter.getItem(0))).getHandler());
                client.refresh();
            }
        });

        distanceOrderButton = (ImageButton)findViewById(R.id.distanceOrderButton);
        //distanceOrderButton.setVisibility(View.INVISIBLE);
        distanceOrderButton.setAlpha(0f);
        distanceOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test", "distanceorder button click");
                client.setTimeLineOrder(Constants.DISTANCE);
                client.setHandler(((TimeLineFragment)(mSectionsPagerAdapter.getItem(0))).getHandler());
                client.refresh();
            }
        });
        likeOrderButton = (ImageButton)findViewById(R.id.likeOrderButton);
        //likeOrderButton.setVisibility(View.INVISIBLE);
        likeOrderButton.setAlpha(0f);
        likeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test", "likeorder button click");
                client.setTimeLineOrder(Constants.LIKE);
                client.setHandler(((TimeLineFragment)(mSectionsPagerAdapter.getItem(0))).getHandler());
                client.refresh();
            }
        });

        optionButton=(ImageButton)findViewById(R.id.mainoptionButton);
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!option) {
                    showOptionAnim();
                }
                else{
                    hideOptionAnim();
                }
            }
        });

        mSettingButton=(ImageButton)findViewById(R.id.mainmenuButton);
        mSettingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showOptionAnim(){
        option = true;

        timeOrderButton.animate().translationY(170).setDuration(500).withLayer();
        timeOrderButton.animate().alpha(1f).setDuration(500).setListener((new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                timeOrderButton.setVisibility(View.VISIBLE);
                //OR
                timeOrderButton.setAlpha(1f);
            }
        }));
        distanceOrderButton.animate().translationY(280).setDuration(500).withLayer();
        distanceOrderButton.animate().alpha(1000).setDuration(500).setListener((new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                timeOrderButton.setVisibility(View.VISIBLE);
                //OR
                timeOrderButton.setAlpha(1f);
            }
        }));;
        likeOrderButton.animate().translationY(390).setDuration(500).withLayer();
        likeOrderButton.animate().alpha(1f).setDuration(500).setListener((new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                timeOrderButton.setVisibility(View.VISIBLE);
                //OR
                timeOrderButton.setAlpha(1f);
            }
        }));
    }

    private void hideOptionAnim(){
        option = false;

        timeOrderButton.animate().translationY(0).setDuration(500).withLayer();
        timeOrderButton.animate().alpha(0f).setDuration(500).setListener((new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                timeOrderButton.setVisibility(View.VISIBLE);
                //OR
                timeOrderButton.setAlpha(0f);
            }
        }));
        distanceOrderButton.animate().translationY(0).setDuration(500).withLayer();
        distanceOrderButton.animate().alpha(0f).setDuration(500).setListener((new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                distanceOrderButton.setVisibility(View.VISIBLE);
                //OR
                distanceOrderButton.setAlpha(0f);
            }
        }));
        likeOrderButton.animate().translationY(0).setDuration(500).withLayer();
        likeOrderButton.animate().alpha(0f).setDuration(500).setListener((new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                likeOrderButton.setVisibility(View.VISIBLE);
                //OR
                likeOrderButton.setAlpha(0f);
            }
        }));
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

/*
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
*/

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

    /*
    public void showOptionAnim(){
        option = true;

        timeOrderButton.setVisibility(View.VISIBLE);
        distanceOrderButton.setVisibility(View.VISIBLE);
        likeOrderButton.setVisibility(View.VISIBLE);

        Log.d("test", "show time pos : " + optionButton.getLeft() + ", " + optionButton.getTop() +
                " ==> " + optionButton.getLeft() + ", " + (optionButton.getTop() + 150));
        TranslateAnimation animTime = new TranslateAnimation(optionButton.getLeft(), optionButton.getLeft(), optionButton.getTop(), optionButton.getTop() + 150);
        //animTime.setFillAfter(true);
        animTime.setFillEnabled(true);
        animTime.setDuration(1000);
        animTime.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("test", "show time layout : " + timeOrderButton.getLeft() + ", " + (timeOrderButton.getTop() + 160) +
                        " / " + timeOrderButton.getRight() + ", " + (timeOrderButton.getBottom() + 160));
                timeOrderButton.layout(timeOrderButton.getLeft(), timeOrderButton.getTop() + 160, timeOrderButton.getRight(), timeOrderButton.getBottom() + 160);
                timeOrderButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        Log.d("test", "show distance pos : " + optionButton.getLeft() + ", " +  optionButton.getTop() + " " +
                "==> " + optionButton.getLeft() + ", " + (optionButton.getTop() + 270));
        TranslateAnimation animDistance = new TranslateAnimation(optionButton.getLeft(), optionButton.getLeft(), optionButton.getTop(), optionButton.getTop() + 270);
        //animDistance.setFillAfter(true);
        animDistance.setFillEnabled(true);
        animDistance.setDuration(1000);
        animDistance.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("test", "show distance layout : " + distanceOrderButton.getLeft() + "," + (distanceOrderButton.getTop() + 270) +
                        " / " + distanceOrderButton.getRight() + "," + (distanceOrderButton.getBottom() + 270));
                distanceOrderButton.layout(distanceOrderButton.getLeft(), distanceOrderButton.getTop() + 270, distanceOrderButton.getRight(), distanceOrderButton.getBottom() + 250);
                distanceOrderButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        Log.d("test", "show like pos : " + optionButton.getLeft() + "," + optionButton.getTop() +
                " ==> " + optionButton.getLeft() + "," + (optionButton.getTop() + 380));
        TranslateAnimation animLike = new TranslateAnimation(optionButton.getLeft(), optionButton.getLeft(), optionButton.getTop(), optionButton.getTop() + 380);
        //animLike.setFillAfter(true);
        animLike.setFillEnabled(true);
        animLike.setDuration(1000);
        animLike.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("test", "show like layout : " + likeOrderButton.getLeft() + "," + (likeOrderButton.getTop() + 380) +
                        " / " + likeOrderButton.getRight() + "," + (likeOrderButton.getBottom() + 380));
                likeOrderButton.layout(likeOrderButton.getLeft(), likeOrderButton.getTop() + 380, likeOrderButton.getRight(), likeOrderButton.getBottom() + 380);
                likeOrderButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

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

        Log.d("test", "hide time pos : " + optionButton.getX() + ", " + timeOrderButton.getY() + " ==> " + optionButton.getX() + ", " + optionButton.getY());
        TranslateAnimation animTime = new TranslateAnimation(optionButton.getX(), optionButton.getX(), timeOrderButton.getY(), optionButton.getY());
        //animTime.setFillAfter(true);
        animTime.setFillEnabled(true);
        animTime.setDuration(1000);
        animTime.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                timeOrderButton.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        Log.d("test", "hide time pos : " + optionButton.getX() + "," + distanceOrderButton.getY() + " ==> " + optionButton.getX() + "," + optionButton.getY());
        TranslateAnimation animDistance = new TranslateAnimation(optionButton.getX(), optionButton.getX(), distanceOrderButton.getY(), optionButton.getY());
        //animDistance.setFillAfter(true);
        animDistance.setFillEnabled(true);
        animDistance.setDuration(1000);
        animDistance.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                distanceOrderButton.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        Log.d("test", "hide time pos : " + optionButton.getX() + "," + likeOrderButton.getY() + " ==> " + optionButton.getX() + "," + optionButton.getY());
        TranslateAnimation animLike = new TranslateAnimation(optionButton.getX(), optionButton.getX(), likeOrderButton.getY(), optionButton.getY());
        //animLike.setFillAfter(true);
        animLike.setFillEnabled(true);
        animLike.setDuration(1000);
        animLike.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                likeOrderButton.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

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
*/
}
