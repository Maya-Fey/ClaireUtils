package claire.util.memory.buffer;

import java.lang.reflect.Array;

import claire.util.memory.array.Buffer;
import claire.util.memory.util.ArrayUtil;

@SuppressWarnings("unchecked")
public class IterativeBuffer <T> {
	
	final Class<T> class_;
	final int outputSize;
	
	Buffer<T> buffer;
	
	int pos = 0;
	int len = 0;
	
	public IterativeBuffer(Class<T> class_, int size, int output)
	{
		buffer = new Buffer<T>(class_, size);
		this.outputSize = output;
		this.class_ = class_;
	}
	
	public void addTo(T[] t)
	{
		buffer.add(t);
		len += t.length;
	}
	
	public boolean hasNext()
	{
		if((len - pos) > outputSize)
			return true;
		return false;
	}
	
	public T[] next()
	{
		T[] t = (T[]) Array.newInstance(class_, outputSize);
		pos += outputSize;
		System.arraycopy(buffer.getArray(), pos, t, 0, outputSize);
		return t;
	}
	
	public void next(T[] t)
	{
		pos += outputSize;
		System.arraycopy(buffer.getArray(), pos, t, 0, outputSize);
	}
	
	public T[] remaining()
	{
		T[] t = (T[]) Array.newInstance(class_, len - pos);
		System.arraycopy(buffer.getArray(), pos, t, 0, len - pos);
		pos = len;
		return t;
	}
	
	public void remaining(T[] t)
	{
		System.arraycopy(buffer.getArray(), pos, t, 0, len - pos);
		pos = len;
	}
	
	public void clear()
	{
		buffer.clear();
		this.pos = 0;
		this.len = 0;
	}
	
	public void gc()
	{
		T[] t = (T[]) Array.newInstance(class_, len - pos);
		System.arraycopy(buffer.getArray(), pos, t, 0, len - pos);
		buffer = new Buffer<T>(t);
	}
	
	public static final class Byte 
	{
		final int outputSize;
		
		byte[] buffer;
		int pos = 0;
		int len = 0;
		
		public Byte(int output)
		{
			buffer = new byte[0];
			this.outputSize = output;
		}
		
		public void addTo(byte[] b)
		{
			buffer = ArrayUtil.concat(buffer, b);
			len += b.length;
		}
		
		public boolean hasNext()
		{
			if((len - pos) > outputSize)
				return true;
			return false;
		}
		
		public byte[] next()
		{
			byte[] n = new byte[outputSize];
			System.arraycopy(buffer, pos, n, 0, outputSize);
			pos += outputSize;
			return n;
		}
		
		public void next(byte[] n)
		{
			System.arraycopy(buffer, pos, n, 0, outputSize);
			pos += outputSize;
		}
		
		public byte[] remaining()
		{
			byte[] b = new byte[len - pos];
			remaining(b);
			return b;
		}
		
		public void remaining(byte[] n)
		{		
			System.arraycopy(buffer, pos, n, 0, len - pos);
			pos = len;
		}
		
		public void clear()
		{
			this.buffer = new byte[0];
			this.len = 0;
			this.pos = 0;
		}
		
		public void gc()
		{
			byte[] n = new byte[len - pos];
			System.arraycopy(buffer, pos, n, 0, len - pos);
			this.buffer = n;
		}
		
	}

}
