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

import com.example.application.PhysicalArchitecture.ClientControl;
import com.example.application.R;


public class PostsFragment extends Fragment {

    private ClientControl client = null;
    private Handler handler;

    public ImageView writer_image;
    public ImageView post_image;
    public TextView writer_name;
    public TextView post_text;
    public ImageButton like_button;

    String name;
    String contents;

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
            client = ClientControl.getClientControl();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_posts, container, false);

        writer_image=(ImageView)view.findViewById(R.id.user_image);

        writer_name=(TextView)view.findViewById(R.id.user_name);
        writer_name.setText(name);

        post_image=(ImageView)view.findViewById(R.id.post_image);

        post_text=(TextView)view.findViewById(R.id.post_text);
        post_text.setText(contents);

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
