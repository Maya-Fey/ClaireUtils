package claire.util.display.component.file;

import java.awt.GridBagConstraints;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class MultiFilePane
	   extends SMultiFilePane
{
	private static final long serialVersionUID = -2127410786080696314L;
	
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
	
}
