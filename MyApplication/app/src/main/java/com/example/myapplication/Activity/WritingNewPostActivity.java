package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.PhysicalArchitecture.ClientController;
import com.example.myapplication.ProblemDomain.Constants;
import com.example.myapplication.ProblemDomain.Location;
import com.example.myapplication.ProblemDomain.Song;
import com.example.myapplication.R;

public class WritingNewPostActivity extends AppCompatActivity {

    private ClientController client;

    private Song song;
    private Location location;

    private ImageButton fin;
    private ImageButton del;

    private ImageButton postingPictureButton;
    private ImageButton postingVideoButton;
    private ImageButton postingMusicButton;
    private ImageButton postingLocationButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_new_post);

        fin=(ImageButton)findViewById(R.id.postingBack);
        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        del=(ImageButton)findViewById(R.id.postingFinish);
        del.setOnClickListener(new View.OnClickListener() {
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
                song = (Song)(data.getSerializableExtra("SONG"));

                Log.d("test", song.toString());
            }
            else if(requestCode == Constants.GET_LOCATION){
                location = (Location) (data.getSerializableExtra("LOCATION"));

                Log.d("test", location.toString());
            }
        }
    }

}
