package main;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CryptoUnit extends JPanel
{
	private static final long serialVersionUID = 6539788295550882565L;
	
	public boolean showSelected;
	public char ch;
	public JTextField input;
	Main main;
	
	public CryptoUnit(char enciphered, Main main)
	{
		ch = enciphered;
		this.main = main;
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		input = new JTextField();
		
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
		
		Graphics2D g2d = (Graphics2D)g;
		if(showSelected)
		{
			g2d.setColor(Color.BLUE);
			g2d.setStroke(new BasicStroke(5));
			g2d.drawRect(0, 0, getWidth(), getHeight());
		}
		else
		{
			g2d.setBackground(Color.WHITE);
			g2d.clearRect(0, 0, getWidth(), getHeight());
		}
	}
}
