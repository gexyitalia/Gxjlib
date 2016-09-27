package com.gexy.auth;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gexy.auth.excepion.AuthLoginDisableException;
import com.gexy.auth.excepion.AuthLoginFailedException;
import com.gexy.auth.excepion.AuthOrganizationDomainNotExistException;
import com.gexy.auth.excepion.AuthUsernameWrongSyntaxException;
import com.gexy.auth.excepion.OrganizationRegisterUserException;
import com.gexy.config.exception.ConfigDBNameException;
import com.gexy.logger.Logger;
import com.gexy.mysql.Mysql;
import com.gexy.mysql.exception.MysqlTryOpenWithoutData;
import com.gexy.security.BCrypt;

public class Auth {
	static Logger log;
	static Mysql dbAuth;
	static Organization organization;

	/**
	 * TEMPORANEA
	 */
	public Auth(Logger _log){
		log=_log;
	}

	/**
	 * 
	 * @param _db
	 */
	public Auth(Mysql _dbAuth){
		dbAuth=_dbAuth;
	}

	/**
	 * 
	 * 
	 * @param _dbHost
	 * @param _dbDatabasae
	 * @param _dbUsername
	 * @param _dbPassword
	 * @throws MysqlTryOpenWithoutData 
	 * @throws ConfigDBNameException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 */
	public Auth(Logger _log,String _dbHost,String _dbDatabase,String _dbUsername,String _dbPassword) throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException, ConfigDBNameException, MysqlTryOpenWithoutData{
		log=_log;

		//apro la connessione al db dell'autenticazione
		dbAuth = new Mysql(_log,
				_dbHost,
				_dbDatabase,
				_dbUsername,
				_dbPassword
				);
		dbAuth.open();
	}


	/**
	 * 
	 * @param _log com.gexy.Logger.log
	 * @param _authDatabase com.gexy.Mysql
	 */
	public Auth(Logger _log, Mysql _authDatabase) {
		log=_log;
		dbAuth=_authDatabase;
	}

	/**
	 * Verifica che l'utente sia autenticato. Prende come argomenti
	 * l'ID dell'utente e l'id del token di autenticaaione.
	 * Cerca l'utente in base all'id e poi confronta l'id del token
	 * memorizzato nella classe User con quello che gli è stato 
	 * passato, se sono uguali l'utente è autenticato.
	 * 
	 * @param _id Integer ID dell'utente
	 * @param _authTokenId ID del token dell'utente
	 * @return com.gexy.auth.User
	 * @throws AuthLoginFailedException
	 */
	public User isAuth(int _id, String _authTokenId) throws AuthLoginFailedException{
		log.debug("Check auth for user id: "+_id);
		try{
			//recupero l'utente dall'id
			User user = organization.getUserById(_id);
			//verifico che il token passato sia uguale a quello generato in fase di login
			if(!user.getAuthToken().id.equals(_authTokenId)){
				throw new AuthLoginFailedException("User with id "+_id+" is not authenticated");
			}
			
			return user;

		}catch(NullPointerException e){
			throw new AuthLoginFailedException("User with id "+_id+" is not loged");
		}
	}


	/**
	 * Esegue il login dell'utente
	 * @param _username
	 * @param _password
	 * @throws AuthLoginFailedException
	 * @throws AuthUsernameWrongSyntaxException
	 * @throws AuthOrganizationDomainNotExistException
	 * @throws AuthLoginDisableException
	 */
	public AuthToken login(String _username, String _password) throws AuthLoginFailedException, AuthUsernameWrongSyntaxException, AuthOrganizationDomainNotExistException, AuthLoginDisableException{
		log.info("Tring login for "+_username);
		try {
			//a questo punto la connessione al datbase dell'autenticazione dovrebbe già essere aperto

			//verifico sintassi username
			if(_username.matches("[\\w\\Q!\"#$%&'()*+,-/:;<=>?[\\]^_`{|}~\\E]+")){
				throw new AuthUsernameWrongSyntaxException();
			}

			//divido la parte del dominio dell'username
			String organizationDomanin = _username.split("@")[1];

			/*
			 * controllo se il dominio esiste nell'elenco delle organizzazioni
			 * si:	leggo i dati dell'organizzazione
			 * 		istanzio una classe Organization
			 */
			ResultSet orgData = dbAuth.query("SELECT * FROM organizations WHERE domain='"+organizationDomanin+"'");
			if(!orgData.next()){
				//se nessuna organizzazione con questo dominio è stata trovata
				throw new AuthOrganizationDomainNotExistException("Nothing organization founded for the domain "+organizationDomanin);
			}else{
				organization = new Organization(log,
						Integer.parseInt(orgData.getString("id")),
						orgData.getString("name"),
						organizationDomanin,
						orgData.getString("db_server"),
						orgData.getString("db_name"),
						orgData.getString("db_username"),
						orgData.getString("db_password"));
				organization.getDatabase().open();
			}


			//verifico correttezza username e password
			ResultSet userAuthData = organization.getDatabase().query("SELECT id, username, password,status FROM users WHERE username='"+_username+"'");


			if(!userAuthData.next()){
				//non esiste nessun utente con questo username
				throw new AuthLoginFailedException("Login Failed for "+_username+", user sames not exist");
				//			}else{
				/*
				 * NON Sè CAPITO PORCO DIO PERCHè NON FUNZIONA
				 * ATTENZIONE NON C'è VERIFICA DELLA PASSWORD
				 */
				//				//verifico la password
				//				if (!BCrypt.checkpw(_password,userAuthData.getString("password"))){
				//					throw new AuthLoginFailedException();
				//				}
			}

			// verifico che l'utente sia abilitato all'accesso
			if(!userAuthData.getBoolean("status")){
				throw new AuthLoginDisableException();
			}

			//verifico che il gruppo dell'utente sia abilitato

			//eseguo procedure di sicurezza utente

			/*------------------------
			 * AUTENTICAZIONE RIUSCITA
			 ------------------------*/

			log.info("Login for "+_username+" successfull");
			
			//creo il token di autenticazione dell'utente
			AuthToken token = new AuthToken();
			token.id=BCrypt.hashpw(userAuthData.getString("id")+_username+organizationDomanin, BCrypt.gensalt(10));
			token.userId=userAuthData.getInt("id");
			
			//setta un utente nell'organizzazione recuperandolo dal database
			User user = organization.registerUser(userAuthData.getInt("id")); 
			user.setAuthToken(token);
			
			return token;

		}catch (OrganizationRegisterUserException|IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException
				| ConfigDBNameException | MysqlTryOpenWithoutData e) {
			log.error("Errore");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Esegue il logout di un utente
	 * @param _id Integer L'ID dell'utente
	 * @throws AuthLoginFailedException 
	 */
	public void logout(int _id,String _authTokenId) throws AuthLoginFailedException{
		try {
			isAuth(_id, _authTokenId);
			organization.unregisterUser(_id, _authTokenId);
		} catch (AuthLoginFailedException | OrganizationRegisterUserException e) {
			log.error("Logout user with id "+_id+" failed, user not loged in or invalid auth");
			throw new AuthLoginFailedException("Logout user with id "+_id+" failed, user not loged in or invalid auth");
		}
	}
	
	/**
	 * Restituisce l'istanza dell'organizzazione
	 * con cui far riferimento hai dati
	 * 
	 * MANCA: il controllo se l'organizzazione è settata
	 * @return com.geyx.auth.Organization
	 */
	public Organization getOrganization(){
		return organization;
	}

}
