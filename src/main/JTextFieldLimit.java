package main;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

class JTextFieldLimit extends PlainDocument
{
	private static final long serialVersionUID = 464100995339269114L;

	private int limit;
	private Main main = null;
	private char ch;

	JTextFieldLimit(int limit)
	{
		super();
		this.limit = limit;
	}
	
	JTextFieldLimit(int limit, Main main, char ch) {
		super();
		this.limit = limit;
	}

	@Override public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException
	{
		if (str == null)
			return;

		if ((getLength() + str.length()) <= limit) {
			super.insertString(offset, str, attr);
		}
	}

	@Override protected void removeUpdate(DefaultDocumentEvent chng) {
//		System.out.println("Remove update");
//		if(main != null) {
//			main.charMap.put(ch, null);
//		}
		super.removeUpdate(chng);
	}
}