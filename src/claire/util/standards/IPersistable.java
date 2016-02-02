package claire.util.standards;

import java.io.File;
import java.io.IOException;

import claire.util.io.Factory;
import claire.util.io.FileOutgoingStream;
import claire.util.logging.Log;
import claire.util.memory.buffer.ByteArrayIncomingStream;
import claire.util.memory.buffer.ByteArrayOutgoingStream;
import claire.util.standards.io.IOutgoingStream;

public interface IPersistable<T> {
	
	void export(IOutgoingStream stream) throws IOException;
	void export(byte[] bytes, int offset);
	int exportSize();
	Factory<T> factory();
	
	default byte[] export() 
	{
		byte[] bytes = new byte[(int) this.exportSize()];
		this.export(bytes);
		return bytes;
	}
	
	default void export(byte[] bytes) 
	{
		this.export(bytes, 0);
	}
	
	default void export(File file) throws IOException
	{
		FileOutgoingStream stream = new FileOutgoingStream(file);
		stream.persist(this);
		stream.close();
	}
	
	public static <T extends IPersistable<T> & IUUID<T>> int test(T t)
	{
		try {
			int e = 0;
			Factory<T> f = t.factory();
			byte[] bytes = new byte[t.exportSize() + 20];
			t.export(bytes, 0);
			T t2 = f.resurrect(bytes, 20);
			if(!t.equals(t2)) {
				Log.err.println("Persisting and resurrecting from raw bytes did not yield the same object for class " + t.getClass().getSimpleName());
				e++;
			}
			boolean b = false;
			try {
				t.export(bytes, 21);
			} catch (Exception ex) {
				b = true;
			}
			if(b) {
				Log.err.println("Succeeded in persisting " + t.getClass().getSimpleName() + " with insufficient space, likely misreported required size");
				e++;
			}
			ByteArrayOutgoingStream os = new ByteArrayOutgoingStream(bytes);
			t.export(os);
			T t3 = f.resurrect(new ByteArrayIncomingStream(bytes));
			if(!t.equals(t3)) {
				Log.err.println("Persisting and resurrecting from stream did not yield the same object for class " + t.getClass().getSimpleName());
				e++;
			}
			if(!t2.equals(t3)) {
				Log.err.println("Persisting and resurrecting from stream and raw bytes did not yield the same object for class " + t.getClass().getSimpleName());
				e++;
			}
			return e;
		} catch (Exception e) {
			Log.err.println("An unexpected " + e.getClass().getSimpleName() + ": " + e.getMessage() + " occured while testing the persistability of " + t.getClass().getSimpleName());
			return 1;
		}
	}
	
}
