package com.example.myapplication.CustomAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.myapplication.ProblemDomain.Posts;
import com.example.myapplication.R;

import java.util.ArrayList;

/**
 * Created by YoungJu on 2017-10-22.
 */
class pictureContianer {
    Posts s1;
    Posts s2;
    Posts s3;

    public pictureContianer(Posts s1, Posts s2,Posts s3) {
        this.s1 = s1;
        this.s2 = s2;
        this.s3 = s3;
    }
}

public class CustomAdapter2 extends BaseAdapter {

    ArrayList<pictureContianer> contentslist = new ArrayList<pictureContianer>();

    public CustomAdapter2(ArrayList<Posts> contentslist) {
        int cnt = contentslist.size();
        for (int i = 0; i < cnt-2; ) {
            if (i % 3 == 0 ) {
                pictureContianer tmp = new pictureContianer(contentslist.get(i), contentslist.get(i + 1), contentslist.get(i + 2));
                i += 3;
                this.contentslist.add(tmp);
                Log.d("test","CustomAdapter2: adding i "+i);
            }
        }
        if (cnt % 3 != 0) {
            if (cnt % 3 == 1) {
                pictureContianer tmp = new pictureContianer(contentslist.get(cnt-1), null, null);
                this.contentslist.add(tmp);
            } else {
                pictureContianer tmp = new pictureContianer(contentslist.get(cnt-2), contentslist.get(cnt - 1), null);
                this.contentslist.add(tmp);
            }
        }

        Log.d("test","CustomAdapter2: finish Constructor");
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


    Drawable ByteToDrawable(byte[] b) {
        Drawable image;
        Bitmap bitmap= BitmapFactory.decodeByteArray(b, 0, b.length);
        Log.d("test","bitmap");
        image = new BitmapDrawable(bitmap);
        return image;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.instagram_listview, parent, false);
        }

        final ImageView image1 = (ImageView) convertView.findViewById(R.id.firstimage);
        final ImageView image2 = (ImageView) convertView.findViewById(R.id.secondimage);
        final ImageView image3 = (ImageView) convertView.findViewById(R.id.thirdimage);

        final pictureContianer s = contentslist.get(position);
        Thread mThread = new Thread() {
            public void run() {
                try {
                    image1.setImageDrawable(ByteToDrawable(s.s1.getImage()));
                    image2.setImageDrawable(ByteToDrawable(s.s2.getImage()));
                    image3.setImageDrawable(ByteToDrawable(s.s3.getImage()));
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
