package claire.util.display.message;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JPanel;

import claire.util.display.component.file.SMultiFilePane;

public class MultiFileSelectionMessage
	   extends BasicMessage
	   implements ActionListener
{

	private static final long serialVersionUID = 5754140720953562887L;

	public MultiFileSelectionMessage(Window arg0, JPanel panel, String message)
	{
		super(arg0, panel, message);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		// TODO Fix yo shiet
		
	}

	public static final SMultiFilePane newMultiFilePane(Window owner, MultiFileSelectionMessage m, File f, int type)
	{
		switch(type)
		{
			default:
				throw new java.lang.NullPointerException("Invalid type");
		}
	}
	
}
