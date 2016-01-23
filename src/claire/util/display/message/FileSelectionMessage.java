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
import claire.util.display.component.file.SFilePane;

public class FileSelectionMessage 
	   extends BasicMessage
	   implements ActionListener {
	
	private static final long serialVersionUID = -5479749734176388607L;
	
	private final SFilePane filepane;
	
	private boolean ok = false;

	public FileSelectionMessage(Window arg0, SFilePane pane, String message, boolean cancel) {
		super(arg0, message);
		filepane = pane;
		TablePane table = new TablePane(GridBagConstraints.BOTH);
		table.newRow(1.0D);
		if(cancel) {
			table.newCol(pane, 3);
			table.newRow();
			table.newCol(new JPanel(), 1.0D);
			JButton ok = new JButton("Ok");
			JButton can = new JButton("Cancel");
			ok.setActionCommand("1");
			can.setActionCommand("2");
			ok.addActionListener(this);
			can.addActionListener(this);
			table.newCol(DisplayHelper.nestBorderWide(ok, new EmptyBorder(4, 4, 4, 4)), 0.1D);
			table.newCol(DisplayHelper.nestBorderWide(can, new EmptyBorder(4, 4, 4, 4)), 0.1D);
		} else {
			table.newCol(pane, 2);
			table.newRow();
			table.newCol(new JPanel(), 1.0D);
			JButton ok = new JButton("Ok");
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
	
	public boolean isOk()
	{
		return this.ok;
	}
	
	public File getFile()
	{
		return filepane.getFile();
	}

}
