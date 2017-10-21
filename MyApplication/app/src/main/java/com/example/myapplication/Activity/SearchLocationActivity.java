package com.example.myapplication.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.myapplication.PhysicalArchitecture.APIController;
import com.example.myapplication.ProblemDomain.Constants;
import com.example.myapplication.ProblemDomain.Location;
import com.example.myapplication.R;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

import java.util.ArrayList;

public class SearchLocationActivity extends AppCompatActivity{

    private Context mContext = null;

    private TMapGpsManager tMapGpsManager = null;
    private TMapView tMapView = null;
    private static String APIKey = "254504f1-4949-3f5b-95d9-4325510614f1";
    private static int mMarkerID;

    private Handler handler;

    private APIController apiController;

    private ArrayList<TMapPoint> pointList = new ArrayList<TMapPoint>();
    private ArrayList<String> markerIdList = new ArrayList<String>();
    private ArrayList<Location> locationList = new ArrayList<Location>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);

        apiController = APIController.getAPIController();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                if(msg.what== Constants.RECEIVE_SUCCESSS){
                    locationList = ((ArrayList<Location>)(msg.obj));
                    Log.d("test", "location data received");
                    /*
                        if receive result, set marker on Tmap
                     */
                    showMarkerPoint();

                }else if(msg.what== Constants.RECEIVE_FAILED){

                }
            }
        };

        mContext = this;

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.mapView);
        tMapView = new TMapView(this);

        linearLayout.addView(tMapView);
        tMapView.setSKPMapApiKey(APIKey);

        // current sight direction
        tMapView.setCompassMode(true);
        // mark current location
        tMapView.setIconVisibility(true);

        // zoom level
        tMapView.setZoomLevel(15);

        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);

        tMapGpsManager= new TMapGpsManager(SearchLocationActivity.this);
        tMapGpsManager.setMinTime(1000);
        tMapGpsManager.setMinDistance(5);

        // get location by connected internet
        tMapGpsManager.setProvider(tMapGpsManager.NETWORK_PROVIDER);

        // get location by GPS sensor
        //tMapGpsManager.setProvider(TMapGpsManager.GPS_PROVIDER);
        tMapGpsManager.OpenGps();

        // center of window move to current location
        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);

        tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback(){
            @Override
            public void onCalloutRightButton(TMapMarkerItem markerItem){
                Toast.makeText(SearchLocationActivity.this, "클릭", Toast.LENGTH_SHORT).show();
            }
        });

        apiController.getLocationList(tMapView.getLongitude(), tMapView.getLatitude(), 1500, handler);
    }

    private void showMarkerPoint(){
        for(int i=0; i<locationList.size(); i++){
            TMapPoint point = new TMapPoint(locationList.get(i).getMapX(), locationList.get(i).getMapY());
            TMapMarkerItem item = new TMapMarkerItem();
            Bitmap bitmap = null;
            bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);

            item.setTMapPoint(point);
            item.setName(locationList.get(i).getTitle());
            item.setCalloutSubTitle(locationList.get(i).getTitle());
            item.setCanShowCallout(true);
            item.setAutoCalloutVisible(true);

            Bitmap bitmap_i = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);
            item.setCalloutRightButtonImage(bitmap_i);
            String strID = String.format("pmarker%d", mMarkerID++);

            tMapView.addMarkerItem(strID, item);
            markerIdList.add(strID);
        }
    }
}
