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
import claire.util.display.component.file.AdvancedFilePane;
import claire.util.display.component.file.AdvancedFolderPane;
import claire.util.display.component.file.FilePane;
import claire.util.display.component.file.FolderPane;
import claire.util.display.component.file.SFilePane;

public class FileSelectionMessage 
	   extends BasicMessage
	   implements ActionListener {
	
	private static final long serialVersionUID = -5479749734176388607L;
	
	private final SFilePane filepane;
	
	private boolean ok = false;

	public FileSelectionMessage(Window arg0, int type, File f, String message,  boolean cancel) 
	{
		this(arg0, type, f, message, "Ok", cancel);
	}
	
	public FileSelectionMessage(Window arg0, int type, File f, String message, String button, boolean cancel) 
	{
		super(arg0, message);
		filepane = newFilePane(this.getOwner(), f, type);
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

	public void actionPerformed(ActionEvent arg0)
	{
		switch(arg0.getActionCommand())
		{
			case "1":
				if(!filepane.hasSelected()) {
					ErrorMessage m = new ErrorMessage(this.getOwner(), "No file selected!");
					DisplayHelper.center(m);
					m.start();
					break;
				}
				this.ok = true;
				this.close();
				break;
			case "2":
				this.close();
		}
	}
	
	protected void done()
	{
		this.ok = true;
		this.close();
	}
	
	public boolean isOk()
	{
		return this.ok;
	}
	
	public File getFile()
	{
		return filepane.getFile();
	}
	
	public static final int FILEPANE = 0;
	public static final int ADVANCEDFILEPANE = 1;
	public static final int FOLDERPANE = 2;
	public static final int ADVANCEDFOLDERPANE = 3;
	
	public static final SFilePane newFilePane(Window w, File f, int type)
	{
		switch(type)
		{
			case 0:
				return new FilePane(w, f);
			case 1:
				return new AdvancedFilePane(w, null, f);
			case 2: 
				return new FolderPane(w, f);
			case 3:
				return new AdvancedFolderPane(w, f);
			default:
				throw new java.lang.ArrayIndexOutOfBoundsException();
		}
	}
	
	public static final FileSelectionMessage saveFilePane(Window w, File f, String name, boolean cancel)
	{
		return new FileSelectionMessage(w, ADVANCEDFILEPANE, f, name, "Save File", cancel);
	}
	
	public static final FileSelectionMessage openFilePane(Window w, File f, String name, boolean cancel)
	{
		return new FileSelectionMessage(w, FILEPANE, f, name, "Open File", cancel);
	}

}
