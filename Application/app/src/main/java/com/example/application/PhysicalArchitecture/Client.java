package com.example.application.PhysicalArchitecture;

import android.util.Log;

import com.example.application.Foundation.PostsList;
import com.example.application.ProblemDomain.Constants;
import com.example.application.ProblemDomain.Posts;
import com.example.application.ProblemDomain.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class Client extends Thread implements Serializable
{
	Socket sock;
	public clientWrite clientW;
	public clientRead clientR;
	private ClientControl cControl;

	private static final String host = "222.104.203.106";
	private static final int port = 11114;

	private static Client client = null;

	public void run(){
		try {
			cControl = cControl.getClientControl();

			sock = new Socket(host, port);

		} catch (IOException e) {
			Log.d("TEST1", "connect failed");
			e.printStackTrace();
		}

		Log.d("TEST1", "connect complete");

		clientW=new clientWrite(sock);
		clientR=new clientRead(sock,cControl);

		clientW.start();
		clientR.start();
	}

	static public Client getClient(){
		if(client != null)
			return client;
		else
			return new Client();
	}

	public void sendToServer(Object obj)
	{
		if(obj instanceof String){
			if(clientW != null) {
				clientW.sendToServer((String) obj);
				Log.d("TEST1", "send message : " + (String)obj);
			}
			else
				Log.d("TEST1", "clientW : NULL");
		}
		else if(obj instanceof Posts) {
			if(clientW != null) {
				clientW.sendToServerPosts((Posts) obj);
				Log.d("TEST1", "[clientW:sendToServer:Posts] send message : " + ((Posts) obj).toString());
			}
			else
				Log.d("TEST1", "[clientW:sendToServer:Posts] NULL");
		}
	}

	public ClientControl getcControl() {
		return cControl;
	}
	public void setcControl(ClientControl cControl) {
		this.cControl = cControl;
	}
}

class clientRead extends Thread implements Serializable
{
	Socket socket;
	private ClientControl cControl;

	public clientRead(Socket socket,ClientControl cControl)
	{
		this.socket=socket;
		this.cControl=cControl;
	}

