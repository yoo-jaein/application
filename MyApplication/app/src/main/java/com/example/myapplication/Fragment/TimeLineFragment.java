package com.example.myapplication.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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

    private ArrayList<Posts> postsArrayList;
    private ImageButton op1;
    private ImageButton op2;
    private ImageButton op3;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView timeline;
    private ListAdapter adapter;

    private boolean lastitemVisibleFlag = false;        //화면에 리스트의 마지막 아이템이 보여지는지 체크

    private View view;

    private int cursor=0;

    private boolean curr=true;

    public TimeLineFragment() {
        super();

        if(client == null)
            client = ClientController.getClientControl();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.d("handler", "received message in handler");
                if (msg.what == Constants.RECEIVE_REFRESH) {
                    Log.d("test","timeline handler : refresh");
                    postsArrayList = client.getTimeLine();
                    adapter = new CustomAdapter(postsArrayList,client.getMe());
                    timeline.setAdapter(adapter);
                    timeline.deferNotifyDataSetChanged();
                    cursor=client.getTimeLine().size();
                    Log.d("test",">>cursor size"+cursor);
                } else if(msg.what == Constants.RECEIVE_MORE ){
                    if(cursor!=client.getTimeLine().size()) {
                        Log.d("test", "timeline handler : receivemore");
                        postsArrayList = client.getTimeLine();
                        adapter = new CustomAdapter(postsArrayList, client.getMe());
                        timeline.setAdapter(adapter);
                        timeline.deferNotifyDataSetChanged();
                        timeline.setSelection(cursor-1);
                        Log.d("cursor", "set>>cursor size" + cursor);
                        curr=true;
                    }
                } else if (msg.what == Constants.RECEIVE_FAILED) {
                    // TODO when receive err message
                }
            }
        };
        client.setTimeLineHandler(handler);
        client.refresh();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
/*
        if(client == null)
            client = ClientController.getClientControl();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Log.d("handler", "received message in handler");
                client.setHandler(null);
                if (msg.what == Constants.RECEIVE_REFRESH) {
                    postsArrayList = client.getTimeLine();
                    adapter = new CustomAdapter(postsArrayList,client.getMe());
                    timeline.setAdapter(adapter);
                    timeline.deferNotifyDataSetChanged();
                } else if(msg.what == Constants.RECEIVE_MORE){
                    // 게시물을 더 받아왔을 경우 더 받아온 포스트를 현재 ArrayList에 더함.
                    // client.getMoreList는 타임라인이든 내게시물이든 내가 좋아요 누른 게시물이든 더 받은 포스트가 들어있다.
                    // TODO 리스트뷰 갱신이 자동으로 될지 테스트 필요
                    postsArrayList.addAll(client.getMoreList());
                } else if (msg.what == Constants.RECEIVE_FAILED) {
                    // TODO when receive err message
                }
            }
        };
        client.setTimeLineHandler(handler);
        client.refresh();
        */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_time_line, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_layout);

        timeline = (ListView) view.findViewById(R.id.timeline);

        client.setTimeLineHandler(handler);
        client.refresh();


        timeline.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem+visibleItemCount==totalItemCount && totalItemCount !=0) {
                    Log.d("cursor",">>>>>>>>>>>timeline last item");
                    if(curr) {
                        client.setTimeLineHandler(handler);
                        client.morePosts();
                        curr=false;
                        cursor = client.getTimeLine().size();
                    }
                }
            }
        });

        return view;
    }

    public Handler getHandler(){
        return handler;
    }
}
