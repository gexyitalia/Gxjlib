package com.gexy.gxdrv;

import java.util.ArrayList;

import com.gexy.config.exception.ConfigDBNameException;
import com.gexy.gxdrv.exception.GxDrvFileNotFoundException;
import com.gexy.mysql.Mysql;
import com.gexy.mysql.exception.MysqlTryOpenWithoutData;
import com.gexy.security.Hash;

import java.awt.List;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
public class GxDrvFile {
	//costanti tipo di file
	final int TYPE_FILE = 0;
	final int TYPE_DIR = 1;
	final int TYPE_LINK = 2;

	//costanti per il calcolo con getSize()
	//	final int SIZE_BYTE = 0;
	//	final int SIZE_KILOBYTE = 1;
	//	final int SIZE_MEGABYTE = 2;
	//	final int SIZE_GIGABYTE = 3;
	//	final int SIZE_TERABYTE = 4;


	//metadati
	String name;
	float size;
	int inode;
	int type;
	String mime;
	boolean inUse =false;
	boolean deleted =false;
	int parentInode;
	ArrayList<String> tags;
	String hash;

	//database
	Mysql db;

	boolean opened = false;
	boolean writeable = true;
	boolean readable = true;

	byte[] content; //contenuto del file


	public GxDrvFile(Mysql _db, int _inode){
		db=_db;
		inode=_inode;
	}

	public GxDrvFile(Mysql _db, GxDrvFileMetadata _meta){
		db=_db;

		//popolo i dati
		name=_meta.name;
		inode=_meta.inode;
		type=_meta.type;
		mime=_meta.mime;
		inUse=_meta.inUse;
		deleted=_meta.deleted;
		parentInode=_meta.parentInode;
		tags=_meta.tags;
		hash=_meta.hash;
	}

	/**
	 * 
	 * Manca:
	 * lettura del parentInode e i tag
	 * 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws ConfigDBNameException
	 * @throws MysqlTryOpenWithoutData
	 * @throws GxDrvFileNotFoundException
	 */
	public void open() throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException, ConfigDBNameException, MysqlTryOpenWithoutData, GxDrvFileNotFoundException{
		//verifico se il file esiste
		if(!exist()){
			throw new GxDrvFileNotFoundException("File with inode "+inode+" not found in storage table");
		}

		/*
		 * richiedo i dati del file
		 * 
		 * BUG: usando una sola quaery non si può distinguere quando un file
		 * esiste ma non è posizionato nella tabella drv_tree(quindi è isolato)
		 * il che significa che esiste ma non è in nessuna directory
		 * bisognerebbe dividere in due query
		 */
		ResultSet data = db.query("SELECT "
				+ "drv_store.name AS name,"
				+ "drv_store.size AS size,"
				+ "drv_store.inuse AS inuse,"
				+ "drv_store.deleted AS deleted,"
				+ "drv_store.hash AS hash,"
				+ "drv_store.mime AS mime,"
				+ "drv_store.type AS type,"
				+ "drv_tree.parent AS parent_inode " 
				+ "FROM drv_store "
				+ "JOIN drv_tree drv_tree ON drv_store.inode = drv_tree.inode "
				+ "WHERE drv_store.inode='"+inode+"'");
		data.next();


		//memorizzo i dati nella classe
		name = data.getString("name");
		type = data.getInt("type");
		mime = data.getString("mime");
		inUse = data.getBoolean("inuse");
		deleted = data.getBoolean("deleted");
		hash = data.getString("hash");
		parentInode=data.getInt("parent_inode");

		opened=true;
	}

	/**
	 * Chiude il file
	 */
	public void close(){
		opened=false;
	}

