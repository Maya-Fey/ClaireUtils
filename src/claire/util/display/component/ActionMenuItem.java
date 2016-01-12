package claire.util.display.component;

import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenuItem;

public abstract class ActionMenuItem 
	   			extends JMenuItem 
	   			implements ActionListener {

	private static final long serialVersionUID = 2244981980214817200L;

	public ActionMenuItem(Icon arg0) 
	{
		super(arg0);
	}

	public ActionMenuItem(String arg0) 
	{
		super(arg0);
	}

	public ActionMenuItem(Action arg0) 
	{
		super(arg0);
	}

	public ActionMenuItem(String text, Icon icon) 
	{
		super(text, icon);
	}

	public ActionMenuItem(String text, int mnemonic)
	{
		super(text, mnemonic);
	}

}
