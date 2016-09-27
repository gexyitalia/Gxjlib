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

public class GxButtonURL extends JButton{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4023629684190126323L;

	protected Font font;
	protected Color BgColor,BgColorPressed,BgColorRollover;

	/**
	 * Create a button like link, without border and show text 
	 * underline when mouse rollover
	 * @param _text
	 * @param _font
	 */
	public GxButtonURL(String _text, Font _font){
		super();
		font=_font;

		setFont(font.deriveFont(Font.PLAIN, 15f));
		setBackground(Color.WHITE);
		setFocusPainted(false);
		setBorderPainted(false);
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		setContentAreaFilled(false);


		setText(_text);

		addMouseListener(new MouseAdapter()
		{
			Font original;

			public void mouseEntered(MouseEvent e)
			{
				original = e.getComponent().getFont();
				Map attributes = original.getAttributes();
				attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
				e.getComponent().setFont(original.deriveFont(attributes));
			}
			public void mouseExited(MouseEvent e)
			{
				e.getComponent().setFont(original);
			}

		});
		
		



	}
	
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