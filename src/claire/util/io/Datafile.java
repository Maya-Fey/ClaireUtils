package claire.util.io;

import java.io.File;
import java.io.IOException;

import claire.util.standards.IPersistable;

public class Datafile<Obj extends IPersistable<Obj>> {
	
	private final File indexf;
	private final FileBidirectionalStream data;
	private final PositionIndex index;
	private final Factory<Obj> factory;
	
	private boolean edited = false;
	
	public Datafile(File data, File index, Factory<Obj> factory) throws IOException, InstantiationException
	{
		if(!data.exists())
			data.createNewFile();
		if(!index.exists()) {
			this.index = new PositionIndex();
			index.createNewFile();
		} else
			this.index = PositionIndex.factory.resurrect(index);
		this.indexf = index;
		this.data = new FileBidirectionalStream(data);
		this.factory = factory;	
	}
	
	public Obj resurrect(int pos) throws IOException, InstantiationException
	{
		data.seek(index.getStart(pos));
		return factory.resurrect(data);
	}
	
	public void persist(Obj obj) throws IOException
	{
		data.seek(index.getTotalLength());
		index.add((int) obj.exportSize());
		data.persist(obj);
		if(!edited)
			edited ^= true;
	}
	
	public void deleteFrom(int pos) throws IOException
	{
		index.deleteFrom(pos);
		data.truncate(index.getTotalLength());
		if(!edited)
			edited ^= true;
	}
	
	public int getSize()
	{
		return index.getTotalLength();
	}
	
	public int getLength()
	{
		return index.getSize();
	}
	
	public void save() throws IOException
	{
		if(edited) {
			index.export(indexf);
			edited ^= true;
		}
	}
	
	protected void finalize() throws Throwable
	{
		super.finalize();
		this.save();
	}

}
