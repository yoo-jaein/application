package com.example.myapplication.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.myapplication.R;

public class Help3Fragment extends Fragment {

    Animation animFadein;
    Animation animFadeout;

    ImageView image1;
    ImageView image2;

    private Handler handler;

    public Help3Fragment(){}

    void setAnimListenr() {

        animFadeout = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        animFadeout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d("test","animFadeout : Start");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("test","animFadeout : End");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animFadein = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        animFadein.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Log.d("test","animFadein : Start");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (animation == animFadein) {
                    Log.d("test","animFadein : End");
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            image2.startAnimation(animFadein);
        }
    };
    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            image1.startAnimation(animFadeout);
            handler.postDelayed(runnable1,2000);
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("test","Help Fragment 3 : OnCreateView");

        View view=inflater.inflate(R.layout.fragment_help3, container, false);

        image1=(ImageView)view.findViewById(R.id.guide3Image1);
        image2=(ImageView)view.findViewById(R.id.guide3Image2);

        handler = new Handler();

        setAnimListenr();

        image1.setVisibility(View.VISIBLE);
        image2.setVisibility(View.INVISIBLE);

        handler.postDelayed(runnable2,1000);

        return view;
    }


}
