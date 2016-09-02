package claire.util.display.message;

import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JPanel;

public class BasicMessage 
	   extends JDialog {
	
	private static final long serialVersionUID = 5397913021099878819L;

	public BasicMessage(Window arg0, JPanel panel, String message) 
	{
		super(arg0, message, ModalityType.APPLICATION_MODAL);
		this.add(panel);
	}
	
	public BasicMessage(Window arg0, String message) 
	{
		super(arg0, message, ModalityType.APPLICATION_MODAL);
	}

	public void start()
	{
		this.setVisible(true);
	}
	
	public void disable()
	{
		this.setVisible(false);
	}

	public void close()
	{
		this.setVisible(false);
		this.dispose();
	}
}