	/**
	 * Legge il contenuto. Se è un file riceve il contenuto del file
	 * se invece è una directory legge i file che contiene.
	 * 
	 * Ritorna due tipi di dati in base al tipo di file(ogetto) di
	 * cui si è chiesto il contenuto. Se è una directory ritorna un array
	 * di ogetti com.gexy.drive.GxDrvFile, se invece è un file ritorna
	 * un array di byte(che ne è il contenuto).
	 * 
	 * Se il file è di tipo TYPE_FILE, una volta chiamato questo metodo
	 * il contenuto potrà essere recuperato successivamente con getContent()
	 * potrà comunque sempre essere richiamato questo metodo(read()) per aggiornare
	 * il contenuto dal database.
	 * @return GxDrvFile[]
	 * @return byte[] 
	 * @throws GxDrvFileNotFoundException 
	 * @throws MysqlTryOpenWithoutData 
	 * @throws ConfigDBNameException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 */
	@SuppressWarnings("unchecked")
	public <T> T read() throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException, ConfigDBNameException, MysqlTryOpenWithoutData, GxDrvFileNotFoundException{
		//se il file è aperto
		if(!isOpen()){
			open();//se il file non è aperto lo apro
		}

		switch(getType()){
		case TYPE_FILE:
			//se è un file ne legge il contenuto
			return (T) readFileContent();
		case TYPE_DIR:
			//ARRAYLIST VA OSTITUIT CON UN ARAY NORMALE
			
			//se è una directory ne legge la lista dei file figli
			//e restituisce una lista di oggetti com.gexy.drive.GxDrvFile
			ArrayList<GxDrvFile> childs = new ArrayList<GxDrvFile>();
			for(int childInode:getDirContent()){
				childs.add(new GxDrvFile(db,childInode));
			}
			return (T) childs;
		}
		return null; //da togliere
	}

	/**
	 * Verifica se il file esiste, la verifica
	 * viene effettuata con l'inode del file
	 * @return boolean
	 * @throws MysqlTryOpenWithoutData 
	 * @throws ConfigDBNameException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 */
	public boolean exist() throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException, ConfigDBNameException, MysqlTryOpenWithoutData{
		ResultSet data = db.query("SELECT EXISTS(SELECT NULL FROM drv_store WHERE inode ='"+inode+"' LIMIT 1) AS exist");
		data.next();
		if(data.getBoolean("exist")){
			return true;
		}else{
			return false;
		}

	}

	/**
	 * Legge il contenuto di un file di tipo TYPE_FILE
	 * così com'è senza alcuna lavorazione ne codifica
	 * i dati vengono restituiti come un array di byte.
	 * @return byte[]
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws ConfigDBNameException
	 * @throws MysqlTryOpenWithoutData
	 * @throws SQLException
	 */
	private byte[] readFileRowContent() throws  IllegalAccessException, InstantiationException, ClassNotFoundException, ConfigDBNameException, MysqlTryOpenWithoutData, SQLException{
		ResultSet data = db.query("SELECT content FROM drv_store WHERE inode='"+inode+"'");
		Blob blob = null;
		data.next();
		blob = data.getBlob("content");
		return blob.getBytes(1,(int) blob.length());
	}

	/**
	 * Restituisce il contenuto di un file di tipo TYPE_FILE
	 * @return byte[]
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 * @throws ConfigDBNameException
	 * @throws MysqlTryOpenWithoutData
	 * @throws SQLException
	 */
	private byte[] readFileContent() throws IllegalAccessException, InstantiationException, ClassNotFoundException, ConfigDBNameException, MysqlTryOpenWithoutData, SQLException{
		byte[] c = readFileRowContent();
		content=c;
		return content;
	}

	/**
	 * Legge il contenuto di una directory, lista i file in una directory. 
	 * Assunto che il file(oggetto)
	 * in questione rappresenti una directory identificata dall'inode, 
	 * questo metodo tenta di leggere nella tabella drv_tree tutti gli 
	 * inode che possiedono l'ogetto(l'inode di questo oggetto) come 
	 * parent e restituisce una lista di interi.
	 * 
	 * Non prende in considerazione i file contrassegnati come cestinati
	 * 
	 * @throws MysqlTryOpenWithoutData 
	 * @throws ConfigDBNameException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 */
	private int[] getDirContent() throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException, ConfigDBNameException, MysqlTryOpenWithoutData{
		ResultSet data = db.query("SELECT drv_tree.inode AS inode FROM drv_tree "
				+ "JOIN drv_store drv_store ON drv_tree.inode = drv_store.inode "
				+ "WHERE drv_tree.parent='"+inode+"' AND drv_store.deleted='0'");
		//conto i risultati
		int rowcount = 0;
		if (data.last()) {
			rowcount = data.getRow();
			data.beforeFirst(); 
		}

		int[] ret = new int[rowcount];

		//prendo i risultati
		if(data.next()){
			ret[data.getRow()]=data.getInt("inode");
			while(data.next()){
				ret[data.getRow()]=data.getInt("inode");	
			}
			return ret;
		}else{
			return ret;
		}

	}

