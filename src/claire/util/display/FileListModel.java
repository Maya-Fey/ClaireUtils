package claire.util.display;

import java.io.File;

import javax.swing.AbstractListModel;

public class FileListModel 
	   extends AbstractListModel<String> {

	private static final long serialVersionUID = 5228257893170437289L;
	
	private File[] files;
	
	public FileListModel() {};
	
	public FileListModel(File[] files)
	{
		this.files = files;
	}
	
	public FileListModel(File base)
	{
		this.files = base.listFiles();
	}
	
	public String getElementAt(int arg0)
	{
		return files[arg0].getName();
	}
	
	public int getSize()
	{
		return files.length;
	}
	
	public void setFiles(File[] files)
	{
		this.files = files;
	}
	
	public void setFiles(File base)
	{
		this.files = base.listFiles();
	}

}
