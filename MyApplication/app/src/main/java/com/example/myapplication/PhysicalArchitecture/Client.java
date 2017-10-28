package com.example.myapplication.PhysicalArchitecture;

import android.system.ErrnoException;
import android.util.Log;

import com.example.myapplication.Foundation.PostsList;
import com.example.myapplication.ProblemDomain.Constants;
import com.example.myapplication.ProblemDomain.Posts;
import com.example.myapplication.ProblemDomain.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @author jm
 * 서버-클라이언트 연결 클래스
 */
public class Client extends Thread implements Serializable
{
	private Socket sock;
	private clientWrite clientW;
	private clientRead clientR;
	private ClientController cControl;

	private static final String host = "222.104.203.106";
	private static final int port = 11114;

	private static Client client = null;

	public void run(){
		cControl = cControl.getClientControl();

		sock = new Socket();
		SocketAddress addr = new InetSocketAddress(host, port);
		try {
			sock.connect(addr, 1000);
		} catch (IOException e) {
			Log.d("CLIENT", "connect failed");
			e.printStackTrace();
		}

		Log.d("CLIENT", "connect complete");

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

	 void sendToServer(Object obj)
	{
		if(obj instanceof String){
			if(clientW != null) {
				clientW.sendToServer((String) obj);
				Log.d("CLIENT", "send message : " + obj);
			}
			else
				Log.d("CLIENT", "clientW : NULL");
		}
		else if(obj instanceof Posts) {
			if(clientW != null) {
				clientW.sendToServerPosts((Posts) obj);
				Log.d("CLIENT", "[clientW:sendToServer:Posts] send message : " + obj.toString());
			}
			else
				Log.d("CLIENT", "[clientW:sendToServer:Posts] NULL");
		}
	}

	public ClientController getcControl() {
		return cControl;
	}
	public void setcControl(ClientController cControl) {
		this.cControl = cControl;
	}
}

class clientRead extends Thread implements Serializable
{
	private Socket socket;
	private ClientController cControl;

	 clientRead(Socket socket,ClientController cControl)
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

					Log.d("CLIENT", "waiting is true");

					if(cControl.getStartTime() - System.currentTimeMillis() > 5000){
						Log.d("CLIENT", "time over!!!!");
						cControl.setWaiting(false);
						cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_FAILED);
					}

					temp = clientInputStream.readObject();

					/*
						if reading data from socket is string send message to mainthread handler.
						current setting is #fin and #err but we must add err code for each reason
					 */
					if (temp instanceof String) {
						if(cControl.isLogin()){
							Log.d("CLIENT", "login");
							cControl.setLogin(false);

							if(((String)temp).compareTo("#fin")==0){
								cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
							}
							else if(((String)temp).compareTo("#err")==0) {
								cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_FAILED);
							}
						}
						else if(cControl.isRegister()){
							Log.d("CLIENT", "register");
							cControl.setRegister(false);

							if(((String)temp).compareTo("#fin")==0){
								cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
							}
							else if(((String)temp).compareTo("#err")==0) {
								cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_FAILED);
							}
						}
						else if(cControl.isPost()){
							Log.d("CLIENT", "post");
							cControl.setPost(false);

							if(((String)temp).compareTo("#fin")==0){
								cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
							}
							else if(((String)temp).compareTo("#err")==0) {
								cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_FAILED);
							}
						}
						else if(cControl.isDelete()){
							Log.d("CLIENT", "delete");
							cControl.setDelete(false);

							if(((String)temp).compareTo("#fin")==0){
								cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
							}
							else if(((String)temp).compareTo("#err")==0) {
								cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_FAILED);
							}
						}
						else if(cControl.isLike()){
							Log.d("CLIENT", "like");
							cControl.setLike(false);

							if(((String)temp).compareTo("#fin")==0){
								cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
							}
							else if(((String)temp).compareTo("#err")==0) {
								cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_FAILED);
							}
						}
						else if(cControl.isDislike()){
							Log.d("CLIENT", "disLike");
							cControl.setLike(false);

							if(((String)temp).compareTo("#fin")==0){
								cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
							}
							else if(((String)temp).compareTo("#err")==0) {
								cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_FAILED);
							}
						}
						Log.d("CLIENT", temp.toString());
						cControl.setWaiting(false);
					}
					/*
						if reading data from socket is User
					 */
					else if (temp instanceof User) {
						if(cControl.isLogin()){
							Log.d("CLIENT", "login");
							cControl.setLogin(false);
							cControl.setMe((User)temp);
							cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
						}
						else if(cControl.isRegister()){
							Log.d("CLIENT", "register");
							cControl.setRegister(false);
							cControl.setMe((User)temp);
							cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
						}
						else if(cControl.isUpdateUser()){
							Log.d("CLIENT", "updateUser");
							cControl.setLike(false);
							cControl.setMe((User)temp);
							cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
						}
						Log.d("CLIENT", temp.toString());
						cControl.setWaiting(false);
					}
					/*
						if reading data from socket is PostsList
					 */
					else if (temp instanceof PostsList) {
						if(cControl.isMorePosts()){
							Log.d("CLIENT", "morePosts");
							cControl.setMorePosts(false);
							cControl.addTimeLine((PostsList)temp);
							cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
						}
						else if(cControl.isRefresh()){
							Log.d("CLIENT", "refresh");
							cControl.setRefresh(false);
							cControl.setTimeLine((PostsList)temp);
							cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
						}
						else if(cControl.isMyPosts()){
							Log.d("CLIENT", "myPosts");
							cControl.setMyPosts(false);
							cControl.setMyPostsList((PostsList)temp);
							cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
						}
						else if(cControl.isMoreMyPosts()){
							Log.d("CLIENT", "moreMyPosts");
							cControl.setMoreMyPosts(false);
							cControl.addMyPostsList((PostsList)temp);
							cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
						}
						else if(cControl.isMyLike()){
							Log.d("CLIENT", "myLike");
							cControl.setMyLike(false);
							cControl.setMyLikeList((PostsList)temp);
							cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
						}
						else if(cControl.isMoreMyLike()){
							Log.d("CLIENT", "moreMyLike");
							cControl.setMoreMyLike(false);
							cControl.addMyLikeList((PostsList)temp);
							cControl.getHandler().sendEmptyMessage(Constants.RECEIVE_SUCCESSS);
						}
						Log.d("CLIENT", temp.toString());
						cControl.setWaiting(false);
					}
					else if(temp == null)
						Log.d("CLIENT", "temp is null");
					else
						Log.d("CLIENT", "temp is not expected");
				}
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(ClassNotFoundException fe){
			fe.printStackTrace();
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

	protected clientWrite(Socket socket)
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

				while (sendToReadyString) {
					Log.d("CLIENT", "in thread before writing");
					out.writeObject(console);
					Log.d("CLIENT", "write complete");
					sendToReadyString = false;
				}
				while (sendToReadyPosts) {
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

	 void sendToServer(String msg)
	{
		console=msg;
		sendToReadyString=true;
	}
	   void sendToServerPosts(Posts posts)
	{
		postsConsole=posts;
		sendToReadyPosts=true;
	}
}