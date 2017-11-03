package com.example.myapplication.ProblemDomain;

import java.io.Serializable;

/**
 * Created by jm on 2017-04-23.
 */

public class Constants{
    // contents type/code
    public static final String[]    CONTENTS_TYPE = { "타입 선택", "관광지", "문화시설", "행사/공연/축제", "여행코스", "레포츠", "숙박", "쇼핑", "음식점"};
    public static final int[]       CONTENTES_TYPE_CODE = {-1, 12, 14, 15, 25, 28, 32, 38, 39};

    // index
    public static final int NAME = 0;
    public static final int CODE = 1;

    // handler request code
    public static final int RECEIVE_FAILED = -3;
    public static final int RECEIVE_ERROR = -2;
    public static final int RECEIVE_SUCCESSS = -1;
    public static final int RECEIVE_REFRESH = 40;
    public static final int RECEIVE_MORE = 41;
    public static final int RECEIVE_CODE_LIST = 0;
    public static final int RECEIVE_LOCATION_LIST = 1;

    public static final int RECEIVE_LIKE = 2;
    public static final int RECEIVE_DISLIKE = 3;

    public static final int SEARCH_MAP_MODE = 10;
    public static final int VIEW_LOCATION_MODE = 11;

    public static final int GET_SONG = 20;
    public static final int GET_LOCATION = 21;
    public static final int GET_PICTURE_URI = 22;

    public static final int TIME = 30;
    public static final int DISTANCE = 31;
    public static final int LIKE = 32;

    public static final int WAIT_LOGIN = 50;
    public static final int WAIT_REGISTER = 51;
    public static final int WAIT_FINDPASS = 52;
    public static final int WAIT_REFRESH = 53;
    public static final int WAIT_MOREPOSTS = 54;
    public static final int WAIT_TOTALLIKE = 55;
    public static final int WAIT_MYPOSTSLIST = 56;
    public static final int WAIT_MOREMYPOSTS = 57;
    public static final int WAIT_MYLIKELIST = 58;
    public static final int WAIT_MOREMYLIKE = 59;
    public static final int WAIT_POST = 60;
    public static final int WAIT_DELETE = 61;
    public static final int WAIT_LIKE = 62;
    public static final int WAIT_DISLIKE = 63;
    public static final int WAIT_UPDATEUSER = 64;
    public static final int WAIT_GETPOSTLIKEBYPOSTID = 65;
}
