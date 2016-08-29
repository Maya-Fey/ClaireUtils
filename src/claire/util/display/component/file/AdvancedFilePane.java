package claire.util.display.component.file;

import java.awt.GridBagConstraints;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import claire.util.display.DisplayHelper;
import claire.util.display.component.FilenameCollectionPanel;
import claire.util.display.message.ErrorMessage;
import claire.util.display.message.InformationCollectionMessage;

public class AdvancedFilePane
	   extends SFilePane
	   implements ActionListener,
	   			  ListSelectionListener,
	   			  KeyListener {

	private static final long serialVersionUID = 4983742565289393874L;
	private static final Border border = DisplayHelper.uniformBorder(6);	

	private final ActionListener onselect;
	
	private final JTextField folder = new JTextField();
	private final JTextField file = new JTextField();
	private final JList<String> list = new JList<String>();
	private final JLabel status = new JLabel("No file or folder selected");
	private final Window owner;
	
	private File[] files = null;
	private File selected = null;
	private File current = null;
	
	public AdvancedFilePane(Window owner, ActionListener selected, File f)
	{
		super(GridBagConstraints.BOTH);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.onselect = selected;
		this.owner = owner;
		folder.setActionCommand("0");
		folder.addActionListener(this);
		file.setActionCommand("2");
		file.addActionListener(this);
		list.addListSelectionListener(this);
		list.addKeyListener(this);
		
		this.newRow();
		JButton up = new JButton("UP");
		up.setActionCommand("1");
		up.addActionListener(this);
		this.newCol(DisplayHelper.nestBorderWide(up, border));
		this.newCol(DisplayHelper.nestBorderWide(folder, border), 2, 1.0D);
		this.newRow();
		
		JButton nf = new JButton("New Folder");
		nf.setActionCommand("3");
		nf.addActionListener(this);
		this.newCol(DisplayHelper.nestBorderWide(nf, border));
		JButton rf = new JButton("Rename Folder");
		rf.setActionCommand("4");
		rf.addActionListener(this);
		this.newCol(DisplayHelper.nestBorderWide(rf, border));
		this.newMCol(DisplayHelper.nestBorderWide(status, border), 1, 1.0D);
		
		this.newRow();
		JButton nfi = new JButton("New File");
		nfi.setActionCommand("5");
		nfi.addActionListener(this);
		this.newCol(DisplayHelper.nestBorderWide(nfi, border));
		JButton rfi = new JButton("Rename File");
		rfi.setActionCommand("6");
		rfi.addActionListener(this);
		this.newCol(DisplayHelper.nestBorderWide(rfi, border));
		
		this.newRow(1.0D);
		JScrollPane pane = new JScrollPane(list);
		this.newCol(DisplayHelper.nestBorderWide(pane, border), 3);
		this.newRow();
		JLabel t = new JLabel("File Selected: ");
		DisplayHelper.addBorder(t, border);
		this.newCol(t);
		this.newCol(DisplayHelper.nestBorderWide(file, border), 2, 1.0D);
		this.update(f);
	}
	
	public void update(File f)
	{
		File[] sub = f.listFiles();
		if(sub == null)
			return;
		files = sub;
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
	
	public void unselect()
	{
		this.selected = null;
		this.file.setText("");
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
		this.unselect();
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
				this.unselect();
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
				break;
			case "3":
				FilenameCollectionPanel s = new FilenameCollectionPanel("Enter name for folder");
				s.initialize();
				InformationCollectionMessage m = new InformationCollectionMessage(owner, s, "Create New Folder", true);
				DisplayHelper.center(m);
				m.start();
				if(m.isOk()) {
					File n = new File(current.getAbsolutePath() + '/' + s.getString());
					if(!n.mkdir()) {
						ErrorMessage m1 = new ErrorMessage(owner, "Failed to create directory.");
						DisplayHelper.center(m1);
						m1.start();
					} else
						this.update(current);
				}
				break;
			case "4":
				if(list.getSelectedIndex() == -1) {
					ErrorMessage m1 = new ErrorMessage(owner, "No file selected.");
					DisplayHelper.center(m1);
					m1.start();
					break;
				}
				File sel = files[list.getSelectedIndex()];
				if(!sel.isDirectory()) {
					ErrorMessage m1 = new ErrorMessage(owner, "Selected object is not a directory.");
					DisplayHelper.center(m1);
					m1.start();
					break;
				}
				s = new FilenameCollectionPanel("Enter name for folder");
				s.initialize();
				m = new InformationCollectionMessage(owner, s, "Rename Folder", true);
				DisplayHelper.center(m);
				m.start();
				if(m.isOk()) {
					File n = new File(current.getAbsolutePath() + '/' + s.getString());
					if(!sel.renameTo(n)) {
						ErrorMessage m1 = new ErrorMessage(owner, "Failed to rename directory.");
						DisplayHelper.center(m1);
						m1.start();
					} else
						this.update(current);
				}
				break;
			case "5":
				s = new FilenameCollectionPanel("Enter name for file");
				s.initialize();
				m = new InformationCollectionMessage(owner, s, "Create New File", true);
				DisplayHelper.center(m);
				m.start();
				if(m.isOk()) {
					File n = new File(current.getAbsolutePath() + '/' + s.getString());
					try {
						if(!n.createNewFile()) {
							ErrorMessage m1 = new ErrorMessage(owner, "Failed to create file.");
							DisplayHelper.center(m1);
							m1.start();
						} else
							this.update(current);
					} catch (IOException e) {
						ErrorMessage m1 = new ErrorMessage(owner, "Error encountered while trying to create file: " + e.getMessage());
						DisplayHelper.center(m1);
						m1.start();
						e.printStackTrace();
					}
				}
				break;
			case "6":
				if(list.getSelectedIndex() == -1) {
					ErrorMessage m1 = new ErrorMessage(owner, "No file selected.");
					DisplayHelper.center(m1);
					m1.start();
					break;
				}
				sel = files[list.getSelectedIndex()];
				if(!sel.isFile()) {
					ErrorMessage m1 = new ErrorMessage(owner, "Selected object is not a file.");
					DisplayHelper.center(m1);
					m1.start();
					break;
				}
				s = new FilenameCollectionPanel("Enter name for file");
				s.initialize();
				m = new InformationCollectionMessage(owner, s, "Rename File", true);
				DisplayHelper.center(m);
				m.start();
				if(sel == selected)
					this.unselect();
				if(m.isOk()) {
					File n = new File(current.getAbsolutePath() + '/' + s.getString());
					if(!sel.renameTo(n)) {
						ErrorMessage m1 = new ErrorMessage(owner, "Failed to rename file.");
						DisplayHelper.center(m1);
						m1.start();
					} else
						this.update(current);
				}
				break;
		}
	}

	public void valueChanged(ListSelectionEvent arg0)
	{
		if(!arg0.getValueIsAdjusting()) {
			int i = list.getSelectedIndex(); 
			if(i > -1) {
				File f = files[i];
				if(f.isDirectory()) {
					status.setText("Directory " + f.getName() + " selected, press Enter to browse");
					this.unselect();
				} else {
					status.setText("File " + f.getName() + " selected, " + f.length() + " bytes.");
					this.select(f);
				}
			} else
				status.setText("No file or folder selected.");
		}
	}

	public void keyPressed(KeyEvent arg0)
	{
		int code = arg0.getKeyCode();
		if(code == KeyEvent.VK_ENTER && list.getSelectedIndex() > -1) {
			File f = files[list.getSelectedIndex()];
			if(f.isDirectory()) { 
				this.update(f);
				this.unselect();
			} else if(onselect != null)
				onselect.actionPerformed(new ActionEvent(this, 0, "fs"));
		} else if(code == KeyEvent.VK_BACK_SPACE) {
			back();
		}
			
	}

	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg) {}

}
