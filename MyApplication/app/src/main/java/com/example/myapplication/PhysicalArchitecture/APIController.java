package com.example.myapplication.PhysicalArchitecture;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.myapplication.ProblemDomain.Constants;
import com.example.myapplication.ProblemDomain.Location;
import com.example.myapplication.ProblemDomain.Music;

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
 * @author jm
 * 멜론, 관광정보 API 클래스.
 */
public class APIController {
    private static final String TOUR_INIT_URI = "http://api.visitkorea.or.kr/openapi/service/rest/KorService/";
    private static final String TOUR_SERVICE_KEY = "?ServiceKey=Q%2Bh3qGHK7KUnkP%2FiO5s%2BmFf59UnBlEmg4Bkuiuwi8aZxGnzRchGJqZK46x4%2Fh9BGemhiUekc37nT%2BwPGxJMFzA%3D%3D";
    private static final String TOUR_MOBILE_OS = "&MobileOS=AND";
    private static final String TOUR_MOBILE_APP = "&MobileApp=Application";

    private static final String MELON_SEARCH_INIT_URI = "http://apis.skplanetx.com/melon/";
    private static final String MELON_API_KEY   = "254504f1-4949-3f5b-95d9-4325510614f1";
    private static final String MELON_VERSION = "?version=";
    private static final String MELON_PAGE = "&page=";
    private static final String MELON_COUNT = "&count=";
    private static final String MELON_SEARCH_KEYWORD = "&searchKeyword=";

    private static final int SEARCH_BY_AREA_CODE = 1;   // search area code
    private static final int SEARCH_BY_CATEGORY_CODE = 2;   // search category code
    private static final int SEARCH_BY_AREA_BASED_LIST = 3;   // search tour list by area code
    private static final int SEARCH_BY_LOCATION_BASED_LIST = 4;   // search tour list by x, y location

    private static final int SEARCH_ALBUM = 5;  // search album
    private static final int SEARCH_ARTIST = 6; // search artist
    private static final int SEARCH_SONG = 7;   // search music

    private static APIController apiController = new APIController();

    private Handler handler = null;
    private MelonAPIThread melonAPIThread = null;

    public static APIController getAPIController(){
        return apiController;
    }

    private String getQueryMode(int code) {
        switch (code) {
            case SEARCH_BY_AREA_CODE:
                return "areaCode";
            case SEARCH_BY_CATEGORY_CODE:
                return "categoryCode";
            case SEARCH_BY_AREA_BASED_LIST:
                return "areaBasedList";
            case SEARCH_BY_LOCATION_BASED_LIST:
                return "locationBasedList";
            case SEARCH_ALBUM:
                return "albums";
            case SEARCH_ARTIST:
                return "artists";
            case SEARCH_SONG:
                return "songs";
            default:
                return "";
        }
    }

    public void setHandlerNull(){
        this.handler = null;
    }

    public void getLocationList(double mapX, double mapY, int radius, Handler handler){
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

        thread = new TourAPIThread(SEARCH_BY_LOCATION_BASED_LIST, query);
        thread.start();
    }

    public void getLocationContentIdList(double mapX, double mapY, int radius, Handler handler){
        this.handler = handler;
        TourAPIContentIdThread thread;

        String query = "";
        query += "&mapX=";
        query += mapX;
        query += "&mapY=";
        query += mapY;
        query += "&radius=";
        query += radius;
        query += "&numOfRows=";
        query += 50;

        thread = new TourAPIContentIdThread(SEARCH_BY_LOCATION_BASED_LIST, query, handler);
        thread.start();
    }

    public void getMusicList(String keyword, Handler handler){
        this.handler = handler;

        melonAPIThread = new MelonAPIThread(SEARCH_SONG, keyword);

        melonAPIThread.start();
    }

    private class MelonAPIThread extends Thread {
        private ArrayList<Music> musicList;
        private String keyword = "";
        private String call;
        private String queryURL;
        private boolean play = false;

        private MelonAPIThread(int callCode, String keyword){
            this.call = getQueryMode(callCode);
            this.keyword = keyword;

            queryURL = "" + MELON_SEARCH_INIT_URI + call + MELON_VERSION + "1" + MELON_PAGE + "1"
                    + MELON_COUNT + "10" + MELON_SEARCH_KEYWORD + keyword;

            musicList = new ArrayList<>();

            Log.d("test", "create query : " + queryURL);
        }

