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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static android.util.Log.d;

/*
    < Usage >
    1) writingNewPostActivity   : intent.putExtra("MODE", SEARCH_LOCATION_MODE)
    2) timeLine                 : intent.putExtra("MODE", VIEW_LOCATION_MODE), intent.putExtra("LOCATION", location)
 */
public class SearchLocationActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback {

    private Context mContext = null;
    private boolean m_bTrackingMode = true;

    private TMapGpsManager tMapGpsManager = null;
    private TMapView tMapView = null;
    private static String APIKey = "254504f1-4949-3f5b-95d9-4325510614f1";

    private Handler handler;

    private APIController apiController;

    private ArrayList<TMapPoint> pointList = new ArrayList<TMapPoint>();
    private ArrayList<String> markerIdList = new ArrayList<String>();
    private ArrayList<Location> locationList = new ArrayList<Location>();

    private HashMap<Integer, Bitmap> bitmapList = new HashMap<>();
    private HashMap<Integer, Bitmap> pinIconList = new HashMap<>();

    @Override
    public void onLocationChange(android.location.Location location) {
        if (m_bTrackingMode) {
            tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
            updateMarker(location.getLongitude(), location.getLatitude());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_location);

        apiController = APIController.getAPIController();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == Constants.RECEIVE_SUCCESSS) {
                    locationList.addAll((ArrayList<Location>) (msg.obj));

                    for (Location location : locationList)
                        Log.d("test", location.toString());
                    /*
                        if receive result, set marker on Tmap
                     */
                    getLocationImage();
                } else if (msg.what == Constants.RECEIVE_BITMAP_LIST) {
                    d("test", "received bitmap list");
                 //   bitmapList.addAll((ArrayList<Bitmap>) (msg.obj));
                    showMarkerPoint();
                } else if (msg.what == Constants.RECEIVE_FAILED) {

                }
            }
        };

        mContext = this;

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.mapView);
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

        tMapGpsManager = new TMapGpsManager(SearchLocationActivity.this);
        tMapGpsManager.setMinTime(1000);
        tMapGpsManager.setMinDistance(100);

        // get location by connected internet
        tMapGpsManager.setProvider(tMapGpsManager.NETWORK_PROVIDER);

        // get location by GPS sensor
        //tMapGpsManager.setProvider(TMapGpsManager.GPS_PROVIDER);
        tMapGpsManager.OpenGps();

        // center of window move to current location
        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);

        tMapView.setOnCalloutRightButtonClickListener(new TMapView.OnCalloutRightButtonClickCallback() {
            @Override
            public void onCalloutRightButton(TMapMarkerItem markerItem) {
                Toast.makeText(SearchLocationActivity.this, "클릭", Toast.LENGTH_SHORT).show();
            }
        });

        initPinIcon();

        int mode = getIntent().getExtras().getInt("MODE");

        if (mode == Constants.SEARCH_MAP_MODE) {
            updateMarker(tMapGpsManager.getLocation().getLongitude(), tMapGpsManager.getLocation().getLatitude());
        } else if (mode == Constants.VIEW_LOCATION_MODE) {
            Location location = (Location) (getIntent().getSerializableExtra("LOCATION"));
            tMapView.setLocationPoint(location.getMapY(), location.getMapX());

            locationList.add(location);

            getLocationImage();
        } else {
            d("ERR", "invalid intent value");
            finish();
        }

    }

    /*
        when user location change detected, update marker and related data
     */
    private void updateMarker(double longtitude, double latitude) {
        pointList.clear();
        markerIdList.clear();
        bitmapList.clear();
        locationList.clear();

        apiController.getLocationList(longtitude, latitude, 1500, handler);
    }

    private void showMarkerPoint() {
        int mMarkerID = 0;

        for (int i = 0; i < locationList.size(); i++) {

            TMapPoint point = new TMapPoint(locationList.get(i).getMapY(), locationList.get(i).getMapX());
            TMapMarkerItem item = new TMapMarkerItem();

            Bitmap pinIconImage = null;

            int contentTypeID = locationList.get(i).getContentTypeID();

            pinIconImage = getPinIconByContentTypeID(contentTypeID);

            item.setTMapPoint(point);
            item.setName(locationList.get(i).getTitle());
            item.setVisible(item.VISIBLE);
            item.setIcon(pinIconImage);
            item.setPosition((float)0.5, (float)1.0);

            item.setCalloutTitle(locationList.get(i).getTitle());
            item.setCalloutSubTitle(locationList.get(i).getTitle());
            item.setCanShowCallout(true);
            item.setAutoCalloutVisible(false);

            if (locationList.get(i).getFirstimage()==null)
                item.setCalloutRightButtonImage(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher));
            else
                item.setCalloutRightButtonImage(bitmapList.get(i));

            String strID = String.format("marker_%d", mMarkerID++);

            tMapView.addMarkerItem(strID, item);
            markerIdList.add(strID);
        }
    }

    /*
        Init Pin Icons for each content type ID
     */
    private void initPinIcon(){

        Bitmap bitmap = null;

        for (int code : Constants.CONTENTES_TYPE_CODE) {
            if (code == Constants.CONTENTES_TYPE_CODE[1]) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_tourlistattraction);
                pinIconList.put(code, resizeBitmap(bitmap, 50));
            } else if (code == Constants.CONTENTES_TYPE_CODE[2]) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_culturalfacility);
                pinIconList.put(code, resizeBitmap(bitmap, 50));
            } else if (code == Constants.CONTENTES_TYPE_CODE[3]) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_event_performance_festival);
                pinIconList.put(code, resizeBitmap(bitmap, 50));
            } else if (code == Constants.CONTENTES_TYPE_CODE[4]) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_travelcourse);
                pinIconList.put(code, resizeBitmap(bitmap, 50));
            } else if (code == Constants.CONTENTES_TYPE_CODE[5]) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_leisure);
                pinIconList.put(code, resizeBitmap(bitmap, 50));
            } else if (code == Constants.CONTENTES_TYPE_CODE[6]) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_lodging);
                pinIconList.put(code, resizeBitmap(bitmap, 50));
            } else if (code == Constants.CONTENTES_TYPE_CODE[7]) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_shopping);
                pinIconList.put(code, resizeBitmap(bitmap, 50));
            } else if (code == Constants.CONTENTES_TYPE_CODE[8]) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_restaurant);
                pinIconList.put(code, resizeBitmap(bitmap, 50));
            }
        }
    }


    private Bitmap getPinIconByContentTypeID(int contentTypeID){
        Bitmap bitmap = pinIconList.get(contentTypeID);

        return bitmap;
    }

    private Bitmap resizeBitmap(Bitmap bitmap, int resizeHeight){

        double aspectRatio = (double) bitmap.getHeight() / (double) bitmap.getWidth();
        int targetWidth = (int) (resizeHeight * aspectRatio);
        Bitmap result = Bitmap.createScaledBitmap(bitmap, targetWidth, resizeHeight, false);

        if (result != bitmap) {
            bitmap.recycle();
        }
        return result;
    }

    /*
        get location image to hashMap by using URL
     */
    private void getLocationImage(){

        Thread mThread = new Thread(){
            public void run() {
                for(int i=0; i<locationList.size(); i++) {
                    try {
                        URL url = new URL(locationList.get(i).getFirstimage());

                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();

                        InputStream is = conn.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(is);

                        bitmapList.put(i, resizeBitmap(bitmap, 80));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                Log.d("test", "received bitmap list");
            }
        };

        mThread.start();

        try{
            mThread.join();

            showMarkerPoint();
        }catch(InterruptedException e){

        }
    }
}
