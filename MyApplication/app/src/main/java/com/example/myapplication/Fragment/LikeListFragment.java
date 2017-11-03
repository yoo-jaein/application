package com.example.myapplication.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.myapplication.CustomAdapter.CustomAdapter;
import com.example.myapplication.PhysicalArchitecture.ClientController;
import com.example.myapplication.ProblemDomain.Constants;
import com.example.myapplication.ProblemDomain.Posts;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class LikeListFragment extends Fragment {

    private ClientController client = null;
    private Handler handler;

    private ListView likelist;
    private CustomAdapter adapter;

    private View view;

    private ArrayList<Posts> mylikeList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_like_list, container, false);

        likelist = (ListView)view.findViewById(R.id.likelist);

        if(client == null)
            client = ClientController.getClientControl();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                client.setMyLikeListHandler(null);

                if(msg.what== Constants.RECEIVE_REFRESH){
                    mylikeList = client.getMyLikeList();
                    adapter = new CustomAdapter(mylikeList,client.getMe());
                    likelist.setAdapter(adapter);
                }else if(msg.what==Constants.RECEIVE_FAILED){
                    // TODO when receive err message
                }
            }
        };

        client.setMyLikeListHandler(handler);
        client.myLike();
        return  view;
    }
}
