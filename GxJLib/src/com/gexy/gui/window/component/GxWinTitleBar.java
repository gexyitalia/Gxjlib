package com.gexy.gui.window.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GxWinTitleBar extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5372523962930188309L;
	private Point initialClick;
	private JFrame parent; //JFrame parente di questa titlebar
	protected JLabel winTitle,winMin,winMax,winClose; //bottoni barra del titolo e titolo
	protected JPanel winCtrlSx,winCtrlDx; //contenitori bottoni destra e sinistra del titolo
	protected boolean dragable; //se il drag è permesso


	/**
	 * Create a JPanel Object used as title of window.
	 * 
	 * @param _parent		JFrame		JFrame parent of this titleBar
	 * @param _title		String 		Titile of window
	 * @param _minimizeable
	 * @param _maximizeable
	 * @param _closeable
	 * @param _dragable
	 */
	public GxWinTitleBar(final JFrame _parent, String _title, boolean _minimizeable, boolean _maximizeable, boolean _closeable, boolean _dragable){
		this.parent = _parent;

		setOpaque(false);
		setPreferredSize(new Dimension(parent.getWidth(), 35));
		setLayout(new BorderLayout());

		dragable=_dragable;

		//titolo finestra
		winTitle = new JLabel(_title,SwingConstants.CENTER);
		winTitle.setOpaque(false);
		winTitle.setFont(parent.getFont().deriveFont(Font.PLAIN, 15f));
		add(winTitle, BorderLayout.CENTER);

		//controlli Sx finestra
		JPanel winCtrlSx = new JPanel();
		winCtrlSx.setOpaque(false);
		winCtrlSx.setBackground(parent.getBackground());
		add(winCtrlSx,BorderLayout.LINE_START);
		
		//controlli dx finestra
		JPanel winCtrlDx = new JPanel();
		winCtrlDx.setOpaque(false);
		winCtrlDx.setBackground(parent.getBackground());
		add(winCtrlDx,BorderLayout.LINE_END);

		

		//bottone minimze
		winMin = new JLabel();
		winMin.setOpaque(false);
//		winMin.setPreferredSize(new Dimension(20,20));
		ImageIcon winMinIcon = new ImageIcon("winmin.png");
		winMin.setIcon(winMinIcon);
		winCtrlDx.add(winMin, BorderLayout.EAST);
		if(_minimizeable){winMin.setVisible(true);}else{winMin.setVisible(false);}
		
		//bottone maximize
		winMax = new JLabel();
		winMax.setOpaque(false);
//		winMax.setPreferredSize(new Dimension(20,20));
		ImageIcon winMaxIcon = new ImageIcon("winmax.png");
		winMax.setIcon(winMaxIcon);
		winCtrlDx.add(winMax, BorderLayout.EAST);
		if(_maximizeable){winMax.setVisible(true);}else{winMax.setVisible(false);}

		//bottone close
		winClose = new JLabel();
		winClose.setOpaque(false);
//		winClose.setPreferredSize(new Dimension(20,20));
		ImageIcon winCloseIcon = new ImageIcon("winclose.png");
		winClose.setIcon(winCloseIcon);
		winCtrlDx.add(winClose, BorderLayout.EAST);
		if(_closeable){winClose.setVisible(true);}else{winClose.setVisible(false);}

		//setto grandezza del winCrtlSx come winCrtlDx per centrare winTitle
		winCtrlSx.setPreferredSize(winCtrlDx.getPreferredSize());

		//drag window
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				initialClick = e.getPoint();
				getComponentAt(initialClick);
			}
		});


		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {

				if(dragable){
					// get location of Window
					int thisX = parent.getLocation().x;
					int thisY = parent.getLocation().y;

					// Determine how much the mouse moved since the initial click
					int xMoved = (thisX + e.getX()) - (thisX + initialClick.x);
					int yMoved = (thisY + e.getY()) - (thisY + initialClick.y);

					// Move window to this position
					int X = thisX + xMoved;
					int Y = thisY + yMoved;
					parent.setLocation(X, Y);
				}
			}
		});


	}

	/**
	 * Set text of title
	 * @param _value	boolean
	 */
	public void setText(String _text){
		winTitle.setText(_text);
	}
	/**
	 * Return text of title
	 * @param _value	boolean
	 */
	public String getText(){
		return winTitle.getText();
	}
}
