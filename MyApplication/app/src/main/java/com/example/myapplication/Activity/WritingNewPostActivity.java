package com.example.myapplication.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.example.myapplication.PhysicalArchitecture.ClientController;
import com.example.myapplication.R;

public class WritingNewPostActivity extends AppCompatActivity {

    ClientController client;

    ImageButton fin;
    ImageButton del;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_new_post);

        fin=(ImageButton)findViewById(R.id.postingback);
        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        del=(ImageButton)findViewById(R.id.postingfinish);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*client = ClientController.getClientControl();*/
    }

}
