package com.example.myapplication.ProblemDomain;

import java.io.File;
import java.io.Serializable;

public class Posts implements Serializable{

	private static final long serialVersionUID = 300L;

	private int index;
	private Location locationInfo;
	private Song song;
	private String comment;

	private int userID;
	private int postsID;
	private int like;

	private long createTime;
	private File Fimage;
	private byte[] Iimage;

	public Posts(){
		this.index = 0;
		this.locationInfo = new Location();
		this.song = null;
		this.postsID = 0;
		this.like = 0;
		this.createTime = 0;
		this.Fimage = null;
		this.Iimage = null;
	}

	public Posts(int index){
		this.index = index;
		this.locationInfo = new Location();
		this.song = null;
		this.postsID = 0;
		this.like = 0;
		this.createTime = 0;
		this.Fimage = null;
		this.Iimage = null;
	}

	public int getPostsIndex(){
		return index;
	}
	public void setPostsIndex(int index){
		this.index = index;
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
	public int getPostsID() {
		return postsID;
	}
	public void setPostsID(int postsID) {
		this.postsID = postsID;
	}
	public int getLike() {
		return like;
	}
	public void setLike(int like) {
		this.like = like;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getFileName(){
		return Integer.toString(index);
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
		return "Posts [index=" + index + ", locationInfo=" + locationInfo + ",song=" + song.toString() + ", comment=" + comment + ", postsID=" + postsID + ", like=" + like
				+ ", createTime=" + createTime + ", Fimage=" + Fimage + ", Iimage=" + Iimage + "]";
	}
}