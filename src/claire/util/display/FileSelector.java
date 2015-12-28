package claire.util.display;

import java.io.File;

import javax.swing.JFrame;

public class FileSelector 
	   extends JFrame {

	private static final long serialVersionUID = 4983742565289393874L;

	public FileSelector(String name)
	{
		super(name);
		this.setSize(480, 360);
	}
	
	public FileSelector()
	{
		this("Select File");
	}
	
	public File getFile(File base)
	{
		this.setVisible(true);
		return base;
	}

}
