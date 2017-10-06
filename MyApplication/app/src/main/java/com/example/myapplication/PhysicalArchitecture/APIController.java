package com.example.myapplication.PhysicalArchitecture;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.myapplication.ProblemDomain.Constants;
import com.example.myapplication.ProblemDomain.Location;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.example.myapplication.ProblemDomain.Constants.CODE;
import static com.example.myapplication.ProblemDomain.Constants.NAME;

/**
 * Created by jm on 2017-10-03.
 */

public class APIController {
    private static final String TOUR_INIT_URI = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/";
    private static final String TOUR_SERVICE_KEY = "?ServiceKey=Q%2Bh3qGHK7KUnkP%2FiO5s%2BmFf59UnBlEmg4Bkuiuwi8aZxGnzRchGJqZK46x4%2Fh9BGemhiUekc37nT%2BwPGxJMFzA%3D%3D";
    private static final String TOUR_MOBILE_OS = "&MobileOS=AND";
    private static final String TOUR_MOBILE_APP = "&MobileApp=Application";

    private static final String MELON_INIT_URI = "http://apis.skplanetx.com/melon/";
    private static final String MELON_API_KEY = "254504f1-4949-3f5b-95d9-4325510614f1";

    private static final int AREA_CODE = 1;   // search area code
    private static final int CATEGORY_CODE = 2;   // search category code
    private static final int AREA_BASED_LIST = 3;   // search tour list by area code
    private static final int LOCATION_BASED_LIST = 4;   // search tour list by x, y location

    private Handler handler = null;

    private String getQueryMode(int code) {
        switch (code) {
            case AREA_CODE:
                return "areaCode";
            case CATEGORY_CODE:
                return "categoryCode";
            case AREA_BASED_LIST:
                return "areaBasedList";
            case LOCATION_BASED_LIST:
                return "locationBasedList";
            default:
                return "";
        }
    }

    public void getLocationListByCurrent(double mapX, double mapY, int radius, Handler handler){
        if(this.handler != null)
            return;

        this.handler = handler;
        TourAPIThread thread;

        String query = "";
        query += "&mapX=";
        query += mapX;
        query += "&mapY=";
        query += mapY;
        query += "&radius=";
        query += radius;
        query += "&numOfRows=";
        query += 50;

        thread = new TourAPIThread(LOCATION_BASED_LIST, query);
        thread.start();
    }

    class TourAPIThread extends Thread {
        // save code and name String list
        private ArrayList<ArrayList<String>> list;
        private ArrayList<Location> locationList;

        private String query = "";
        private String call;

        public TourAPIThread(int callCode, String query) {
            this.call = getQueryMode(callCode);
            this.query = query;

            list = new ArrayList<ArrayList<String>>();
            locationList = new ArrayList<Location>();
        }

        @Override
        public void run() {
            String queryURL = "" + TOUR_INIT_URI + call + TOUR_SERVICE_KEY + query
                    + TOUR_MOBILE_OS + TOUR_MOBILE_APP;
            Log.d("URL", queryURL);

            boolean nameCodeList = false;

            try {
                Location location = new Location();

                URL url = new URL(queryURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream is = new BufferedInputStream(urlConnection.getInputStream());

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new InputStreamReader(is, "UTF-8"));

                String tag;

                xpp.next();
                int eventType = xpp.getEventType();

                ArrayList<String> nameList = new ArrayList<>();
                ArrayList<String> codeList = new ArrayList<>();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;

                        case XmlPullParser.START_TAG:
                            tag = xpp.getName();

                            if (tag.equals("code")) {
                                xpp.next();
                                codeList.add(xpp.getText());
                            } else if (tag.equals("name")) {
                                xpp.next();
                                nameList.add(xpp.getText());
                                nameCodeList = true;
                            } else if (tag.equals("addr1")) {
                                if (location.getTitle() != null) {
                                    locationList.add(location);
                                    location = new Location();
                                }
                                xpp.next();
                            } else if (tag.equals("addr2")) {
                                xpp.next();
                            } else if (tag.equals("contentid")) {
                                xpp.next();
                                location.setContentID(Integer.parseInt(xpp.getText()));
                            } else if (tag.equals("contenttypeid")) {
                                xpp.next();
                                location.setContentTypeID(Integer.parseInt(xpp.getText()));
                            } else if (tag.equals("firstimage")) {
                                xpp.next();
                            } else if (tag.equals("secondimage")) {
                                xpp.next();
                            } else if (tag.equals("mapx")) {
                                xpp.next();
                                location.setMapX(Double.parseDouble(xpp.getText()));
                            } else if (tag.equals("mapy")) {
                                xpp.next();
                                location.setMapY(Double.parseDouble(xpp.getText()));
                            } else if (tag.equals("tel")) {
                                xpp.next();
                            } else if (tag.equals("title")) {
                                xpp.next();
                                location.setTitle(xpp.getText());
                            } else if (tag.equals("mlevel")) {
                                xpp.next();
                            } else if (tag.equals("cat1")) {
                                xpp.next();
                                location.setBigLocation(xpp.getText());
                            } else if (tag.equals("cat2")) {
                                xpp.next();
                                location.setMidLocation(xpp.getText());
                            } else if (tag.equals("cat3")) {
                                xpp.next();
                                location.setSmallLocation(xpp.getText());
                            }

                        case XmlPullParser.TEXT:
                            break;
                        case XmlPullParser.END_TAG:
                            tag = xpp.getName();
                            break;
                    }
                    eventType = xpp.next();
                }
                list.add(NAME, nameList);
                list.add(CODE, codeList);
                locationList.add(location);
                Message message;

                if(nameCodeList)
                    message = Message.obtain(handler, Constants.RECEIVE_CODE_LIST, list);
                else
                    message = Message.obtain(handler, Constants.RECEIVE_LOCATION_LIST, locationList);

                handler.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class MelonAPIThread extends Thread {}

}

