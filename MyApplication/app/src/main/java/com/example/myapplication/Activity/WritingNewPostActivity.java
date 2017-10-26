package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.PhysicalArchitecture.ClientController;
import com.example.myapplication.ProblemDomain.Constants;
import com.example.myapplication.ProblemDomain.Location;
import com.example.myapplication.ProblemDomain.Music;
import com.example.myapplication.ProblemDomain.Posts;
import com.example.myapplication.R;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WritingNewPostActivity extends AppCompatActivity {

    private static Handler handler;

    private ClientController client;

    private Music music;
    private Location location;

    private ImageButton finButton;
    private ImageButton delButton;

    private ImageButton postingPictureButton;
    private ImageButton postingVideoButton;
    private ImageButton postingMusicButton;
    private ImageButton postingLocationButton;

    private TextView postingMusicText;
    private TextView postingLocationText;
    private EditText postingOpinionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_new_post);

        postingMusicText = (TextView)findViewById(R.id.postingMusicText);
        postingLocationText = (TextView)findViewById(R.id.postingLocationText);
        postingOpinionText = (EditText)findViewById(R.id.postingOpinionText);

        finButton=(ImageButton)findViewById(R.id.postingFinish);
        finButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Posts posts = new Posts();
                posts.setLocationInfo(location);
                posts.setMusic(music);
                posts.setComment(postingOpinionText.getText().toString());
        //        posts.setUserID(client.getMe().getUserIndex());

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

                posts.setCreateTime(sdfNow.format(date));

                Log.d("test", posts.toString());

                handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if(msg.what== Constants.RECEIVE_SUCCESSS){
                            client.setHandlerNull();
                            finish();
                        }
                        else if(msg.what==Constants.RECEIVE_FAILED){
                            // TODO when received err message
                        }
                    }
                };

                /*
                client.setHandler(handler);
                client.post(posts);
                */

                finish();
            }
        });

        delButton=(ImageButton)findViewById(R.id.postingBack);
        delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        postingPictureButton = (ImageButton)findViewById(R.id.postingPictureButton);
        postingPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        postingVideoButton = (ImageButton)findViewById(R.id.postingVideoButton);
        postingVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        postingMusicButton = (ImageButton)findViewById(R.id.postingMusicButton);
        postingMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchMusicActivity.class);

                startActivityForResult(intent, Constants.GET_SONG);
            }
        });

        postingLocationButton = (ImageButton)findViewById(R.id.postingLocationButton);
        postingLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SearchLocationActivity.class);
                intent.putExtra("MODE", Constants.SEARCH_MAP_MODE);

                startActivityForResult(intent, Constants.GET_LOCATION);
            }
        });
        /*client = ClientController.getClientControl();*/
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == Constants.GET_SONG){
                music = (Music)(data.getSerializableExtra("SONG"));
                postingMusicText.setText(music.getArtistName() + " - " + music.getMusicName());
                Log.d("test", music.toString());
            }
            else if(requestCode == Constants.GET_LOCATION){
                location = (Location) (data.getSerializableExtra("LOCATION"));
                postingLocationText.setText(location.getTitle());
                Log.d("test", location.toString());
            }
        }
    }


}
