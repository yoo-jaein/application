package com.example.myapplication.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.PhysicalArchitecture.ClientController;
import com.example.myapplication.ProblemDomain.Constants;
import com.example.myapplication.ProblemDomain.Location;
import com.example.myapplication.ProblemDomain.Music;
import com.example.myapplication.ProblemDomain.Posts;
import com.example.myapplication.R;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.myapplication.ProblemDomain.Constants.GET_PICTURE_URI;

public class WritingNewPostActivity extends AppCompatActivity {

    private static Handler handler;

    private ClientController client;

    private Music music;
    private Location location;

    private ImageButton finButton;
    private ImageButton delButton;

    private ImageButton postingPictureButton;
    private ImageButton postingMusicButton;
    private ImageButton postingLocationButton;

    private TextView postingMusicText;
    private TextView postingLocationText;
    private EditText postingOpinionText;
    private Posts posts = new Posts();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_new_post);

        getWindow().setStatusBarColor(Color.parseColor("#516FA5"));

        client = ClientController.getClientControl();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== Constants.RECEIVE_SUCCESSS){
                    client.setHandlerNull();
                    finish();
                }
                else if(msg.what==Constants.RECEIVE_ERROR){
                    // TODO when received err message
                }
            }
        };

        postingMusicText = (TextView)findViewById(R.id.postingMusicText);
        postingLocationText = (TextView)findViewById(R.id.postingLocationText);
        postingOpinionText = (EditText)findViewById(R.id.postingOpinionText);

        finButton=(ImageButton)findViewById(R.id.postingFinish);
        finButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                posts.setLocationInfo(location);
                posts.setMusic(music);
                posts.setComment(postingOpinionText.getText().toString());
                posts.setUserID(client.getMe().getUserIndex());

                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

                posts.setCreateTime(sdfNow.format(date));

                client.setHandler(handler);
                client.post(posts);
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
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent, GET_PICTURE_URI);
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

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.GET_PICTURE_URI) {
                try {
                    Uri selPhotoUri = data.getData();
                    Bitmap selPhoto = MediaStore.Images.Media.getBitmap( getContentResolver(), selPhotoUri );

                    byte[] image=bitmapToByteArray(selPhoto);
                    posts.setIImage(image);

                    ImageView imageView = (ImageView) findViewById(R.id.postingPicture) ;
                    imageView.setImageBitmap(selPhoto);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(requestCode == Constants.GET_SONG) {
                music = (Music)(data.getSerializableExtra("SONG"));
                postingMusicText.setText(music.getArtistName() + " - " + music.getMusicName());
                Log.d("test", music.toString());
            }
            else if(requestCode == Constants.GET_LOCATION) {
                location = (Location) (data.getSerializableExtra("LOCATION"));
                postingLocationText.setText(location.getTitle());
                Log.d("test", location.toString());
            }
        }
    }

    public byte[] bitmapToByteArray( Bitmap bitmap ) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
        bitmap.compress( Bitmap.CompressFormat.JPEG, 100, stream) ;
        byte[] byteArray = stream.toByteArray() ;
        return byteArray ;
    }

}
