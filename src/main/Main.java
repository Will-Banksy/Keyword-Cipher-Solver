package main;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Main
{
	public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
	
	Frame frame = null;
	JDialog alphabetEditorDialog = null;
	AlphabetDialog alphDiag = null;
	ArrayList<CryptoUnit> units = null;
	NthCommon nthCommon = NthCommon.MOST_COMMON;
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
			if(dictionary != null)
				dictionary = getResourceFileAsString("Dictionary.txt").split("\n");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		frame = new Frame();
		frame.setup(this);
		
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
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
						if(alphDiag.alphabetUnits != null)
						{
							for(CryptoUnit u : alphDiag.alphabetUnits)
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
						if(alphDiag.alphabetUnits != null)
						{
							for(CryptoUnit u : alphDiag.alphabetUnits)
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
						if(alphDiag.alphabetUnits != null)
						{
							for(CryptoUnit u : alphDiag.alphabetUnits)
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