	@Override
	public void run(){
		try {
			ObjectInputStream clientInputStream = new ObjectInputStream(socket.getInputStream());
			Object temp;

			while(true) {
				try {
					Thread.sleep(10);
					cControl.setStartTime(System.currentTimeMillis());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				/*
					when client sends message to server,
					client reads data from socket and handles the func
				 */
				while(cControl.isWaiting()) {

					// time over --- break roop
					if(System.currentTimeMillis() - cControl.getStartTime() > 5000)
						cControl.setWaiting(false);

					Log.d("TEST1", "waiting is true");

					/*
					if(cControl.getStartTime() - System.currentTimeMillis() > 5000){
						cControl.setWaiting(false);
					}
					*/

					temp = clientInputStream.readObject();

					/*
						if reading data from socket is string send message to mainthread handler.
						current setting is #fin and #err but we must add err code for each reason
					 */
					if (temp instanceof String) {
						if(cControl.isLogin()){
							Log.d("TEST1", "login");
							cControl.setLogin(false);

							if(((String)temp).compareTo("#fin")==0){
								cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
							}
							else if(((String)temp).compareTo("#err")==0) {
								cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_FAILED);
							}
						}
						else if(cControl.isRegister()){
							Log.d("TEST1", "register");
							cControl.setRegister(false);

							if(((String)temp).compareTo("#fin")==0){
								cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
							}
							else if(((String)temp).compareTo("#err")==0) {
								cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_FAILED);
							}
						}
						else if(cControl.isPost()){
							Log.d("TEST1", "post");
							cControl.setPost(false);

							if(((String)temp).compareTo("#fin")==0){
								cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
							}
							else if(((String)temp).compareTo("#err")==0) {
								cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_FAILED);
							}
						}
						else if(cControl.isDelete()){
							Log.d("TEST1", "delete");
							cControl.setDelete(false);

							if(((String)temp).compareTo("#fin")==0){
								cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
							}
							else if(((String)temp).compareTo("#err")==0) {
								cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_FAILED);
							}
						}
						else if(cControl.isLike()){
							Log.d("TEST1", "like");
							cControl.setLike(false);

							if(((String)temp).compareTo("#fin")==0){
								cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
							}
							else if(((String)temp).compareTo("#err")==0) {
								cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_FAILED);
							}
						}
						else if(cControl.isDislike()){
							Log.d("TEST1", "disLike");
							cControl.setLike(false);

							if(((String)temp).compareTo("#fin")==0){
								cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
							}
							else if(((String)temp).compareTo("#err")==0) {
								cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_FAILED);
							}
						}
						Log.d("TEST1", ((String)temp.toString()));
						cControl.setWaiting(false);
					}
					/*
						if reading data from socket is User
					 */
					else if (temp instanceof User) {
						if(cControl.isLogin()){
							Log.d("TEST1", "login");
							cControl.setLogin(false);
							cControl.setMe((User)temp);
							cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
						}
						else if(cControl.isRegister()){
							Log.d("TEST1", "register");
							cControl.setRegister(false);
							cControl.setMe((User)temp);
							cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
						}
						else if(cControl.isUpdateUser()){
							Log.d("TEST1", "updateUser");
							cControl.setLike(false);
							cControl.setMe((User)temp);
							cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
						}
						Log.d("TEST1", ((User)temp).toString());
						cControl.setWaiting(false);
					}
					/*
						if reading data from socket is PostsList
					 */
					else if (temp instanceof PostsList) {
						if(cControl.isMorePosts()){
							Log.d("TEST1", "morePosts");
							cControl.setMorePosts(false);
							cControl.addTimeLine((PostsList)temp);
							cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
						}
						else if(cControl.isRefresh()){
							Log.d("TEST1", "refresh");
							cControl.setRefresh(false);
							cControl.setTimeLine((PostsList)temp);
							cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
						}
						else if(cControl.isMyPosts()){
							Log.d("TEST1", "myPosts");
							cControl.setMyPosts(false);
							cControl.setMyPostsList((PostsList)temp);
							cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
						}
						else if(cControl.isMoreMyPosts()){
							Log.d("TEST1", "moreMyPosts");
							cControl.setMoreMyPosts(false);
							cControl.addMyPostsList((PostsList)temp);
							cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
						}
						else if(cControl.isMyLike()){
							Log.d("TEST1", "myLike");
							cControl.setMyLike(false);
							cControl.setMyLikeList((PostsList)temp);
							cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
						}
						else if(cControl.isMoreMyLike()){
							Log.d("TEST1", "moreMyLike");
							cControl.setMoreMyLike(false);
							cControl.addMyLikeList((PostsList)temp);
							cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
						}
						Log.d("TEST1", ((PostsList)temp).toString());
						cControl.setWaiting(false);
					}
					else if(temp == null)
						Log.d("TEST1", "temp is null");
					else
						Log.d("TEST1", "temp is not expected");
				}
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

class clientWrite extends Thread implements Serializable
{
	private Socket socket;
	private String console;
	private Posts postsConsole;

	private boolean sendToReadyString;
	private boolean sendToReadyPosts;

	public clientWrite(Socket socket)
	{
		this.socket=socket;
		sendToReadyString=false;
		sendToReadyPosts=false;
	}

	@Override
	public void run() {
		ObjectOutputStream out;

		try {
			out = new ObjectOutputStream(socket.getOutputStream());

			while(true) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				while (sendToReadyString == true) {
					Log.d("TEST1", "in thread before writing");
					out.writeObject(console);
					Log.d("TEST1", "write complete");
					sendToReadyString = false;
				}
				while (sendToReadyPosts == true) {
					out.writeObject(postsConsole);
					sendToReadyPosts = false;
				}
			}
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendToServer(String msg)
	{
		console=msg;
		sendToReadyString=true;
	}
	public void sendToServerPosts(Posts posts)
	{
		postsConsole=posts;
		sendToReadyPosts=true;
	}
}