package claire.util.display.component.file;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

import claire.util.display.DisplayHelper;

public class MultiFilePane
	   extends SMultiFilePane
	   implements ActionListener
{
	private static final long serialVersionUID = -2127410786080696314L;
	private static final Border border = DisplayHelper.uniformBorder(6);	
	
	private File parent;
	private File[] current;
	
	private final JList<String> list = new JList<String>();
	private final JTextField folder = new JTextField();
	private final JTextField file = new JTextField();
	private final JLabel status = new JLabel("No file or folder selected");
	
	public MultiFilePane()
	{
		super(GridBagConstraints.BOTH);
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}

	@Override
	public boolean hasSelected()
	{
		// TODO Fix yo shiet
		return false;
	}

	@Override
	public File[] getFiles()
	{
		// TODO Fix yo shiet
		return null;
	}

	public void actionPerformed(ActionEvent e)
	{
		// TODO Fix yo shiet
		
	}
	
}
