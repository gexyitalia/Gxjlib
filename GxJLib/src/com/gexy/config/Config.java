package com.gexy.config;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.gexy.cli.exception.CLIOptionArgumentValueRequiredException;
import com.gexy.config.exception.ConfigDBNameException;
import com.gexy.config.exception.ConfigFileContextNotFound;
import com.gexy.config.exception.ConfigFileParameterTooManyContext;
import com.gexy.config.exception.ConfigFilePathException;

import com.gexy.logger.Logger;




/**
 * classe per la gestione della configurazione
 * 
 * Caratteristiche
 * - Legge configurazione da file xml
 * - Scrive configurazione su file xml
 * - Legge configurazione da Mysql
 * 
 * Legge la configurazione in base al supporto specificato e memorizza 
 * i valori in un array, cos� da avere una gestione centralizzata e da
 * non dover fare richieste a suppordi diversi dalla RAM
 * 
 * E' possibile anche criptare/descriptare i valori di ogni parametro
 * 
 * 
 * Recuperare il valore di un parametro
 * ------------------------------------
 * una volta letta la configurazione questa è disponibile in parametri richiamabili dal nome
 * con il metogo
 * 
 * String val = conf.getValueByName(<nome_parametro>);
 * 
 * 
 * Recuperare un parametro
 * -----------------------
 * E' possibile recuperare la classe delparametro con tutti i sui data
 * 
 * Conf parm = conf.getElementByName(<nome_parametro>);
 * 
 * 
 * Aggiungere un parametro
 * -----------------------
 * Per aggiungere un parametro alla configurazione 
 * 
 * conf.addElement(<nome_parametro>,<valore>,<unico>);
 * 
 * dove <unico> può essere true se il parametro deve presentarsi una volta
 * sola nell'intera lista di parametri, false se può essere presente più volte.
 * La differenza sta nel fatto che quando si recuperano parametri o valori da
 * parametri presenti più volte nella lista questi vengono restituiti tutti
 * in un array mentre se è unico non è permesso aggiungere un nuovo 
 * parametro con lo stesso nome e quando viene prelevato il valore o l'intero
 * parametro viene restituito un solo oggetto o un solo valore.
 * 
 * Verrà restituito un elemento della classe Conf
 * 
 * Per aggiungere un parametro crittografato
 * 
 * conf.addEncryptedElement(<nome_parametro>,<valore>,<unico>);
 * 
 * 
 * Settare il valore di un parametro esistente
 * -------------------------------------------
 * Per settare un valore 
 * 
 * conf.setValueByName(<nome_parametro>,<valore>);
 * 
 * 
 * Rimuovere in parametro
 * ----------------------
 * conf.removeElement(<nome_parametro>);
 * 
 * 
 * Verificare se un parametro esiste
 * ---------------------------------
 * boolean exist = conf.exist(<nome_parametro>);
 * 
 * 
 * Svuotare la configurazione
 * --------------------------
 * Usando il metodo reset() tutti i parametri vengono eliminati
 * 
 * ------------------------------------------------
 * XML
 * ------------------------------------------------
 * 
 * Per leggere la configurazione da un file xml
 * --------------------------------------------
 * Il nodo radice deve sempre essere <gexy>
 * 
 	try {
		conf.readConfigFromXml(<file>,<contesto>);
 	} catch (ConfigFilePathException | ConfigFileContextNotFound | ConfigFileParameterTooManyContext e) {
 		System.exit(1);
 	}
 * 
 * Un esempio del file xml può essere questo
 * 
 	<gexy>
    	<contesto1>
    		<apiServerPort>6061</apiServerPort>
    		<apiServerBindAddress>localhost</apiServerBindAddress>
    	</contesto1>
    	<contesto2>
    		<apiServerPort>6062</apiServerPort>
    		<apiServerBindAddress>localhost</apiServerBindAddress>
    	</contesto2>
 	</gexy>
 	
 * 
 * 
 * Salavre configurazione esistente in un file xml
 * -----------------------------------------------
 * writeConfigToXMl(<file>,<contesto>);
 * 
 * il file xml verrà completamente sovrascritto con i parametri
 * specificato a runtime
 * 
 * un esempio del comando può essere questo
 * 
	conf.writeConfigToXml(conf.getXmlConfigFile(),conf.getXmlConfigContextNode());
 * 
 * I due metodi getXmlConfigFile() e getXmlConfigContextNode() restituiscono il nome
 * del file di configurazione usato all'inizio per leggere i parametri e il nodo
 * del contesto usato.
 * 
 * 
 * @version 	1.2
 */

public class Config {
	public static Logger log; //logger
	protected List<Conf> config = new ArrayList<Conf>(); //array contenente i dai configurazione

	/*
	 *  XML
	 */
	protected String xmlConfigFilePath;
	protected String xmlConfigContextNode;

