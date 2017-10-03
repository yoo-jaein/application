package com.example.application.PhysicalArchitecture;

import android.util.Log;

import com.example.application.ProblemDomain.Location;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static com.example.application.ProblemDomain.Constants.CODE;
import static com.example.application.ProblemDomain.Constants.NAME;

/**
 * Created by jm on 2017-10-03.
 */

public class APIController {
    private static final String TOUR_INIT_URI     = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/";
    private static final String TOUR_SERVICE_KEY  = "?ServiceKey=Q%2Bh3qGHK7KUnkP%2FiO5s%2BmFf59UnBlEmg4Bkuiuwi8aZxGnzRchGJqZK46x4%2Fh9BGemhiUekc37nT%2BwPGxJMFzA%3D%3D";
    private static final String TOUR_MOBILE_OS    = "&MobileOS=AND";
    private static final String TOUR_MOBILE_APP   = "&MobileApp=Application";

    private static final String MELON_INIT_URI     = "http://apis.skplanetx.com/melon/";

    private static final int AREA_CODE           = 1;
    private static final int SERVICE_CODE        = 2;
    private static final int AREA_TOUR_CODE      = 3;
    private static final int LOCATION_TOUR_CODE  = 4;


    private String getCallString(int code) {
        switch (code) {
            case AREA_CODE:
                return "areaCode";
            case SERVICE_CODE:
                return "categoryCode";
            case AREA_TOUR_CODE:
                return "areaBasedList";
            case LOCATION_TOUR_CODE:
                return "locationBasedList";
            default:
                return "";
        }

        class TourAPIThread extends Thread {
            // save code and name String list
            private ArrayList<ArrayList<String>> list;
            private ArrayList<Location> locationList;

            private String query = "";
            private String call;

            public TourAPIThread(int callCode, String query) {
                this.call = getCallString(callCode);
                this.query = query;

                list = new ArrayList<ArrayList<String>>();

                locationList = new ArrayList<Location>();
            }

            @Override
            public void run() {
                String queryURL = "" + TOUR_INIT_URI + call + TOUR_SERVICE_KEY + query
                        + TOUR_MOBILE_OS + TOUR_MOBILE_APP;
                Log.d("URL", queryURL);

                try {
                    URL url = new URL(queryURL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream is = new BufferedInputStream(urlConnection.getInputStream());

                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser xpp = factory.newPullParser();
                    xpp.setInput(new InputStreamReader(is, "UTF-8"));  //inputstream �쑝濡쒕��꽣 xml �엯�젰諛쏄린

                    String tag;

                    xpp.next();
                    int eventType = xpp.getEventType();

                    Location location = new Location();

                    ArrayList<String> nameList = new ArrayList<>();
                    ArrayList<String> codeList = new ArrayList<>();

                    Log.d("api", "start while : " + location.getTitle());

                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_DOCUMENT:
                                break;

                            case XmlPullParser.START_TAG:
                                tag = xpp.getName();    //�뀒洹� �씠由� �뼸�뼱�삤湲�

                                if (tag.equals("code")) {
                                    xpp.next();
                                    codeList.add(xpp.getText());
                                } else if (tag.equals("name")) {
                                    xpp.next();
                                    nameList.add(xpp.getText());
                                } else if (tag.equals("addr1")) {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public ArrayList<ArrayList<String>> getList() {
                return list;
            }

            public ArrayList<Location> getTourDataList() {
                return locationList;
            }

            Handler handler = new Handler() {
                @Override
                public void publish(LogRecord logRecord) {

                }

                @Override
                public void flush() {

                }

                @Override
                public void close() throws SecurityException {

                }
            };
        }
    }
}

