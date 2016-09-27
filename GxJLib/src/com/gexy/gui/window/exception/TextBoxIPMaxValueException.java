package com.gexy.gui.window.exception;

public class TextBoxIPMaxValueException extends Exception{
	private static final long serialVersionUID = 809759687580912578L;

	public TextBoxIPMaxValueException () {

    }

    public TextBoxIPMaxValueException (String _message) {
        super (_message);
    }

    public TextBoxIPMaxValueException (Throwable _cause) {
        super (_cause);
    }

    public TextBoxIPMaxValueException (String _message, Throwable _cause) {
        super (_message, _cause);
    }
    public TextBoxIPMaxValueException(String _message, Throwable _cause, boolean _enableSuppression, boolean _writableStackTrace){
    			super(_message, _cause, _enableSuppression, _writableStackTrace);
    		}
}
