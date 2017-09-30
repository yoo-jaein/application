package com.example.application.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.application.CustomAdapter.CustomAdapter;
import com.example.application.R;

import java.util.ArrayList;

public class LikeListFragment extends Fragment {

    ListView likelist;

    public LikeListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_like_list, container, false);

        likelist=(ListView)view.findViewById(R.id.likelist);
        ArrayList<String> arr=new ArrayList<String>();
        for (int i=0;i<10;i++)
            arr.add("like list test "+i);
        likelist.setAdapter(new CustomAdapter(arr));

        return  view;
    }

}
