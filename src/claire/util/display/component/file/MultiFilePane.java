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
import claire.util.display.message.ErrorMessage;

public class MultiFilePane
	   extends SMultiFilePane
	   implements ActionListener,
	   	          ListSelectionListener,
	   	          KeyListener
{
	private static final long serialVersionUID = -2127410786080696314L;
	private static final Border border = DisplayHelper.uniformBorder(6);	
	
	private File parent;
	private File[] current;
	private int[] selected;
	
	private final JList<String> list = new JList<String>();
	private final JTextField folder = new JTextField();
	private final JTextField file = new JTextField();
	private final JLabel status = new JLabel("No files selected");
	private final Window owner;
	private final ActionListener onselect; 
	
	public MultiFilePane(Window owner, ActionListener select, File f)
	{
		super(GridBagConstraints.BOTH);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		this.owner = owner;
		this.onselect = select;
		
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
		this.newCol(DisplayHelper.nestBorderWide(folder, border), 1.0D);
		this.newRow();
		this.newCol(DisplayHelper.nestBorderWide(status, border), 2);
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
		File[] sub = f.listFiles();
		if(sub == null)
			return;
		current = sub;
		String[] str = new String[sub.length];
		for(int i = 0; i < sub.length; i++)
			if(sub[i].isDirectory())
				str[i] = sub[i].getName() + "/";
			else
				str[i] = sub[i].getName();
		list.setListData(str);
		folder.setText(f.getAbsolutePath());
		parent = f;
		unselect();
	}
	
	public void back()
	{
		File p = parent.getParentFile();
		update(p);
	}
	
	public void unselect()
	{
		this.selected = null;
		this.file.setText("");
	}
	
	public void select(int[] sel)
	{
		this.selected = sel;
		showSelection();
		int total = 0;
		for(int i : selected)
			total += current[i].length();
		status.setText(selected.length + " files selected, totalling " + total + " bytes.");
	}
	
	public void showSelection()
	{
		String s = "";
		s += current[selected[0]].getAbsolutePath();
		for(int i = 1; i < selected.length; i++)
			s += " ;:; " + current[selected[i]].getAbsolutePath();
		this.file.setText(s);
	}

	public boolean hasSelected()
	{
		if(selected == null)
			return false;
		if(selected.length == 0)
			return false;
		for(int i : selected)
			if(current[i].isDirectory())
				return false;
		return true;
	}

	public File[] getFiles()
	{
		File[] files = new File[selected.length];
		for(int i = 0; i < selected.length; i++)
			files[i] = current[selected[i]];
		return files;
	}

	public void actionPerformed(ActionEvent e)
	{
		switch(e.getActionCommand())
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
				String[] s = this.file.getText().split(" :;: ");
				File[] fs = new File[s.length];
				for(int i = 0; i < s.length; i++)
					fs[i] = new File(s[i]);
				boolean okay = false;
				for(File file : fs)
					if(file.exists())
						if(!file.isFile()) {
							ErrorMessage m = new ErrorMessage(owner, file.getAbsolutePath() + " is not a file.");
							DisplayHelper.center(m);
							m.start();
							okay = true;
							break;
						} else;
					else {
						ErrorMessage m = new ErrorMessage(owner, "One of the specified files does not exist");
						DisplayHelper.center(m);
						m.start();
						okay = true;
						break;
					}
				if(okay)
					break;
				File parent = fs[0].getParentFile();
				for(int i = 1; i < s.length; i++)
					if(!parent.equals(fs[i].getParentFile())) {
						ErrorMessage m = new ErrorMessage(owner, "Your files do not share the same parent");
						DisplayHelper.center(m);
						m.start();
					}
				if(!this.parent.equals(parent)) 
					this.update(parent);
				int[] selected = new int[fs.length];
				for(int i = 0; i < selected.length; i++)
					for(int j = 0; j < current.length; j++)
						if(fs[i].equals(current[j])) {
							selected[i] = j;
							break;
						}
				this.select(selected);
		}
	}

	public void valueChanged(ListSelectionEvent arg0)
	{
		if(!arg0.getValueIsAdjusting())
		{
			int[] selected = list.getSelectedIndices();
			if(selected.length == 0) {
				status.setText("No files selected");
				return;
			}
			if(selected.length == 1) {
				File f = current[selected[0]];
				if(f.isFile()) {
					status.setText("File " + f.getName() + " selected, " + f.length() + " bytes.");
					select(selected);
					showSelection();
				} else
					status.setText("Directory " + f.getName() + " selected, press Enter to browse");
			} 
			boolean af = true;
			for(int i : selected)
				if(!(af &= current[i].isFile()))
					break;
			if(af) {
				select(selected);
				return;
			} else 
				status.setText("Folders or both folders and files selected");
			unselect();
		}
	}

	public void keyPressed(KeyEvent arg0)
	{
		if(arg0.getKeyCode() == KeyEvent.VK_BACK_SPACE)
			back();
		else if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
			int[] ind = list.getSelectedIndices();
			if(ind.length == 1)
				if(current[ind[0]].isDirectory())
					this.update(current[ind[0]]);
				else;
			else if(this.hasSelected())
				this.onselect.actionPerformed(new ActionEvent(this, 0, "fs"));
		}
	}

	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}
	
}
