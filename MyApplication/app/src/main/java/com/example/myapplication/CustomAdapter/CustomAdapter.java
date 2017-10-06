package com.example.myapplication.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.myapplication.R;

import java.util.ArrayList;

/**
 * Created by YoungJu on 2017-09-30.
 */

public class CustomAdapter extends BaseAdapter {

    ArrayList<String> contentslist=new ArrayList<String>();

    public CustomAdapter(ArrayList<String> contentslist){
        this.contentslist=contentslist;
    }
    @Override
    public int getCount() {
        return contentslist.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fragment_posts, parent, false);
        }
        final TextView name=(TextView)convertView.findViewById(R.id.user_name);
        final TextView content=(TextView)convertView.findViewById(R.id.post_text);

        final ImageButton likebutton=(ImageButton)convertView.findViewById(R.id.likeButton);
        final ImageButton unlikebutton=(ImageButton)convertView.findViewById(R.id.unlikeButton);

        final String s = contentslist.get(position);

        Thread mThread = new Thread() {
            public void run() {
                try {
                    name.setText(s);
                    content.setText(s);
                    likebutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            unlikebutton.setVisibility(View.VISIBLE);
                            likebutton.setVisibility(View.INVISIBLE);
                        }
                    });
                    unlikebutton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            likebutton.setVisibility(View.VISIBLE);
                            unlikebutton.setVisibility(View.INVISIBLE);
                        }
                    });
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
}
