package main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Frame extends JFrame
{
	private static final long serialVersionUID = 1418976186596179265L;
	
	JPanel cryptoArea;
	JDialog inputDiag;

	public Frame() throws HeadlessException {
		super();
	}

	public Frame(GraphicsConfiguration gc) {
		super(gc);
	}

	public Frame(String title, GraphicsConfiguration gc) {
		super(title, gc);
	}

	public Frame(String title) throws HeadlessException {
		super(title);
	}
	
	public void setup(Main main) {
		setTitle("Cryptogram");
		setSize(400, 400);
		setDefaultCloseOperation(Frame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(400, 400));
		
		setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.insets = new Insets(5, 5, 5, 5);
		
		JButton setCiphertextBtn = new JButton("Set Cipher Text");
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		add(setCiphertextBtn, c);
		
		cryptoArea = new JPanel();
		cryptoArea.setLayout(new BorderLayout());
		c.gridy = 1;
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(0, 5, 5, 5);
		add(cryptoArea, c);
		
		c.gridwidth = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.weighty = 0;
		
		JButton getSolvedText = new JButton("Get Inputted Text");
		c.gridx = 0;
		c.gridy = 2;
		add(getSolvedText, c);
		
		JButton showAlphabetEditorDialog = new JButton("Show Alphabet");
		c.gridx = 1;
		c.gridy = 2;
		c.insets = new Insets(0, 0, 5, 5);
		add(showAlphabetEditorDialog, c);
		
		setCiphertextBtn.addActionListener((actionEvent) -> {
			showInputDialog(main);
		});
		
		getSolvedText.addActionListener((actionEvent) -> {
			if(main.units == null)
				return;
			
			StringBuilder sb = new StringBuilder();
			for(CryptoUnit u : main.units) {
				if(Character.isUpperCase(u.ch)) {
					sb.append(u.input.getText().toUpperCase());
				} else {
					sb.append(u.input.getText().toLowerCase());
				}
				if(!Character.isLetter(u.ch)) {
					sb.append(u.ch);
				}
			}
			main.showOutputDialog(main.frame, sb.toString());
		});
		
		showAlphabetEditorDialog.addActionListener((actionEvent) -> { // Use lambda - Shorter syntax
			if(main.units == null)
				return;
			
			main.showAlphabetEditorDialog(main.frame, main.units, false);
		});
	}
	
	public void showInputDialog(Main main) {
		if(inputDiag == null) {
			inputDiag = new JDialog(this);
			inputDiag.setTitle("Enter Text");
			inputDiag.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
			inputDiag.setSize(400, 400);
			inputDiag.setMinimumSize(new Dimension(400, 400));
			
			inputDiag.setLayout(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.insets = new Insets(5, 5, 5, 5);
			
			JTextArea txtArea = new JTextArea();
			c.gridx = 0;
			c.gridy = 0;
			c.gridwidth = 2;
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1;
			c.weighty = 1;
			inputDiag.add(txtArea, c);
			
			JButton cancel = new JButton("Cancel");
			c.gridy = 1;
			c.gridwidth = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 0.5;
			c.weighty = 0;
			c.insets = new Insets(0, 5, 5, 5);
			inputDiag.add(cancel, c);
			
			JButton submit = new JButton("Submit");
			c.gridx = 1;
			inputDiag.add(submit, c);
			
			cancel.addActionListener((actionEvent) -> {
				inputDiag.setVisible(false);
			});
			
			submit.addActionListener((actionEvent) -> {
				main.units = main.createAndAddUnits(main.frame, txtArea.getText());
				inputDiag.setVisible(false);
				main.frame.pack();
				main.frame.setLocationRelativeTo(null);
				main.frame.revalidate();
				main.showAlphabetEditorDialog(main.frame, main.units, true);
			});
		} else {
//			for(Component comp : inputDiag.getComponents()) {
//				if(comp instanceof JTextArea) {
//					JTextArea area = (JTextArea)comp;
//					area.setText("");
//				}
//			}
		}

		inputDiag.setLocationRelativeTo(this);
		inputDiag.setVisible(true);
	}
}
