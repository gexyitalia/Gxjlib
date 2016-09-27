package com.gexy.api.exception;


public class ApiLoaderApiNotFoundException extends Exception{
	private static final long serialVersionUID = 581987783219334385L;
	public ApiLoaderApiNotFoundException () {
		super("API Server exception: API name not found");
	}

	public ApiLoaderApiNotFoundException (String _message) {
		super (_message);
	}

	public ApiLoaderApiNotFoundException (Throwable _cause) {
		super (_cause);
	}

	public ApiLoaderApiNotFoundException (String _message, Throwable _cause) {
		super (_message, _cause);
	}
	public ApiLoaderApiNotFoundException(String _message, Throwable _cause, boolean _enableSuppression, boolean _writableStackTrace){
		super(_message, _cause, _enableSuppression, _writableStackTrace);
	}
}
