package claire.util.io;

import java.io.IOException;
import java.util.Iterator;

import claire.util.standards.ICache;
import claire.util.standards.IPersistable;

public class CachedDB<T extends IPersistable<T>> {
	
	private final RegistryDB<T> db;
	private final ICache<T> cache;

	public CachedDB(RegistryDB<T> db, ICache<T> cache)
	{
		this.db = db;
		this.cache = cache;
	}
	
	public void persist(T obj) throws IOException, Exception
	{
		db.persist(obj);
	}
	
	public T get(int ID) throws IOException, Exception
	{
		T ret = cache.get(ID);
		if(ret != null)
			return ret;
		else
			ret = db.resurrect(ID);
		cache.add(ret, ID);
		return ret;
	}
	
	public void deleteFrom(int ID) throws IOException, Exception
	{
		db.deleteFrom(ID);
	}
	
	public void save() throws IOException
	{
		db.save();
	}
	
	public Iterator<T> iterator()
	{
		return db.iterator();
	}
	
}
