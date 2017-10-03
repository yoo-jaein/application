package com.example.myapplication.PhysicalArchitecture;

import android.os.Handler;

import com.example.application.Foundation.PostsList;
import com.example.application.ProblemDomain.Posts;
import com.example.application.ProblemDomain.User;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientControl implements Serializable{

	private ArrayList<Posts> timeLine;
	private ArrayList<Posts> myPostsList;
	private ArrayList<Posts> myLikeList;
	private User me = null;

	// for change mainthread UI
	private Handler handler = null;

	private String message = "";
	/*
       checking request complete
     */
	private boolean waiting = false;

	private boolean login = false;
	private boolean register = false;
	private boolean refresh = false;
	private boolean morePosts = false;
	private boolean myPosts = false;
	private boolean moreMyPosts = false;
	private boolean myLike = false;
	private boolean moreMyLike = false;
	private boolean post = false;
	private boolean delete = false;
	private boolean like = false;
	private boolean dislike = false;
	private boolean updateUser = false;
	private boolean closeSocket = false;

	private long startTime = 0;

	public Client client;
	private static ClientControl cControl = new ClientControl();

	public ClientControl() {
		timeLine = new ArrayList<Posts>();
		myPostsList = new ArrayList<Posts>();
		myLikeList  = new ArrayList<Posts>();

		me = null;

		client = new Client();
	}

	public static ClientControl getClientControl(){
		return cControl;
	}

   /*
      received data control
    */

	public void setTimeLine(PostsList postsList) { timeLine = postsList.getAll();}

	public void addTimeLine(PostsList postsList) {   timeLine.addAll(postsList.getAll());   }

	public void setMyPostsList(PostsList postsList) {   myPostsList = postsList.getAll();   }

	public void addMyPostsList(PostsList postsList) {   myPostsList.addAll(postsList.getAll());   }

	public void setMyLikeList(PostsList postsList) {   myLikeList = postsList.getAll();   }

	public void addMyLikeList(PostsList postsList) {   myLikeList.addAll(postsList.getAll());   }

	public void setMe(User user) {
		me = user;
	}


	public void resetAll() {
		timeLine = new ArrayList<Posts>();
		myPostsList = new ArrayList<Posts>();
		myLikeList  = new ArrayList<Posts>();

		me = null;
	}

	/*
       send message to server
       if checking boolean is true, don't execute
     */

	public void login(String id, String pass){
		if(!waiting) {
			startTime = System.currentTimeMillis();

			waiting = true;
			login = true;

			message = "#login%";
			message += id;
			message += "%";
			message += pass;

			client.sendToServer(message);
			message = "";
		}
	}

	public void register(String id, String pass){
		if(!waiting) {
			startTime = System.currentTimeMillis();

			waiting = true;
			register = true;

			message = "#register%";
			message += id;
			message += "%";
			message += pass;

			client.sendToServer(message);

			message = "";
		}
	}

	public void refresh(){
		if(!waiting) {
			startTime = System.currentTimeMillis();

			waiting = true;
			refresh = true;

			message = "#refresh";

			client.sendToServer(message);

			message = "";
		}
	}

	public void morePosts(){
		if(!waiting) {
			startTime = System.currentTimeMillis();

			waiting = true;
			morePosts = true;

			message = "#morePosts";

			client.sendToServer(message);

			message = "";
		}
	}

	public void myPosts(){
		if(!waiting) {
			startTime = System.currentTimeMillis();

			waiting = true;
			myPosts = true;

			message = "#myPosts";

			client.sendToServer(message);

			message = "";
		}
	}

	public void moreMyPosts(){
		if(!waiting) {
			startTime = System.currentTimeMillis();

			waiting = true;
			moreMyPosts = true;

			message = "#moreMyPosts";

			client.sendToServer(message);

			message = "";
		}
	}

	public void myLike(){
		if(!waiting) {
			startTime = System.currentTimeMillis();

			waiting = true;

			message = "#myLike";

			client.sendToServer(message);

			message = "";
		}
	}

	public void moreMyLike(){
		if(!waiting) {
			startTime = System.currentTimeMillis();

			waiting = true;
			moreMyLike = true;

			message = "#moreLike";

			client.sendToServer(message);

			message = "";
		}
	}

	public void post(Posts p){
		if(!waiting) {
			startTime = System.currentTimeMillis();

			waiting = true;
			post = true;

			client.sendToServer(p);
		}
	}

	public void delete(int index){
		if(!waiting) {
			startTime = System.currentTimeMillis();

			waiting = true;
			delete = true;

			message = "#delete%";
			message += index;

			client.sendToServer(message);

			message = "";
		}
	}

	public void like(int index   ){
		if(!waiting) {
			startTime = System.currentTimeMillis();

			waiting = true;
			like = true;

			message = "#like%";
			message += index;

			client.sendToServer(message);

			message = "";
		}
	}

	public void dislike(int index){
		if(!waiting) {
			startTime = System.currentTimeMillis();

			waiting = true;

			message = "#dislike%";
			message += index;

			client.sendToServer(message);

			message = "";
		}
	}

	public void updateUser(){
		if(!waiting) {
			startTime = System.currentTimeMillis();

			waiting = true;

			message = "#updateUser";

			client.sendToServer(message);

			message = "";
		}
	}

   /*
      get and set method
    */
	public Handler getHandler() { return handler;}

   public void setHandler(Handler handler) { this.handler = handler;}

   public boolean isWaiting() {
		return waiting;
	}

	public void setWaiting(boolean waiting) {
		this.waiting = waiting;
	}

	public boolean isLogin() {
		return login;
	}

	public boolean isRegister() {
		return register;
	}

	public boolean isRefresh() {
		return refresh;
	}

	public boolean isMorePosts() {
		return morePosts;
	}

	public boolean isMyLike() {
		return myLike;
	}

	public boolean isMyPost() {
		return myPosts;
	}

	public boolean isMoreMyLike() {
		return moreMyLike;
	}

	public boolean isPost() {
		return post;
	}

	public boolean isDelete() {
		return delete;
	}

	public boolean isLike() {
		return like;
	}

	public boolean isDislike() {
		return dislike;
	}

	public boolean isUpdateUser() {
		return updateUser;
	}

	public void setStartTime(long time) { this.startTime = time; }

	public long getStartTime() {
		return startTime;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}

	public void setRegister(boolean register) {
		this.register = register;
	}

	public void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}

	public void setMorePosts(boolean morePosts) {
		this.morePosts = morePosts;
	}

	public void setMyLike(boolean myLike) {
		this.myLike = myLike;
	}

	public void setMoreMyLike(boolean moreLike) {
		this.moreMyLike = moreLike;
	}

	public void setPost(boolean post) {
		this.post = post;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public void setLike(boolean like) {
		this.like = like;
	}

	public void setDislike(boolean dislike) {
		this.dislike = dislike;
	}

	public void setUpdateUser(boolean updateUser) {
		this.updateUser = updateUser;
	}

	public boolean isMyPosts() {
		return myPosts;
	}

	public void setMyPosts(boolean myPosts) {
		this.myPosts = myPosts;
	}

	public boolean isMoreMyPosts() {
		return moreMyPosts;
	}

	public void setMoreMyPosts(boolean moreMyPosts) {
		this.moreMyPosts = moreMyPosts;
	}

	public boolean isCloseSocket(){   return closeSocket;   }

	public void setCloseSocket() {
		closeSocket = false;
	}

	public User getMe() {
		return me;
	}


	/*
       get data
     */
	public ArrayList<Posts> getTimeLine() {
		return timeLine;
	}

	public ArrayList<Posts> getMyPostsList() {
		return myPostsList;
	}

	public ArrayList<Posts> getMyLikeList() {
		return myLikeList;
	}

	/*
       socket connection method
     */
	public void terminateConnection(){
		closeSocket = true;
	}

}