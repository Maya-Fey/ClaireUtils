package claire.util.display.display;

import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class BasicDisplay 
	   extends Display {

	private static final long serialVersionUID = 6175377403843301070L;
	
	private final JMenuBar bar;

	public BasicDisplay(String s) 
	{
		super(s);
		bar = new JMenuBar();
		this.setJMenuBar(bar);
	}
	
	public BasicDisplay(String s, JMenuBar menu)
	{
		super(s);
		bar = menu;
		this.setJMenuBar(menu);
	}
	
	public JMenu addMenu(String name)
	{
		JMenu menu = new JMenu(name);
		bar.add(menu);
		return menu;
	}

	public void cleanup() {}

}
