package com.gexy.api.scheduler;

import java.util.ArrayList;

import com.gexy.api.ApiObject;
import com.gexy.auth.Auth;

public class Test extends ApiObject {
	public Test(){
		super("com.gexy.api.scheduler.Test",new ArrayList<String>());
	}
	public void run()
	{
		System.out.println(apiName +" argomenti: "+apiArgs.toString());
		setResponce("OK");
	}



}
