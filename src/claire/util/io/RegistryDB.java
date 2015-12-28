package claire.util.io;

import java.io.File;
import java.io.IOException;

import claire.util.standards.IIterable;
import claire.util.standards.IIterator;
import claire.util.standards.IPersistable;

public class RegistryDB<Obj extends IPersistable<Obj>> 
	   implements IIterable<Obj> {
	
	private final int maxsize;
	private final File dbfile;
	private final PositionIndex index;
	private final String name;
	private final Factory<Obj> factory;
	
	private Datafile<Obj> top;
	private Datafile<Obj> cur;
	
	private int toppos;
	private int curpos = -1;
	
	public RegistryDB(String path, String name, int file, Factory<Obj> factory) throws IOException, InstantiationException
	{
		this.factory = factory;
		this.maxsize = file;
		this.name = path + "/" + name;
		this.dbfile = new File(this.name + ".db");
		if(dbfile.exists()) {
			if(dbfile.isFile())
				index = PositionIndex.factory.resurrect(dbfile);
			else
				throw new java.io.IOException("Was expecting file, recieved directory.");
		} else {
			index = new PositionIndex();
			index.add(0);
		}
		toppos = index.getTop();
		top = fileAtIndex(toppos);
	}
	
	public void persist(Obj obj) throws InstantiationException, IOException
	{
		if(obj.exportSize() + top.getSize() > maxsize) 
		{
			top.save();
			top = fileAtIndex(++toppos);
			index.add(0);
		}
		index.increment();
		top.persist(obj);
	}
	
	public Obj resurrect(int pos) throws InstantiationException, IOException
	{
		int file = index.indexFromPos(pos);
		if(file == toppos) {
			return top.resurrect(normalizePos(pos, file));
		} 
		if(file != curpos) {
			curpos = file;
			cur = fileAtIndex(file);
		}
		return cur.resurrect(normalizePos(pos, file));
	}
	
	public void deleteFrom(int pos) throws InstantiationException, IOException
	{
		final int file = index.indexFromPos(pos);
		final Datafile<Obj> df; 
		if(file == toppos) {
			df = top;
		} else {
			for(int i = file + 1; i <= toppos; i++) {
				new File(name + i + ".dat").delete();
				new File(name + i + ".ini").delete();
			}
			toppos = file;
			df = fileAtIndex(toppos);
		}
		df.deleteFrom(normalizePos(pos, file));
		int n = pos - index.getStart(file);
		index.deleteFrom(file);
		index.add(n);
		df.save();
	}

	public void save() throws IOException
	{
		top.save();
		index.export(dbfile);
	}
	
	private final int normalizePos(int pos, int file)
	{
		return pos - index.getStart(file);
	}
	
	private final Datafile<Obj> fileAtIndex(int index) throws InstantiationException, IOException
	{
		return new Datafile<Obj>(new File(this.name + index + ".dat"),
								 new File(this.name + index + ".ini"),
								 factory);
	}
	
	public IIterator<Obj> iterator()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
