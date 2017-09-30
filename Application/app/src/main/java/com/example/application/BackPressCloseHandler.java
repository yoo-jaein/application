package com.example.application;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by 유재인 on 2017-08-15.
 */

public class BackPressCloseHandler {

    private long backKeyPressedTime = 0;
    private Toast toast;

    private Activity activity;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finishAffinity();
            System.runFinalizersOnExit(true);
            System.exit(0);
            toast.cancel();
        }
    }

    public void showGuide() {
        toast = Toast.makeText(activity,
                "뒤로 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }
}