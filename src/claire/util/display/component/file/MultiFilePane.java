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
	
	private final JList<String> list = new JList<String>();
	private final JTextField folder = new JTextField();
	private final JTextField file = new JTextField();
	private final JLabel status = new JLabel("No file or folder selected");
	private final Window owner;
	
	public MultiFilePane(Window owner, File f)
	{
		super(GridBagConstraints.BOTH);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
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
	}
	
	public void back()
	{
		File p = parent.getParentFile();
		update(p);
	}

	public boolean hasSelected()
	{
		int[] selected = list.getSelectedIndices();
		if(selected.length == 0)
			return false;
		for(int i : selected)
			if(current[i].isDirectory())
				return false;
		return true;
	}

	public File[] getFiles()
	{
		int[] selected = list.getSelectedIndices();
		File[] files = new File[selected.length];
		for(int i = 0; i < files.length; i++)
			files[i] = current[files.length];
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
		}
	}

	public void valueChanged(ListSelectionEvent arg0)
	{
		// TODO Fix yo shiet
		
	}

	public void keyPressed(KeyEvent arg0)
	{
		// TODO Fix yo shiet
		
	}

	public void keyReleased(KeyEvent arg0) {}
	public void keyTyped(KeyEvent arg0) {}
	
}
