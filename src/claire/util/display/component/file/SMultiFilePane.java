package claire.util.display.component.file;

import java.io.File;

import claire.util.display.component.TablePane;

/**
 * @author Claire
 */
public abstract class SMultiFilePane
				extends TablePane {
	
	private static final long serialVersionUID = -219684324323530032L;

	public SMultiFilePane(int gbc) 
	{
		super(gbc);
	}

	public abstract boolean hasSelected();
	
	public abstract File[] getFiles();

}
