package com.gexy.config;
/*
 * struttura record configurazione
 */
public class Conf {


	public Conf(String _name, String _value){
		recordName = _name;
		recordValue = _value;
		setSaveable(true);
		setConstant(false);
		setAsDefault(false);
	}
	public String recordName; //nome record
	public String recordValue; //valore record
	public boolean encrypetd; //se criptato o no
	public boolean enableSave; //abilita/disabilita il salvataggio
	public boolean enableConstant; //rende questo elemento non modificabile
	public boolean enableAsDefault; //rende il parametro come valore di default

	/**
	 * converte il recordValue in int
	 */
	public int toInt(){
		return Integer.parseInt(recordValue);
	}
	/**
	 * converte il recordValue in long
	 */
	public long toLong(){
		return Long.parseLong(recordValue);
	}
	/**
	 * converte il recordValue in Boolean
	 */
	public boolean toBoolean(){
		return Boolean.parseBoolean(recordValue);
	}
	


	/**
	 * serve per verificare se la classe ritornata dal metodo get()
	 * � vuota. E' true se sia recordName che recordValue sono vuoti
	 */
	public boolean empty(){
		if(recordName==null && recordValue==null){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Setta il parametro come salvabile o no quando viene chiamato un metodo 
	 * della classe com.gexy.config.Config per il salvataggio/esportazione 
	 * della configurazine su altri supporti(ad esempio il salvataggio della 
	 * configurazione su file XML). I parametri che sono hanno questa
	 * proprietà settata su false non vengono salvati. Per default è true.
	 * 
	 * @param _val boolean
	 */
	public void setSaveable(boolean _val){
		enableSave=_val;
	}
	
	/**
	 * Verifica se il parametro è salvabile
	 * @see setSaveable();
	 * @return boolean
	 */
	public boolean isSaveable(){
		return enableSave;
	}
	
	/**
	 * Setta questo elemento come una costante, una volta settato non potrà
	 * essere modificato(ne valore, ne nome)
	 * @param _val boolean
	 */
	public void setConstant(boolean _val){
		enableConstant=_val;
	}
	
	/**
	 * Verifica se il parametro è una costante
	 * @see setConstant();
	 * @return boolean
	 */
	public boolean isConstant(){
		return enableConstant;
	}
	
	/**
	 * Marca il parametro come un valore di default
	 * @param _val boolean
	 */
	public void setAsDefault(boolean _val){
		enableAsDefault=_val;
	}
	
	/**
	 * Verifica se il parametro e marcato come avente 
	 * un valore di default
	 * @see setAsDefault();
	 * @return boolean
	 */
	public boolean isDefault(){
		return enableAsDefault;
	}
	
	/**
	 * Setta il valore di questo elemento
	 * @param _value String
	 * @return boolean
	 */
	public boolean setValue(String _value){
		//se c'è un valore memorizzato edè una costante
		if(enableConstant && recordValue!=null){
			return false;
		}
		//se non c'era nessun valore memorizzato
		else if(recordValue!=null){
			enableAsDefault=false;
			recordValue=_value;
			return true;
		}
		//se non è una costante ma c'è un valore memorizzato
		else{
			recordValue=_value;
			return true;
		}
	}
	
	/**
	 * restituisce il valore del parametro
	 * 
	 * @return
	 */
	public String getValue(){
		return recordValue;
	}

}
