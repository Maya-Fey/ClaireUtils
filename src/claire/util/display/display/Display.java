package claire.util.display.display;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public abstract class Display 
				extends JFrame {

	private static final long serialVersionUID = -4597196310178844187L;
	
	{
		this.addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent e) 
			{
		        Display.this.dispose();
		    }
		});
	}
	
	public Display(String s) 
	{
		super(s);
		this.initialize();
	}
	
	public void start()
	{
		this.setVisible(true);
	}
	
	public final void dispose()
	{
		this.cleanup();
		super.dispose();
	}
	
	protected void initialize()
	{
		this.setSize(800, 450);
	}

	protected abstract void cleanup();

}
