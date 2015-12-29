package claire.util.io;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;

import claire.util.standards.io.IIncomingStream;

public abstract class Factory<Type> {
	
	private final Class<Type> class_;
	
	protected Factory(Class<Type> class_)
	{
		this.class_ = class_;
	}

	public abstract Type resurrect(byte[] data, int start) throws InstantiationException;
	public abstract Type resurrect(IIncomingStream stream) throws InstantiationException, IOException;
	
	public Type resurrect(byte[] data) throws InstantiationException
	{
		return resurrect(data, 0);
	}
	
	@SuppressWarnings("unchecked")
	public Type[] arrayInstance(int size)
	{	
		return (Type[]) Array.newInstance(class_, size);
	}
	
	public Type resurrect(File file) throws InstantiationException, IOException
	{
		FileIncomingStream is = new FileIncomingStream(file);
		try {
			return this.resurrect(is);
		} finally {
			is.close();
		}
	}
	
}
