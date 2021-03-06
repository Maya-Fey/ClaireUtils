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
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import claire.util.display.DisplayHelper;
import claire.util.display.component.FilenameCollectionPanel;
import claire.util.display.message.ErrorMessage;
import claire.util.display.message.InformationCollectionMessage;

public class AdvancedFolderPane
	   extends SFilePane
	   implements ActionListener,
	   			  ListSelectionListener,
	   			  KeyListener  {

	private static final long serialVersionUID = 4983742565289393874L;
	private static final Border border = DisplayHelper.uniformBorder(6);	

	private final JTextField folder = new JTextField();
	private final JList<String> list = new JList<String>();
	private final JLabel status = new JLabel("No folder selected");
	private final Window owner;

	private File[] files = null;
	private int[] ints = null;
	private File current = null;
	private File selected = null;
	
	public AdvancedFolderPane(Window owner, File f)
	{
		super(GridBagConstraints.BOTH);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		this.owner = owner;
		folder.setActionCommand("0");
		folder.addActionListener(this);
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
		nf.setActionCommand("2");
		nf.addActionListener(this);
		this.newCol(DisplayHelper.nestBorderWide(nf, border), 0.001D);
		JButton rf = new JButton("Rename Folder");
		rf.setActionCommand("3");
		rf.addActionListener(this);
		this.newCol(DisplayHelper.nestBorderWide(rf, border), 0.001D);
		this.newCol(DisplayHelper.nestBorderWide(status, border), 1.0D);
		this.newRow(1.0D);
		JScrollPane pane = new JScrollPane(list);
		this.newCol(DisplayHelper.nestBorderWide(pane, border), 3);
		this.update(f);
	}
	
	public void update(File f)
	{
		File[] sub = f.listFiles();
		if(sub == null)
			return;
		files = sub;
		int len = 0;
		for(int i = 0; i < sub.length; i++)
			if(sub[i].isDirectory())
				len++;
		if(ints == null || ints.length < len)
			ints = new int[len];
		String[] str = new String[len];
		len = 0;
		for(int i = 0; i < sub.length; i++)
			if(sub[i].isDirectory()) {
				ints[len ] = i;
				str[len++] = sub[i].getName();
			}
		list.setListData(str);
		folder.setText(f.getAbsolutePath());
		current = f;
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
		this.unselect();
	}
	
	public void select(File file)
	{
		this.selected = file;
	}
	
	public void unselect()
	{
		this.selected = null;
	}

	public boolean hasSelected()
	{
		return this.selected != null;
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
				FilenameCollectionPanel s = new FilenameCollectionPanel("Enter name for folder");
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
			case "3":
				if(list.getSelectedIndex() == -1) {
					ErrorMessage m1 = new ErrorMessage(owner, "No file selected.");
					DisplayHelper.center(m1);
					m1.start();
					break;
				}
				s = new FilenameCollectionPanel("Enter name for folder");
				m = new InformationCollectionMessage(owner, s, "Rename Folder", true);
				DisplayHelper.center(m);
				m.start();
				if(m.isOk()) {
					File n = new File(current.getAbsolutePath() + '/' + s.getString());
					if(!files[ints[list.getSelectedIndex()]].renameTo(n)) {
						ErrorMessage m1 = new ErrorMessage(owner, "Failed to rename directory.");
						DisplayHelper.center(m1);
						m1.start();
					} else
						this.update(current);
				}
				this.unselect();
		}
	}

	public void valueChanged(ListSelectionEvent arg0)
	{
		if(!arg0.getValueIsAdjusting()) {
			int i = list.getSelectedIndex();
			if(i > -1) {
				File f = files[ints[i]];
				status.setText("Directory " + f.getName() + " selected, press Enter to browse");
				this.select(f);
			} else
				status.setText("No folder selected.");
		}
	}

	public void keyPressed(KeyEvent arg0)
	{
		int code = arg0.getKeyCode();
		if(code == KeyEvent.VK_ENTER && list.getSelectedIndex() > -1) 
			this.update(files[ints[list.getSelectedIndex()]]);
		else if(code == KeyEvent.VK_BACK_SPACE) {
			back();			
		}
	}

	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg) {}

}
