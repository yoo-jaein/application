package com.example.myapplication.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;

public class Help2Fragment extends Fragment {

    public Help2Fragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("test","Help Fragment 2 : OnCreateView");

        return inflater.inflate(R.layout.fragment_help2, container, false);

    }

}
