package com.example.myapplication.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.CustomAdapter.CustomAdapter;
import com.example.myapplication.PhysicalArchitecture.ClientController;
import com.example.myapplication.ProblemDomain.Constants;
import com.example.myapplication.R;

import java.util.ArrayList;

public class MyPageFragment extends Fragment {

    private Handler handler;
    private ClientController client = null;
    TextView nametext;
    ListView mylist;

    public MyPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(client == null)
            client = ClientController.getClientControl();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what== Constants.RECEIVE_SUCCESSS){

                }else if(msg.what==Constants.RECEIVE_FAILED){

                }
            }
        };

        client.setHandler(handler);
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
