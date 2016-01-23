package claire.util.display.component.file;

import java.io.File;

import claire.util.display.component.TablePane;

/**
 * TODO: Make Advanced*Pane classes extend their basic cousins
 * @author Claire
 */
public abstract class SFilePane
				extends TablePane {
	
	private static final long serialVersionUID = -2196804608103530032L;

	public SFilePane(int gbc) 
	{
		super(gbc);
	}

	public abstract boolean hasSelected();
	
	public abstract File getFile();

}
