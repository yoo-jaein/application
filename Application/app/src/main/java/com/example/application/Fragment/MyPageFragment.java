package com.example.application.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.application.CustomAdapter.CustomAdapter;
import com.example.application.R;

import java.util.ArrayList;

public class MyPageFragment extends Fragment {

    TextView nametext;
    ListView mylist;

    public MyPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_my_page, container, false);

        nametext=(TextView)view.findViewById(R.id.user_name);
        nametext.setText("young ju");

        mylist=(ListView)view.findViewById(R.id.mylist);
        ArrayList<String> arr=new ArrayList<String>();
        for (int i=0;i<10;i++)
            arr.add("my page test "+i);
        mylist.setAdapter(new CustomAdapter(arr));


        return view;
    }

}
