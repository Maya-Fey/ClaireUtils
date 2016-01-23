package claire.util.display.component.file;

import java.awt.GridBagConstraints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import claire.util.display.DisplayHelper;
import claire.util.display.component.TablePane;
import claire.util.display.message.ErrorMessage;

public class FilePane
	   extends TablePane
	   implements ActionListener,
	   			  ListSelectionListener,
	   			  KeyListener,
	   			  IFilePane {

	private static final long serialVersionUID = 4983742565289393874L;
	private static final Border border = DisplayHelper.uniformBorder(6);	

	private final JTextField folder = new JTextField();
	private final JTextField file = new JTextField();
	private final JList<String> list = new JList<String>();
	private final JLabel status = new JLabel("No file or folder selected");
	private final Window owner;
	
	private File[] files = null;
	private File selected = null;
	private File current = null;
	
	public FilePane(Window owner, File f)
	{
		super(GridBagConstraints.BOTH);
		this.owner = owner;
		folder.setActionCommand("0");
		folder.addActionListener(this);
		file.setActionCommand("2");
		file.addActionListener(this);
		list.addListSelectionListener(this);
		list.addKeyListener(this);
		
		DisplayHelper.addBorder(status, border);
		
		this.newRow();
		JButton up = new JButton("UP");
		up.setActionCommand("1");
		up.addActionListener(this);
		this.newCol(DisplayHelper.nestBorderWide(up, border));
		this.newCol(DisplayHelper.nestBorderWide(folder, border), 1.0D);
		this.newRow();
		this.newCol(status, 2);
		this.newRow(1.0D);
		JScrollPane pane = new JScrollPane(list);
		this.newCol(DisplayHelper.nestBorderWide(pane, border), 2);
		this.newRow();
		JLabel t = new JLabel("File Selected: ");
		DisplayHelper.addBorder(t, border);
		this.newCol(t);
		this.newCol(DisplayHelper.nestBorderWide(file, border), 1.0D);
		this.update(f);
	}
	
	public void update(File f)
	{
		File[] sub = files = f.listFiles();
		String[] str = new String[sub.length];
		for(int i = 0; i < sub.length; i++)
			if(sub[i].isDirectory())
				str[i] = sub[i].getName() + "/";
			else
				str[i] = sub[i].getName();
		list.setListData(str);
		folder.setText(f.getAbsolutePath());
		current = f;
	}
	
	public void select(File f)
	{
		this.selected = f;
		this.file.setText(f.getAbsolutePath());
	}
	
	public void back()
	{
		File p = current.getParentFile();
		if(p == null) {
			ErrorMessage m = new ErrorMessage(owner, "Cannot return ; you are in a root directory.");
			DisplayHelper.center(m);
			m.start();
			return;
		}
		update(p);
	}
	
	public boolean hasSelected()
	{
		return selected != null;
	}
	
	public File getFile()
	{
		return this.selected;
	}

	public void actionPerformed(ActionEvent arg0)
	{
		switch(arg0.getActionCommand()) 
		{
			case "0":
				File f = new File(this.folder.getText());
				if(f.exists()) {
					if(f.isDirectory()) {
						this.update(f);
					} else {
						ErrorMessage m = new ErrorMessage(owner, "That is not a directory.");
						DisplayHelper.center(m);
						m.start();
					}
				} else {
					ErrorMessage m = new ErrorMessage(owner, "That folder does not exist.");
					DisplayHelper.center(m);
					m.start();
				}
				break;
			case "1":
				back();
				break;
			case "2":
				f = new File(this.file.getText());
				if(f.exists()) {
					if(f.isFile()) {
						this.select(f);
					} else {
						ErrorMessage m = new ErrorMessage(owner, "That is not a file.");
						DisplayHelper.center(m);
						m.start();
					}
				} else {
					ErrorMessage m = new ErrorMessage(owner, "That file does not exist.");
					DisplayHelper.center(m);
					m.start();
				}
		}
	}

	public void valueChanged(ListSelectionEvent arg0)
	{
		if(arg0.getValueIsAdjusting()) {
			File f = files[list.getSelectedIndex()];
			if(f.isDirectory()) 
				status.setText("Directory " + f.getName() + " selected, press Enter to browse");
			else 
				status.setText("File " + f.getName() + " selected, " + f.length() + " bytes. Press Enter to Browse.");
		}
	}

	public void keyPressed(KeyEvent arg0)
	{
		int code = arg0.getKeyCode();
		if(code == KeyEvent.VK_ENTER) {
			File f = files[list.getSelectedIndex()];
			if(f.isDirectory()) 
				this.update(f);
			else 
				this.select(f);
		} else if(code == KeyEvent.VK_BACK_SPACE) {
			back();
		}
			
	}

	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg) {}

}
