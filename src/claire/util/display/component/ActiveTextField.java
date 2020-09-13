package claire.util.display.component;

import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import claire.util.concurrency.IGenericListener;
import claire.util.memory.util.ArrayUtil;

public class ActiveTextField 
	   extends JTextArea 
	   implements DocumentListener {
	
	private static final long serialVersionUID = 6260148363530005349L;
	
	private IGenericListener[] listeners = new IGenericListener[1];
	private int pos;
	
	private boolean changed = false;

	public ActiveTextField() 
	{
		this.getDocument().addDocumentListener(this);
	}

	public ActiveTextField(String arg0) 
	{
		super(arg0);
		this.getDocument().addDocumentListener(this);
	}

	public ActiveTextField(Document arg0) 
	{
		super(arg0);
		this.getDocument().addDocumentListener(this);
	}

	public ActiveTextField(int arg0, int arg1) 
	{
		super(arg0, arg1);
		this.getDocument().addDocumentListener(this);
	}

	public ActiveTextField(String arg0, int arg1, int arg2) 
	{
		super(arg0, arg1, arg2);
		this.getDocument().addDocumentListener(this);
	}

	public ActiveTextField(Document arg0, String arg1, int arg2, int arg3)
	{
		super(arg0, arg1, arg2, arg3);
		this.getDocument().addDocumentListener(this);
	}

	public void changedUpdate(DocumentEvent arg0)
	{
		changed = true;
		event();
	}
	
	public void insertUpdate(DocumentEvent arg0)
	{
		changed = true;
		event();
	}

	public void removeUpdate(DocumentEvent arg0)
	{
		changed = true;
		event();
	}
	
	public boolean hasChanged()
	{
		return this.changed;
	}
	
	public void reset()
	{
		this.changed = false;
	}
	
	public void addListener(IGenericListener listener)
	{
		if(pos == listeners.length)
			listeners = ArrayUtil.upsize(listeners, 2);
		listeners[pos++] = listener;
	}
	
	private void event()
	{
		for(int i = 0; i < pos; i++)
			listeners[i].call();
	}

}
