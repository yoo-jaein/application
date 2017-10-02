package com.example.application.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.application.CustomAdapter.CustomAdapter;
import com.example.application.PhysicalArchitecture.ClientControl;
import com.example.application.ProblemDomain.Constants;
import com.example.application.R;

import java.util.ArrayList;

public class LikeListFragment extends Fragment {

    private ClientControl client = null;
    private Handler handler;
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


        if(client == null)
            client = ClientControl.getClientControl();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what== Constants.RECEIVE_SUCCESSS){
                    ArrayList<String> arr=new ArrayList<String>();
                    for (int i=0;i<client.getMyLikeList().size();i++)
                        arr.add("like list test "+i);
                    likelist.setAdapter(new CustomAdapter(arr));
                }else if(msg.what==Constants.RECEIVE_FAILED){

                }
            }
        };

        client.setHandler(handler);


        client.myLike();


        return  view;
    }

}
