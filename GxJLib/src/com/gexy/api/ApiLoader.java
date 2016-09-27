package com.gexy.api;

import java.util.ArrayList;
import java.util.List;


import com.gexy.api.exception.ApiLoaderApiNotFoundException;
import com.gexy.auth.Auth;
import com.gexy.logger.Logger;

public class ApiLoader {
	public List<ApiObject> apis =new ArrayList<ApiObject>();
	Logger log;
	
	public ApiLoader(){
		log=new Logger();
	}
	
	public ApiLoader(Logger _log){
		log=_log;
	}
	
	
	public void add(ApiObject _apiObject){
		apis.add(_apiObject);
	}
	
	public void remove(int _index){
		apis.remove(_index);
	}
	
	public void remove(String _name) throws ApiLoaderApiNotFoundException{
		for(int index=0;index<apis.size();index++){
			if(apis.get(index).apiName.equals(_name)){
				apis.remove(index);
				return;
			}
		}
		throw new ApiLoaderApiNotFoundException();
	}
	
	public void get(int _index){
		apis.get(_index);
	}
	
	
	public ApiObject get(String _name) throws ApiLoaderApiNotFoundException{
		for(int index=0;index<apis.size();index++){
			if(apis.get(index).apiName.equals(_name)){
				return apis.get(index);
			}
		}
		System.out.println("API not finded: "+_name);
		throw new ApiLoaderApiNotFoundException();
	}
	
	
	/**
	 * Questo metodo esegue le istruzioni di una API,
	 * ritorna l'oggetto ApiObject che è stato eseguito
	 * @param _apiName String Nome della api
	 * @return ApiObject
	 * @throws ApiLoaderApiNotFoundException
	 */
	public ApiObject call(String _apiName,Auth _authReference) throws ApiLoaderApiNotFoundException{
		ApiObject ao = get(_apiName);
		ao.setAuth(_authReference);
		ao.run();
		return ao;
	}
	
	/**
	 * Questo metodo esegue le istruzioni di una API,
	 * ritorna l'oggetto ApiObject che è stato eseguito
	 * @param _apiName String Nome della api
	 * @param _args ArrayList<String>
	 * @return ApiObject
	 * @throws ApiLoaderApiNotFoundException
	 */
	public ApiObject call(String _apiName, List<String> _args,Auth _authReference) throws ApiLoaderApiNotFoundException{
		ApiObject ao = get(_apiName);
		ao.setAuth(_authReference);
		ao.setArgs(_args);
		ao.run();
		ao.resetArgs();
		return ao;
	}
	
	
}