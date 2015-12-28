package claire.util.display;

import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;

public abstract class ActionButton 
	   			extends JButton 
	   			implements ActionListener {

	private static final long serialVersionUID = 262588734846449375L;

	public ActionButton() 
	{
		this.addActionListener(this);
	}

	public ActionButton(Icon arg0) 
	{
		super(arg0);
		this.addActionListener(this);
	}

	public ActionButton(String arg0)
	{
		super(arg0);
		this.addActionListener(this);
	}

	public ActionButton(Action arg0)
	{
		super(arg0);
		this.addActionListener(this);
	}

	public ActionButton(String arg0, Icon arg1) 
	{
		super(arg0, arg1);
		this.addActionListener(this);
	}

}
