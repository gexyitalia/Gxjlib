package com.gexy.api.drive;

import java.util.ArrayList;

import com.gexy.api.ApiObject;
import com.gexy.api.ApiResponces;

public class Test extends ApiObject {

	public Test(){
		super("com.gexy.api.drive.Test",new ArrayList<String>());

	}
	public void run()
	{
		System.out.println(apiName +" argomenti: "+apiArgs.toString());
		setResponce(ApiResponces.OK);
	}



}
