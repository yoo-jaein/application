package com.example.myapplication.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
        getWindow().setStatusBarColor(Color.parseColor("#516FA5"));

        gpsInfo = new GPSInfo(MainActivity.this);
        // GPS 사용유무 가져오기
        if (gpsInfo.isGetLocation()) {
            double latitude = gpsInfo.getLatitude();
            double longitude = gpsInfo.getLongitude();
        } else {
            // GPS 를 사용할수 없으므로
            gpsInfo.showSettingsAlert();
        }

        client.setGpsInfo(gpsInfo);

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
        timeOrderButton.setVisibility(View.GONE);
        timeOrderButton.setAlpha(0f);
        timeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test", "timeorder button click");
                client.setTimeLineOrder(Constants.TIME);
                if(mSectionsPagerAdapter.getTimeLine() != null) {
                    if(mSectionsPagerAdapter.getTimeLine().getHandler() == null){
                        Log.d("handler", "timeline handler is null");
                    }
                    else {
                        Log.d("handler", "timeline handler is not null");
                        client.setTimeLineHandler(mSectionsPagerAdapter.getTimeLine().getHandler());
                    }
                }
                else
                    Log.d("test", "timeLine is null");
                client.refresh();

                hideOptionAnim();
            }
        });

        distanceOrderButton = (ImageButton)findViewById(R.id.distanceOrderButton);
        distanceOrderButton.setVisibility(View.GONE);
        distanceOrderButton.setAlpha(0f);
        distanceOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test", "distanceorder button click");
                client.setTimeLineOrder(Constants.DISTANCE);
                if(mSectionsPagerAdapter.getTimeLine() != null)
                    client.setTimeLineHandler(mSectionsPagerAdapter.getTimeLine().getHandler());
                else
                    Log.d("test", "timeLine is null");
                client.refresh();

                hideOptionAnim();
            }
        });
        likeOrderButton = (ImageButton)findViewById(R.id.likeOrderButton);
        likeOrderButton.setVisibility(View.GONE);
        likeOrderButton.setAlpha(0f);
        likeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test", "likeorder button click");
                client.setTimeLineOrder(Constants.LIKE);
                if(mSectionsPagerAdapter.getTimeLine() != null)
                    client.setTimeLineHandler(mSectionsPagerAdapter.getTimeLine().getHandler());
                else
                    Log.d("test", "timeLine is null");
                client.refresh();

                hideOptionAnim();
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

        timeOrderButton.setVisibility(View.VISIBLE);
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
        distanceOrderButton.setVisibility(View.VISIBLE);
        distanceOrderButton.animate().translationY(280).setDuration(500).withLayer();
        distanceOrderButton.animate().alpha(1000).setDuration(500).setListener((new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                distanceOrderButton.setVisibility(View.VISIBLE);
                //OR
                distanceOrderButton.setAlpha(1f);
            }
        }));
        likeOrderButton.setVisibility(View.VISIBLE);
        likeOrderButton.animate().translationY(390).setDuration(500).withLayer();
        likeOrderButton.animate().alpha(1f).setDuration(500).setListener((new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                likeOrderButton.setVisibility(View.VISIBLE);
                //OR
                likeOrderButton.setAlpha(1f);
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
                timeOrderButton.setVisibility(View.GONE);
                //OR
                timeOrderButton.setAlpha(0f);
            }
        }));
        distanceOrderButton.animate().translationY(0).setDuration(500).withLayer();
        distanceOrderButton.animate().alpha(0f).setDuration(500).setListener((new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                distanceOrderButton.setVisibility(View.GONE);
                //OR
                distanceOrderButton.setAlpha(0f);
            }
        }));
        likeOrderButton.animate().translationY(0).setDuration(500).withLayer();
        likeOrderButton.animate().alpha(0f).setDuration(500).setListener((new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                likeOrderButton.setVisibility(View.GONE);
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

        TimeLineFragment timeLineFragment = null;
        LikeListFragment likeListFragment = null;
        MyPageFragment myPageFragment = null;

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:/*
                    Log.d("fragment", "timeline selected");
                    if (timeLineFragment == null) {
                        timeLineFragment = new TimeLineFragment();
                        return timeLineFragment;
                    }
                    else return timeLineFragment;*/
                    return new TimeLineFragment();
                case 1:/*
                    Log.d("fragment", "mylikelist selected");
                    if (likeListFragment == null) {
                        likeListFragment = new LikeListFragment();
                        return likeListFragment;
                    }
                    else return likeListFragment;*/
                    return new LikeListFragment();
                case 2:/*
                    Log.d("fragment", "mypage selected");
                    if (myPageFragment == null) {
                        myPageFragment = new MyPageFragment();
                        return myPageFragment;
                    }
                    else return myPageFragment;*/
                    return new MyPageFragment();
            }
            return null;
        }

        public TimeLineFragment getTimeLine(){
            return timeLineFragment;
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
