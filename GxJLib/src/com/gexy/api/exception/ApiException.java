package com.gexy.api.exception;


public class ApiException extends Exception{
	private static final long serialVersionUID = 581987783219334385L;
	public ApiException () {
		super("API exception: unknow exception");
	}

	public ApiException (String _message) {
		super (_message);
	}

	public ApiException (Throwable _cause) {
		super (_cause);
	}

	public ApiException (String _message, Throwable _cause) {
		super (_message, _cause);
	}
	public ApiException(String _message, Throwable _cause, boolean _enableSuppression, boolean _writableStackTrace){
		super(_message, _cause, _enableSuppression, _writableStackTrace);
	}
}
