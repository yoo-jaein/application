package com.example.myapplication.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.myapplication.PhysicalArchitecture.APIController;
import com.example.myapplication.ProblemDomain.Constants;
import com.example.myapplication.ProblemDomain.Location;
import com.example.myapplication.R;
import com.skp.Tmap.TMapGpsManager;
import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPOIItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * @author jm
 * 관광정보 검색 액티비티. TMap 연동
 *   < Usage >
 *   1) writingNewPostActivity   : intent.putExtra("MODE", SEARCH_LOCATION_MODE)
 *   2) timeLine                 : intent.putExtra("MODE", VIEW_LOCATION_MODE), intent.putExtra("LOCATION", location)
 */
public class SearchLocationActivity extends AppCompatActivity implements TMapGpsManager.onLocationChangedCallback, TMapView.OnClickListenerCallback {

    private Context mContext = null;
    private boolean m_bTrackingMode = true;

    private TMapGpsManager tMapGpsManager = null;
    private TMapView tMapView = null;
    private static String APIKey = "254504f1-4949-3f5b-95d9-4325510614f1";

    private Spinner mapFilterSpinner = null;
    private ArrayAdapter<String> mapFilterAdapter = null;
    private static Handler handler;

    private APIController apiController;

    private ArrayList<TMapMarkerItem> markerItemList = new ArrayList<>();
    private ArrayList<String> markerIdList = new ArrayList<>();
    private ArrayList<Location> locationList = new ArrayList<>();

    private Bitmap locationImage = null;
    private SparseArray<Bitmap> pinIconList = new SparseArray<>();
    private Bitmap rightButtonImage = null;

    @Override
    public void onLocationChange(android.location.Location location) {
        if (m_bTrackingMode) {
            tMapView.setLocationPoint(location.getLongitude(), location.getLatitude());
            updateMarker(location.getLongitude(), location.getLatitude());
        }
    }

    @Override
    public boolean onPressEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {

        return false;
    }

