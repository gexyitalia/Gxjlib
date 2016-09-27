package com.gexy.gui.window.component;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class GxButton extends JButton{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4023629684190126323L;

	protected Font font;
	protected Color BgColor,BgColorPressed,BgColorRollover;

	public GxButton(String _text, Font _font){
		super();
		font=_font;

		setFont(font.deriveFont(Font.PLAIN, 15f));
		setBackground(Color.WHITE);
		setFocusPainted(false);
		borderEnable();
//		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setContentAreaFilled(false);


		setText(_text);
	}
	
	/**
	 * Create border 1px gray and make it visible
	 */
	public void borderEnable(){
		setBorderPainted(true);
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
	}
	
	/**
	 * make border unvisible
	 */
	public void borderDisable(){
		setBorderPainted(false);
	}
//	//grafica
//	@Override
//	protected void paintComponent(Graphics g) {
//		g.setColor(getBackground());	
//		g.fillRect(0, 0, getWidth(), getHeight());
//		super.paintComponent(g);
//
//
//	}
}