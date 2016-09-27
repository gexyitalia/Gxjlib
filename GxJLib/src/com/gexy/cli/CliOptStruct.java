/*
 * Zerounozero Lab - Java Library
 * Library Version: 1.0.0
 * File version:	1.0.0
 * License:			Zerounozero Lab 2015 - all right reserved
 * 
 * Descrizione:
 * 					Questa classe implementa la struttura delle opzioni per un gestore 
 * 					di opzioni a riga di comando, guardare CLIOptionsParser.java
 */



package com.gexy.cli;

public class CliOptStruct{
	private String argLong; //argomento lungo
	private char argShort; //argomento singola lettera
	private String argVal; //valore dell'argomento
	private String description; //descrizione per l'help
	private boolean argReq; //argomento non facoltativo
	private boolean valReq; //richiesti dati per l'argomento

	private boolean passed; //argomento passato via CLI

	/**
	 * 
	 * @param _argShort
	 * @param _argLong
	 * @param _description
	 * @param _argReq
	 * @param _valReq
	 */
	public CliOptStruct(char _argShort, String _argLong, String _description, boolean _argReq, boolean _valReq){
		argLong = _argLong;
		argShort = _argShort;
		argVal=null;
		description = _description;
		argReq = _argReq;
		valReq = _valReq;

		passed = false;
	}
	
	/**
	 * 
	 * @param _argShort
	 * @param _argLong
	 * @param _argValDfl
	 * @param _description
	 * @param _argReq
	 * @param _valReq
	 */
	public CliOptStruct(char _argShort, String _argLong, String _argValDfl, String _description, boolean _argReq, boolean _valReq){
		argLong = _argLong;
		argShort = _argShort;
		argVal=_argValDfl;
		description = _description;
		argReq = _argReq;
		valReq = _valReq;

		passed = false;
	}

	/**
	 * Verify if short argument was specified 
	 * 
	 * @return boolean
	 */
	public boolean hasShortArgument(){if(this.argShort==' '){return false;}else{return true;}}
	/**
	 * Verify if long argument was specified 
	 * 
	 * @return boolean
	 */
	public boolean hasLongArgument(){if(this.argLong==" "){return false;}else{return true;}}

	/**
	 * Ritorna il valore dell'argomento, se non è settato è null o il valore di default
	 * 
	 * @return
	 */
	public String getValue(){
		return argVal;
	}

	/**
	 * Setta il valore dell'argomento
	 */
	public void setValue(String _val){
		argVal=_val;
	}
	
	/**
	 * ritorna l'argomento corto
	 * 
	 * @return
	 */
	public char getArgument(){
		return argShort;
	}
	
	/**
	 * ritorna l'argomento lungo
	 * 
	 * @return
	 */
	public String getLongArgument(){
		return argLong;
	}
	
	/**
	 * Return description string
	 * @return String
	 */
	public String getDescription(){
		return description;
	}
	
	/**
	 * Check if the argument is required
	 * 
	 * @return True if argument is required or false is not
	 */
	public boolean isRequired(){
		if(argReq){return true;}else{return false;}
	}
	
	/**
	 * Check if the argument value is required
	 * 
	 * @return True if argument value is required or false is not
	 */
	public boolean isRequiredValue(){
		if(valReq){return true;}else{return false;}
	}
	
	/**
	 * setta che 'argomento è stato passato da CLI
	 */
	public void setPassed(){
		passed=true;
	}
	
	/**
	 * setta che l'argomento è stato passato o no da CLI
	 * 
	 * @param	_value	boolean
	 */
	public void setPassed(boolean _value){
		passed=_value;
	}
	
	/**
	 * controlla se l'argomento è stato passato una o più volte dalla command line
	 * 
	 * @return
	 */
	public boolean isPassed(){
		return passed;
	}
}