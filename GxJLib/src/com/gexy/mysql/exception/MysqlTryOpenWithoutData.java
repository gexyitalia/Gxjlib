package com.gexy.mysql.exception;

public class MysqlTryOpenWithoutData extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -884026255522871934L;
	public MysqlTryOpenWithoutData () {
		super ("Error: trying open mysql connection without host, database name, username or password");
	}

	public MysqlTryOpenWithoutData (String _message) {
		super (_message);
	}

	public MysqlTryOpenWithoutData (Throwable _cause) {
		super (_cause);
	}

	public MysqlTryOpenWithoutData (String _message, Throwable _cause) {
		super (_message, _cause);
	}
	public MysqlTryOpenWithoutData(String _message, Throwable _cause, boolean _enableSuppression, boolean _writableStackTrace){
		super(_message, _cause, _enableSuppression, _writableStackTrace);
	}

}
