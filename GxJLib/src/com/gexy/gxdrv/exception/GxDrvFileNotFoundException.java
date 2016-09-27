package com.gexy.gxdrv.exception;


public class GxDrvFileNotFoundException extends Exception{
	private static final long serialVersionUID = 7279722431685726162L;
	public GxDrvFileNotFoundException () {
		super("Gexy Drive - File not found");
	}

	public GxDrvFileNotFoundException (String _message) {
		super (_message);
	}

	public GxDrvFileNotFoundException (Throwable _cause) {
		super (_cause);
	}

	public GxDrvFileNotFoundException (String _message, Throwable _cause) {
		super (_message, _cause);
	}
	public GxDrvFileNotFoundException(String _message, Throwable _cause, boolean _enableSuppression, boolean _writableStackTrace){
		super(_message, _cause, _enableSuppression, _writableStackTrace);
	}
}
