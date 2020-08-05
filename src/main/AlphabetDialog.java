package main;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import main.Main.NthCommon;

public class AlphabetDialog extends JDialog {
	private static final long serialVersionUID = -5494023807014386701L;
	
	ArrayList<CryptoUnit> alphabetUnits = null;
	Main.NthCommon nthCommon;
	JLabel nthCommonCounts;
	JPanel unitContainer;
	ArrayList<JLabel> letterCounts = null;
	JDialog outputDiag = null;
	JTextArea outputTextArea = null;
	
	private static class CharInString {
		int index;
		char ch;
		
		public CharInString(int i, char c) {
			index = i;
			ch = c;
		}
		
		@Override public boolean equals(Object o) {
			if(o instanceof CharInString) {
				CharInString cis = (CharInString)o;
				if(index == cis.index && ch == cis.ch) {
					return true;
				}
			}
			return false;
		}
		
		public static ArrayList<CharInString> fromString(String str) {
			ArrayList<CharInString> list = new ArrayList<CharInString>();
			for(int i = 0; i < str.length(); i++) {
				list.add(new CharInString(i, str.charAt(i)));
			}
			return list;
		}
	}
	
	public AlphabetDialog(Frame owner) {
		super(owner);
	}
	
	public void setup(Main main, ArrayList<CryptoUnit> list) {
		setTitle("Alphabet");
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setSize(400, 400);
		setMinimumSize(new Dimension(400, 400));
		setLayout(new GridBagLayout());
		
		alphabetUnits = new ArrayList<CryptoUnit>();
		
		letterCounts = new ArrayList<JLabel>();
		
		unitContainer = new JPanel();
		unitContainer.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.insets = new Insets(5, 5, 5, 5);
		c.gridy = 0;
		
		for(int j = 0; j < Main.ALPHABET.length(); j++) {
			c.gridx = j;
			c.gridy = 0;
			CryptoUnit unit = new CryptoUnit(Main.ALPHABET.charAt(j), main);
			unit.input.addFocusListener(new FocusListener() {
				@Override public void focusGained(FocusEvent arg0) {
					main.doFocusThings(unit, true, false, (char)0);
//					unit.showSelected = true;
//					unit.repaint();
//					for(CryptoUnit u : list) {
//						// If the two units cipher (immutable) character is the same
//						if(Character.toLowerCase(u.ch) == Character.toLowerCase(unit.ch)) //(u.ch + "").toLowerCase().contentEquals((unit.ch + "").toLowerCase()))
//						{
//							u.showSelected = true;
//							u.repaint();
//						}
//						else
//						{
//							u.showSelected = false;
//							u.repaint();
//						}
//						// if the two units input character is the same
//						if(u.input.getText().equalsIgnoreCase(unit.input.getText()) && !Main.stringUnimportant(u.input.getText()))
//						{
//							// And their cipher character is different
//							if(Character.toLowerCase(u.ch) != Character.toLowerCase(unit.ch))
//							{
//								u.showSelectedBad = true;
//								u.repaint();
//							}
//							else
//							{
//								u.showSelectedBad = false;
//								u.repaint();
//							}
//						}
//					}
//					if(alphabetUnits != null) {
//						for(CryptoUnit u : alphabetUnits) {
//							// If the two units cipher (immutable) character is the same
//							if(Character.toLowerCase(u.ch) == Character.toLowerCase(unit.ch)) //(u.ch + "").toLowerCase().contentEquals((unit.ch + "").toLowerCase()))
//							{
//								u.showSelected = true;
//								u.repaint();
//							}
//							else
//							{
//								u.showSelected = false;
//								u.repaint();
//							}
//							// if the two units input character is the same
//							if(u.input.getText().equalsIgnoreCase(unit.input.getText()) && !Main.stringUnimportant(u.input.getText()))
//							{
//								// And their cipher character is different
//								if(Character.toLowerCase(u.ch) != Character.toLowerCase(unit.ch))
//								{
//									u.showSelectedBad = true;
//									u.repaint();
//								}
//								else
//								{
//									u.showSelectedBad = false;
//									u.repaint();
//								}
//							}
//						}
//					}
				}

				@Override public void focusLost(FocusEvent arg0) {
					main.doFocusThings(unit, false, false, (char)0);
//					for(CryptoUnit u : list) {
//						u.showSelected = false;
//						u.repaint();
//					}
//					if(alphabetUnits != null) {
//						for(CryptoUnit u : alphabetUnits) {
//							u.showSelected = false;
//							u.repaint();
//						}
//					}
				}
			});

			unit.input.setDocument(new JTextFieldLimit(1));
			unit.input.addKeyListener(new KeyAdapter()
			{
				@Override public void keyTyped(KeyEvent e)
				{
					main.doFocusThings(unit, true, true, e.getKeyChar());
					
					char ch = e.getKeyChar();
					main.charMap.put(unit.ch, ch);
					for(CryptoUnit u : list)
					{
						if(Character.toLowerCase(u.ch) == Character.toLowerCase(unit.ch))
						{
							u.repaint();
						}
					}
					for(CryptoUnit u : alphabetUnits)
					{
						if(Character.toLowerCase(u.ch) == Character.toLowerCase(unit.ch))
						{
							u.repaint();
						}
					}
				}
			});

			unitContainer.add(unit, c);
			alphabetUnits.add(unit);
			
			int counter = 0;
			for(int i = 0; i < list.size(); i++)
			{
				if(Character.toLowerCase(list.get(i).ch) == unit.ch)
				{
					counter++;
				}
			}
			
			JLabel letterCount = new JLabel(String.valueOf(counter));
			c.gridy = 1;
			unitContainer.add(letterCount, c);
			
			letterCount.setToolTipText("Letter Count");
			letterCounts.add(letterCount);
		}
		
		JScrollPane pane = new JScrollPane(unitContainer);

		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		add(pane, c);
		
		nthCommon = NthCommon.MOST_COMMON;
		
		String mcLetters = main.getMostCommonStrings(list, 1, nthCommon);
		String mcBigrams = main.getMostCommonStrings(list, 2, nthCommon);
		String mcTrigrams = main.getMostCommonStrings(list, 3, nthCommon);
		
		nthCommonCounts = new JLabel("Most Common Letters: " + mcLetters.toUpperCase() + "  Most Common Bigrams: " + mcBigrams.toUpperCase() + "  Most Common Trigrams: " + mcTrigrams.toUpperCase());
		
		nthCommonCounts.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent arg0)
			{
				switch(nthCommon)
				{
					case MOST_COMMON:
						nthCommon = NthCommon.SECOND_MOST_COMMON;
						String mcLetters_0 = main.getMostCommonStrings(main.units, 1, nthCommon);
						String mcBigrams_0 = main.getMostCommonStrings(main.units, 2, nthCommon);
						String mcTrigrams_0 = main.getMostCommonStrings(main.units, 3, nthCommon);
						nthCommonCounts.setText("Second Most Common Letters: " + mcLetters_0.toUpperCase() + "  Second Most Common Bigrams: " + mcBigrams_0.toUpperCase() + "  Second Most Common Trigrams: " + mcTrigrams_0.toUpperCase());
						nthCommonCounts.repaint();
						break;
						
					case SECOND_MOST_COMMON:
						nthCommon = NthCommon.THIRD_MOST_COMMON;
						String mcLetters_1 = main.getMostCommonStrings(main.units, 1, nthCommon);
						String mcBigrams_1 = main.getMostCommonStrings(main.units, 2, nthCommon);
						String mcTrigrams_1 = main.getMostCommonStrings(main.units, 3, nthCommon);
						nthCommonCounts.setText("Third Most Common Letters: " + mcLetters_1.toUpperCase() + "  Third Most Common Bigrams: " + mcBigrams_1.toUpperCase() + "  Third Most Common Trigrams: " + mcTrigrams_1.toUpperCase());
						nthCommonCounts.repaint();
						break;
						
					case THIRD_MOST_COMMON:
						nthCommon = NthCommon.MOST_COMMON;
						String mcLetters = main.getMostCommonStrings(main.units, 1, nthCommon);
						String mcBigrams = main.getMostCommonStrings(main.units, 2, nthCommon);
						String mcTrigrams = main.getMostCommonStrings(main.units, 3, nthCommon);
						nthCommonCounts.setText("Most Common Letters: " + mcLetters.toUpperCase() + "  Most Common Bigrams: " + mcBigrams.toUpperCase() + "  Most Common Trigrams: " + mcTrigrams.toUpperCase());
						nthCommonCounts.repaint();
						break;
				}
			}
		});
		
		c.gridy = 1;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weighty = 0;
		c.insets = new Insets(0, 5, 5, 5);
		add(nthCommonCounts, c);
		
		c.gridy = 2;
		c.gridwidth = 1;
		
		JButton getKeywords = new JButton("Get possible keywords");
		add(getKeywords, c);
		
		JButton autoFill = new JButton("Autofill");
		c.gridx = 1;
		c.insets = new Insets(0, 0, 5, 5);
		add(autoFill, c);
		
		getKeywords.addActionListener((actionEvent) -> {
			findKeywords(main);
//			ArrayList<String> possibleKeywords = new ArrayList<String>();
//			String cipherAlphabet;
//			StringBuilder sb = new StringBuilder();
//			for(CryptoUnit unit : alphabetUnits)
//			{
//				if((int)unit.input.getText().charAt(0) >= 32)
//					sb.append(unit.input.getText());
//			}
//			cipherAlphabet = sb.toString().toLowerCase();
//			
//			for(String str : Main.dictionary)
//			{
//				String alph = Main.GetCipherAlphabet(str, false);
//				if(cipherAlphabet.equals(alph))
//				{
//					possibleKeywords.add(str);
//				}
//			}
//			
//			sb = new StringBuilder();
//			for(String str : possibleKeywords) {
//				sb.append(str + "\n");
//			}
//			
//			//JOptionPane.showMessageDialog(main.frame, possibleKeywords);
//			showOutputDialog(main, sb.toString());
//			System.out.println("SB: " + sb.toString());
		});
		
		autoFill.addActionListener((actionEvent) -> {
			autoFill(main);
		});
		
		pack();
		setLocationRelativeTo(main.frame);
		setVisible(true);
	}
	
	public void reset(Main main, ArrayList<CryptoUnit> list, boolean forceRemake) {
		for(int j = 0; j < Main.ALPHABET.length(); j++) {
			int counter = 0;
			for(int i = 0; i < list.size(); i++)
			{
				if(Character.toLowerCase(list.get(i).ch) == alphabetUnits.get(j).ch)
				{
					counter++;
				}
			}
			
			letterCounts.get(j).setText(String.valueOf(counter));
			letterCounts.get(j).repaint();
		}
		
		nthCommon = NthCommon.MOST_COMMON;
		
		String mcLetters = main.getMostCommonStrings(list, 1, nthCommon);
		String mcBigrams = main.getMostCommonStrings(list, 2, nthCommon);
		String mcTrigrams = main.getMostCommonStrings(list, 3, nthCommon);
		
		nthCommonCounts.setText("Most Common Letters: " + mcLetters.toUpperCase() + "  Most Common Bigrams: " + mcBigrams.toUpperCase() + "  Most Common Trigrams: " + mcTrigrams.toUpperCase());
		nthCommonCounts.repaint();
		
		pack();
		setLocationRelativeTo(main.frame);
		setVisible(true);
	}
	
	public void showOutputDialog(Main main, String str) {
		if(outputDiag == null) {
			outputDiag = new JDialog(this);
			outputDiag.setTitle("Output");
			outputDiag.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
			outputDiag.setSize(400, 400);
			outputDiag.setMinimumSize(new Dimension(400, 400));
			
			outputDiag.setLayout(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
			
			outputTextArea = new JTextArea(str);
			c.gridx = 0;
			c.gridy = 0;
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1;
			c.weighty = 1;
			c.insets = new Insets(5, 5, 5, 5);
			
			JScrollPane scrollPane = new JScrollPane(outputTextArea);
			outputDiag.add(scrollPane, c);
			
			outputDiag.setLocationRelativeTo(this);
		} else {
			outputTextArea.setText(str);
		}

		outputDiag.repaint();
		outputDiag.setVisible(true);
	}
	
	public void autoFill(Main main) {
//		StringBuilder sb = new StringBuilder();
//		for(CryptoUnit unit : alphabetUnits) {
//			sb.append(unit.input.getText());
//		}
//		String cipAlph = Main.GetCipherAlphabet(sb.toString(), false);
		
		ArrayList<CharInString> alphabet = new ArrayList<CharInString>();
		for(int i = 0; i < alphabetUnits.size(); i++) {
			if(!alphabetUnits.get(i).input.getText().isBlank() && (int)alphabetUnits.get(i).input.getText().charAt(0) >= 32) {
				alphabet.add(new CharInString(i, alphabetUnits.get(i).input.getText().charAt(0))); // Get first char in textbox
			}
		}
		
//		System.out.println("AlphLen: " + alphabet.size() + ", alph[0]: " + (int)alphabet.get(0).ch);
		
		for(Map.Entry<String, String> entry : Main.cipherAlphabets.entrySet()) {
			ArrayList<CharInString> cipAlph = CharInString.fromString(entry.getValue()); // Get cipher alphabet for entry as arraylist of CharInStrings
			if(compareAgainst(alphabet, cipAlph)) { // If each textfield content in alphabetUnits is equal to what it would be in the new cipher alphabet
				for(int i = 0; i < Main.ALPHABET.length(); i++) {
					main.charMap.put(Main.ALPHABET.charAt(i), cipAlph.get(i).ch);
				}
				// Repaint all units
				main.frame.repaintUnits(main);
				repaintUnits(main);
				break; // Break out of looping over the cipherAlphabets - we've found a suitable candidate
			}
		}
	}
	
	public void findKeywords(Main main) {
		ArrayList<CharInString> alphabet = new ArrayList<CharInString>();
		for(int i = 0; i < alphabetUnits.size(); i++) {
			if(!alphabetUnits.get(i).input.getText().isBlank() && (int)alphabetUnits.get(i).input.getText().charAt(0) >= 32) {
				alphabet.add(new CharInString(i, alphabetUnits.get(i).input.getText().charAt(0))); // Get first char in textbox
			}
		}
		
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<String, String> entry : Main.cipherAlphabets.entrySet()) {
			ArrayList<CharInString> cipAlph = CharInString.fromString(entry.getValue()); // Get cipher alphabet for entry as arraylist of CharInStrings
			if(compareAgainst(alphabet, cipAlph)) { // If each textfield content in alphabetUnits is equal to what it would be in the new cipher alphabet
				sb.append(entry.getKey() + '\n'); // Add the keyword to the StringBuilder
			}
		}
		showOutputDialog(main, sb.toString());
	}
	
	/**
	 * Returns true if all the characters in chars match one in compAgainst
	 */
	public boolean compareAgainst(ArrayList<CharInString> chars, ArrayList<CharInString> compAgainst) {
		for(int i = 0; i < chars.size(); i++) {
			if(!compAgainst.contains(chars.get(i))) {
				return false;
			}
		}
		return true;
	}
	
	public void repaintUnits(Main main) {
		for(CryptoUnit unit : alphabetUnits) {
			unit.repaint();
		}
	}
}
