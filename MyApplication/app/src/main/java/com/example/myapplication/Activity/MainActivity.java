package com.example.myapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.BackPressCloseHandler;
import com.example.myapplication.Fragment.LikeListFragment;
import com.example.myapplication.Fragment.MyPageFragment;
import com.example.myapplication.Fragment.TimeLineFragment;
import com.example.myapplication.PhysicalArchitecture.ClientController;
import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private BackPressCloseHandler backPressCloseHandler;
    private ViewPager mViewPager;

    private ClientController client;

    boolean option =false;

    private ImageButton op1;
    private ImageButton op2;
    private ImageButton op3;

    private FloatingActionButton goToWritingButton;

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

        /*client = ClientController.getClientControl();*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WritingNewPostActivity.class);
                startActivity(intent);
                //finish();
            }
        });

        op1=(ImageButton)findViewById(R.id.mainoptionone);
        op2=(ImageButton)findViewById(R.id.mainoptiontwo);
        op3=(ImageButton)findViewById(R.id.mainoptionthree);

        goToWritingButton = (FloatingActionButton)findViewById(R.id.goToWritingButton);

        op1.setVisibility(View.INVISIBLE);
        op2.setVisibility(View.INVISIBLE);
        op3.setVisibility(View.INVISIBLE);

        goToWritingButton.setVisibility(View.VISIBLE);

        goToWritingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test","goToWritingbutton onclick listener :");

                Intent intent = new Intent(getApplicationContext(), WritingNewPostActivity.class);
                startActivity(intent);
            }
        });

        ImageButton optionButton=(ImageButton)findViewById(R.id.mainoptionButton);
        optionButton.bringToFront();
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test","option button onclick listener :");

                if(option) {
                    Log.d("test", ""+ option);
                    op1.setVisibility(View.INVISIBLE);
                    op2.setVisibility(View.INVISIBLE);
                    op3.setVisibility(View.INVISIBLE);
                } else {
                    op1.setVisibility(View.VISIBLE);
                    op2.setVisibility(View.VISIBLE);
                    op3.setVisibility(View.VISIBLE);
                    op1.bringToFront();
                    op2.bringToFront();
                    op3.bringToFront();
                    Log.d("test", ""+ option);

                }
                option = !option;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        protected SectionsPagerAdapter(FragmentManager fm) {
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
