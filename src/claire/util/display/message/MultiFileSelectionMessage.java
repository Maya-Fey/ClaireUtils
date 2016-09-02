package claire.util.display.message;

import java.awt.GridBagConstraints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import claire.util.display.DisplayHelper;
import claire.util.display.component.TablePane;
import claire.util.display.component.file.MultiFilePane;
import claire.util.display.component.file.SMultiFilePane;

public class MultiFileSelectionMessage
	   extends BasicMessage
	   implements ActionListener
{

	private static final long serialVersionUID = 5754140720953562887L;
	
	private final SMultiFilePane filepane;
	
	private boolean ok;

	public MultiFileSelectionMessage(Window arg0, int type, File f, String message, boolean cancel) 
	{
		this(arg0, type, f, message, "Ok", cancel);
	}
	
	public MultiFileSelectionMessage(Window arg0, int type, File f, String message, String button, boolean cancel) 
	{
		super(arg0, message);
		filepane = newMultiFilePane(this.getOwner(), this, f, type);
		TablePane table = new TablePane(GridBagConstraints.BOTH);
		table.newRow(1.0D);
		if(cancel) {
			table.newCol(filepane, 3);
			table.newRow();
			table.newCol(new JPanel(), 1.0D);
			JButton ok = new JButton(button);
			JButton can = new JButton("Cancel");
			ok.setActionCommand("1");
			can.setActionCommand("2");
			ok.addActionListener(this);
			can.addActionListener(this);
			table.newCol(DisplayHelper.nestBorderWide(ok, new EmptyBorder(4, 4, 4, 4)), 0.1D);
			table.newCol(DisplayHelper.nestBorderWide(can, new EmptyBorder(4, 4, 4, 4)), 0.1D);
		} else {
			table.newCol(filepane, 2);
			table.newRow();
			table.newCol(new JPanel(), 1.0D);
			JButton ok = new JButton(button);
			ok.setActionCommand("1");
			ok.addActionListener(this);
			table.newCol(DisplayHelper.nestBorderWide(ok, new EmptyBorder(4, 4, 4, 4)), 0.3D);
		}
		this.setSize(800, 450);
		this.add(table);		
	}
	
	public void actionPerformed(ActionEvent e)
	{
		switch(e.getActionCommand())
		{
			case "1":
				if(!filepane.hasSelected()) {
					ErrorMessage m = new ErrorMessage(this.getOwner(), "No files selected!");
					DisplayHelper.center(m);
					m.start();
					break;
				}
				this.done();
				break;
			case "2":
				this.close();
				break;
			case "fs":
				this.done();
				break;
		}
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
