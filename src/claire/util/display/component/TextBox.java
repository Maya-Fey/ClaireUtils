package claire.util.display.component;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class TextBox 
	   extends JTextField
	   implements DocumentListener {
	
	private static final long serialVersionUID = 626015435330005349L;
	
	private boolean changed = false;

	public TextBox() 
	{
		this.getDocument().addDocumentListener(this);
	}

	public TextBox(String arg0) 
	{
		super(arg0);
		this.getDocument().addDocumentListener(this);
	}

	public TextBox(int i) 
	{
		super(i);
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
