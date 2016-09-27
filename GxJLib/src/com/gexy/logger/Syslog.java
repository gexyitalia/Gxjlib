package com.gexy.logger;

import java.io.*;
import java.net.*;

import com.gexy.logger.exception.SyslogException;


public class Syslog
{
	/**
	 * Priorities: system is unusable
	 */
	public static final int PRIORITIES_EMERG	= 0; 
	/**
	 * Priorities: ction must be taken immediately
	 */
	public static final int PRIORITIES_ALERT	= 1; 
	/**
	 * Priorities: critical conditions
	 */
	public static final int PRIORITIES_CRIT	= 2; 
	/**
	 * Priorities: error conditions
	 */
	public static final int PRIORITIES_ERR	= 3;
	/**
	 * Priorities: warning conditions
	 */
	public static final int PRIORITIES_WARNING	= 4;
	/**
	 * Priorities: normal but significant condition
	 */
	public static final int LPRIORITIES_NOTICE	= 5; 
	/**
	 * Priorities: informational
	 */
	public static final int PRIORITIES_INFO	= 6; 
	/**
	 * Priorities: debug-level messages
	 */
	public static final int PRIORITIES_DEBUG	= 7; 
	/**
	 * Priorities: mask to extract priority
	 */
	public static final int PRIORITIES_PRIMASK = 0x0007; 

	/**
	 * Facilities: kernel messages
	 */
	public static final int FACILITIES_KERN	= (0<<3); 
	/**
	 * Facilities: random user-level messages
	 */
	public static final int FACILITIES_USER	= (1<<3); 
	/**
	 * Facilities: mail system
	 */
	public static final int FACILITIES_MAIL	= (2<<3); 
	/**
	 * Facilities: system daemons
	 */
	public static final int FACILITIES_DAEMON	= (3<<3); 
	/**
	 * Facilities: security/authorization
	 */
	public static final int FACILITIES_AUTH	= (4<<3); 
	/**
	 * Facilities: internal syslogd use
	 */
	public static final int FACILITIES_SYSLOG	= (5<<3); 
	/**
	 * Facilities: line printer subsystem
	 */
	public static final int FACILITIES_LPR		= (6<<3); 
	/**
	 * Facilities: network news subsystem
	 */
	public static final int FACILITIES_NEWS	= (7<<3); 
	/**
	 * Facilities: UUCP subsystem
	 */
	public static final int FACILITIES_UUCP	= (8<<3); 
	/**
	 * Facilities: clock daemon
	 */
	public static final int FACILITIES_CRON	= (15<<3); 
	/**
	 * Facilities: reserved for local use
	 */
	public static final int FACILITIES_LOCAL0	= (16<<3); 
	/**
	 * Facilities: reserved for local use
	 */
	public static final int FACILITIES_LOCAL1	= (17<<3); 
	/**
	 * Facilities: reserved for local use
	 */
	public static final int FACILITIES_LOCAL2	= (18<<3); 
	/**
	 * Facilities: reserved for local use
	 */
	public static final int FACILITIES_LOCAL3	= (19<<3); 
	/**
	 * Facilities: reserved for local use
	 */
	public static final int FACILITIES_LOCAL4	= (20<<3); 
	/**
	 * Facilities: reserved for local use
	 */
	public static final int FACILITIES_LOCAL5	= (21<<3); 
	/**
	 * Facilities: reserved for local use
	 */
	public static final int FACILITIES_LOCAL6	= (22<<3); 
	/**
	 * Facilities: reserved for local use
	 */
	public static final int FACILITIES_LOCAL7	= (23<<3); 

	/**
	 * mask to extract facility
	 */
	public static final int LOG_FACMASK	= 0x03F8; 

	/**
	 * Opt:  PRIORITIES the pid with each message
	 */
	public static final int OPT_PID	= 0x01; // 
	/**
	 * Opt:  log on the console if errors
	 */
	public static final int OPT_CONS	= 0x02; // 
	/**
	 * Opt:  don't delay open
	 */
	public static final int OPT_NDELAY	= 0x08; // 
	/**
	 * Opt:  don't wait for console forks
	 */
	public static final int OPT_NOWAIT	= 0x10; // 





	private String		ident;
	private int			logopt;
	private int			facility;

	private int port;
	private InetAddress		address;
	private DatagramPacket	packet;
	private DatagramSocket	socket;


	public Syslog( String ident, int logopt, int facility, int _port, String _address ) throws SyslogException, UnknownHostException
	{
		this.port = _port;
		address = InetAddress.getByName(_address);
		if ( ident == null )
			this.ident = new String( Thread.currentThread().getName() );
		else
			this.ident = ident;
		this.logopt = logopt;
		this.facility = facility;

		try
		{
			socket = new DatagramSocket();
		}
		catch ( SocketException e )
		{
			throw new SyslogException( 
					"error creating syslog udp socket: " + e.getMessage() );
		}
	}


	public void write( String msg,int priority ) throws SyslogException
	{
		int		pricode;
		int		length;
		int		idx, sidx, nidx;
		StringBuffer	buffer;
		byte[]		data;
		byte[]		numbuf = new byte[32];
		String		strObj;

		pricode = MakePriorityCode( facility, priority );
		Integer priObj = new Integer( pricode );

		length = 4 + ident.length() + msg.length() + 1;
		length += ( pricode > 99 ) ? 3 : ( ( pricode > 9 ) ? 2 : 1 );

		data = new byte[length];

		idx = 0;
		data[idx++] = '<';

		strObj = priObj.toString( priObj.intValue() );
		strObj.getBytes( 0, strObj.length(), data, idx );
		idx += strObj.length();

		data[idx++] = '>';

		ident.getBytes( 0, ident.length(), data, idx );
		idx += ident.length();

		data[idx++] = ':';
		data[idx++] = ' ';

		msg.getBytes( 0, msg.length(), data, idx );
		idx += msg.length();

		data[idx] = 0;

		packet = new DatagramPacket( data, length, address, port );

		try
		{
			socket.send( packet );
		}
		catch ( IOException e )
		{
			throw new SyslogException(
					"error sending message: '" + e.getMessage() + "'" );
		}
	}

	private int MakePriorityCode( int facility, int priority )
	{
		return ( ( facility & LOG_FACMASK ) | priority );
	}


	/******************************************************************************
	 /// Test routine.
	 public static void main( String args[] )
		{
		try
		    {
		    Syslog s = new Syslog( null, 0, LOG_LOCAL1 );
		    s.syslog( Syslog.LOG_ERR, "Hello." );
		    }
		catch ( SyslogException e )
		    {
		    System.err.println( e.toString() );
		    }
		}
	 ******************************************************************************/

}


