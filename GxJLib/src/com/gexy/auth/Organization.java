package com.gexy.auth;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gexy.auth.excepion.OrganizationRegisterUserException;
import com.gexy.config.exception.ConfigDBNameException;
import com.gexy.logger.Logger;
import com.gexy.mysql.Mysql;
import com.gexy.mysql.exception.MysqlTryOpenWithoutData;


public class Organization {
	Logger log;
	static String name;
	static int id;
	static String domain; //dominio web dell'organizzazione
	
	public Mysql db; //connessione db organizzazione
	
	List<User> users = new ArrayList<User>();
	
	/**
	 * Classe di un organizzazione
	 * Serve da contenitore dei dati di un organizzazione
	 * @param _id
	 * @param _name
	 */
	public Organization(
			int _id, 
			String _name,
			String _domain)
	{
		id=_id;
		name=_name;
		domain=_domain;
	}
	
	/**
	 * Classe di un organizzazione
	 * Serve da contenitore dei dati di un organizzazione
	 * @param _log Logger Istanza classe com.gexy.logger.Logger
	 * @param _id Integer Id dell'organizzazione
	 * @param _name String Nome dell'organizzazione
	 * @param _domain String Dominio dell'organizzazione
	 * @param _dbHost String Hostname del server sql con i database dell'organizzaione
	 * @param _dbName String Nome del database da selezionare per l'organizzazione
	 * @param _dbUsername String Username per il database _dbName
	 * @param _dbPassword String Password per il database _dbName
	 */
	
	public Organization(
			Logger _log,
			int _id, 
			String _name,
			String _domain,
			String _dbHost,
			String _dbName,
			String _dbUsername,
			String _dbPassword)
	{
		log=_log;
		id=_id;
		name=_name;
		domain=_domain;
		db=new Mysql(_log,_dbHost,_dbName,_dbUsername,_dbPassword);
	}
	
	
	public String getName(){return Organization.name;}
	public int getId(){return Organization.id;}
	public String getDomain(){return Organization.domain;}
	public Mysql getDatabase(){
		return this.db;
	}
	
	/**
	 * Recupera e setta nella classe Organization un utente dal db
	 * 
	 * @param _id Integer L'id dell'utente
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws ConfigDBNameException
	 * @throws MysqlTryOpenWithoutData
	 * @throws OrganizationRegisterUserException
	 */
	public User registerUser(int _id) throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException, ConfigDBNameException, MysqlTryOpenWithoutData, OrganizationRegisterUserException{
		if(userExistById(_id)){
			throw new OrganizationRegisterUserException("Register user failed: user "+_id+" is just register in this organization "+name);
		}else{
			User user = new User(_id,this);
			users.add(user);
			return user;
		}
	}
	
	/**
	 * Rimuove un utente settato dal db
	 * 
	 * @param _id Integer L'ID dell'utente
	 * @param _authTokenId L'ID del token di autenticazione
	 * @throws OrganizationRegisterUserException 
	 */
	public void unregisterUser(int _id, String _authTokenId) throws OrganizationRegisterUserException {
		if(userExistById(_id)){
			for(int a=0;a<users.size();a++){
				if(users.get(a).getId()==_id){users.remove(a);}
			}
		}else{
			throw new OrganizationRegisterUserException("Unregister user failed: user "+_id+" is not register in this organization "+name);
		}
		
	}
	
	/**
	 * Verifica se un utente esiste dal suo id
	 * @param _id Integer
	 * @return boolean
	 */
	public boolean userExistById(int _id){
		for(int a=0;a<users.size();a++){
			if(users.get(a).getId()==_id){return true;}
		}
		return false;
	}
	
	/**
	 * Restituisce un utente(Classe User) dall'id utente
	 * se non trova l'utente restituisce Null
	 * @param _id Integer
	 * @return com.gexy.auth.User|Null
	 */
	public User getUserById(int _id){
		for(int a=0;a<users.size();a++){
			if(users.get(a).getId()==_id){return users.get(a);}
		}
		return null;
	}
	
	/**
	 * Restituisce la lista di tuti gli utenti
	 * che hanno eseguito il login
	 * @return List<User>
	 */
	public List<User> getUsers(){
		return users;
	}

	
}
