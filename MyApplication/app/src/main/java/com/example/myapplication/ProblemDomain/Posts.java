package com.example.myapplication.ProblemDomain;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;

public class Posts implements Serializable{

	private static final long serialVersionUID = 300L;

	private int postsID;

	private Location locationInfo;
	private Music music;
	private String comment;

	private int userID;

	private int like;

	private String createTime;
	private File Fimage;
	private byte[] Iimage;

	public Posts(){
		this.postsID = 0;
		this.locationInfo = new Location();
		this.music = null;
		this.like = 0;
		this.createTime = "";
		this.Fimage = null;
		this.Iimage = null;
	}

	public Posts(int index){
		this.postsID = index;
		this.locationInfo = new Location();
		this.music = null;
		this.like = 0;
		this.createTime = "";
		this.Fimage = null;
		this.Iimage = null;
	}

	public int getPostsIndex(){
		return postsID;
	}
	public void setPostsIndex(int postsID){
		this.postsID = postsID;
	}

	public Location getLocationInfo() {
		return locationInfo;
	}
	public void setLocationInfo(Location locationInfo) {
		this.locationInfo = locationInfo;
	}

	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public int getLike() {
		return like;
	}
	public void setLike(int like) {
		this.like = like;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public void setMusic(Music music){
		this.music = music;
	}
	public Music getMusic(){
		return music;
	}
	public String getFileName(){
		return Integer.toString(postsID);
	}
	public File getFImage(){
		return Fimage;
	}
	public void setFImage(File image){
		this.Fimage = image;
	}
	public byte[] getIImage(){
		return Iimage;
	}
	public void setIImage(byte[] image){
		this.Iimage = image;
	}

	public byte[] getImage() {
		return Iimage;
	}

	@Override
	public String toString() {
		return "Posts [postsID=" + postsID + ", locationInfo=" + locationInfo + ", music=" + music + ", comment=" + comment
				+ ", userID=" + userID + ", like=" + like + ", createTime=" + createTime + ", Fimage=" + Fimage
				+ ", Iimage=" + Arrays.toString(Iimage) + "]";
	}


}