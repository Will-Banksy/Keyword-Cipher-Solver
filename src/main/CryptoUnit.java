package main;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CryptoUnit extends JPanel
{
	private static final long serialVersionUID = 6539788295550882565L;
	
	public boolean showSelected;
	public boolean showSelectedBad;
	public char ch;
	public JTextField input;
	Main main;
	
	public CryptoUnit(char enciphered, Main main)
	{
		ch = enciphered;
		this.main = main;
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		input = new JTextField() {
			private static final long serialVersionUID = -2924418574624912384L;

			@Override public void paint(Graphics g) {
				paintComponent(g);
			}
			
			@Override public void paintComponent(Graphics g) {
//				super.paintComponent(g);
				String txt = input.getText();
				boolean focused = input.isFocusOwner();
				
				Graphics2D g2d = (Graphics2D)g;
				
				g2d.setBackground(Color.WHITE);
				g2d.clearRect(0, 0, getWidth(), getHeight());
				
				g2d.setColor(new Color(120, 136, 151));
				g2d.setStroke(new BasicStroke(1));
				g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
				
				if(focused) {
					g2d.drawLine(6, getHeight() - 5, getWidth() - 7, getHeight() - 5);
				}
				
				g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				
				g2d.setColor(new Color(51, 51, 51));
				drawCentredString(g2d, txt, new Rectangle(0, 0, getWidth() - 1, getHeight() - 1), getFont());
			}
		};
		
		JLabel encipheredLetter = new JLabel("" + enciphered);
		encipheredLetter.setHorizontalTextPosition(encipheredLetter.getWidth() / 2);
		
		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());
		container.setPreferredSize(new Dimension(30, 40));
		
		container.add(input, BorderLayout.CENTER);
		container.add(encipheredLetter, BorderLayout.SOUTH);
		
		c.insets = new Insets(5, 5, 5, 5);
		add(container, c);
		
		if(!Character.isLetter(ch))
		{
			input.setEditable(false);
		}
	}
	
	@Override public void paintComponent(Graphics g)
	{
		Character displayValue = main.charMap.get(ch);
		boolean setText = false;
		if(input.getText().isEmpty()) {
			setText = true;
		} else if(displayValue != input.getText().charAt(0)) {
			setText = true;
		}
		if(setText && displayValue != null) {
			input.setText(String.valueOf(displayValue.charValue()));
			input.repaint();
		}
		
		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D)g;
		if(showSelected)
		{
			g2d.setColor(new Color(126, 185, 244));
			g2d.setStroke(new BasicStroke(5));
			g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		}
		else if(showSelectedBad)
		{
			g2d.setColor(new Color(244, 126, 126));
			g2d.setStroke(new BasicStroke(5));
			g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
		}
		else
		{
			g2d.setBackground(Color.WHITE);
			g2d.clearRect(0, 0, getWidth(), getHeight());
		}
	}
	
	public static void drawCentredString(Graphics g, String text, Rectangle rect, Font font) {
	    // Get the FontMetrics
	    FontMetrics metrics = g.getFontMetrics(font);
	    // Determine the X coordinate for the text
	    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
	    // Determine the Y coordinate for the text (note we add the ascent, as in java 2d 0 is top of the screen)
	    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
	    // Set the font
	    g.setFont(font);
	    // Draw the String
	    g.drawString(text, x, y);
	}
}
