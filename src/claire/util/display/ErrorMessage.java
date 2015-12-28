package claire.util.display;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

public class ErrorMessage 
	   extends JDialog
	   implements ActionListener {

	private static final long serialVersionUID = 2746064158494094128L;

	public ErrorMessage(Window arg0, String error) 
	{
		super(arg0, "Error", ModalityType.APPLICATION_MODAL);
		ErrorPanel panel = new ErrorPanel(error);
		panel.getButton().addActionListener(this);
		this.add(panel);
		this.setSize(320, 140);
	}
	
	public void start()
	{
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent arg0)
	{
		this.setVisible(false);
		this.dispose();
	}

}
