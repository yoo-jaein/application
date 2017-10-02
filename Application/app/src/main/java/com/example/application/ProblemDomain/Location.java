package com.example.application.ProblemDomain;

import java.io.Serializable;

public class Location implements Serializable{
	private static final long serialVersionUID = 200L;

	private String bigLocation;
	private String midLocation;
	private String smallLocation;

	private int contentID;
	private int contentTypeID;
	private String title;

	private double mapX;
	private double mapY;

	public Location(){
		bigLocation = "";
		midLocation = "";
		smallLocation = "";
		contentID = 0;
		contentTypeID = 0;
		mapX=-1;
		mapY=-1;
		title = null;
	}
	public String getBigLocation() {
		return bigLocation;
	}
	public void setBigLocation(String bigLocation) {
		this.bigLocation = bigLocation;
	}
	public String getMidLocation() {
		return midLocation;
	}
	public void setMidLocation(String midLocation) {
		this.midLocation = midLocation;
	}
	public String getSmallLocation() {
		return bigLocation;
	}
	public void setSmallLocation(String smallLocation) {
		this.smallLocation = smallLocation;
	}
	public int getContentID() {
		return contentID;
	}
	public void setContentID(int contentID) {
		this.contentID = contentID;
	}
	public int getContentTypeID() {
		return contentTypeID;
	}
	public void setContentTypeID(int contentTypeID) {
		this.contentTypeID = contentTypeID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public double getMapX() {
		return mapX;
	}
	public void setMapX(double mapX) {
		this.mapX = mapX;
	}
	public double getMapY() {
		return mapY;
	}
	public void setMapY(double mapY) {
		this.mapY = mapY;
	}

	@Override
	public String toString() {
		return "Location [bigLocation=" + bigLocation + ", midLocation=" + midLocation + ", smallLocation="
				+ smallLocation + ", contentID=" + contentID + ", contentTypeID=" + contentTypeID + ", title=" + title
				+ "]";
	}

}