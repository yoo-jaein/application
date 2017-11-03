package com.example.myapplication.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.myapplication.CustomAdapter.CustomAdapter;
import com.example.myapplication.PhysicalArchitecture.ClientController;
import com.example.myapplication.ProblemDomain.Constants;
import com.example.myapplication.ProblemDomain.Posts;
import com.example.myapplication.R;

import java.util.ArrayList;

public class LikeListFragment extends Fragment {

    private ClientController client = null;
    private Handler handler;

    private ListView likelist;

    private View view;

    private ArrayList<Posts> mylikeList;

    public LikeListFragment() {
        Log.d("fragment", "in LikeListFragment()");

        if(client == null)
            client = ClientController.getClientControl();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                Log.d("test", "in likelist handler");
                client.setMyLikeListHandler(null);

                if(msg.what== Constants.RECEIVE_REFRESH){
                    mylikeList = client.getMyLikeList();

                    likelist.setAdapter(new CustomAdapter(mylikeList, client.getMe()));
                }else if(msg.what==Constants.RECEIVE_FAILED){
                    // TODO when receive err message
                }
            }
        };

        client.setMyLikeListHandler(handler);
        client.myLike();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("test", "Likelist Fragment:in onCreateView()");

        view= inflater.inflate(R.layout.fragment_like_list, container, false);

        likelist = (ListView)view.findViewById(R.id.likelist);
        likelist.setAdapter(new CustomAdapter(mylikeList, client.getMe()));

        client.setMyLikeListHandler(handler);
        client.myLike();

        return  view;
    }
}
