package com.example.application.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.application.CustomAdapter.CustomAdapter;
import com.example.application.R;

import java.util.ArrayList;

public class TimeLineFragment extends Fragment {

    boolean clickedCategory=false;

    ImageButton category;
    ImageButton filter1;
    ImageButton filter2;
    ImageButton filter3;
    ListView timeline;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view =inflater.inflate(R.layout.fragment_time_line, container, false);

        timeline=(ListView)view.findViewById(R.id.timeline);
        ArrayList<String> arr=new ArrayList<String>();
        for (int i=0;i<10;i++)
            arr.add("time line test "+i);
        timeline.setAdapter(new CustomAdapter(arr));

        category=(ImageButton)view.findViewById(R.id.category);
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedCategory = !clickedCategory;
                controlClick(clickedCategory);
            }
        });

        filter1=(ImageButton)view.findViewById(R.id.filter1);
        filter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlClick(true);
            }
        });
        filter2=(ImageButton)view.findViewById(R.id.filter2);
        filter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlClick(true);
            }
        });
        filter3=(ImageButton)view.findViewById(R.id.filter3);
        filter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controlClick(true);
            }
        });

        LinearLayout buttonset=(LinearLayout)view.findViewById(R.id.button_set);
        buttonset.bringToFront();

        return view;
    }

    public void controlClick(boolean check) {
        clickedCategory=check;

        if (clickedCategory) {
            filter1.setVisibility(View.INVISIBLE);
            filter2.setVisibility(View.INVISIBLE);
            filter3.setVisibility(View.INVISIBLE);
        } else {
            filter1.setVisibility(View.VISIBLE);
            filter2.setVisibility(View.VISIBLE);
            filter3.setVisibility(View.VISIBLE);
        }
    }

}
