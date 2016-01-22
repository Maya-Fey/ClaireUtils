package claire.util.display.component;

import java.io.File;

import javax.swing.JPanel;

public class FileSelector 
	   extends JPanel {

	private static final long serialVersionUID = 4983742565289393874L;

	public FileSelector()
	{
		this.setSize(480, 270);
	}
	
	public File getFile(File base)
	{
		this.setVisible(true);
		return base;
	}

}
