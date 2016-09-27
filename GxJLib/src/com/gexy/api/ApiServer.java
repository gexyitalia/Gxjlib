package com.gexy.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.gexy.api.exception.ApiLoaderApiNotFoundException;
import com.gexy.auth.Auth;
import com.gexy.auth.AuthToken;
import com.gexy.auth.excepion.AuthLoginDisableException;
import com.gexy.auth.excepion.AuthLoginFailedException;
import com.gexy.auth.excepion.AuthOrganizationDomainNotExistException;
import com.gexy.auth.excepion.AuthUsernameWrongSyntaxException;
import com.gexy.config.exception.ConfigDBNameException;
import com.gexy.logger.Logger;
import com.gexy.mysql.Mysql;
import com.gexy.mysql.exception.MysqlTryOpenWithoutData;

/**
 * Questa è la classe che crea un server TCP per l'accesso alle API.
 * 
 * Quando un client effettua una connessione viene creato un nuovo thread per gestirla
 * L'handler della connessione del client passa tutto ciò che riceve ad un parser.
 * 
 * Per ogni utente viene creata una cloasse Auth per l'autenticazione
 * 
 * L'input del parser deve essere in formato JSON. Il parser controlla
 * se l'input è una richiesta di login o un comando o un api.
 * 
 * Esempi di input, per una api
 * {
 * 	auth:{
 * 		uid:"15",
 * 		tid:"15jhondoe@test.it"
 * 		},
 * 	api:{
 * 		name:"com.gexy.qualcosa.Test"
 * 		}
 * }
 * 
 * Esempi di input, per il login
 * {
 * 	login:{
 * 		username:"jhondoe@test.it",
 * 		password:"testtesttesdt"
 * 		}
 * }
 * 
 * Esempi di input, per un comando
 * {
 * 	auth:{
 * 		uid:"15",
 * 		tid:"15jhondoe@test.it"
 * 		},
 * 	cmd:{
 * 		name:"comendo-di-test"
 * 		}
 * }
 * 
 * Una ltro comando è close, che chiude la connessione del client e non richiede autenticazione
 * {cmd:{name:"close"}}
 * 
 * 
 * @author eugenio.liguori
 *
 */
public class ApiServer extends Thread{
	protected Logger log; //logger
	protected int serverListenPort; //porta d'ascolto del server
	static protected ApiLoader apiLoader;
	protected List<ClientHandlerThread> clients = new ArrayList<ClientHandlerThread>(); //array delle connessioni client
	protected Mysql authDatabase;

	public ApiServer(Logger _log,int _port,ApiLoader _apiLoader, Mysql _authDatabase){
		this.serverListenPort=_port;
		this.log=_log;

		apiLoader = _apiLoader;
		authDatabase=_authDatabase;

		initializeServer();
	}

	/**
	 * @deprecated
	 * @param _log
	 * @param _port
	 * @param _apiLoader
	 */
	public ApiServer(Logger _log,int _port,ApiLoader _apiLoader){
		this.serverListenPort=_port;
		this.log=_log;

		apiLoader = _apiLoader;

		initializeServer();
	}

	/**
	 * @deprecated
	 * @param _port
	 * @param _apiLoader
	 */
	public ApiServer(int _port,ApiLoader _apiLoader){
		this.serverListenPort=_port;
		this.log=new Logger();

		apiLoader = new ApiLoader(log);
	}

	/**
	 * Inizializza il server
	 */
	private void initializeServer()
	{
		//We need a try-catch because lots of errors can be thrown
		try {
			@SuppressWarnings("resource")
			ServerSocket sSocket = new ServerSocket(this.serverListenPort);
			log.debug("API server initializing on "+sSocket.getLocalSocketAddress()+":"+sSocket.getLocalPort());


			//Loop that runs server functions
			while(true) {
				//Wait for a client to connect
				Socket socket = sSocket.accept();

				//Create a new custom thread to handle the connection
				log.debug("Client "+socket.getRemoteSocketAddress().toString()+" tring to connect...");
				clients.add(new ClientHandlerThread(log,socket,apiLoader,authDatabase));

				ClientHandlerThread cT = clients.get(clients.size()-1);
				//Start the thread!
				new Thread(cT).start();

			}
		} catch(IOException exception) {
			this.log.error("API server error: " + exception);
		}
	}

