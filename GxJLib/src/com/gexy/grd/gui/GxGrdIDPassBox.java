package com.gexy.grd.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GxGrdIDPassBox extends JPanel {
	private static final long serialVersionUID = -8385383745120690885L;

	
	public GxGrdIDPassBox(){

		setLayout(new BorderLayout());
		setMaximumSize(new Dimension(350, 80));
		/*
		 * riga ID utente
		 */
		JPanel UserIdContainer = new JPanel();
		add(UserIdContainer,BorderLayout.NORTH);
		
		JPanel lblUserIdContainer = new JPanel();
		lblUserIdContainer.setBackground(new Color(0,152,212));
		lblUserIdContainer.setPreferredSize(new Dimension(150,30));
		UserIdContainer.add(lblUserIdContainer);
		
		JLabel lblUserId = new JLabel("Il tuo ID"); 
		lblUserId.setForeground(Color.WHITE);
		lblUserIdContainer.add(lblUserId);
		
		JLabel userId = new JLabel("746 223 234"); 
		UserIdContainer.add(userId);

		/*
		 * Riga password utente
		 */
		JPanel UserPassContainer = new JPanel();
		add(UserPassContainer,BorderLayout.SOUTH);
		
		JPanel lblUserPassContainer = new JPanel();
		lblUserPassContainer.setBackground(new Color(0,152,212));
		lblUserPassContainer.setPreferredSize(new Dimension(150,30));
		UserPassContainer.add(lblUserPassContainer);
		
		JLabel lblUserPass = new JLabel("Password"); 
		lblUserPass.setForeground(Color.WHITE);
		lblUserPassContainer.add(lblUserPass);
		
		JLabel userPass = new JLabel("746 223"); 
		UserPassContainer.add(userPass);
	}
	
	
	
}
