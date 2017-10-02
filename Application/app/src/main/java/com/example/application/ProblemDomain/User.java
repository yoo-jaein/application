package com.example.application.ProblemDomain;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author 안준영
 *
 */
public class User implements Serializable {

	private static final long serialVersionUID = 400L;

	private int index;
	private String id, pw;
	private ArrayList<Object> likeList;
	private ArrayList<Object> myList;
	private File Fimage;
	private byte[] Iimage;

	public User() {
		this.index = 0;
		this.id = null;
		this.pw = null;
		this.likeList = new ArrayList<Object>();
		this.myList = new ArrayList<Object>();
		this.Fimage = null;
		this.Iimage = null;
	}

	public User(int index, String id, String pw) {

		this.index = index;
		this.id = id;
		this.pw = pw;
		this.likeList = new ArrayList<Object>();
		this.myList = new ArrayList<Object>();
		this.Fimage = null;
		this.Iimage = null;

	}

	public String getId() {
		return id;
	}

	public String getPw() {
		return pw;
	}

	public int getUserIndex() {
		return index;
	}

	public ArrayList<Object> getLikeList() {
		return likeList;
	}

	public ArrayList<Object> getMyList() {
		return myList;
	}

	public void setUser(int index, String id, String pw) {
		this.index = index;
		this.id = id;
		this.pw = pw;
	}

	public void delLikeList(int postsIndex) {
		likeList.remove(postsIndex);
	}

	public void addLikeList(int postIndex) {
		likeList.add(postIndex);
	}

	public void setUserIndex(int userIndex) {
		this.index = userIndex;
	}

	public void setUserId(String id) {
		this.id = id;
	}

	public void setUserPw(String pw) {
		this.pw = pw;
	}

	public void setUserLikeList(ArrayList<Object> likeList) {
		this.likeList = likeList;
	}

	public void setUserMyList(ArrayList<Object> myList) {
		this.myList = myList;
	}

	public File getFImage() {
		return Fimage;
	}

	public void setFImage(File image) {
		this.Fimage = image;
	}

	public byte[] getIImage() {
		return Iimage;
	}

	public void setIImage(byte[] image) {
		this.Iimage = image;
	}

	@Override
	public String toString() {
		return "User [index=" + index + ", id=" + id + ", pw=" + pw + ", likeList=" + likeList + ", myList=" + myList
				+ ", Fimage=" + Fimage + ", Iimage=" + Arrays.toString(Iimage) + "]";
	}


}