    @Override
    public boolean onPressUpEvent(ArrayList<TMapMarkerItem> arrayList, ArrayList<TMapPOIItem> arrayList1, TMapPoint tMapPoint, PointF pointF) {
        TMapMarkerItem item;
        int index;

        Log.d("test", "클릭된 마커 갯수 : " + arrayList.size());

        for(int i=0; i<arrayList.size(); i++) {
            item = arrayList.get(i);
            Log.d("test", "클릭된 마커 정보 : " + arrayList.get(i).getName());

            index = Integer.parseInt(arrayList.get(i).getID());
            getLocationImage(index);

            item.setCalloutTitle(locationList.get(index).getTitle());
            item.setCalloutSubTitle(locationList.get(index).getSubTitle());

            item.setCanShowCallout(true);
            item.setAutoCalloutVisible(true);

            item.setCalloutLeftImage(locationImage);
            item.setAutoCalloutVisible(true);

            tMapView.removeMarkerItem(item.getID());
            tMapView.addMarkerItem(item.getID(), item);
        }
        return false;
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
                    showMarkerPoint();
                } else if (msg.what == Constants.RECEIVE_FAILED) {
                    //TODO when received err message
                } else {
                    //   bitmapList.addAll((ArrayList<Bitmap>) (msg.obj));
                    int markerID = msg.what;
                    markerItemList.get(markerID).setCalloutLeftImage(locationImage);
                }
            }
        };

        mContext = this;

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.mapView);
        tMapView = new TMapView(this);

        linearLayout.addView(tMapView);

        mapFilterSpinner = (Spinner)findViewById(R.id.map_filter_spinner);
        mapFilterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, Constants.CONTENTS_TYPE);
        mapFilterSpinner.setAdapter(mapFilterAdapter);
        mapFilterSpinner.setPrompt("관광 데이터 필터");

        mapFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                showMarkerPointWithFilter(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        tMapView.setSKPMapApiKey(APIKey);

        // current sight direction
        //tMapView.setCompassMode(true);
        // mark current location
        tMapView.setIconVisibility(true);

        // zoom level
        tMapView.setZoomLevel(15);

        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tMapView.setOnClickListenerCallBack(this);

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
            public void onCalloutRightButton(TMapMarkerItem tMapMarkerItem) {
                Location selectedLocation = locationList.get(Integer.parseInt(tMapMarkerItem.getID()));
                Intent intent = new Intent();
                intent.putExtra("LOCATION", selectedLocation);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        initPinIcon();

        int mode = getIntent().getExtras().getInt("MODE");

        if (mode == Constants.SEARCH_MAP_MODE) {
            updateMarker(tMapGpsManager.getLocation().getLongitude(), tMapGpsManager.getLocation().getLatitude());
        } else if (mode == Constants.VIEW_LOCATION_MODE) {
            Location location = (Location) (getIntent().getSerializableExtra("LOCATION"));
            tMapView.setLocationPoint(location.getMapY(), location.getMapX());
            locationList.clear();
            locationList.add(location);
            showMarkerPoint();
        } else {
            Log.d("ERR", "invalid intent value");
            finish();
        }
    }

    /*
        when user location change detected, update marker and related data
     */
    private void updateMarker(double longtitude, double latitude) {
        locationImage = null;
        locationList.clear();

        apiController.getLocationList(longtitude, latitude, 1500, handler);
    }

    /*
        when user select contentType filter spinner
     */
    private void showMarkerPointWithFilter(int filter){
        if(Constants.CONTENTES_TYPE_CODE[filter] != Constants.CONTENTES_TYPE_CODE[0]) {
            for (int i = 0; i < locationList.size(); i++) {
                if (locationList.get(i).getContentTypeID() == Constants.CONTENTES_TYPE_CODE[filter]) {
                    markerItemList.get(i).setVisible(TMapMarkerItem.VISIBLE);
                } else {
                    markerItemList.get(i).setVisible(TMapMarkerItem.HIDDEN);
                }

            }
        }
        else {
            for (int i = 0; i < locationList.size(); i++) {
                markerItemList.get(i).setVisible(TMapMarkerItem.VISIBLE);
            }
        }
        tMapView.refreshMap();
    }

    private void showMarkerPoint() {
        int mMarkerID = 0;

        markerItemList.clear();
        markerIdList.clear();

        for (int i = 0; i < locationList.size(); i++) {

            TMapPoint point = new TMapPoint(locationList.get(i).getMapY(), locationList.get(i).getMapX());
            TMapMarkerItem item = new TMapMarkerItem();

            Bitmap pinIconImage;

            int contentTypeID = locationList.get(i).getContentTypeID();

            pinIconImage = getPinIconByContentTypeID(contentTypeID);

            item.setTMapPoint(point);
            item.setName(locationList.get(i).getTitle());

            item.setVisible(item.VISIBLE);
            item.setIcon(pinIconImage);
            item.setPosition((float)0.5, (float)1.0);
            /*
            if (locationList.get(i).getFirstimage()==null)
                item.setCalloutLeftImage(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher));
            else
                item.setCalloutLeftImage(bitmapList.get(i));
            */
            item.setCalloutRightButtonImage(rightButtonImage);

            String strID = Integer.toString(mMarkerID++);

            tMapView.addMarkerItem(strID, item);
            markerIdList.add(strID);
            markerItemList.add(item);
        }
    }

    /*
        Init Pin Icons for each content type ID
     */
    private void initPinIcon(){

        Bitmap bitmap;
        rightButtonImage = BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher);

        for (int code : Constants.CONTENTES_TYPE_CODE) {
            if (code == Constants.CONTENTES_TYPE_CODE[1]) {
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_tourlistattraction);
                pinIconList.append(code, resizeBitmap(bitmap, 200));
            } else if (code == Constants.CONTENTES_TYPE_CODE[2]) {
                //bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_culturalfacility);
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_tourlistattraction);
                pinIconList.append(code, resizeBitmap(bitmap, 200));
            } else if (code == Constants.CONTENTES_TYPE_CODE[3]) {
                //bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_event_performance_festival);
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_tourlistattraction);
                pinIconList.append(code, resizeBitmap(bitmap, 200));
            } else if (code == Constants.CONTENTES_TYPE_CODE[4]) {
                //bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_travelcourse);
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_tourlistattraction);
                pinIconList.append(code, resizeBitmap(bitmap, 200));
            } else if (code == Constants.CONTENTES_TYPE_CODE[5]) {
                //bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_leisure);
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_tourlistattraction);
                pinIconList.append(code, resizeBitmap(bitmap, 200));
            } else if (code == Constants.CONTENTES_TYPE_CODE[6]) {
                //bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_lodging);
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_tourlistattraction);
                pinIconList.append(code, resizeBitmap(bitmap, 200));
            } else if (code == Constants.CONTENTES_TYPE_CODE[7]) {
                //bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_shopping);
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_tourlistattraction);
                pinIconList.append(code, resizeBitmap(bitmap, 200));
            } else if (code == Constants.CONTENTES_TYPE_CODE[8]) {
                //bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_restaurant);
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.map_pin_tourlistattraction);
                pinIconList.append(code, resizeBitmap(bitmap, 200));
            }
        }
    }

    /*
        get Pin Icon for show on map
     */
    private Bitmap getPinIconByContentTypeID(int contentTypeID){
        return pinIconList.get(contentTypeID);
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
    private void getLocationImage(int index){
        final int i = index;

        Thread mThread = new Thread(){
            public void run() {
                Location location = locationList.get(i);
                if(location.getFirstimage() != null) {
                    try {
                        URL url = new URL(location.getFirstimage());

                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoInput(true);
                        conn.connect();

                        InputStream is = conn.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(is);

                        locationImage = resizeBitmap(bitmap, 400);

                    } catch (IOException ex) {
                        locationImage = null;
                        handler.sendEmptyMessage(Constants.RECEIVE_FAILED);
                        ex.printStackTrace();
                    } finally {
                        handler.sendEmptyMessage(i);
                    }
                }
            }
        };

        mThread.start();

        try{
            mThread.join();

            showMarkerPoint();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
