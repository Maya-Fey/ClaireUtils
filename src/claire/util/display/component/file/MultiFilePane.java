package claire.util.display.component.file;

import java.io.File;

import javax.swing.JList;

public class MultiFilePane
	   extends SMultiFilePane
{
	private int[] selected;
	
	private File parent;
	private File[] current;
	
	private final JList<String> list = new JList<String>();
	
	public MultiFilePane(int gbc)
	{
		super(gbc);
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
