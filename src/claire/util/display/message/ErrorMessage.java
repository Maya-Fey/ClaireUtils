package claire.util.display.message;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

public class ErrorMessage 
	   extends JDialog
	   implements ActionListener {

	private static final long serialVersionUID = 2746064158494094128L;
	private static final int MIN_WIDTH = 320;
	private static final int MIN_HEIGHT = 142;
	private static final double IDEAL = Math.sqrt(9D / 4D);
	//Uses a 9:4 aspect ratio

	public ErrorMessage(Window arg0, String error) 
	{
		super(arg0, "Error", ModalityType.APPLICATION_MODAL);
		ErrorPanel panel = new ErrorPanel(error);
		panel.getButton().addActionListener(this);
		this.add(panel);
		this.pack();
		Dimension dim = panel.getPreferredSize();
		double root = Math.sqrt(dim.width * dim.height);
		int width = (int) (root * IDEAL) + 40;
		int height = (int) (root / IDEAL) + 40;
		if(width > MIN_WIDTH)
			this.setSize(width, height);
		else
			this.setSize(MIN_WIDTH, MIN_HEIGHT);
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
