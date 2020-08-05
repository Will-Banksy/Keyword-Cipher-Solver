package main;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Main
{
	public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
	
	Frame frame = null;
	AlphabetDialog alphDiag = null;
	ArrayList<CryptoUnit> units = null;
	NthCommon nthCommon = NthCommon.MOST_COMMON;
	// The volatile keyword guarantees visibility of changes to variables across threads: http://tutorials.jenkov.com/java-concurrency/volatile.html
	static volatile String[] dictionary;
	static volatile LinkedHashMap<String, String> cipherAlphabets = null;
	
	/**
	 * The key is the letter that was inputted, the value if the letter that has been used to replace it<br>
	 * The value the the textbox is the value in the map<br>
	 * The input value that appears in the label is the key in the map
	 */
	public LinkedHashMap<Character, Character> charMap;
	
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
		if(cipherAlphabets == null || dictionary == null) {
			Thread thread = new Thread(() -> {
				if(dictionary == null) {
					try {
						dictionary = getResourceFileAsString("Dictionary.txt").split("\n");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(cipherAlphabets == null) {
					cipherAlphabets = new LinkedHashMap<String, String>();
					for(String str : dictionary)
					{
						cipherAlphabets.put(str, GetCipherAlphabet(str, false));
					}
				}
			});
			thread.start();
		}
		
		charMap = new LinkedHashMap<Character, Character>();
		for(int i = 0; i < ALPHABET.length(); i++) {
			charMap.put(ALPHABET.charAt(i), (char)0);
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
				CryptoUnit unit = new CryptoUnit(lines[i].charAt(j), this);
				unit.input.addFocusListener(new FocusListener() {
					@Override public void focusGained(FocusEvent arg0)
					{
						doFocusThings(unit, true, false, (char)0);
					}
					
					@Override public void focusLost(FocusEvent arg0)
					{
						doFocusThings(unit, false, false, (char)0);
					}
				});
				
				unit.input.setDocument(new JTextFieldLimit(1));
				unit.input.addKeyListener(new KeyAdapter()
				{
					@Override public void keyTyped(KeyEvent e)
					{
						doFocusThings(unit, true, true, e.getKeyChar());
						
						char ch = e.getKeyChar();
						charMap.put(unit.ch, ch);
						for(CryptoUnit u : list)
						{
							if(u != unit)
							{
								if(Character.toLowerCase(u.ch) == Character.toLowerCase(unit.ch))
								{
									u.repaint();
								}
							}
						}
						for(CryptoUnit u : alphDiag.alphabetUnits)
						{
							if(u != unit)
							{
								if(Character.toLowerCase(u.ch) == Character.toLowerCase(unit.ch))
								{
									u.repaint();
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
			String ss = getStringFromUnits(list, i, length);
			
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
					insertAt++;
				}
			}
		}
		return caps ? sb.toString().toUpperCase() : sb.toString().toLowerCase();
	}
	
	protected static ArrayList<String> GetKeywords(String cipherAlph)
	{
		ArrayList<String> possibleKeywords = new ArrayList<String>();
		String cipherAlphabet = cipherAlph;
		
		for(String str : dictionary) {
			String alph = cipherAlphabets.get(str);
			if(cipherAlphabet.equals(alph)) {
				possibleKeywords.add(str);
			}
		}
		return possibleKeywords;
	}
	
	/**
	 * Returns true if the string is empty, blank or contains all characters that are < 32
	 */
	public static boolean stringUnimportant(String str) {
		if(str.isBlank()) {
			return true;
		} else {
			// Check if there's any important characters in the string
			for(int i = 0; i < str.length(); i++) {
				if(str.charAt(i) >= 32) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void doFocusThings(CryptoUnit unit, boolean focusGained, boolean useNewInput, char unitNewInput) {
		String unitInputText = useNewInput ? String.valueOf(unitNewInput) : unit.input.getText();
		if(focusGained) {
			unit.showSelected = true;
//			unit.repaint();
			
			for(CryptoUnit u : units)
			{
				if(u.showSelectedBad) {
					u.showSelectedBad = false;
					u.repaint();
				}
			}
			if(alphDiag.alphabetUnits != null)
			{
				for(CryptoUnit u : alphDiag.alphabetUnits)
				{
					if(u.showSelectedBad) {
						u.showSelectedBad = false;
						u.repaint();
					}
				}
			}
			
			for(CryptoUnit u : units)
			{
				// If the two units cipher (immutable) character is the same
				if(Character.toLowerCase(u.ch) == Character.toLowerCase(unit.ch))
				{
					u.showSelected = true;
					u.repaint();
				}
				else
				{
					u.showSelected = false;
					u.repaint();
				}
				// if the two units input character is the same
				if(u.input.getText().equalsIgnoreCase(unitInputText) && !Main.stringUnimportant(u.input.getText()))
				{
					// And their cipher character is different
					if(Character.toLowerCase(u.ch) != Character.toLowerCase(unit.ch))
					{
						u.showSelectedBad = true;
						u.repaint();
					}
					else
					{
						u.showSelectedBad = false;
						u.repaint();
					}
				}
			}
			if(alphDiag.alphabetUnits != null)
			{
				for(CryptoUnit u : alphDiag.alphabetUnits)
				{
					// If the two units cipher (immutable) character is the same
					if(Character.toLowerCase(u.ch) == Character.toLowerCase(unit.ch))
					{
						u.showSelected = true;
						u.repaint();
					}
					else
					{
						u.showSelected = false;
						u.repaint();
					}
					// if the two units input character is the same
					if(u.input.getText().equalsIgnoreCase(unitInputText) && !Main.stringUnimportant(u.input.getText()))
					{
						// And their cipher character is different
						if(Character.toLowerCase(u.ch) != Character.toLowerCase(unit.ch))
						{
							u.showSelectedBad = true;
							u.repaint();
						}
						else
						{
							u.showSelectedBad = false;
							u.repaint();
						}
					}
				}
			}
		} else {
			for(CryptoUnit u : units)
			{
				u.showSelected = false;
				u.showSelectedBad = false;
				u.repaint();
			}
			if(alphDiag.alphabetUnits != null)
			{
				for(CryptoUnit u : alphDiag.alphabetUnits)
				{
					u.showSelected = false;
					u.showSelectedBad = false;
					u.repaint();
				}
			}
		}
	}
}
