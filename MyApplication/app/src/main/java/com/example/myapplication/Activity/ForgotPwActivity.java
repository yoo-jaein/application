package com.example.myapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.PhysicalArchitecture.ClientController;
import com.example.myapplication.ProblemDomain.Constants;
import com.example.myapplication.R;

public class ForgotPwActivity extends Activity {

    private TextView txtbar;
    private EditText emailtext;
    private Button sendbutton;
    private ImageButton exitbutton;

    private TextView txtdefault;
    private TextView txtfail;

    private ClientController client;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forgot_pw);

        client = ClientController.getClientControl();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== Constants.RECEIVE_SUCCESSS){
                    client.setHandler(null);
                    Toast.makeText(getApplicationContext(), "비밀번호 전송 완료", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else if(msg.what==Constants.RECEIVE_ERROR){
                    // TODO when received err message 회원가입 실패 시
                }
            }
        };

        txtbar = (TextView) findViewById(R.id.forgot_pw_txt);
        emailtext = (EditText) findViewById(R.id.forgot_pw_email);
        txtdefault = (TextView) findViewById(R.id.textView_default);
        txtfail = (TextView) findViewById(R.id.textView_fail);
        sendbutton = (Button) findViewById(R.id.send_button);

        txtdefault.setVisibility(View.VISIBLE);      // default
        txtfail.setVisibility(View.INVISIBLE);

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailstring = emailtext.getText().toString();

                client.setHandler(handler);
                client.findPass(emailtext.getText().toString());

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailstring).matches())    // 이메일 유효성 검사
                {
                    txtdefault.setVisibility(View.INVISIBLE);
                    txtfail.setVisibility(View.VISIBLE);        // fail

                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    emailtext.startAnimation(shake);

                    emailtext.requestFocus();  // emailtext로 커서 focus 변경
                    return;
                }
            }
        });

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
