package com.example.myapplication.CustomAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.myapplication.R;

import java.util.ArrayList;

/**
 * Created by YoungJu on 2017-10-22.
 */
class pictureContianer {
    String s1;
    String s2;
    String s3;
    public pictureContianer(String s1,String s2,String s3) {
        this.s1=s1;
        this.s2=s2;
        this.s3=s3;
    }
    public void setS1(String s1) {
        this.s1=s1;
    }
    public void setS2(String s2) {
        this.s2=s2;
    }
    public void setS3(String s3) {
        this.s3=s3;
    }
}
public class CustomAdapter2 extends BaseAdapter{

    ArrayList<pictureContianer> contentslist=new ArrayList<pictureContianer>();

    public CustomAdapter2(ArrayList<String> contentslist){
        int cnt=0;
        for (int i=0;i<contentslist.size();i++) {
            if(i%3==0) {
                this.contentslist.get(cnt).setS1(contentslist.get(i));
            } else if(i%3==1) {
                this.contentslist.get(cnt).setS2(contentslist.get(i));
            } else{
                this.contentslist.get(cnt).setS3(contentslist.get(i));
                cnt++;
            }
        }

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

        final ImageView image1=(ImageView)convertView.findViewById(R.id.firstimage);
        final ImageView image2=(ImageView)convertView.findViewById(R.id.secondimage);
        final ImageView image3=(ImageView)convertView.findViewById(R.id.thirdimage);

        final pictureContianer s = contentslist.get(position);
        Thread mThread = new Thread() {
            public void run() {
                try {
                    image1.setImageResource(R.drawable.profiletest_1);
                    image2.setImageResource(R.drawable.profiletest_2);
                    image3.setImageResource(R.drawable.profiletest_3);
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
