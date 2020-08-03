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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import main.Main.NthCommon;

public class AlphabetDialog extends JDialog {
	private static final long serialVersionUID = -5494023807014386701L;
	
	ArrayList<CryptoUnit> alphabetUnits = null;
	Main.NthCommon nthCommon;
	JLabel nthCommonCounts;
	JPanel content;
	ArrayList<JLabel> letterCounts = null;
	
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
		
		content = new JPanel();
		content.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.insets = new Insets(5, 5, 5, 5);
		c.gridy = 0;
		
		for(int j = 0; j < Main.ALPHABET.length(); j++) {
			c.gridx = j;
			c.gridy = 0;
			CryptoUnit unit = new CryptoUnit(Main.ALPHABET.charAt(j));
			unit.input.addFocusListener(new FocusListener() {
				@Override public void focusGained(FocusEvent arg0) {
					unit.showSelected = true;
					unit.repaint();
					for(CryptoUnit u : list) {
						if((u.ch + "").toLowerCase().contentEquals((unit.ch + "").toLowerCase())) {
							u.showSelected = true;
							u.repaint();
						} else {
							u.showSelected = false;
							u.repaint();
						}
					}
					if(alphabetUnits != null) {
						for(CryptoUnit u : alphabetUnits) {
							if((u.ch + "").toLowerCase().contentEquals((unit.ch + "").toLowerCase())) {
								u.showSelected = true;
								u.repaint();
							} else {
								u.showSelected = false;
								u.repaint();
							}
						}
					}
				}

				@Override public void focusLost(FocusEvent arg0) {
					for(CryptoUnit u : list) {
						u.showSelected = false;
						u.repaint();
					}
					if(alphabetUnits != null) {
						for(CryptoUnit u : alphabetUnits) {
							u.showSelected = false;
							u.repaint();
						}
					}
				}
			});

			unit.input.setDocument(new JTextFieldLimit(1));
			unit.input.addKeyListener(new KeyAdapter()
			{
				@Override public void keyTyped(KeyEvent e)
				{
					for(CryptoUnit u : list)
					{
						if((u.ch + "").toLowerCase().contentEquals((unit.ch + "").toLowerCase()))
						{
							u.input.setText(e.getKeyChar() + "");
							u.input.repaint();
						}
					}
					if(alphabetUnits != null)
					{
						for(CryptoUnit u : alphabetUnits)
						{
							if((u.ch + "").toLowerCase().contentEquals((unit.ch + "").toLowerCase()))
							{
								u.input.setText(e.getKeyChar() + "");
								u.input.repaint();
							}
						}
					}
				}
			});

			content.add(unit, c);
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
			content.add(letterCount, c);
			
			letterCount.setToolTipText("Letter Count");
			letterCounts.add(letterCount);
		}
		
		JScrollPane pane = new JScrollPane(content);

		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 1;
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
		JButton getKeywords = new JButton("Get possible keywords");
		add(getKeywords, c);
		
		getKeywords.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent arg0)
			{
				ArrayList<String> possibleKeywords = new ArrayList<String>();
				String cipherAlphabet;
				StringBuilder sb = new StringBuilder();
				for(CryptoUnit unit : alphabetUnits)
				{
					sb.append(unit.input.getText());
				}
				cipherAlphabet = sb.toString().toLowerCase();
				System.out.println(cipherAlphabet);
				
				for(String str : Main.dictionary)
				{
					String alph = Main.GetCipherAlphabet(str, false);
					if(cipherAlphabet.equals(alph))
					{
						possibleKeywords.add(str);
					}
				}
				JOptionPane.showMessageDialog(main.frame, possibleKeywords);
			}
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
}