        @Override
        public void run() {
            Log.d("URL", queryURL);

            try {
                Music music = new Music();

                URL url = new URL(queryURL);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                if(urlConnection == null){
                    Log.d("test" , "urlConnection is null");
                }
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("Accept", "application/xml");
                urlConnection.setRequestProperty("appKey", MELON_API_KEY);

                InputStream is = new BufferedInputStream(urlConnection.getInputStream());

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();

                xpp.setInput(new InputStreamReader(is, "UTF-8"));

                String tag;

                xpp.next();
                int eventType = xpp.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {
                    switch (eventType) {
                        case XmlPullParser.START_DOCUMENT:
                            break;

                        case XmlPullParser.START_TAG:
                            tag = xpp.getName();

                            if (tag.equals("songId")) {
                                xpp.next();
                                if (music.getMusicId() != -1) {
                                    musicList.add(music);
                                    music = new Music();
                                }
                                music.setMusicId(Integer.parseInt(xpp.getText()));
                            } else if (tag.equals("songName")) {
                                xpp.next();
                                music.setMusicName(xpp.getText());
                                Log.d("test", "musicName : " + xpp.getText());
                            } else if (tag.equals("albumId")) {
                                xpp.next();
                                music.setAlbumId(Integer.parseInt(xpp.getText()));
                            } else if (tag.equals("albumName")) {
                                xpp.next();
                                music.setAlbumName(xpp.getText());
                            } else if (tag.equals("artistId")) {
                                xpp.next();
                                if (music.getArtistId() == -1)
                                    music.setArtistId(Integer.parseInt(xpp.getText()));
                            } else if (tag.equals("artistName")) {
                                xpp.next();
                                if (music.getArtistName() == null)
                                    music.setArtistName(xpp.getText());
                            } else if (tag.equals("menuId")) {
                                xpp.next();
                                if (music.getMenuId() == -1)
                                    music.setMenuId(Integer.parseInt(xpp.getText()));
                            }

                        case XmlPullParser.TEXT:
                            break;
                        case XmlPullParser.END_TAG:
                            break;
                    }
                    eventType = xpp.next();
                }

                if(music.getMusicId() != -1)
                    musicList.add(music);

                Message message;

                message = Message.obtain(handler, Constants.RECEIVE_LOCATION_LIST, musicList);
                message.what = Constants.RECEIVE_SUCCESSS;

                // if get data fininsh, edit main thread UI
                handler.sendMessage(message);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private class TourAPIContentIdThread extends Thread{
        private ArrayList<Integer> locationContentIdList;

        private String query = "";
        private String call;

        private Handler contentIdHandler;

        private TourAPIContentIdThread(int callCode, String query, Handler handler) {
            this.call = getQueryMode(callCode);
            this.query = query;

            contentIdHandler = handler;
            locationContentIdList = new ArrayList<>();
        }

        @Override
        public void run() {
            String queryURL = "" + TOUR_INIT_URI + call + TOUR_SERVICE_KEY + query
                    + TOUR_MOBILE_OS + TOUR_MOBILE_APP;
            Log.d("URL", queryURL);

            boolean nameCodeList = false;

            try {
                URL url = new URL(queryURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream is = new BufferedInputStream(urlConnection.getInputStream());

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new InputStreamReader(is, "UTF-8"));

                String tag;
                String sub = "";

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

                            if (tag.equals("contentid")) {
                                xpp.next();
                                locationContentIdList.add(Integer.parseInt(xpp.getText()));
                            }
                        case XmlPullParser.TEXT:
                            break;
                        case XmlPullParser.END_TAG:
                            tag = xpp.getName();
                            break;
                    }
                    eventType = xpp.next();
                }

                if(locationContentIdList.size() > 0) {
                    Message message = Message.obtain(handler, Constants.RECEIVE_LOCATION_LIST, locationContentIdList);
                    message.what = Constants.RECEIVE_SUCCESSS;
                    contentIdHandler.sendMessage(message);
                }else{
                    contentIdHandler.sendEmptyMessage(Constants.RECEIVE_FAILED);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class TourAPIThread extends Thread {
        // save code and name String list
        private ArrayList<ArrayList<String>> list;
        private ArrayList<Location> locationList;

        private String query = "";
        private String call;

        private TourAPIThread(int callCode, String query) {
            this.call = getQueryMode(callCode);
            this.query = query;

            list = new ArrayList<>();
            locationList = new ArrayList<>();
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
                String sub = "";

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
                                    location.setSubTitle(sub);
                                    sub = "";
                                    locationList.add(location);
                                    location = new Location();
                                }
                                xpp.next();
                                sub += xpp.getText();
                                sub += " ";
                            } else if (tag.equals("addr2")) {
                                xpp.next();
                                sub += xpp.getText();
                                sub += " ";
                            } else if (tag.equals("contentid")) {
                                xpp.next();
                                location.setContentID(Integer.parseInt(xpp.getText()));
                            } else if (tag.equals("contenttypeid")) {
                                xpp.next();
                                location.setContentTypeID(Integer.parseInt(xpp.getText()));
                            } else if (tag.equals("firstimage2")) {
                                xpp.next();
                                location.setFirstimage(xpp.getText());
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
                                sub += "\n";
                                sub += xpp.getText();
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

                for(int i=0; i<locationList.size(); i++)
                    Log.d("test", locationList.get(i).toString());

                Message message;

                if(nameCodeList)
                    message = Message.obtain(handler, Constants.RECEIVE_CODE_LIST, list);
                else
                    message = Message.obtain(handler, Constants.RECEIVE_LOCATION_LIST, locationList);
                message.what = Constants.RECEIVE_SUCCESSS;

                // if get data fininsh, edit main thread UI
                handler.sendMessage(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

