package com.example.application.ProblemDomain;

import java.io.Serializable;

/**
 * Created by jm on 2017-04-23.
 * 관광 정보 데이터 클래스
 */

public class TourData implements Serializable{

    private String addr1            = null;         // 주소
    private String addr2            = null;         // 상세주소
    private int contentid           = -1;         //
    private int contenttypeid       = -1;         //
    private String firstimage       =  null;       // 메인 이미지
    private String firstimage2      = null;       // 썸네일
    private double mapx = -1, mapy  = -1;    // GPS좌표
    private String tel              = null;         // 전화번호
    private String title            = null;         // 이름
    private int mlevel              = -1;         // 지도 축척

    public TourData(){}

    public TourData(String addr1, String addr2, int contentid, int contenttypeid, String firstimage, String firstimage2, double mapx, double mapy, String tel, String title, int mlevel) {
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.contentid = contentid;
        this.contenttypeid = contenttypeid;
        this.firstimage = firstimage;
        this.firstimage2 = firstimage2;
        this.mapx = mapx;
        this.mapy = mapy;
        this.tel = tel;
        this.title = title;
        this.mlevel = mlevel;
    }

    public void setContentid(int contentid) {
        this.contentid = contentid;
    }

    public void setContenttypeid(int contenttypeid) {
        this.contenttypeid = contenttypeid;
    }

    public void setMapx(double mapx) {
        this.mapx = mapx;
    }

    public void setMapy(double mapy) {
        this.mapy = mapy;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }

    public void setFirstimage(String firstimage) {
        this.firstimage = firstimage;
    }

    public void setFirstimage2(String firstimage2) {
        this.firstimage2 = firstimage2;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMlevel(int mlevel) {
        this.mlevel = mlevel;
    }

    public String getAdd1() {
        if (addr1 != null)
            return addr1;
        else
            return null;
    }

    public String getAdd2() {
        if (addr2 != null)
            return addr2;
        else
            return null;
    }

    public int getContentid() {
        return contentid;
    }

    public int getContenttypeid() {
        return contenttypeid;
    }

    public String getFirstimage() {
        return firstimage;
    }

    public String getSecondimage() {
        return firstimage2;
    }

    public double getMapx() {
        return mapx;
    }

    public double getMapy() {
        return mapy;
    }

    public String getTel() {
        return tel;
    }

    public String getTitle() {
        return title;
    }

    public int getMlevel() {
        return mlevel;
    }
}
