package com.example.myapplication.CustomAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.PhysicalArchitecture.ClientController;
import com.example.myapplication.PhysicalArchitecture.ImageController;
import com.example.myapplication.ProblemDomain.Constants;
import com.example.myapplication.ProblemDomain.Music;
import com.example.myapplication.ProblemDomain.Posts;
import com.example.myapplication.ProblemDomain.User;
import com.example.myapplication.R;

import java.util.ArrayList;

/**
 * Created by YoungJu on 2017-09-30.
 */

public class CustomAdapter extends BaseAdapter {

    private ArrayList<Posts> contentslist = new ArrayList<Posts>();
    private ArrayList<Integer> likeList;
    private ClientController client = null;
    private int cnt = 0;

    public CustomAdapter(ArrayList<Posts> contentslist, User user) {
        Log.d("test", "CustomAdapter : start CustomAdapter");
        this.contentslist = contentslist;
        this.likeList = user.getLikeList();

        client = ClientController.getClientControl();
    }

    @Override
    public int getCount() {
        try {
            return contentslist.size();
        } catch (Exception e) {
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return contentslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        Log.d("test", "CustomAdapter: getView + position:" + position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_posts, parent, false);
        }
        final TextView content = (TextView) convertView.findViewById(R.id.post_text);
        final TextView location = (TextView) convertView.findViewById(R.id.post_location);
        final TextView music = (TextView) convertView.findViewById(R.id.post_music);
        final TextView time = (TextView) convertView.findViewById(R.id.post_time);

        final ImageView postimage = (ImageView) convertView.findViewById(R.id.post_image);
        final ImageButton likebutton = (ImageButton) convertView.findViewById(R.id.likeButton);
        final ImageButton unlikebutton = (ImageButton) convertView.findViewById(R.id.unlikeButton);
        final Posts posts = contentslist.get(position);

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what== Constants.RECEIVE_SUCCESSS){

                }
                else if(msg.what==Constants.RECEIVE_FAILED){
                    // TODO when received err message
                }
                else if(msg.what == Constants.RECEIVE_LIKE){
                    client.getMe().addLikeList(posts.getPostsIndex());
                    likeList = client.getMe().getLikeList();
                }
                else if(msg.what == Constants.RECEIVE_DISLIKE) {
                    client.getMe().delLikeList(posts.getPostsIndex());
                    likeList = client.getMe().getLikeList();
                }
            }
        };
        final int num = cnt++;

        Thread mThread = new Thread() {
            public void run() {
                try {
                    Log.d("test", "CustomAdapter: " + cnt + "Thread run start");
                    Log.d("test", "CustomAdapter: " + cnt + "post: " + posts.getPostsIndex() + " postname:" + posts.getMusic().getMusicName());
                    for (int p: likeList)  Log.d("test", "like list size : " + p);
                    if (likeList.contains(posts.getPostsIndex())) {
                        Log.d("test", "contain, post index : " + posts.getPostsIndex());
                        likebutton.setVisibility(View.VISIBLE);
                        unlikebutton.setVisibility(View.INVISIBLE);
                        likebutton.bringToFront();
                    } else {
                        likebutton.setVisibility(View.INVISIBLE);
                        unlikebutton.setVisibility(View.VISIBLE);
                        unlikebutton.bringToFront();
                    }

                    likebutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            unlikebutton.setVisibility(View.VISIBLE);
                            likebutton.setVisibility(View.INVISIBLE);
                            likebutton.bringToFront();

                            client.setHandler(handler);
                            client.dislike(posts.getPostsIndex());
                            likeList.remove((Object)posts.getPostsIndex());
                        }
                    });
                    unlikebutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            likebutton.setVisibility(View.VISIBLE);
                            unlikebutton.setVisibility(View.INVISIBLE);
                            unlikebutton.bringToFront();

                            client.setHandler(handler);
                            client.like(posts.getPostsIndex());
                            likeList.add(posts.getPostsIndex());
                        }
                    });

                    location.setText(posts.getLocationInfo().getTitle() + "에서");
                    music.setText(posts.getMusic().getArtistName() + " - " + posts.getMusic().getMusicName());
                    music.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Music selectedMusic = posts.getMusic();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("melonapp://play?" + "cid=" + selectedMusic.getMusicId() + "&ctype=1&menuid=" + selectedMusic.getMenuId()));
                            context.startActivity(intent);
                        }
                    });
                    content.setText(posts.getComment().toString());
                    time.setText(posts.getCreateTime().toString());

                    byte[] image = posts.getImage();
                    Log.d("test", "CustomAdapter : " + cnt + " postimage setting start :" + image);
                    if (image == null) postimage.setImageResource(R.drawable.drawemptybox);
                    else postimage.setImageDrawable(ImageController.ByteToDrawable(image));
                    Log.d("test", "CustomAdapter : " + cnt + " postimage setting success");
                    Log.d("test", "CustomAdapter : " + cnt + " getView end===========================================================");

                } catch (Exception ex) {

                }
            }
        };
        mThread.start();

        try {
            mThread.join();
        } catch (InterruptedException e) {

        }
        return convertView;
    }

    private void likeOn(){

    }
}

