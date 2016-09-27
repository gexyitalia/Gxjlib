package com.gexy.gui.window;

import com.gexy.gui.window.component.GxWinTitleBar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.gexy.config.Config;

import com.gexy.logger.Logger;

public class GxWindow extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2916912301306509403L;

	protected static Logger log;
	protected static Config conf;

	public Font font; //font

	protected JPanel container; //contenitore globale
	GxWinTitleBar winTitleBar; //titolo e controlli finestra

	
	/**
	 * Create a gexy style window, all other object must be add in object named container
	 * @param _conf				Config
	 * @param _title			String
	 * @param _minimizeable		Boolean
	 * @param _maximizeable		Boolean
	 * @param _closeable		Boolean
	 * @param _dragable			Boolean
	 */
	public GxWindow(Config _conf,String _title,boolean _minimizeable, boolean _maximizeable, boolean _closeable, boolean _dragable){
		super(_title);
		
//		log = _conf.getLogger();
		conf = _conf;

		//importo font
		Font ttfBase = null;
		font = null;
		InputStream myStream = null;
		try {
			myStream = new BufferedInputStream(new FileInputStream("font/OpenSans-CondLight.ttf"));
			ttfBase = Font.createFont(Font.TRUETYPE_FONT, myStream);
			font = ttfBase.deriveFont(Font.PLAIN, 24); 
			setFont(font);
		} catch (Exception e) {
			log.worn("Font not loaded: "+e.getMessage());
		}

		setUndecorated(true);
		getContentPane().setBackground(Color.WHITE);
		((JComponent) getContentPane()).setBorder(BorderFactory.createLineBorder(Color.GRAY)); 

		//title bar
		winTitleBar = new GxWinTitleBar(this,_title,_minimizeable,_maximizeable,_closeable,_dragable);
		getContentPane().add(winTitleBar,BorderLayout.PAGE_START);

		//contenitore globale
		container = new JPanel(new BorderLayout());
		container.setOpaque(false);
		//		wc.setSize(getWidth()-20, getHeight());
		//		wc.setLocation(5, 5);
		getContentPane().add(container,BorderLayout.CENTER);
	}
	
	/**
	 * Setta il frame al centro dello schermo
	 */
	public void setPositionCenter(){
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screen.width - 500) / 2,(screen.height - 300)/2);
	}
	
	/**
	 * Restituisce il contenitore(JPanel) principale
	 * @return JPanel
	 */
	public JPanel getContainer(){
		return container;
	}
}
