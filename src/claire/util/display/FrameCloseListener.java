package claire.util.display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class FrameCloseListener 
	   implements ActionListener {

	private final JFrame frame;
	
	public FrameCloseListener(JFrame frame)
	{
		this.frame = frame;
	}
	
	public void actionPerformed(ActionEvent e)
	{
		frame.dispose();
	}

}
