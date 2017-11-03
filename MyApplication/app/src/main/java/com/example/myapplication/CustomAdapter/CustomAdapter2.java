package com.example.myapplication.CustomAdapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.myapplication.PhysicalArchitecture.ImageController;
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



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.instagram_listview, parent, false);
        }

        final LinearLayout layout=(LinearLayout)convertView.findViewById(R.id.instaLinear);

        final ImageView image1 = (ImageView) convertView.findViewById(R.id.firstimage);
        final ImageView image2 = (ImageView) convertView.findViewById(R.id.secondimage);
        final ImageView image3 = (ImageView) convertView.findViewById(R.id.thirdimage);

        final pictureContianer s = contentslist.get(position);
        Thread mThread = new Thread() {
            public void run() {
                try {
                    byte[] b;
                    if((b=s.s1.getImage())!=null) image1.setImageDrawable(ImageController.ByteToDrawable(s.s1.getImage()));
                    if((b=s.s2.getImage())!=null) image2.setImageDrawable(ImageController.ByteToDrawable(s.s2.getImage()));
                    if((b=s.s3.getImage())!=null) image3.setImageDrawable(ImageController.ByteToDrawable(s.s3.getImage()));

                    layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            int w=layout.getWidth()/3;
                            int h=w;

                            Log.d("test","CustomAdapter2: w="+w+" h="+h);

                            image1.setLayoutParams(new LinearLayout.LayoutParams(w,h));
                            image2.setLayoutParams(new LinearLayout.LayoutParams(w,h));
                            image3.setLayoutParams(new LinearLayout.LayoutParams(w,h));

                        }});
                    int w=layout.getWidth()/3;
                    int h=w;

                    Log.d("test","CustomAdapter2: w="+w+" h="+h);

                    image1.setLayoutParams(new LinearLayout.LayoutParams(w,h));
                    image2.setLayoutParams(new LinearLayout.LayoutParams(w,h));
                    image3.setLayoutParams(new LinearLayout.LayoutParams(w,h));

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