	/**
	 * Legge il contenuto di una directory, come getDirContent() ma
	 * senza filtri leggerà tutti i file indipendentemente che siano in quarantena
	 * o cestinati o altro
	 * 
	 * @throws MysqlTryOpenWithoutData 
	 * @throws ConfigDBNameException 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 */
	private int[] getDirRowContent() throws IllegalAccessException, InstantiationException, ClassNotFoundException, SQLException, ConfigDBNameException, MysqlTryOpenWithoutData{
		ResultSet data = db.query("SELECT drv_tree.inode AS inode FROM drv_tree "
				+ "JOIN drv_store drv_store ON drv_tree.inode = drv_store.inode"
				+ "WHERE drv_tree.parent='"+inode+"' AND drv_store.deleted='0'");
		//conto i risultati
		int rowcount = 0;
		if (data.last()) {
			rowcount = data.getRow();
			data.beforeFirst(); 
		}

		int[] ret = new int[rowcount];

		//prendo i risultati
		if(data.next()){
			ret[data.getRow()]=data.getInt("inode");
			while(data.next()){
				ret[data.getRow()]=data.getInt("inode");	
			}
			return ret;
		}else{
			return ret;
		}

	}

	//comprime i dati di un file
	private void compressFile(){

	}
	//decomprime i dati di un file
	private void uncompressFile(){

	}

	/**
	 * Restituisce il contenuto del file
	 * @return byet[]
	 */
	public byte[] getContent(){
		return content;
	}


	/**
	 * Verifica se il file è aperto
	 * @return boolean
	 */
	public boolean isOpen(){
		return opened;
	}

	/**
	 * Restituisce il tipo di file.
	 * Il tipo è identificato da un intero che fa riferimento
	 * ad una delle costanti:
	 * 
	 * GxDrvFile::TYPE_FILE
	 * GxDrvFile::TYPE_DIR
	 * GxDrvFile::TYPE_LINK
	 *
	 * @return Integer
	 */
	public int getType(){
		return type;
	}

	/**
	 * Verifica se questo oggetto è una directory
	 * @return boolean
	 */
	public boolean isDir(){
		if(type==TYPE_DIR){return true;}else{return false;}
	}

	/**
	 * Verifica se questo oggetto è un file
	 * @return boolean
	 */
	public boolean isFile(){
		if(type==TYPE_FILE){return true;}else{return false;}
	}

	/**
	 * Restituisce la grandezza del file in bytes
	 * @return float
	 */
	public float getSize(){
		return size;
	}

	/**
	 * Restituisce il nome del file
	 * @return String
	 */
	public String getName(){
		return name;
	}

	public String getPath(){
		return "";
	}

	/**
	 * Restituisce l'inode
	 * @return integer
	 */
	public int getInode(){
		return inode;
	}

	/**
	 * Genera l'hash MD5 del contenuto del file.
	 * Questo avviene trasformando i byte del contenuto
	 * in una stringa UTF-8 e facendone l'MD5 tramite
	 * la classe com.gexy.security.Hash
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	private String makeHash() throws UnsupportedEncodingException{
		Hash hash = new Hash();
		return hash.md5(new String(content, "UTF-8"));
	}

	/**
	 * Restituisce l'hash MD5 per il file.
	 * Questo viene generato ad ogni modifica de contenuto.
	 * @return String
	 */
	public String getHash(){
		return hash;
	}
}
