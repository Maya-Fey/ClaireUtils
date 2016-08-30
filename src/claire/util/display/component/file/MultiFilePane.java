package claire.util.display.component.file;

import java.io.File;

public class MultiFilePane
	   extends SMultiFilePane
{
	private int[] selected;
	
	private File parent;
	private File[] current;
	
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
