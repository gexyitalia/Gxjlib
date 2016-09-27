package com.gexy.logger.exception;

public class SyslogException extends Exception{
	private static final long serialVersionUID = -5564246735998177354L;
	public SyslogException () {

    }

    public SyslogException (String _message) {
        super (_message);
    }

    public SyslogException (Throwable _cause) {
        super (_cause);
    }

    public SyslogException (String _message, Throwable _cause) {
        super (_message, _cause);
    }
    public SyslogException(String _message, Throwable _cause, boolean _enableSuppression, boolean _writableStackTrace){
    			super(_message, _cause, _enableSuppression, _writableStackTrace);
    		}
}
