package com.example.application.ProblemDomain;

import java.io.Serializable;

/**
 * Created by jm on 2017-04-23.
 */

public class Constants implements Serializable{
    // contents type/code
    public static final String[]    CONTENTS_TYPE = { "타입 선택", "관광지", "문화시설", "행사/공연/축제", "여행코스", "레포츠", "숙박", "쇼핑", "음식점"};
    public static final int[]       CONTENTES_TYPE_CODE = {-1, 12, 14, 15, 25, 28, 32, 38, 39};
    // tour data call
    public static final int AREA_CODE           = 1;
    public static final int SERVICE_CODE        = 2;
    public static final int AREA_TOUR_CODE      = 3;
    public static final int LOCATION_TOUR_CODE  = 4;

    // youtubeURL call(my API)
    public static final int GET_CONTENT         = 1;
    public static final int GET_TYPE            = 2;
    public static final int ADD                 = 3;
    public static final int URL_CONTENT         = 4;
    public static final int URL_TYPE            = 5;

    // index
    public static final int NAME = 0;
    public static final int CODE = 1;

    public static final String YOUTUBE_KEY = "import android.support.v4.app.Fragment";

    public static final int RECEIVE_SUCCESSS = 1;
    public static final int RECEIVE_FAILED = 2;
}
