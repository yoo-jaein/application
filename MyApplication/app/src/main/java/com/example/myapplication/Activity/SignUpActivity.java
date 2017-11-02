package com.example.myapplication.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.PhysicalArchitecture.ClientController;
import com.example.myapplication.ProblemDomain.Constants;
import com.example.myapplication.R;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private EditText emailtext;
    private EditText pwtext;
    private EditText confirmpwtext;

    private ImageButton signupbutton;
    private ImageView signupgraybutton;

    private ImageView confirmcheck;
    private ImageView confirmcheckgray;

    private ClientController client;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        client = ClientController.getClientControl();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== Constants.RECEIVE_SUCCESSS){

                    client.removeHandler(this);

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else if(msg.what==Constants.RECEIVE_ERROR){
                    // TODO when received err message 회원가입 실패 시
                }
            }
        };

        signupbutton = (ImageButton) findViewById(R.id.signupbutton);
        signupgraybutton = (ImageView) findViewById(R.id.signupbutton_gray);

        signupbutton.setVisibility(View.INVISIBLE);
        signupgraybutton.setVisibility(View.VISIBLE);  // default gray visible


        confirmcheck = (ImageView) findViewById(R.id.confirm_check);
        confirmcheckgray = (ImageView) findViewById(R.id.confirm_check_gray);

        confirmcheck.setVisibility(View.INVISIBLE);
        confirmcheckgray.setVisibility(View.INVISIBLE);  // default gray visible


        emailtext = (EditText) findViewById(R.id.email_text);
        pwtext = (EditText) findViewById(R.id.pw_text);
        pwtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String emailstring = emailtext.getText().toString();
                String pwstring = pwtext.getText().toString();
                String confirmpwstring = confirmpwtext.getText().toString();

                if(!pwstring.equals(confirmpwstring)) {
                    confirmcheck.setVisibility(View.INVISIBLE);
                    confirmcheckgray.setVisibility(View.VISIBLE);

                    signupbutton.setVisibility(View.INVISIBLE);
                    signupgraybutton.setVisibility(View.VISIBLE);
                }
                else {
                    confirmcheck.setVisibility(View.VISIBLE);
                    confirmcheckgray.setVisibility(View.INVISIBLE);

                    signupbutton.setVisibility(View.VISIBLE);
                    signupgraybutton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        confirmpwtext = (EditText) findViewById(R.id.confirm_pw_text);
        confirmpwtext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String emailstring = emailtext.getText().toString();
                String pwstring = pwtext.getText().toString();
                String confirmpwstring = confirmpwtext.getText().toString();

                if (!pwstring.equals("") && (!confirmpwstring.equals(""))) {        // 비밀번호와 비밀번호 확인이 공백이 아니다
                    if (pwstring.equals(confirmpwstring)) {             // 비밀번호와 비밀번호 확인 string이 같다
                        confirmcheck.setVisibility(View.VISIBLE);
                        confirmcheckgray.setVisibility(View.INVISIBLE);

                        if(!emailstring.equals("")) {       // 모두 공백이 아니다
                            signupbutton.setVisibility(View.VISIBLE);
                            signupgraybutton.setVisibility(View.INVISIBLE);
                        }
                        else {
                            signupbutton.setVisibility(View.INVISIBLE);
                            signupgraybutton.setVisibility(View.VISIBLE);
                        }

                    }
                    else {
                        confirmcheck.setVisibility(View.INVISIBLE);
                        confirmcheckgray.setVisibility(View.VISIBLE);

                        signupbutton.setVisibility(View.INVISIBLE);
                        signupgraybutton.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailstring = emailtext.getText().toString();
                String pwstring = pwtext.getText().toString();
                String confirmpwstring = confirmpwtext.getText().toString();

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailstring).matches())    // 이메일 유효성 검사
                {
                    /*
                    if (   ???   )    // 이메일 유효성 검사2 - 중복
                    {
                        Toast.makeText(getApplicationContext(), "중복된 이메일입니다", Toast.LENGTH_SHORT).show();
                        emailtext.requestFocus();  // emailtext로 커서 focus 변경
                        return;
                    }
                    */
                    Toast.makeText(getApplicationContext(), "정확한 이메일주소를 입력해주세요", Toast.LENGTH_SHORT).show();
                    emailtext.requestFocus();  // emailtext로 커서 focus 변경
                    return;
                }

                if(!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{6,16}$", pwstring))     // 비밀번호 유효성 검사 - 영문, 숫자 ,특수문자를 모두 혼합하여 6~16자리
                {
                    Toast.makeText(getApplicationContext(),"유효하지 않은 비밀번호입니다",Toast.LENGTH_SHORT).show();
                    pwtext.requestFocus();

                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    pwtext.startAnimation(shake);

                    return;
                }
                client.addHandler(handler);
                client.register(emailstring, pwstring);

            }
        });


    }
}
