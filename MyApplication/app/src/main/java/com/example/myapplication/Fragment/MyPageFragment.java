package com.example.myapplication.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.CustomAdapter.CustomAdapter;
import com.example.myapplication.CustomAdapter.CustomAdapter2;
import com.example.myapplication.PhysicalArchitecture.ClientController;
import com.example.myapplication.ProblemDomain.Constants;
import com.example.myapplication.ProblemDomain.Posts;
import com.example.myapplication.R;

import java.util.ArrayList;

public class MyPageFragment extends Fragment {

    private Handler handler;
    private ClientController client = null;
    private ArrayList<Posts> myPostsList;

    private ImageButton change_arr;

    private ImageView fillbar;
    private ImageView unfillbar;

    private TextView nametext;
    private ListView mylist;

    private int arr_type=1;

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
                    client.setHandlerNull();
                    myPostsList = client.getMyPostsList();
                    mylist.setAdapter(new CustomAdapter(myPostsList,client.getMe()));
                }else if(msg.what==Constants.RECEIVE_FAILED){
                    // TODO when receive err message
                }
            }
        };

        client.setHandler(handler);
        client.myPosts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_my_page, container, false);

        nametext=(TextView)view.findViewById(R.id.user_name);
        nametext.setText("young ju");

        mylist=(ListView)view.findViewById(R.id.mylist);
        mylist.setAdapter(new CustomAdapter(client.getMyPostsList(),client.getMe()));

        //stretch fill bar according to like count
/*
        fillbar=(ImageView)view.findViewById(R.id.fill_like_bar);
        ViewPager.LayoutParams params = (ViewPager.LayoutParams) fillbar.getLayoutParams();
        unfillbar=(ImageView)view.findViewById(R.id.unfillbar);
        ViewPager.LayoutParams params1 = (ViewPager.LayoutParams) unfillbar.getLayoutParams();
        // change num 0.3 to like count
        params.width = (int) (params1.width * 0.3);
        fillbar.setLayoutParams(params);

*/
        change_arr=(ImageButton)view.findViewById(R.id.mypagechangearr);
        change_arr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("test","Change arr bnt onclick listner start ");
                if(arr_type==1) {
                    arr_type=2;
                    CustomAdapter2 adapter2=new CustomAdapter2(client.getMyPostsList());
                    mylist.setAdapter(adapter2);
                    adapter2.notifyDataSetChanged();
                    change_arr.setImageResource(R.drawable.profilearr1);
                } else {
                    arr_type=1;
                    CustomAdapter adapter=new CustomAdapter(client.getMyPostsList(),client.getMe());
                    mylist.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    change_arr.setImageResource(R.drawable.profilearr2);
                }
            }
        });


        return view;
    }
}
