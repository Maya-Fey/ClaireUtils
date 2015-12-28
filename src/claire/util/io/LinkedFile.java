package claire.util.io;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import claire.util.encoding.CString;
import claire.util.encoding.Decoder;
import claire.util.encoding.TextReader;
import claire.util.memory.array.IndexedMemory;
import claire.util.memory.util.Pair;
import claire.util.standards.IIterable;
import claire.util.standards.IIterator;
import claire.util.standards.io.ITextReader;

public class LinkedFile implements IIterable<CString> {
	
	private final File toParse;	
	private final CString separator;
	private IndexedMemory<CString> registry;
	
	public LinkedFile(File file, CString separator) throws Exception
	{
		this.toParse = file;
		this.separator = separator;
		this.read();
	}
	
	private void read() throws Exception
	{
		FileIncomingStream fin = new FileIncomingStream(toParse);
		ITextReader reader = new TextReader(new Decoder("UTF-8"), fin);
		CString[] lines = reader.readLineCStrings();
		CString[] settings = new CString[lines.length];
		String[] values = new String[lines.length];
		for(int i = 0; i < lines.length; i++)
		{
			CString[] vals = lines[i].split(separator);
			if(vals.length > 2)
				throw new java.io.IOException("Error parsing file: Separator symbols occur more then once on line " + i);
			else if(vals.length < 2)
				throw new java.io.IOException("Error parsing file: No separator symbols occur on line " + i);
			else {
				settings[i] = vals[1];
				values[i] = vals[0].toString();
			}
		}
		this.registry = new IndexedMemory<CString>(settings, values);
		fin.close();
	}
	
	/*private void read() throws IOException
	{
		InputStreamReader reader = new InputStreamReader(new FileInputStream(toParse), "UTF-8");
		BufferedReader buffer = new BufferedReader(reader);
		Stream<String> lines = buffer.lines();
		IndexedMemory<CString> reg = new IndexedMemory<CString>(CString.class, (int) 10);
		Iterator<String> iterator = lines.iterator();
		int i = 0;
		try {
			while(iterator.hasNext()) 
			{
				CString[] index = new CString(iterator.next()).split(separator);
				if(index.length != 2)
					if(index.length > 2)
						throw new java.io.IOException("Error parsing file, separator symbol(s) occured more than once on line " + i);
					else {
						for(CString s : index)
							System.out.println(s);
						throw new java.io.IOException("Error parsing file, no separator symbol detected in file on line " + i);
					}
				reg.allocate(index[1], index[0].toString());
				i++;
			}
		} finally {
			this.registry = reg;
			lines.close();
			buffer.close();
			reader.close();
		}
	}*/
	
	public void write() throws IOException
	{
		toParse.delete();
		toParse.createNewFile();
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(toParse, "UTF-8");
			Iterator<Pair<String, CString>> iterator = registry.pairIterator();
			Pair<String, CString> next;
			while(iterator.hasNext())
			{
				next = iterator.next();
				writer.println(next.getObject() + separator + next.getPair());
			}
		} finally {
			if(writer != null)
				writer.close();
		}
	}
	
	/**
	 * Returns the requested setting. If no such setting can be found, a
	 * nullpointerexception is thrown.
	 * 
	 * @param name Setting ID
	 * @return Setting value
	 */
	public CString getSetting(String name)
	{
		return registry.get(name);
	}
	
	public void edit(String setting, CString value)
	{
		registry.overwrite(setting, value);
	}
	
	public void remove(String setting)
	{
		registry.free(setting);
	}
	
	public void add(String setting, CString value)
	{
		if(new CString(setting).contains(separator))
			throw new java.lang.IllegalArgumentException("Setting name contains separation sequence.\n" + new CString(setting));
		registry.allocate(value, setting);
	}
	
	public Pair<String[], CString[]> getSettings()
	{
		int len = this.registry.reorganize();
		String[] index = new String[len];
		CString[] settings = new CString[len];
		System.arraycopy(registry.index(), 0, index, 0, len);
		System.arraycopy(registry.getArr(), 0, settings, 0, len);
		return new Pair<String[], CString[]>(index, settings);
	}
	
	public Iterator<Pair<String, CString>> pairIterator()
	{
		return registry.pairIterator();
	}
	
	public IIterator<CString> iterator()
	{
		return registry.iterator();
	}
	
	

}
