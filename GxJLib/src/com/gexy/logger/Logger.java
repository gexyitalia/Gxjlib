package com.gexy.logger;





import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.gexy.logger.exception.SyslogException;

/**
 * Questa classe implementa un sistema di log
 * @author 		Eugenio Liguori <e.liguori@zerounozerolab.com>
 * @version 	0.6
 *
 */

public class Logger {
	/**
	 * livelli dei messaggi
	 * 
	 */
	static public int DEBUG = 7;
	static public int INFO = 6;
	static public int DATA = 5;
	static public int WORNING = 4;
	static public int ERROR = 3;


	private String IP; //ip della macchina locale

	private boolean logToStdOut; //log sullo stdout
	private int logToStdOutLvl; //livello minimo di log per lo stdout
	private String logToStdOutFormat;

	private boolean logToFile; //log su file
	private int logToFileLvl; //livello minimo di log per il file
	private String logFile; //log file
	private String logToFileFormat;

	private boolean logToSyslog; //log in rsyslog linux
	private int logToSyslogLvl; //livello minimo di log per syslog
	public Syslog sysLog; //classe Syslog

	private boolean logToDb;
	private int logToDbLvl;
	private String mysqlHost;
	private String mysqlUser;
	private String mysqlPass;
	private String msyqlDb;
	private String msyqlTable;
	private String logToDbFormat;

	public Logger(){
		logToStdOut = true;
		logToStdOutLvl = INFO;

		logFile = "default.log";
		logToFile = false;
		logToFileLvl = INFO;

		logToDb = false;
		logToDbLvl = INFO;

		logToSyslog = false;
		logToSyslogLvl=INFO;

		try {
			IP=InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			System.err.println("Error try read local IP address and hostname, using empty string");
			e.printStackTrace();
			IP="";
		}

		setFormats("%D/%M/%Y %h:%m:%s,%S %l %g");
		setLogToStdOutFormat("%g");
	}

	/**
	 * Set minimum message level to show in all log output channels
	 * @param Integer _level
	 */
	public void setLogLevels(int _level){
		logToStdOutLvl=_level;
		logToFileLvl=_level;
		logToDbLvl=_level;
	}


	/**
	 * abilita/disabilita la stampa dei messaggi sullo stdout	
	 */
	public void setLogToStdOut(boolean _val){
		logToStdOut=_val;

	}
	/**
	 * setta il livello minimo per il log in stdout
	 */
	public void setLogToStdOutLvl(int _val){
		logToStdOutLvl=_val;
	}
	/**
	 * abilita/disabilita la scrittura dei messaggi su file	
	 */
	public void setLogToFile(boolean _val){
		logToFile=_val;
	}

	/**
	 * Abilita/disabilita la scrittura dei messaggi in syslog
	 */
	public void setLogToSyslog(int _port, String _address, int _facility, int _opt, String _ident){
		logToSyslog=true;
		sysLog=null;
		try {
			sysLog = new Syslog(_ident, _opt, _facility, _port, _address);
		} catch (UnknownHostException | SyslogException e) {
			logToStdOut=false;
			e.printStackTrace();
		}
	}
	public void disableLogToSyslog(){
		logToSyslog=false;
	}
	/**
	 * setta il livello minimo per il log in file
	 */
	public void setLogToSyslogLvl(int _val){
		logToSyslogLvl=_val;
	}

	/**
	 * setta il file di log
	 */
	public void setLogFile(String _val){
		logFile=_val;
	}
	/**
	 * setta il livello minimo per il log in file
	 */
	public void setLogToFileLvl(int _val){
		logToFileLvl=_val;
	}

	/**
	 * Set format for message of all log ways
	 * 
	 * %l	message level(String)
	 * %L	message level(Numerical)
	 * %g	Message
	 * 
	 * %D	Day
	 * %M	Month
	 * %Y	Year
	 * 
	 * %h	Hour in 24h format
	 * %m	Minute
	 * %s	Seconds
	 * %S	Milliseconds
	 * 
	 * %F	File name
	 * %T	Method name
	 * 
	 * %I	Local IP/hostname
	 * 
	 * @param String _format	
	 */
	public void setFormats(String _format){
		logToStdOutFormat=_format;
		logToFileFormat=_format;
		logToDbFormat=_format;
	}

	/**
	 * Set format for messages of all log ways
	 * 
	 * %l	message level(String)
	 * %L	message level(Numerical)
	 * %g	Message
	 * 
	 * %D	Day
	 * %M	Month
	 * %Y	Year
	 * 
	 * %h	Hour in 24h format
	 * %m	Minute
	 * %s	Seconds
	 * %S	Milliseconds
	 * 
	 * %F	File name
	 * %T	Method name
	 * 
	 * %I	Local IP/hostname
	 * 
	 * @param String _format	
	 */
	public void setLogToStdOutFormat(String _format){
		logToStdOutFormat=_format;
	}

	/**
	 * set the format of the messages for the log in file
	 * 
	 * %l	message level(String)
	 * %L	message level(Numerical)
	 * %g	Message
	 * 
	 * %D	Day
	 * %M	Month
	 * %Y	Year
	 * 
	 * %h	Hour in 24h format
	 * %m	Minute
	 * %s	Seconds
	 * %S	Milliseconds
	 * 
	 * %F	File name
	 * %T	Method name
	 * 
	 * %I	Local IP/hostname
	 * 
	 * @param String _format	
	 */
	public void setlogToFileFormat(String _format){
		logToFileFormat=_format;
	}

