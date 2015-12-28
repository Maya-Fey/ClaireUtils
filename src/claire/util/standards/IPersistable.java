package claire.util.standards;

import java.io.File;
import java.io.IOException;

import claire.util.io.Factory;
import claire.util.io.FileOutgoingStream;
import claire.util.standards.io.IOutgoingStream;

public interface IPersistable<T> {
	
	void export(IOutgoingStream stream) throws IOException;
	void export(byte[] bytes, int offset);
	int exportSize();
	Factory<T> factory();
	
	default byte[] export() throws IOException
	{
		byte[] bytes = new byte[(int) this.exportSize()];
		this.export(bytes);
		return bytes;
	}
	
	default void export(byte[] bytes) throws IOException
	{
		this.export(bytes, 0);
	}
	
	default void export(File file) throws IOException
	{
		FileOutgoingStream stream = new FileOutgoingStream(file);
		stream.persist(this);
		stream.close();
	}
	
}
