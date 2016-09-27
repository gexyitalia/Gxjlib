package com.gexy.api;

import java.util.ArrayList;
import java.util.List;
import com.gexy.auth.Auth;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Questa classe definisce una API
 * 
 * Va implementato il metodo publi void run() che è la parte che eseguirà
 * le operazioni della api
 * @version 1.0.0
 */

public class ApiObject implements Runnable{
	public String apiName;
	public List<String> apiArgs =new ArrayList<String>();
	
	protected String responce;
	protected boolean errorFlag=false;
	
	protected String help;
	
	protected Auth auth;
	
	public ApiObject(){
		super();
	}
	
	/**
	 * Struttura di una API di Gexy.
	 * L'argomento è una stringa con una struttura JSON
	 * di questo tipo
	 * { "api":{
	 * 		"name":"Nome della api",
	 * 		"args":[
	 * 				"argomento1",
	 * 				"argomento2"
	 * 			   ]
	 * 		}
	 * }
	 * @param _command String
	 */
	public ApiObject(String _command){
		super();
		this.JSONDecoder(_command);
		
	}
	
	/**
	 * Struttura di una API di Gexy
	 * Gli argomenti sono il nome della API grazie al quale può essere richiamata
	 * questa, e gli argomenti(i dati della api)
	 * @param _name String
	 * @param _args ArrayList<String>
	 */
	public ApiObject(String _name, ArrayList<String> _args){
		super();
		this.apiName = _name;
		this.apiArgs = _args;
	}
	
	/**
	 * Setta il riferimento ad una istanza della classe
	 * com.gexy.auth.Auth che ha eseguito l'utenticazione
	 * tramite questa istanza vengono letti i dati dell'utente
	 * altrimenti la API non saprebbe su che dati eseguire le 
	 * operazioni. Nello specifico dall'istanza viene presa la
	 * connessione al database con dell'organizzazione di
	 * cui fa parte l'utente.
	 * @param _authReference com.gexy.auth.Auth
	 */
	public void setAuth(Auth _authReference){
		auth=_authReference;
	}
	
	/**
	 * Verifica se è stata settata l'istanza della classe Auth per questa
	 * API. Questo controllo non viene fatto in automatico, se necessario
	 * ,questo metodo va richiamato nel metodo run()
	 * @see setAuth()
	 * @return boolean
	 */
	public boolean isSetAuth(){
		if(auth==null){return false;}else{return true;}
	}
	
	/**
	 * Fa il parsing di un comando API da una stringa in JSON
	 * 
	 * L'argomento command è una struttura JSON che deve essere tipo questa
	 * 
	 * { "api":{
	 * 		"name":"Nome della api da chiamare",
	 * 		"args":[
	 * 				"argomento1",
	 * 				"argomento2"
	 * 			   ]
	 * 		}
	 * }
	 * 
	 * @param _command String Struttura JSON
	 */
	protected void JSONDecoder(String _command){
		JSONObject obj = new JSONObject(_command);
		this.apiName = obj.getJSONObject("api").getString("name");

		
		this.setArgs(_command);
	}

	
	/**
	 * Setta il testo di aiuto che descrive la API
	 * @param _text String
	 */
	protected void setHelp(String _text){
		help=_text;
	}
	
	/**
	 * Restituisce il testo di aiuto che descrive la API
	 * Il testo viene formattato in JSON e la sintassi è di questo tipo
	 * 
	 * {api:
	 * 		{
	 * 			name="nome della api",
	 * 			version:"versione della api",
	 * 			description:"testo di help"
	 * 		}
	 * }
	 * @return String
	 */
	public String getHelp(){
		String out = "{api:{name:\""+apiName+",versionMajor:\"\",versionMinor:\"\",description:\""+help+"\"}}";
		return out;
	}
	
	/**
	 * Verifica se cè stato un errore, gli errori hanno valori interi da 0 in poi
	 * @return boolean
	 */
	public boolean hasError(){
		if(errorFlag){return true;}else{return false;}
	}
	
	/**
	 * Ritorna il testo in restituzione dall'esecuzione
	 * @return
	 */
	public String getResponce(){
		return responce;
	}
	
	/**
	 * Setta il testo da restituire
	 * @param _responce
	 */
	protected void setResponce(String _responce){
		responce=_responce;
	}
	
	/**
	 * Resetta la lista argomenti per la API
	 * 
	 * @see setArgs()
	 * @see ApiLoader(String _command)
	 */
	public void resetArgs(){
		apiArgs.clear();
	}
	
	/**
	 * Setta gli argomenti da passare alla API durante l'esecuzione
	 * @param _args ArrayList<String>
	 */
	public void setArgs(List<String> _args){
		apiArgs=_args;
	}
	
	/**
	 * Setta gli argomenti da passare alla API durante l'esecuzione
	 * 
	 * @param _args String
	 * 
	 * I dati devono essere passati in formato JSON
	 * questo metodo leggera l'array JSON "args"
	 * { "api":{
	 * 		"name":"Nome della api da chiamare",
	 * 		"args":[
	 * 				"argomento1",
	 * 				"argomento2"
	 * 			   ]
	 * 		}
	 * }
	 */
	public void setArgs(String _args){
		JSONObject obj = new JSONObject(_args);
		JSONArray apiArgs = obj.getJSONObject("api").getJSONArray("args");
		for (int i = 0; i < apiArgs.length(); i++)
		{
			this.apiArgs.add(apiArgs.getString(i));
		}
	}

	@Override
	public void run() {
		
	}

	public String getName() {
		return apiName;
	}
}