	/**
	 * set the format of the messages for the log in the database
	 * 
	 * %l	message level(String)
	 * %L	message level(Numerical)
	 * %g	Message
	 * 
	 * %D	Day
	 * %M	Month
	 * %Y	Year
	 * 
	 * %h	Hour in 24h format
	 * %m	Minute
	 * %s	Seconds
	 * %S	Milliseconds
	 * 
	 * %F	File name
	 * %T	Method name
	 * 
	 * %I	Local IP/hostname
	 * 
	 * @param String _format	
	 */
	public void setlogToDbFormat(String _format){
		logToDbFormat=_format;
	}

	/*
	 * Compone il messaggio e lo scrive sui supporti
	 */
	public void write(String _msg, int _level){

		//data e ora
		Calendar cal = new GregorianCalendar();
		int ms = cal.get(Calendar.MILLISECOND);

		int D = cal.get(Calendar.DAY_OF_MONTH);
		int M = cal.get(Calendar.MONTH);
		int Y = cal.get(Calendar.YEAR);

		int h = cal.get(Calendar.HOUR_OF_DAY);
		int m = cal.get(Calendar.MINUTE);
		int s = cal.get(Calendar.SECOND);


		String out,strLevel;

		if(_level==Logger.DEBUG){
			strLevel="DEBUG";
		}
		else if(_level==Logger.INFO){
			strLevel="INFO";
		}
		else if(_level==Logger.WORNING){
			strLevel="WORNING";
		}
		else if(_level==Logger.ERROR){
			strLevel="ERROR";
		}
		else if(_level==Logger.DATA){
			strLevel="DATA";
		}
		else{
			strLevel="UNKNOW("+_level+")";
		}

		//dati sul chiamante(metodo, classe, numero linea, file)
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		String showSrcFileName=stacktrace[3].getFileName()+"("+stacktrace[3].getLineNumber()+")";
		String showSrcMethodName=stacktrace[3].getClassName()+"."+stacktrace[3].getMethodName()+"()";

		//scrittura sullo stdout
		if(logToStdOut && (_level <= logToStdOutLvl)){
			out=logToStdOutFormat;
			out=out.replace("%l",strLevel);
			out=out.replace("%L",String.valueOf(_level));
			out=out.replace("%g",_msg);
			out=out.replace("%D",String.valueOf(D));
			out=out.replace("%M",String.valueOf(M));
			out=out.replace("%Y",String.valueOf(Y));
			out=out.replace("%h",String.valueOf(h));
			out=out.replace("%m",String.valueOf(m));
			out=out.replace("%s",String.valueOf(s));
			out=out.replace("%S",String.valueOf(ms));
			out=out.replace("%F",showSrcFileName);
			out=out.replace("%T",showSrcMethodName);
			out=out.replace("%I",this.IP);
			writeToStdOut(out,_level);
		} 

		//scrittura su file
		if(logToFile && _level <= logToFileLvl){
			out=logToFileFormat;
			out=out.replace("%l",strLevel);
			out=out.replace("%L",String.valueOf(_level));
			out=out.replace("%g",_msg);
			out=out.replace("%D",String.valueOf(D));
			out=out.replace("%M",String.valueOf(M));
			out=out.replace("%Y",String.valueOf(Y));
			out=out.replace("%h",String.valueOf(h));
			out=out.replace("%m",String.valueOf(m));
			out=out.replace("%s",String.valueOf(s));
			out=out.replace("%S",String.valueOf(ms));
			out=out.replace("%F",showSrcFileName);
			out=out.replace("%T",showSrcMethodName);
			out=out.replace("%I",this.IP);
			writeToFile(out);
		} 

		if(logToSyslog && _level <= logToSyslogLvl){
			try {
				sysLog.write(_msg, _level);
			} catch (SyslogException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * scrive sullo stdout
	 */
	protected void writeToStdOut(String _msg, int _level){
		if(_level == Logger.ERROR){
			System.err.println(_msg);
		}else{
			System.out.println(_msg);
		}
	}

	/*
	 * scrive su file
	 */
	protected void writeToFile(String _msg){
		try{
			FileOutputStream os = new FileOutputStream(logFile,true);
			PrintStream ps = new PrintStream(os);
			ps.println(_msg);
			ps.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/*
	 * alias di write
	 */
	//standard string
	public void debug(String _msg){write(_msg, Logger.DEBUG);}
	public void info(String _msg){write(_msg, Logger.INFO);}
	public void worn(String _msg){write(_msg, Logger.WORNING);}
	public void error(String _msg){write(_msg, Logger.ERROR);}

	//altri string
	public void data(String _msg){write(_msg, Logger.DATA);} //dati in risposta ad una richiesta

	//stadard long
	public void debug(long _msg){write(Long.toString(_msg), Logger.DEBUG);}
	public void info(long _msg){write(Long.toString(_msg), Logger.INFO);}
	public void worn(long _msg){write(Long.toString(_msg), Logger.WORNING);}
	public void error(long _msg){write(Long.toString(_msg), Logger.ERROR);}

	//altri long
	public void data(long _msg){write(Long.toString(_msg), Logger.DATA);}

	//stadard float
	public void debug(float _msg){write(Float.toString(_msg), Logger.DEBUG);}
	public void info(float _msg){write(Float.toString(_msg), Logger.INFO);}
	public void worn(float _msg){write(Float.toString(_msg), Logger.WORNING);}
	public void error(float _msg){write(Float.toString(_msg), Logger.ERROR);}

	//altri float
	public void data(float _msg){write(Float.toString(_msg), Logger.DATA);}

}
