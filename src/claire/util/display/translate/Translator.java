package claire.util.display.translate;

import java.io.File;

import claire.util.encoding.CString;
import claire.util.io.LinkedFile;
import claire.util.memory.array.DynamicRegistry;
import claire.util.standards.ITranslatable;
import claire.util.standards.ITranslator;

public class Translator
	   implements ITranslator {
	
	private static final CString separator = new CString((char) 0);
	
	private final DynamicRegistry<ITranslatable> children = new DynamicRegistry<ITranslatable>(ITranslatable.class, 8);
	private final File file;
	private LinkedFile list;
	
	public Translator(File dir, String defaultlang) throws Exception
	{
		if(!dir.isDirectory() || !dir.exists())
			throw new java.lang.IllegalArgumentException("File is not a directory or doesn't exist.");
		this.file = dir;
		this.list = getLang(defaultlang);
	}
	
	private LinkedFile getLang(String language) throws Exception
	{
		File file = new File(this.file.getAbsolutePath() + "\\" + language + ".lang");
		if(file.isDirectory() || !file.exists())
			throw new java.lang.NullPointerException("Could not read language file: doesn't exist");
		return new LinkedFile(file, separator);
	}
	
	public void setLang(String language) throws Exception
	{
		this.list = getLang(language);
		for(int i = 0; i < children.length(); i++)
			this.children.get(i).languageChanged();
	}

	public String localize(String ID)
	{
		return list.getSetting(ID).toString();
	}

	public CString localizeC(String ID)
	{
		return list.getSetting(ID);
	}

	public void addChild(ITranslatable t)
	{
		children.add(t);
	}

}
