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
    private static Handler handler;

    private ListView likelist;

    private View view;

    private ArrayList<Posts> mylikeList;
    private boolean lastitemVisibleFlag = false;

    public LikeListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        Log.d("test", "in onCreateView()");

        view= inflater.inflate(R.layout.fragment_like_list, container, false);

        likelist = (ListView)view.findViewById(R.id.likelist);
        likelist.setAdapter(new CustomAdapter(mylikeList, client.getMe()));
        likelist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //현재 화면에 보이는 첫번째 리스트 아이템의 번호(firstVisibleItem) + 현재 화면에 보이는 리스트 아이템의 갯수(visibleItemCount)가 리스트 전체의 갯수(totalItemCount) -1 보다 크거나 같을때
                lastitemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //OnScrollListener.SCROLL_STATE_IDLE은 스크롤이 이동하다가 멈추었을때 발생되는 스크롤 상태입니다.
                //즉 스크롤이 바닦에 닿아 멈춘 상태에 처리를 하겠다는 뜻
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {
                    //TODO 화면이 바닦에 닿을때 처리
                    client.setMyPageHandler(handler);
                    client.moreMyPosts();
                }
            }
        });

        client.setMyLikeListHandler(handler);
        client.myLike();

        return  view;
    }
}
