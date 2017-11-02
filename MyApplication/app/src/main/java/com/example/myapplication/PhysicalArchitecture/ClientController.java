package com.example.myapplication.PhysicalArchitecture;

import android.content.Context;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.myapplication.Foundation.PostsList;
import com.example.myapplication.ProblemDomain.Constants;
import com.example.myapplication.ProblemDomain.Posts;
import com.example.myapplication.ProblemDomain.User;
import com.example.myapplication.Service.GPSInfo;

import java.util.ArrayList;

/**
 * @author jm
 * 서버-클라이언트 기능 구현 클래스
 */
public class ClientController{

	private ArrayList<Posts> timeLine;
	private ArrayList<Posts> myPostsList;
	private ArrayList<Posts> myLikeList;
	private ArrayList<Posts> moreList;

	private ArrayList<Integer> locationContentIdList;

	private User me = null;

	private int timeLineOrder = Constants.TIME;

	// for change mainthread UI
	private Handler handler = null;
	private Handler distanceOrderHandler = null;
	private GPSInfo gpsInfo = null;

	private String message = "";

	/*
       checking request complete.
     */
	private boolean waiting = false;

	private boolean login = false;
	private boolean register = false;
	private boolean findPass = false;
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

	private int level = 1;

	public Client client;
	private static ClientController cControl = new ClientController();

	public ClientController() {
		timeLine = new ArrayList<>();
		myPostsList = new ArrayList<>();
		myLikeList  = new ArrayList<>();
		moreList = new ArrayList<>();

		locationContentIdList = new ArrayList<Integer>();

		me = null;

		client = new Client();

		distanceOrderHandler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				if(msg.what == Constants.RECEIVE_SUCCESSS){
                    Log.d("test", "receive GPS inform");
					locationContentIdList = (ArrayList<Integer>)(msg.obj);
					level = 0;
				}
				else if(msg.what == Constants.RECEIVE_FAILED){
					// TODO when received falled
					if(gpsInfo!= null){
						gpsInfo.getLocation(level++);
					}
				}
			}
		};
	}

	public static ClientController getClientControl(){
		return cControl;
	}

   /*
      received data control
    */

	void setTimeLine(PostsList postsList) { timeLine = postsList.getAll();}

	 void addTimeLine(PostsList postsList) {   timeLine.addAll(postsList.getAll());   }

	 void setMyPostsList(PostsList postsList) {   myPostsList = postsList.getAll();   }

	 void addMyPostsList(PostsList postsList) {   myPostsList.addAll(postsList.getAll());   }

	 void setMyLikeList(PostsList postsList) {   myLikeList = postsList.getAll();   }

	 void addMyLikeList(PostsList postsList) {   myLikeList.addAll(postsList.getAll());   }

	void setMoreList(PostsList postsList){ moreList = postsList.getAll(); }

	 void setMe(User user) {
		me = user;
	}

	public void setGpsInfo(GPSInfo gpsInfo){
		this.gpsInfo = gpsInfo;
	}

	public Handler getDistanceOrderHandler() {
		return distanceOrderHandler;
	}

	public void setDistanceOrderHandler(Handler distanceOrderHandler) {
		this.distanceOrderHandler = distanceOrderHandler;
	}

	public void setTimeLineOrder(int order){
		timeLineOrder = order;
	}

	public void resetAll() {
		timeLine.clear();
		myPostsList.clear();
		myLikeList.clear();

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

	public void findPass(String id){
		if(!waiting) {
			startTime = System.currentTimeMillis();

			waiting = true;
			findPass = true;

			message = "#findPass%";
			message += id;

			client.sendToServer(message);

			message = "";
		}
	}

	public void refresh(){
		if(!waiting) {
			startTime = System.currentTimeMillis();

			waiting = true;
			refresh = true;

			message = "#refresh%";

			switch(timeLineOrder){
				case Constants.TIME: message += "time";
					break;
				case Constants.DISTANCE: message += "distance";
					for(Integer locationContentId : locationContentIdList)
						message += "%" + locationContentId;
					break;
				case Constants.LIKE: message += "like";
					break;
			}

			client.sendToServer(message);

			message = "";
		}
	}

	public void morePosts(){
		if(!waiting) {
			startTime = System.currentTimeMillis();

			waiting = true;
			morePosts = true;

			message = "#morePosts%";

			switch(timeLineOrder){
				case Constants.TIME: message += "time"; break;
				case Constants.DISTANCE: message += "distance"; break;
				case Constants.LIKE: message += "like"; break;
			}

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

	public void setHandler(Handler handler) {
		if(this.handler == null) {
			this.handler = handler;
		}
	}

	public void setHandlerNull(){
		this.handler = null;
	}

    boolean isWaiting() {
		return waiting;
	}

	 void setWaiting(boolean waiting) {
		this.waiting = waiting;
	}

	 boolean isLogin() {
		return login;
	}

	 boolean isRegister() {
		return register;
	}

	public boolean isFindPass() {
		return findPass;
	}

	boolean isRefresh() {
		return refresh;
	}

	 boolean isMorePosts() {
		return morePosts;
	}

	 boolean isMyLike() {
		return myLike;
	}

	public boolean isMyPost() {
		return myPosts;
	}

	 boolean isMoreMyLike() {
		return moreMyLike;
	}

	 boolean isPost() {
		return post;
	}

	 boolean isDelete() {
		return delete;
	}

	 boolean isLike() {
		return like;
	}

	 boolean isDislike() {
		return dislike;
	}

	 boolean isUpdateUser() {
		return updateUser;
	}

	 void setStartTime(long time) { this.startTime = time; }

	 long getStartTime() {
		return startTime;
	}

	 void setLogin(boolean login) {
		this.login = login;
	}

	 void setRegister(boolean register) {
		this.register = register;
	}

	public void setFindPass(boolean findPass) {
		this.findPass = findPass;
	}
	 void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}

	 void setMorePosts(boolean morePosts) {
		this.morePosts = morePosts;
	}

	 void setMyLike(boolean myLike) {
		this.myLike = myLike;
	}

	 void setMoreMyLike(boolean moreLike) {
		this.moreMyLike = moreLike;
	}

	 void setPost(boolean post) {
		this.post = post;
	}

	 void setDelete(boolean delete) {
		this.delete = delete;
	}

	 void setLike(boolean like) {
		this.like = like;
	}

	 void setDislike(boolean dislike) {
		this.dislike = dislike;
	}

	 void setUpdateUser(boolean updateUser) {
		this.updateUser = updateUser;
	}

	 boolean isMyPosts() {
		return myPosts;
	}

	 void setMyPosts(boolean myPosts) {
		this.myPosts = myPosts;
	}

	 boolean isMoreMyPosts() {
		return moreMyPosts;
	}

	 void setMoreMyPosts(boolean moreMyPosts) {
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

	public ArrayList<Posts> getMoreList(){ return moreList; }

	/*
       socket connection method
     */
	public void terminateConnection(){
		closeSocket = true;
	}

}