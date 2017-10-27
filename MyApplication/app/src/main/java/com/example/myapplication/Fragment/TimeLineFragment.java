package com.example.myapplication.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.myapplication.CustomAdapter.CustomAdapter;
import com.example.myapplication.PhysicalArchitecture.ClientController;
import com.example.myapplication.ProblemDomain.Constants;
import com.example.myapplication.R;

import java.util.ArrayList;

public class TimeLineFragment extends Fragment {

    private Handler handler;
    private ClientController client = null;


    ImageButton op1;
    ImageButton op2;
    ImageButton op3;

    SwipeRefreshLayout mSwipeRefreshLayout;
    ListView timeline;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view =inflater.inflate(R.layout.fragment_time_line, container, false);

        if(client == null)
            client = ClientController.getClientControl();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what== Constants.RECEIVE_SUCCESSS){
                    client.setHandlerNull();
                }else if(msg.what==Constants.RECEIVE_FAILED){
                    // TODO when receive err message
                }
            }
        };

        client.setHandler(handler);

        client.refresh();

        timeline=(ListView)view.findViewById(R.id.timeline);

/*
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ArrayList<String> arr=new ArrayList<String>();
                for (int i=10;i<20;i++) {
                    arr.add("time line test " + i);
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
*/


        ArrayList<String> arr=new ArrayList<>();
        for (int i=0;i<10;i++) {
            arr.add("time line test " + i);
        }
        timeline.setAdapter(new CustomAdapter(arr));

        op1=(ImageButton)view.findViewById(R.id.mainoptionone);
        op2=(ImageButton)view.findViewById(R.id.mainoptiontwo);
        op3=(ImageButton)view.findViewById(R.id.mainoptionthree);

        op1.setVisibility(View.INVISIBLE);
        op2.setVisibility(View.INVISIBLE);
        op3.setVisibility(View.INVISIBLE);

        return view;
    }
    public void setoption(boolean option){
        if(option) {
            Log.d("test", ""+ option);
            op1.setVisibility(View.INVISIBLE);
            op2.setVisibility(View.INVISIBLE);
            op3.setVisibility(View.INVISIBLE);
        } else {
            LinearLayout optioncontainer=(LinearLayout)getView().findViewById(R.id.optionContainer);
            op1.setVisibility(View.VISIBLE);
            op2.setVisibility(View.VISIBLE);
            op3.setVisibility(View.VISIBLE);
            op1.bringToFront();
            op2.bringToFront();
            op3.bringToFront();
            optioncontainer.bringToFront();
            Log.d("test", ""+ option);

        }
    }

}