	//Here we create the ClientThread inner class and have it implement Runnable
	//This means that it can be used as a thread
	protected class ClientHandlerThread implements Runnable
	{
		Logger log;
		Socket threadSocket;
		ApiLoader apiLoader;
		PrintWriter output;
		BufferedReader input;
		Auth auth;

		/*
		 * Responces predefiniti
		 */



		//This constructor will be passed the socket
		public ClientHandlerThread(Logger _log, Socket _socket, ApiLoader _apiLoader,Mysql _authDatabase)
		{
			log = _log;
			//Here we set the socket to a local variable so we can use it later
			threadSocket = _socket;
			apiLoader=_apiLoader;

			try {
				_authDatabase.open();
			} catch (IllegalAccessException | InstantiationException | ClassNotFoundException | SQLException
					| ConfigDBNameException | MysqlTryOpenWithoutData e) {
				log.error("API Server: Opening auth database connection error, for new api server client");
				e.printStackTrace();
			}
			auth = new Auth(log, _authDatabase);

		}

		public void run(){
			try {
				//Create the streams
				output = new PrintWriter(threadSocket.getOutputStream(), true);
				input = new BufferedReader(new InputStreamReader(threadSocket.getInputStream()));

				//Tell the client that he/she has connected
				log.info("Client "+threadSocket.getRemoteSocketAddress().toString()+" is connected on port "+threadSocket.getLocalPort());

				//Verifico che l'ip e il dispositivo remoto siano abilitati all'accesso
				//DA FARE

				while (true) {
					//This will wait until a line of text has been sent
					String chatInput = input.readLine();
					if(chatInput==null){
						log.error("API server error: client lost");
						connectionClose();
						break;
					}else if(parser(chatInput,auth)){
						continue;
					}else{
						log.info("API server: client "+threadSocket.getRemoteSocketAddress().toString()+" close connection");
						connectionClose();
						break;

					}
				}
			} catch(IOException exception) {
				log.error("API server error: " + exception);
			}
		}

		/**
		 * Chiude la connessione col client
		 */
		public void connectionClose(){
			try {
				input.close();
				output.close();
				threadSocket.close();
			} catch (IOException exception) {
				log.error("API server error to close connection with client: "+exception);
			}
		}

		/**
		 * Invia al client la risposta dell'esecuzione di una API
		 * @param _responce String
		 */
		protected void sendResponse(String _responce){
			output.println(_responce);
		}

