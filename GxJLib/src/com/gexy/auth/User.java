package com.gexy.auth;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.gexy.config.exception.ConfigDBNameException;
import com.gexy.mysql.exception.MysqlTryOpenWithoutData;

public class User {
	static int id;
	static String username;
	
	static Organization orgClassRef;
	
	static AuthToken authToken;
	
	int peoplesId;
	String firstName;
	String lastName;
	String email;
	String title;
	String jobRule;
	
	int groupId;
	String groupName;
	int groupAclLevel;
	
	
	String language;
	String theme;
	String recoveryEmail;
	
	public User(){
		
	}
	
	/**
	 * Classe dell'utente, fa da contenitore e da interfaccia
	 * per i dati dell'utente
	 * 
	 * Prende come argomenti l'id dell'utente e un istanza della classe
	 * Organization di cui fa parte
	 * 
	 * @param _id Integer
	 * @param _authToken Integer
	 * @param _organizationClassReference Organization
	 * 
	 * @throws MysqlTryOpenWithoutData 
	 * @throws ConfigDBNameException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 * 
	 */
	public User(int _id,Organization _organizationClassReference) throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException, ConfigDBNameException, MysqlTryOpenWithoutData {
		id=_id;
		orgClassRef = _organizationClassReference;
		
		//leggo altri dati dell'utente
		ResultSet userData = orgClassRef.getDatabase().query("SELECT "
				+"users.username as username, "
				+"users.id as id, "
				+"users.peoplesId as peoplesId, "
				+"users.groupId as groupId, "
				+"users.language as language, "
				+"users.theme as theme, "
				+"users.sessionTTLInactivity as sessionTTLInactivity, "
				+"users.sessionTTLMax as sessionTTLMax, "
				+"users.recoveryEmail as recoveryEmail, " 
				+"groups.status as groupStatus, "
				+"groups.name as groupName, "
				+"groups.aclLevel as groupAclLevel, "
				+"peoples.firstName as firstName, "
				+"peoples.lastName as lastName, "
				+"peoples.email as email, "
				+"peoples.title as title, "
				+"peoples.jobRule as jobRule "
				+"FROM users "
				+"JOIN groups as groups ON groups.id = groupId "
				+"JOIN peoples as peoples ON peoples.id = users.peoplesId "
				+"WHERE users.id='"+id+"'");
		
		userData.next();
		
		username = userData.getString("username");
		peoplesId = userData.getInt("peoplesId");
		firstName = userData.getString("firstName");
		lastName = userData.getString("lastName");
		email = userData.getString("email");
		title = userData.getString("title");
		jobRule = userData.getString("jobRule");
		groupId = userData.getInt("groupId");
		groupName = userData.getString("groupName");
		groupAclLevel = userData.getInt("groupAclLevel");
		language = userData.getString("language");
		theme = userData.getString("theme");
		recoveryEmail = userData.getString("recoveryEmail");
		
		
	}
	
	
	
	/**
	 * Ritorna l'username completo dell'utente
	 * @return String
	 */
	public String getUsername(){return username;}
	/**
	 * Ritorna l'id dell'utente
	 * @return int
	 */
	public int getId(){return id;}

	/**
	 * Restituisce il token di autenticazione. Se l'utente
	 * non è autenticato avviene un eccezzione NullPointerException
	 * 
	 * @return com.gexy.auth.AuthToken
	 */
	public AuthToken getAuthToken() {
		return authToken;
	}
	
	/**
	 * Setta il token di autenticazione.
	 * @param _authToken com.gexy.auth.AuthToken
	 */
	public void setAuthToken(AuthToken _authToken){
		authToken=_authToken;
	}
}
