package com.example.myapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.R;

public class ForgotPwActivity extends Activity {

    TextView txtbar;
    EditText emailtext;
    Button sendbutton;
    ImageButton exitbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forgot_pw);

        txtbar = (TextView) findViewById(R.id.forgot_pw_txt);
        emailtext = (EditText) findViewById(R.id.forgot_pw_email);
        sendbutton = (Button) findViewById(R.id.send_button);
        exitbutton = (ImageButton) findViewById(R.id.exit_button);
        exitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {     // 바깥레이어 클릭시 안닫히게
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {    // 안드로이드 백버튼 막기
        return;
    }


}