		/**
		 * Fa il parsing dell'input, deve essere implementata dall'utente
		 * 
		 * Deve ritornare un boolean. Il valore di ritorno è inteso come
		 * true se questo metodo ha riconosciuto l'input e lo ha elaborato
		 * false se non lo ha riconosciuto. Ritornando false verrà poi visualizzato
		 * un errore del tipo input non riconosciuto. Ad esempio un parser di comandi
		 * restituirebbe true se il comando è stato riconosciuto e comunque true anche se il
		 * comando non fosse andato a buon fine. Questo sistema tratta solo l'input 
		 * non riconosciuto.
		 * 
		 * @param _input String
		 * @param _auth com.gexy.auth.Auth
		 * @return boolean
		 */
		public boolean parser(String _input,Auth _auth) {
			try{
				JSONObject obj = new JSONObject(_input.trim());

				Iterator<?> keys = obj.keys();

				while( keys.hasNext() ) {
					String key = (String)keys.next();
					if ( obj.get(key) instanceof JSONObject ) {
						//se è un API
						if(key.equals("api")){
							//parsing del nome della api 
							String apiName = obj.getJSONObject("api").getString("name");
							log.debug("API Server info: client request API "+apiName);

							//parsing degli argomenti
							//MANCA:
							//verifica se ci sono args
							JSONArray apiArgs = obj.getJSONObject("api").getJSONArray("args");
							List<String> apiArgsArray = new ArrayList<String>();
							if(apiArgs.length()>0){

								for (int i = 0; i < apiArgs.length(); i++)
								{
									apiArgsArray.add(apiArgs.getString(i));
								}
							}

							//verifico l'autenticazione
							String authUserId = obj.getJSONObject("auth").getString("uid");
							String authTokenId = obj.getJSONObject("auth").getString("tid");


							try {
								auth.isAuth(Integer.parseInt(authUserId), authTokenId);
							} catch (NumberFormatException | AuthLoginFailedException e1) {
								log.error("Not valid authentication found for user to tring API call");
								e1.printStackTrace();
								sendResponse(ApiResponces.ERROR_AUTH_FAILED);
								return false;
							}
							//-----------------------------------------

							ApiObject ao;
							try {
								if(apiArgs.length()>0){
									ao = apiLoader.call(apiName,apiArgsArray,auth); //eseguo la api cona rgomenti
								}else{
									ao = apiLoader.call(apiName,auth); //eseguo la api
								}

								//se l'esecuzion della api a dato errore
								if(ao.hasError()){
									log.error("API Server error: API "+ao.getName()+" return error: "+ao.getResponce());
								}
								sendResponse(ao.getResponce()); //invio al client il responso
							} catch (ApiLoaderApiNotFoundException e) {
								log.error("API Server error: calling unknow API: "+apiName);
								sendResponse(ApiResponces.ERROR_API_NOT_FOUND);
							}
							
						}

						//se è un comando 
						else if(key.equals("cmd")){
							String cmdName = obj.getJSONObject("cmd").getString("name");
							log.debug("API Server: client request command "+cmdName);

							if(cmdName.equals("exit")){
								return false;
							}

							//verifico l'autenticazione
							String authUserId = obj.getJSONObject("auth").getString("uid");
							String authTokenId = obj.getJSONObject("auth").getString("tid");


							try {
								auth.isAuth(Integer.parseInt(authUserId), authTokenId);
							} catch (NumberFormatException | AuthLoginFailedException e1) {
								log.error("Not valid authentication for user to tring API");
								e1.printStackTrace();
								sendResponse(ApiResponces.ERROR_AUTH_FAILED);
								return false;
							}
							//-----------------------------------------

							//COMANDI DIPENDENTI DALL'AUTENTICAZIONE
							//							if(cmdName.equals("exit")){
							//								return false;
							//							}

						}
						//autenticazione
						else if(key.equals("login")){
							String username = obj.getJSONObject("login").getString("username");
							String password = obj.getJSONObject("login").getString("password");

							try {
								//eseguo login
								AuthToken authToken = auth.login(username, password);

								//se il login è andato a buon fine ritorno l'id del token di autenticazione
								//il client lo userà per le future richieste
								sendResponse("{\"auth\":{\"tid\":\""+authToken.id+"\",uid:\""+authToken.userId+"\"}}");
							} catch (AuthLoginFailedException | AuthUsernameWrongSyntaxException
									| AuthOrganizationDomainNotExistException | AuthLoginDisableException e) {
								log.error("User authentication failed");
								e.printStackTrace();
								sendResponse(ApiResponces.ERROR_AUTH_FAILED);
							}
						}
						//controllo se ci sono altre chiavi valide che non vanno 
						//però gestiste qui
						else if(
								key.equals("auth"))
						{
							continue;
						}else{
							/*
							 * se l'eleaborazione arriva in questo punto vuoldire che nessuna chiave
							 * è stata riconoscita quindi l'input è un comando errato
							 */
							log.error("API server error: Unknown input ("+_input+")");
							sendResponse(ApiResponces.ERROR_UNKNOW_INPUT);
						}
					}
					
				}


			}catch(JSONException e){
				log.error("API Server error: wrong input syntax("+e.getMessage()+")");
			}
			return true;
		}
	}


	/**
	 * Restituisce il loader delle API
	 * @return ApiLoader
	 */
	public ApiLoader getApiLoader(){
		return apiLoader;
	}

	/**
	 * Chiude tutte le connessioni di tutti i client
	 */
	protected void clientsClose(){
		log.info("Closing all client connections");
		for(int index=0;index<this.clients.size();index++){
			this.clients.get(index).connectionClose();
		}
	}

	public void close(){
		log.debug("API Server closing");
		clientsClose();
	}


}
