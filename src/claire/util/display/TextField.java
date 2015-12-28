package claire.util.display;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

public class TextField 
	   extends JTextArea 
	   implements DocumentListener {
	
	private static final long serialVersionUID = 6260148363530005349L;
	
	private boolean changed = false;

	public TextField() 
	{
		this.getDocument().addDocumentListener(this);
	}

	public TextField(String arg0) 
	{
		super(arg0);
		this.getDocument().addDocumentListener(this);
	}

	public TextField(Document arg0) 
	{
		super(arg0);
		this.getDocument().addDocumentListener(this);
	}

	public TextField(int arg0, int arg1) 
	{
		super(arg0, arg1);
		this.getDocument().addDocumentListener(this);
	}

	public TextField(String arg0, int arg1, int arg2) 
	{
		super(arg0, arg1, arg2);
		this.getDocument().addDocumentListener(this);
	}

	public TextField(Document arg0, String arg1, int arg2, int arg3)
	{
		super(arg0, arg1, arg2, arg3);
		this.getDocument().addDocumentListener(this);
	}

	public void changedUpdate(DocumentEvent arg0)
	{
		changed = true;
	}
	
	public void insertUpdate(DocumentEvent arg0)
	{
		changed = true;
	}

	public void removeUpdate(DocumentEvent arg0)
	{
		changed = true;
	}
	
	public boolean hasChanged()
	{
		return this.changed;
	}
	
	public void reset()
	{
		this.changed = false;
	}

}
