package com.gexy.gui.window.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JTextField;

public class GxTextField extends JTextField{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4598939318726651630L;
	protected Font font;
	protected Color BgColor,BgColorPressed,BgColorRollover;
	protected String placeholder;

	public GxTextField(String _text, Font _font){
		super();
		font=_font;

		setFont(font.deriveFont(Font.PLAIN, 15f));
		setBackground(Color.WHITE);
		//		setCursor(new Cursor(Cursor.HAND_CURSOR));


		setText(_text);
	}

	/**
	 * Setta il testo placeholder
	 * @param _string
	 */
	public void setPlaceholder(final String _string){
		placeholder = _string;
	}
	
	/**
	 * Ritorna il testo placeholder
	 * @return String
	 */
	public String getPlaceholder() {
		return placeholder;
	}

	@Override
	protected void paintComponent(final Graphics pG) {
		super.paintComponent(pG);

		if (placeholder.length() == 0 || getText().length() > 0) {
			return;
		}

		final Graphics2D g = (Graphics2D) pG;
		g.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(getDisabledTextColor());
		g.drawString(placeholder, getInsets().left, pG.getFontMetrics()
				.getMaxAscent() + getInsets().top);
	}

}
