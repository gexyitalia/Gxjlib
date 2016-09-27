package com.gexy.api;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import com.gexy.auth.AuthToken;
import com.gexy.auth.excepion.AuthLoginFailedException;
import com.gexy.logger.Logger;

public class ApiClient {
	Logger log;
	String apiServerHost;
	int apiServerPort;

	static Socket socket;
	DataOutputStream outStream;
	BufferedReader inStream;

	AuthToken authToken;

	public ApiClient(String _host, int _port) throws UnknownHostException, IOException{
//		log=_log;
		apiServerHost=_host;
		apiServerPort=_port;

		open();

	}

	public void open() throws UnknownHostException, IOException{
			socket = new Socket(apiServerHost, apiServerPort);
			outStream = new DataOutputStream(socket.getOutputStream());
			inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	public void close() throws IOException {
		if(!socket.isClosed()){
			send("{cmd:{name:\"exit\"}}");
			socket.close();
		}

	}
	
	public void callAPI(String _apiName) {
		try {
			send("{auth:{tid:\""+authToken.id+"\",uid:\""+authToken.userId+"\"},api:{name:\""+_apiName+"\",args:[]}}");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void callAPI(String _apiName,String _arg) {
		try {
			send("{auth:{tid:\""+authToken.id+"\",uid:\""+authToken.userId+"\"},api:{name:\""+_apiName+"\",args:[\""+_arg+"\"]}}");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
//	public void callAPI(String _apiName,List<String> _args) {
//		try {
//			send("{auth:{tid:\""+authToken.id+"\",uid:\""+authToken.userId+"\"},api:{name:\""+_apiName+"\",args:[\""+_arg+"\"]}}");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	protected void send(String _data) throws IOException{
		outStream.writeBytes(_data+ '\n');
	}

	protected String recive() throws IOException{
		return inStream.readLine();
	}

	public void login(String _username,String _password) throws IOException, AuthLoginFailedException{
		send("{login:{username:\""+_username+"\",password:\""+_password+"\"}}");
		String in=recive();
		if(in.equals(ApiResponces.ERROR_AUTH_FAILED)){
			throw new AuthLoginFailedException();
		}else{
			JSONObject obj = new JSONObject(in.trim());
			Iterator<?> keys = obj.keys();
			while( keys.hasNext() ) {
				String key = (String)keys.next();
				if ( obj.get(key) instanceof JSONObject ) {
					//se è un API
					if(key.equals("auth")){
						authToken=new AuthToken();
						String tid = obj.getJSONObject("auth").getString("tid");
						authToken.id=tid;

						int uid = obj.getJSONObject("auth").getInt("uid");
						authToken.userId=uid;
					}
				}
			}
		}
	}


}

