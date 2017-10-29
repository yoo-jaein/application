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

public class Help1Fragment extends Fragment {

    Animation animFadein;
    Animation animFadeout;

    ImageView image1;
    ImageView image2;

    private Handler handler;

    public Help1Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {
            image2.startAnimation(animFadein);
        }
    };

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_help1, container, false);

        Log.d("test","Help Fragment 1 : OnCreateView");
        image1=(ImageView)view.findViewById(R.id.guide1Image1);
        image2=(ImageView)view.findViewById(R.id.guide1Image2);
        handler = new Handler();

        setAnimListenr();

        image1.setVisibility(View.VISIBLE);
        image2.setVisibility(View.INVISIBLE);

        image1.startAnimation(animFadeout);
        handler.postDelayed(runnable1,1500);

        return view;
    }

}