	/*
	 * costruttori
	 */
	public Config(){
		log = new Logger();
		log.setLogFile("Config.log");
		log.setLogToFile(false);
	}
	public Config(Logger _log){
		log=_log;
	}

	/**
	 * Ritorna il log
	 * @return
	 */
	public Logger getLogger(){
		return log;
	}

	/**
	 * Delete all local config entry, reset
	 */
	public void reset(){
		this.config.clear();
	}

	/**
	 * Restituisce il file sorgente di configurazione xml
	 * @return String
	 */
	public String getXmlConfigFilePath(){
		return xmlConfigFilePath;
	}

	/**
	 * Restituisce il nome del noto principale del contesto nel sorgente di configurazione xml
	 * @return String
	 */
	public String getXmlConfigContextNode(){
		return xmlConfigContextNode;
	}

	/**
	 * Read configuration from xml file
	 * 
	 * Structure of config file to read
	 * <config>
	 * 		<app_name>
	 * 			<parameter_name>parameter_value</paramete_name>
	 * 			<parameter_name>parameter_value</paramete_name>
	 * 			<parameter_name>parameter_value</paramete_name>
	 * 		</app_name>
	 * </config>
	 * 
	 * @param 	_file
	 * @param 	_node
	 * @return	int		0	Ok
	 * @return	int		-1	Unknow
	 * @return	int		-2	Configuration file not exist
	 * @return	int		-3	App node not exists
	 * @return	int		-4	Too many app node
	 * @throws ConfigFilePathException 
	 * @throws ConfigFileContextNotFound 
	 * @throws ConfigFileParameterTooManyContext 
	 */
	public int readConfigFromXml(String _file, String _node) throws ConfigFilePathException, ConfigFileContextNotFound, ConfigFileParameterTooManyContext{
		if(_file==null){
			throw new ConfigFilePathException("Config file path is null or is not specified");
		}

		//verifico consistenza del file
		File f = new File(_file);
		if(!f.exists()) {
			throw new ConfigFilePathException("Config file "+_file+" not found or not readable");
		}

		log.debug("Reading config from xml file: "+_file);
		xmlConfigFilePath=_file;
		xmlConfigContextNode=_node;
		try { 
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = documentFactory.newDocumentBuilder(); 
			Document document = builder.parse(f);   

			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			document.getDocumentElement().normalize();

			int totRootNode=document.getElementsByTagName(_node).getLength();

			//controllo se non ci sono nodi _node
			if(totRootNode==0){
				throw new ConfigFileContextNotFound("Context "+_node+" not found");
			}
			//controllo se ci sono pi� nodi _node
			if(totRootNode>1){
				throw new ConfigFileParameterTooManyContext("Too many context "+_node+" founded");
			}

			//raccolgo gli elemnti del noto principale
			NodeList node = document.getElementsByTagName(_node); 
			Element nListElement =(Element) node.item(0);
			NodeList nList = nListElement.getChildNodes();


			log.debug("Finded "+nList.getLength()+" parameters in file xml");
			//			config.add(new Conf[parameters.getLength()]); 

			for(int i=0;i<nList.getLength();i++){
				Node element = nList.item(i);
				if (element.getNodeType() == Node.ELEMENT_NODE) { //se � un elemnto
					log.debug("Reading: "+element.getNodeName());
					Element e = (Element) element;
					//se esiste gia lo setto
					if(this.exist(element.getNodeName())){
						//controllo se è criptato
						if(element.getAttributes().getNamedItem("encrypt")!=null){
							Conf conf = getElementByName(element.getNodeName());
							conf.encrypetd=true;
							conf.setAsDefault(false);
							removeElement(element.getNodeName());
							config.add(conf);
						}else{
							this.setValueByName(element.getNodeName(), e.getTextContent());
						}
					}
					//se non esiste lo creo
					else{
						Conf conf = new Conf(element.getNodeName(),e.getTextContent());
						//controllo se è criptata
						if(element.getAttributes().getNamedItem("encrypt")!=null){
							conf.encrypetd=true;
						}
						config.add(conf);
					}
				}
			}
			log.debug("Readed "+config.size()+" parameters");
			return 0; //ok

		} catch (ParserConfigurationException pce) {
			log.error("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
		} catch (IOException ioe) {
			log.error(ioe.getMessage());
		} catch (SAXException se) {
			log.error(se.getMessage());
		}

		return -1;
	}

	/*
	 * scrive la configurazione attuale in un file xml
	 */
	public void writeConfigToXml(String _file, String _node){
		log.debug("Scrittura configurazione in file xml: "+_file);
		Document dom;

		// instance of a DocumentBuilderFactory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// use factory to get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			// create instance of DOM
			dom = db.newDocument();

			Element root = dom.createElement("gexy");
			dom.appendChild(root);

			Element contextNode = dom.createElement(_node);
			root.appendChild(contextNode);


			for (Conf conf : config) {
				//se non è salvabile, lo salto
				if(!conf.isSaveable()){
					continue;
				}
				
				//se è un valore settato di default nel programma lo salto
				if(conf.isDefault()){
					continue;
				}

				Element e = dom.createElement(conf.recordName);
				
				//se è criptato
				if(conf.encrypetd){
					e.setAttribute("encrypt", "");
				}
				
				e.appendChild(dom.createTextNode(conf.recordValue));
				contextNode.appendChild(e);
			}

			//salvataggio nel file
			try {
				Transformer tr = TransformerFactory.newInstance().newTransformer();
				tr.setOutputProperty(OutputKeys.INDENT, "yes");
				tr.setOutputProperty(OutputKeys.METHOD, "xml");
				tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				//	            tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "roles.dtd");
				tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

				// send DOM to file
				tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(_file)));

			} catch (TransformerException te) {
				log.error(te.getMessage());
			} catch (IOException ioe) {
				log.error(ioe.getMessage());
			}
		} catch (ParserConfigurationException pce) {
			log.error("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
		}
	}

	/**
	 * Read configuration parameter from MYSQL 
	 * @param _host
	 * @param _database
	 * @param _table
	 * @param _context	Pu� essere usato o una stringa o (null o "*" per tutti i contesti)
	 * @param _username
	 * @param _password
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws ConfigDBNameException 
	 * 
	 * Use:
	 * try {
	 *		conf.readConfigFromMySql(<host>,<db.table>,<context>,<user>,<pass>);
	 * } catch (IllegalAccessException | InstantiationException
	 *			| ClassNotFoundException | SQLException | ConfigDBNameException e) {
	 *		log.error("Error reading config from mysql: "+e.getMessage());
	 *		System.exit(1);
	 * } 
	 * 
	 * Questo aggiuger� record al configuratore, se esistono record uguali vengono sovrascritte
	 * in questo modo si pu� implementare un override. 
	 * 
	 */
	public void readConfigFromMySql(String _host, String _database, String _context, String _username, String _password) throws IllegalAccessException, InstantiationException, ClassNotFoundException,SQLException, ConfigDBNameException{

		Connection connect = null;
		Statement statement = null;
		ResultSet resultSet = null;

		//controllo ed elaboro nomi database e tabella
		String[] dbt = _database.split("\\.");
		if(dbt.length<2){
			throw new ConfigDBNameException("Database and table name malformed, probly table is not specified");
		}else if(dbt.length>2){
			throw new ConfigDBNameException("Database and table name malformed, probly too meny parameter specified");
		}

		try {
			log.debug("Loading JDBC MYSQL driver");
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			log.debug("Open connection to mysql:"+_host+" database:"+dbt[0]+" username:"+_username);
			connect = DriverManager.getConnection("jdbc:mysql://"+_host+"/"+dbt[0], _username, _password);

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			log.debug("Query for config data");

			String query;
			if(_context=="*"||_context==null){
				query = "SELECT name,value FROM "+dbt[1];
			}else{ 
				query = "SELECT name,value FROM "+dbt[1]+" WHERE context='"+_context+"'";
			}
			resultSet = statement.executeQuery(query);

			while (resultSet.next()) {
				// It is possible to get the columns via name
				// also possible to get the columns via the column number
				// which starts at 1
				// e.g. resultSet.getSTring(2);
				log.debug("Create local config entry for: "+resultSet.getString("name"));
				config.add(new Conf(resultSet.getString("name"),resultSet.getString("value")));
			}
		}finally{
			mysqlClose(connect, statement, resultSet);
		}
	}


	/**
	 * Close the mysql connection
	 * @throws SQLException 
	 */
	private void mysqlClose(Connection _connect, Statement _statement, ResultSet _resultSet) throws SQLException{
		this.log.debug("Close mysql connection");
		if (_resultSet != null) {_resultSet.close();}
		if (_statement != null) {_statement.close();}
		if (_connect != null) {_connect.close();}

	}

	/**
	 * Verifica se esiste un parametro dal nome
	 * @param _name String
	 * @return boolean
	 */
	public boolean exist(String _name){
		for(int a=0;a<config.size();a++){
			if(config.get(a).recordName.equals(_name)){return true;}
		}
		return false;
	}

	/*
	 * restituisce l'elemento(Conf) dal recordName
	 */
	/**
	 * Return element object 
	 * @param _name	Name of element
	 * @return String Value of element
	 */
	public Conf getElementByName(String _name){
		for(int a=0;a<config.size();a++){
			if(config.get(a).recordName.equals(_name)){return config.get(a);}
		}
		log.worn("Nessun elemento trovato nel configuratore per il nome: |"+_name+"|, restituisco elemento null");
		return new Conf(null,null); //se non trovato ritorna un parametro vuoto
	}

	/*
	 * restituisce il recordValue dell'elemento(Conf) dal recordName
	 */
	/**
	 * Return value of element
	 * @param _name	Name of element to extract value
	 * @return String Value of element
	 */
	public String getValueByName(String _name){
		for(int a=0;a<config.size();a++){
			if(config.get(a).recordName.equals(_name)){
				//se è criptato
				if(config.get(a).encrypetd){
					try {
						return decrypt(config.get(a).recordValue);
					} catch (GeneralSecurityException | IOException e) {
						log.error("Decrypting value of config parameter failed: "+e.getMessage());
					}	
				}else{
					return config.get(a).recordValue;
				}
			}
		}
		log.worn("Nessun valore trovato nel configuratore per il nome: |"+_name+"|, restituisco null");
		return null; //se non trovato ritorna un parametro vuoto
	}

	/**
	 * Set value of element
	 * @param _name	Name of element to set value
	 * @param _value	string of value to set
	 * @return boolean
	 */
	public boolean setValueByName(String _name, String _value){
		for(Conf conf : config){
			if(conf.recordName.equals(_name)){
				conf.recordValue=_value;
				return true;
			}
		}
		log.worn("Nessun valore trovato nel configuratore per il nome: |"+_name+"|, restituisco false");
		return false;
	}

	/**
	 * Add an element
	 * If an element exist with same name and it's set to unice, return null.
	 * 
	 * @param _name	Name of element
	 * @param _value String of value
	 * @param _unique Se true e il nuovo elemento esiste gi� non viene creato altrimenti viene creato comunque
	 * @return com.gexy.confi.Config.Conf|null
	 */
	public Conf addElement(String _name, String _value, boolean _unique){
		if(!this.exist(_name)){
			Conf e = new Conf(_name,_value);
			config.add(e);
			return e;
		}else{
			if(_unique){
				log.error("Esiste gi� un elemento con questo nome: "+_name);
				return null;
			}else{
				log.debug("Esiste gi� un elemento con questo nome: "+_name+", viene creato lo stesso quello nuovo");
				Conf e = new Conf(_name,_value);
				config.add(e);
				return e;
			}
		}
	}
	
	/**
	 * Add an encrypted element
	 * @param _name	Name of element
	 * @param _value String of value
	 * @param _unique Se true e il nuovo elemento esiste gi� non viene creato altrimenti viene creato comunque
	 * @return boolean
	 */
	public boolean addEncryptedElement(String _name, String _value, boolean _unique){
		try {
			String eValue = encrypt(_value);
			if(!this.exist(_name)){
				Conf conf = new Conf(_name,eValue);
				conf.encrypetd=true;
				config.add(conf);
				return true;
			}else{
				if(_unique){
					log.error("Esiste gi� un elemento con questo nome: "+_name);
					return false;
				}else{
					log.worn("Esiste gi� un elemento con questo nome: "+_name+", viene creato lo stesso quello nuovo");
					Conf conf = new Conf(_name,eValue);
					conf.encrypetd=true;
					config.add(conf);
					return true;
				}
			}
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			log.error("Adding new element to config failed because encrypting failed: "+e.getMessage());
			return false;
		}
	}

	/**
	 * Elimina un parametro
	 * @param _name String
	 * @return boolean
	 */
	public boolean removeElement(String _name){
		for(int a=0;a<config.size();a++){
			if(config.get(a).recordName.equals(_name)){
				config.remove(a);
				return true;
			}
		}
		log.worn("Nessun valore trovato nel configuratore per il nome: |"+_name+"|, restituisco null");
		return false; //se non trovato ritorna un parametro vuoto
	}
	
	/*
	 * Crittografia
	 */
	private static final char[] PASSWORD = "rZgs2_-cQiRqOyhRNjgvoowgU65CIEDOf2sXMOK0".toCharArray();
    private static final byte[] SALT = {
        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
    };

//    public static void main(String[] args) throws Exception {
//        String originalPassword = "secret";
//        System.out.println("Original password: " + originalPassword);
//        String encryptedPassword = encrypt(originalPassword);
//        System.out.println("Encrypted password: " + encryptedPassword);
//        String decryptedPassword = decrypt(encryptedPassword);
//        System.out.println("Decrypted password: " + decryptedPassword);
//    }

    private static String encrypt(String property) throws GeneralSecurityException, UnsupportedEncodingException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
        return base64Encode(pbeCipher.doFinal(property.getBytes("UTF-8")));
    }

    private static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static String decrypt(String property) throws GeneralSecurityException, IOException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
        return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
    }

    private static byte[] base64Decode(String property) throws IOException {
    	return Base64.getDecoder().decode(property);
    }
    
}
