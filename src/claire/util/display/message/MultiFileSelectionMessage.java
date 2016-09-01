package claire.util.display.message;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JPanel;

import claire.util.display.component.file.MultiFilePane;
import claire.util.display.component.file.SMultiFilePane;

public class MultiFileSelectionMessage
	   extends BasicMessage
	   implements ActionListener
{

	private static final long serialVersionUID = 5754140720953562887L;
	
	private final SMultiFilePane filepane;
	
	private boolean ok;

	public MultiFileSelectionMessage(Window arg0, JPanel panel, String message)
	{
		super(arg0, panel, message);
	}
	
	public void actionPerformed(ActionEvent e)
	{
		// TODO Fix yo shiet
		
	}
	
	public boolean isOk()
	{
		return ok;
	}
	
	protected void done()
	{
		ok = true;
		this.close();
	}
	
	public File[] getFiles()
	{
		return filepane.getFiles();
	}

	public static final int MUTLIFILEPANE = 0;
	
	public static final SMultiFilePane newMultiFilePane(Window owner, MultiFileSelectionMessage m, File f, int type)
	{
		switch(type)
		{
			case 0:
				return new MultiFilePane(owner, m, f);
			default:
				throw new java.lang.NullPointerException("Invalid type");
		}
	}
	
}
