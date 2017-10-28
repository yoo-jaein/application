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
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.myapplication.CustomAdapter.CustomAdapter;
import com.example.myapplication.PhysicalArchitecture.ClientController;
import com.example.myapplication.ProblemDomain.Constants;
import com.example.myapplication.ProblemDomain.Posts;
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


        Thread mThread = new Thread() {
            public void run() {
                ArrayList<Posts> postsArrayList=null;
                try {
                    Log.d("test","TimeLineFragment: thread start");
                    int cnt=0;
                    while(postsArrayList!=null) {
                        postsArrayList=client.getTimeLine();
                        Log.d("test",""+cnt++);
                    }
                    Log.d("test","TimeLineFragment: client.getTimeLine =" +postsArrayList);
                } catch (Exception e) {
                }
                ListAdapter adapter=new CustomAdapter(postsArrayList);
                timeline.setAdapter(adapter);

            }
        };
        mThread.start();
        try {
            mThread.join();
        } catch (InterruptedException e) {}

        return view;
    }

}
