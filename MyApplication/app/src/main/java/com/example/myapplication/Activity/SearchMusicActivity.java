package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.myapplication.PhysicalArchitecture.APIController;
import com.example.myapplication.ProblemDomain.Constants;
import com.example.myapplication.ProblemDomain.Music;
import com.example.myapplication.R;

import java.util.ArrayList;

public class SearchMusicActivity extends AppCompatActivity {

    private APIController apiController;
    private Handler handler;
    private String[] completeList = null;
    private ArrayAdapter<String> adapter;

    private SearchMusicActivity searchMusicActivity;
    private AutoCompleteTextView autoCompleteTextView;

    private ArrayList<Music> musicList;
    private Music selectedMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_music);

        searchMusicActivity = this;

        autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what== Constants.RECEIVE_SUCCESSS){
                    String temp;
                    musicList = ((ArrayList<Music>)(msg.obj));

                    Log.d("test", "receive message from Melon API thread");

                    /*
                        only if search result exist, set string list adapter for auto complete text view
                     */
                    if(musicList.size() > 0) {
                        completeList = new String[musicList.size()];

                        for (int i = 0; i < musicList.size(); i++) {
                            temp = "";
                            temp += musicList.get(i).getArtistName();
                            temp += " - ";
                            temp += musicList.get(i).getMusicName();
                            completeList[i] = temp;
                            Log.d("test", "completeList[i] : " + completeList[i]);
                        }
                        adapter = new ArrayAdapter<String>
                                (searchMusicActivity, android.R.layout.simple_dropdown_item_1line, completeList);
                        adapter.setNotifyOnChange(true);
                        autoCompleteTextView.setAdapter(adapter);
                        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                selectedMusic = musicList.get(i);
                                Log.d("test", "selectedMusic : " + selectedMusic.toString());

                                Intent intent = new Intent();
                                intent.putExtra("SONG", selectedMusic);
                                setResult(RESULT_OK, intent);
                                finish();
                                //    play music in melon app code
                                /*
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse("melonapp://play?" + "cid=" + selectedMusic.getMusicId() + "&ctype=1&menuid=" + selectedMusic.getMenuId()));
                                startActivity(intent);
                                */
                            }
                        });
                        adapter.notifyDataSetChanged();
                    }
                }else if(msg.what== Constants.RECEIVE_FAILED){

                }
            }
        };

        apiController = APIController.getAPIController();

        /*
            edit text change event
         */
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length() >= 2){
                    String keyword = charSequence.toString();
                    Log.d("test", "test changed");

                    if(keyword.matches("^[가-힣]*$"));
                        apiController.getMusicList(keyword, handler);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        autoCompleteTextView.addTextChangedListener(textWatcher);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
        super.onBackPressed();
    }
}
