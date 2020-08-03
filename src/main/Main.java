package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Main
{
	public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
	
	Frame frame;
	JDialog alphabetEditorDialog = null;
	ArrayList<CryptoUnit> alphabetUnits = null;
	ArrayList<CryptoUnit> units = null;
	NthCommon nthCommon;
	static String[] dictionary;
	
	public enum NthCommon
	{
		MOST_COMMON,
		SECOND_MOST_COMMON,
		THIRD_MOST_COMMON
	}
	
	public static void main(String[] args)
	{
		new Main().init();
	}
	
	public void init()
	{
		try
		{
			dictionary = getResourceFileAsString("Dictionary.txt").split("\n");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		frame = new Frame();
		frame.setup(this);
//		frame.setTitle("Cryptogram");
//		frame.setSize(400, 400);
//		frame.setDefaultCloseOperation(Frame.EXIT_ON_CLOSE);
//		frame.setMinimumSize(new Dimension(400, 400));
		
//		JDialog textInputDialog = new JDialog(frame);
//		textInputDialog.setTitle("Enter Text");
//		textInputDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
//		textInputDialog.setLocationRelativeTo(frame);
//		textInputDialog.setSize(400, 400);
//		textInputDialog.setMinimumSize(new Dimension(400, 400));
//		
//		JTextArea textInput = new JTextArea("", 1, 1);
//		JScrollPane inputOverflow = new JScrollPane(textInput);
//		textInputDialog.add(inputOverflow, BorderLayout.CENTER);
//		
//		JButton submitText = new JButton("Submit");
//		textInputDialog.add(submitText, BorderLayout.SOUTH);
//		
//		submitText.addActionListener(new ActionListener() {
//			@Override public void actionPerformed(ActionEvent arg0)
//			{
//				units = createAndAddUnits(frame, textInput.getText());
//				textInputDialog.setVisible(false);
//				frame.pack();
//				frame.setLocationRelativeTo(null);
//				frame.revalidate();
//				showAlphabetEditorDialog(frame, units);
//			}
//		});
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
//		textInputDialog.setLocation(frame.getLocation());
//		textInputDialog.setVisible(true);
	}
	
	public ArrayList<CryptoUnit> createAndAddUnits(Frame frame, String text)
	{
		ArrayList<CryptoUnit> list = new ArrayList<CryptoUnit>();
		
		JPanel container = new JPanel();
		container.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.insets = new Insets(5, 5, 5, 5);
		
		String[] lines = text.split("\n");
		for(int i = 0; i < lines.length - 1; i++)
		{
			lines[i] = lines[i] + '\n';
		}
		
		for(int i = 0; i < lines.length; i++)
		{
			for(int j = 0; j < lines[i].length(); j++)
			{
				c.gridx = j;
				c.gridy = i;
				CryptoUnit unit = new CryptoUnit(lines[i].charAt(j));
				unit.input.addFocusListener(new FocusListener() {
					@Override public void focusGained(FocusEvent arg0)
					{
						unit.showSelected = true;
						unit.repaint();
						for(CryptoUnit u : list)
						{
							if((u.ch + "").toLowerCase().contentEquals((unit.ch + "").toLowerCase()))
							{
								u.showSelected = true;
								u.repaint();
							}
							else
							{
								u.showSelected = false;
								u.repaint();
							}
						}
						if(alphabetUnits != null)
						{
							for(CryptoUnit u : alphabetUnits)
							{
								if((u.ch + "").toLowerCase().contentEquals((unit.ch + "").toLowerCase()))
								{
									u.showSelected = true;
									u.repaint();
								}
								else
								{
									u.showSelected = false;
									u.repaint();
								}
							}
						}
					}
					
					@Override public void focusLost(FocusEvent arg0)
					{
						for(CryptoUnit u : list)
						{
							u.showSelected = false;
							u.repaint();
						}
						if(alphabetUnits != null)
						{
							for(CryptoUnit u : alphabetUnits)
							{
								u.showSelected = false;
								u.repaint();
							}
						}
					}
				});
				
				unit.input.setDocument(new JTextFieldLimit(1));
				unit.input.addKeyListener(new KeyListener()
				{
					@Override public void keyPressed(KeyEvent e)
					{
					}

					@Override public void keyReleased(KeyEvent e)
					{
					}

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
				
				container.add(unit, c);
				list.add(unit);
			}
		}
		
		JScrollPane pane = new JScrollPane(container);
		
		frame.cryptoArea.removeAll();
		frame.cryptoArea.add(pane, BorderLayout.CENTER);
		
		return list;
	}
	
	public static boolean isLetter(char character)
	{
		for(int i = 0; i < Main.ALPHABET.length(); i++)
		{
			if(Main.ALPHABET.charAt(i) == Character.toLowerCase(character))
			{
				return true;
			}
		}
		return false;
	}
	
	public void showOutputDialog(Frame parentWindow, String output)
	{
		JDialog textOutputDialog = new JDialog(parentWindow);
		textOutputDialog.setTitle("Output");
		textOutputDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		textOutputDialog.setSize(400, 400);
		textOutputDialog.setMinimumSize(new Dimension(400, 400));
		
		JTextArea textOutput = new JTextArea(output, 1, 1);
		JScrollPane inputOverflow = new JScrollPane(textOutput);
		textOutputDialog.add(inputOverflow, BorderLayout.CENTER);

		textOutputDialog.setLocationRelativeTo(parentWindow);
		textOutputDialog.setVisible(true);
	}
		
	public void showAlphabetEditorDialog(Frame parentWindow, ArrayList<CryptoUnit> list, boolean forceRemake)
	{
		if(alphabetEditorDialog == null || forceRemake)
		{
			if(alphabetEditorDialog == null) {
				alphabetEditorDialog = new JDialog(parentWindow);
				alphabetEditorDialog.setTitle("Alphabet");
				alphabetEditorDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
				alphabetEditorDialog.setSize(400, 400);
				alphabetEditorDialog.setMinimumSize(new Dimension(400, 400));
				alphabetEditorDialog.setLayout(new GridBagLayout());
			}
			
			alphabetUnits = new ArrayList<CryptoUnit>();
			
			JPanel container = new JPanel();
			container.setLayout(new GridBagLayout());
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridy = 0;
			
			for(int j = 0; j < ALPHABET.length(); j++)
			{
				c.gridx = j;
				c.gridy = 0;
				CryptoUnit unit = new CryptoUnit(ALPHABET.charAt(j));
				unit.input.addFocusListener(new FocusListener() {
					@Override public void focusGained(FocusEvent arg0)
					{
						unit.showSelected = true;
						unit.repaint();
						for(CryptoUnit u : list)
						{
							if((u.ch + "").toLowerCase().contentEquals((unit.ch + "").toLowerCase()))
							{
								u.showSelected = true;
								u.repaint();
							}
							else
							{
								u.showSelected = false;
								u.repaint();
							}
						}
						if(alphabetUnits != null)
						{
							for(CryptoUnit u : alphabetUnits)
							{
								if((u.ch + "").toLowerCase().contentEquals((unit.ch + "").toLowerCase()))
								{
									u.showSelected = true;
									u.repaint();
								}
								else
								{
									u.showSelected = false;
									u.repaint();
								}
							}
						}
					}

					@Override public void focusLost(FocusEvent arg0)
					{
						for(CryptoUnit u : list)
						{
							u.showSelected = false;
							u.repaint();
						}
						if(alphabetUnits != null)
						{
							for(CryptoUnit u : alphabetUnits)
							{
								u.showSelected = false;
								u.repaint();
							}
						}
					}
				});

				unit.input.setDocument(new JTextFieldLimit(1));
				unit.input.addKeyListener(new KeyListener()
				{
					@Override public void keyPressed(KeyEvent e)
					{
					}

					@Override public void keyReleased(KeyEvent e)
					{
					}

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

				container.add(unit, c);
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
				container.add(letterCount, c);
				
				letterCount.setToolTipText("Letter Count");
			}
			
			JScrollPane pane = new JScrollPane(container);

			c.gridx = 0;
			c.gridy = 0;
			c.fill = GridBagConstraints.BOTH;
			c.weightx = 1;
			c.weighty = 1;
			c.gridwidth = 1;
			alphabetEditorDialog.add(pane, c);
			
			nthCommon = NthCommon.MOST_COMMON;
			
			String mcLetters = getMostCommonStrings(list, 1, nthCommon);
			String mcBigrams = getMostCommonStrings(list, 2, nthCommon);
			String mcTrigrams = getMostCommonStrings(list, 3, nthCommon);
			
			JLabel mostCommon = new JLabel("Most Common Letters: " + mcLetters.toUpperCase() + "  Most Common Bigrams: " + mcBigrams.toUpperCase() + "  Most Common Trigrams: " + mcTrigrams.toUpperCase());
			
			mostCommon.addMouseListener(new MouseListener() {
				@Override public void mouseClicked(MouseEvent arg0)
				{
					switch(nthCommon)
					{
						case MOST_COMMON:
							nthCommon = NthCommon.SECOND_MOST_COMMON;
							String mcLetters_0 = getMostCommonStrings(list, 1, nthCommon);
							String mcBigrams_0 = getMostCommonStrings(list, 2, nthCommon);
							String mcTrigrams_0 = getMostCommonStrings(list, 3, nthCommon);
							mostCommon.setText("Second Most Common Letters: " + mcLetters_0.toUpperCase() + "  Second Most Common Bigrams: " + mcBigrams_0.toUpperCase() + "  Second Most Common Trigrams: " + mcTrigrams_0.toUpperCase());
							mostCommon.repaint();
							break;
							
						case SECOND_MOST_COMMON:
							nthCommon = NthCommon.THIRD_MOST_COMMON;
							String mcLetters_1 = getMostCommonStrings(list, 1, nthCommon);
							String mcBigrams_1 = getMostCommonStrings(list, 2, nthCommon);
							String mcTrigrams_1 = getMostCommonStrings(list, 3, nthCommon);
							mostCommon.setText("Third Most Common Letters: " + mcLetters_1.toUpperCase() + "  Third Most Common Bigrams: " + mcBigrams_1.toUpperCase() + "  Third Most Common Trigrams: " + mcTrigrams_1.toUpperCase());
							mostCommon.repaint();
							break;
							
						case THIRD_MOST_COMMON:
							nthCommon = NthCommon.MOST_COMMON;
							String mcLetters = getMostCommonStrings(list, 1, nthCommon);
							String mcBigrams = getMostCommonStrings(list, 2, nthCommon);
							String mcTrigrams = getMostCommonStrings(list, 3, nthCommon);
							mostCommon.setText("Most Common Letters: " + mcLetters.toUpperCase() + "  Most Common Bigrams: " + mcBigrams.toUpperCase() + "  Most Common Trigrams: " + mcTrigrams.toUpperCase());
							mostCommon.repaint();
							break;
					}
				}

				@Override public void mouseEntered(MouseEvent arg0)
				{
				}

				@Override public void mouseExited(MouseEvent arg0)
				{
				}

				@Override public void mousePressed(MouseEvent arg0)
				{
				}

				@Override public void mouseReleased(MouseEvent arg0)
				{
				}
			});
			
			c.gridy = 1;
			c.fill = GridBagConstraints.HORIZONTAL;
			c.weighty = 0;
			alphabetEditorDialog.add(mostCommon, c);
			
			c.gridy = 2;
			JButton getKeywords = new JButton("Get possible keywords");
			alphabetEditorDialog.add(getKeywords, c);
			
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
					
					for(String str : dictionary)
					{
						String alph = GetCipherAlphabet(str, false);
						if(cipherAlphabet.equals(alph))
						{
							possibleKeywords.add(str);
						}
					}
					JOptionPane.showMessageDialog(parentWindow, possibleKeywords);
				}
			});
			
			alphabetEditorDialog.pack();
			alphabetEditorDialog.setLocationRelativeTo(parentWindow);
		}

		alphabetEditorDialog.setVisible(true);
	}
	
	public String getMostCommonStrings(ArrayList<CryptoUnit> list, int length, NthCommon type)
	{
		HashMap<String, Integer> strings = new HashMap<String, Integer>();
		for(int i = 0; i < list.size() - (length - 1); i++)
		{
			String ss = getStringFromUnits(list, i, length);//list.get(i) + "" + list.get(i + 1);
			
			boolean hasInvalidChar = false;
			for(int j = 0; j < ss.length(); j++)
			{
				if(!Character.isLetter(ss.charAt(j)))
				{
					hasInvalidChar = true;
					break;
				}
			}
			if(hasInvalidChar)
			{
				continue;
			}

			ss = ss.toUpperCase();
			if(strings.containsKey(ss))
			{
				strings.put(ss, strings.get(ss) + 1);
			}
			else
			{
				strings.put(ss, 1);
			}
		}
		
		int maxCount1 = -1;
		StringBuilder mcStrings = new StringBuilder();
		for(Map.Entry<String, Integer> entry : strings.entrySet())
		{
			if(entry.getValue() > maxCount1)
			{
				maxCount1 = entry.getValue();
				mcStrings = new StringBuilder(entry.getKey());
			}
			else if(entry.getValue() == maxCount1)
			{
				mcStrings.append(", " + entry.getKey());
			}
		}
		
		int maxCount2 = -1;
		StringBuilder smcStrings = new StringBuilder();
		for(Map.Entry<String, Integer> entry : strings.entrySet())
		{
			if(entry.getValue() > maxCount2 && entry.getValue() < maxCount1)
			{
				maxCount2 = entry.getValue();
				smcStrings = new StringBuilder(entry.getKey());
			}
			else if(entry.getValue() == maxCount2)
			{
				smcStrings.append(", " + entry.getKey());
			}
		}
		
		int maxCount3 = -1;
		StringBuilder tmcStrings = new StringBuilder();
		for(Map.Entry<String, Integer> entry : strings.entrySet())
		{
			if(entry.getValue() > maxCount3 && entry.getValue() < maxCount2)
			{
				maxCount3 = entry.getValue();
				tmcStrings = new StringBuilder(entry.getKey());
			}
			else if(entry.getValue() == maxCount3)
			{
				tmcStrings.append(", " + entry.getKey());
			}
		}
		
		switch(type)
		{
			case MOST_COMMON:
				return mcStrings.toString();
				
			case SECOND_MOST_COMMON:
				return smcStrings.toString();
				
			case THIRD_MOST_COMMON:
				return tmcStrings.toString();
				
			default:
				return mcStrings.toString();
		}
	}

	public String getStringFromUnits(ArrayList<CryptoUnit> list, int i, int length)
	{
		StringBuilder sb = new StringBuilder();
		for(int j = i; j < i + length; j++)
		{
			sb.append(list.get(j).ch);
		}
		return sb.toString();
	}
	
	public static String getResourceFileAsString(String fileName) throws IOException
	{
	    ClassLoader classLoader = ClassLoader.getSystemClassLoader();
	    try (InputStream is = classLoader.getResourceAsStream(fileName))
	    {
	        if(is == null)
	        {
	        	return null;
	        }
	        try(InputStreamReader isr = new InputStreamReader(is);
	        		
	        BufferedReader reader = new BufferedReader(isr)) 
	        {
	            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
	        }
	    }
	}
	
	public static String GetCipherAlphabet(String keyword, boolean caps)
	{
		StringBuilder kwSlicer = new StringBuilder(keyword.toLowerCase());
		for(int i = 0; i < ALPHABET.length(); i++)
		{
			boolean appearedOnce = false;
			for(int j = 0; j < kwSlicer.length(); j++)
			{
				if(kwSlicer.charAt(j) == ALPHABET.charAt(i))
				{
					if(appearedOnce)
					{
						kwSlicer.deleteCharAt(j);
						j--;
					}
					else
					{
						appearedOnce = true;
					}
				}
			}
		}
		
		keyword = kwSlicer.toString();
		
		StringBuilder sb = new StringBuilder(ALPHABET);
		int insertAt = 0;
		for(int i = 0; i < keyword.length(); i++)
		{
			for(int j = insertAt; j < sb.length(); j++)
			{
				if(sb.charAt(j) == keyword.charAt(i))
				{
					sb.deleteCharAt(j);
					sb.insert(insertAt, keyword.charAt(i));
					//sb.setCharAt(insertAt, keyword.charAt(i));
					insertAt++;
				}
			}
		}
		return caps ? sb.toString().toUpperCase() : sb.toString().toLowerCase();
	}
	
	protected static ArrayList<String> GetKeywords(String cipherAlph)
	{
		if(dictionary == null)
		{
			try
			{
				dictionary = getResourceFileAsString("Dictionary.txt").split("\n");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		ArrayList<String> possibleKeywords = new ArrayList<String>();
		String cipherAlphabet = cipherAlph;
		
		for(String str : dictionary)
		{
			String alph = GetCipherAlphabet(str, false);
			if(cipherAlphabet.equals(alph))
			{
				possibleKeywords.add(str);
			}
			System.out.println(alph);
		}
		return possibleKeywords;
	}
}
