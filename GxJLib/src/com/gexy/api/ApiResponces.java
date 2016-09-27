package com.gexy.api;

/**
 * Questa classe contiene metodi e costati per la gestione dei messaggi delle API
 * Serve per utilizzare le risposte ovunque serva senza riscriverle
 * @author eugenio.liguori
 *
 */
public class ApiResponces {
	//generico OK
	public final static String OK = "{\"error\":[\"number\":\"0\",\"message\":\"OK\"]}";
	
	//API non trovata
	public final static String ERROR_API_NOT_FOUND = "{\"error\":[\"number\":\"1000\",\"message\":\"API not found\"]}";
	//API server: input non trovato
	public final static String ERROR_UNKNOW_INPUT = "{\"error\":[\"number\":\"1100\",\"message\":\"Unknow input\"]}";
	//autenticazione non valida, credenziali errate	
	public final static String ERROR_AUTH_FAILED = "{\"error\":[\"number\":\"1200\",\"message\":\"Authentication failed\"]}";
	
}
