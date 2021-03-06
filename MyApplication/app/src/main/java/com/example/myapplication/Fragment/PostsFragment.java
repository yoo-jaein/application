package com.example.myapplication.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.PhysicalArchitecture.ClientController;
import com.example.myapplication.R;


public class PostsFragment extends Fragment {

    private ClientController client = null;
    private Handler handler;

    private ImageView writer_image;
    private ImageView post_image;
    private TextView writer_name;
    private TextView post_text;

    private String name;
    private String contents;

    private ImageButton likebutton;
    private ImageButton unlikebutton;

    public PostsFragment() {
        Bundle args=getArguments();
        try {
            name = (String) args.get("name");
            contents = (String)args.get("contents");
        }catch (Exception e) {
            Log.d("test","posts fragments fail to get name or contents");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(client == null)
            client = ClientController.getClientControl();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_posts, container, false);
        likebutton=(ImageButton)view.findViewById(R.id.likeButton);
        unlikebutton=(ImageButton)view.findViewById(R.id.unlikeButton);

        writer_image=(ImageView)view.findViewById(R.id.user_image);

        writer_name=(TextView)view.findViewById(R.id.user_name);
        writer_name.setText(name);

        post_image=(ImageView)view.findViewById(R.id.post_image);

        post_text=(TextView)view.findViewById(R.id.post_text);
        post_text.setText(contents);

        likebutton.setVisibility(View.INVISIBLE);
        unlikebutton.setVisibility(View.VISIBLE);
        likebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test","postfrag: likebutton listener");
                unlikebutton.setVisibility(View.VISIBLE);
                likebutton.setVisibility(View.INVISIBLE);
            }
        });
        unlikebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test","postfrag: unlikebutton listener");
                likebutton.setVisibility(View.VISIBLE);
                unlikebutton.setVisibility(View.INVISIBLE);
            }
        });

        return view;
    }

    public void setWriter_image(ImageView writer_image) {
        this.writer_image = writer_image;
    }

    public void setPost_image(ImageView post_image) {
        this.post_image = post_image;
    }

    public void setWriter_name(TextView writer_name) {
        this.writer_name = writer_name;
    }

    public void setPost_text(TextView post_text) {
        this.post_text = post_text;
    }
}
