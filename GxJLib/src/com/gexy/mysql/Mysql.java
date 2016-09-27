package com.gexy.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.gexy.config.exception.ConfigDBNameException;
import com.gexy.logger.Logger;
import com.gexy.mysql.exception.MysqlTryOpenWithoutData;


public class Mysql {
	Logger log;
	static Connection connect = null;
	static Statement statement = null;
	static ResultSet resultSet = null;
	
	String dbHost,dbName,dbUsername,dbPassword;
	
	/**
	 * Classe per l'astrazione della connessione al database
	 * @param String _host 			Hostname o ip del database
	 * @param String _database		Nome del database
	 * @param String _username		Username
	 * @param String _password		Password
	 */
	public Mysql(String _host, String _database,String _username, String _password){
		this.log = new Logger();
		dbHost=_host;
		dbUsername=_username;
		dbPassword=_password;
		dbName=_database;
	}
	
	/**
	 * Classe per l'astrazione della connessione al database
	 * @param Logger _log			Istanza della classe com.gexy.logger.Logger
	 * @param String _host 			Hostname o ip del database
	 * @param String _database		Nome del database
	 * @param String _username		Username
	 * @param String _password		Password
	 */
	public Mysql(Logger _log,String _host, String _database,String _username, String _password){
		this.log=_log;
		dbHost=_host;
		dbUsername=_username;
		dbPassword=_password;
		dbName=_database;
	}
	
	
	/**
	 * Apre una connessione con il database
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws ConfigDBNameException
	 * @throws MysqlTryOpenWithoutData
	 */
	public void open() throws IllegalAccessException, InstantiationException, ClassNotFoundException,SQLException, ConfigDBNameException,MysqlTryOpenWithoutData{
		if(dbHost == null){
			throw new MysqlTryOpenWithoutData();
		}
		if(dbUsername == null){
			throw new MysqlTryOpenWithoutData();
		}
		if(dbPassword == null){
			throw new MysqlTryOpenWithoutData();
		}
		
		if(dbName== null){
			throw new MysqlTryOpenWithoutData();
		}
		
		this.log.debug("Opening database "+dbName);
		
		
		//controllo ed elaboro nomi database e tabella
		String[] dbt = dbName.split("\\.");
//		if(dbt.length<2){
//			throw new ConfigDBNameException("Database and table name malformed, probly table is not specified");
//		}else 
			if(dbt.length>2){
			throw new ConfigDBNameException("Database and table name malformed, probly too meny parameter specified");
		}
		
//			log.debug("Loading JDBC MYSQL driver");
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
//			log.debug("Open connection to mysql:"+dbHost+" database:"+dbt[0]);
			connect = DriverManager.getConnection("jdbc:mysql://"+dbHost+"/"+dbt[0], dbUsername, dbPassword);
	}
	
	/**
	 * chiude la connessione col database
	 * @throws SQLException 
	 */
	public void close() throws SQLException{
		this.log.debug("Closeing database");
		if (resultSet != null) {resultSet.close();}
		if (statement != null) {statement.close();}
		if (connect != null) {connect.close();}
	}
	
	/**
	 * Esegue una query
	 * @param String _query 
	 * @return 
	 * @throws SQLException
	 * @throws MysqlTryOpenWithoutData 
	 * @throws ConfigDBNameException 
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 */
	public ResultSet query(String _query) throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException, ConfigDBNameException, MysqlTryOpenWithoutData{
		// Statements allow to issue SQL queries to the database
				statement = connect.createStatement();
				// Result set get the result of the SQL query
				
				this.resultSet = statement.executeQuery(_query);

//				while (resultSet.next()) {
//					// It is possible to get the columns via name
//					// also possible to get the columns via the column number
//					// which starts at 1
//					// e.g. resultSet.getSTring(2);
////					config.add(new Conf(resultSet.getString("name"),resultSet.getString("value")));
//				}
				
				return this.resultSet;
	}
	
	/**
	 * Verifica che la connessione è aperta
	 * @return boolean
	 */
	public boolean isOpen(){
		if (connect == null) {return false;}else{return true;}
	}
	
	/**
	 * Restituisce il nome del database selezionato
	 * @return String
	 */
	public String getDatabaseName(){
		return this.dbName;
	}
	
	/**
	 * Setta il nome del database.
	 * Se esiste una connessione aperta questa viene prima chiusa
	 * e aperta una nuova connessione
	 * @param _value String Il nome del database
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws ConfigDBNameException
	 * @throws MysqlTryOpenWithoutData
	 */
	public void setDatabaseName(String _value) throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException, ConfigDBNameException, MysqlTryOpenWithoutData{
		this.close();
		dbName=_value;
		this.open();
		
	}
	
	/**
	 * Restituisce l'hostname o ip settato del server sql
	 * @return String
	 */
	public String getDatabaseHost(){
		return this.dbHost;
	}
	
	/**
	 * Setta l'host del server sql.
	 * Se esiste una connessione aperta, questa viene prima chiusa
	 * e aperta una nuova connessione
	 * @param _value
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws ConfigDBNameException
	 * @throws MysqlTryOpenWithoutData
	 */
	public void setDatabaseHost(String _value) throws SQLException, IllegalAccessException, InstantiationException, ClassNotFoundException, ConfigDBNameException, MysqlTryOpenWithoutData{
		this.close();
		dbName=_value;
		this.open();
	}
}
