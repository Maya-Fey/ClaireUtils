package claire.util.memory.buffer;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class ByteHolder {

	private byte[] arr;
	private int size;
		
	public ByteHolder(int init)
	{
		this.arr = new byte[init];
		this.size = init;
	}
	
	public void hold(byte[] b)
	{
		this.size = b.length;
		ensureBig();
		System.arraycopy(b, 0, arr, 0, size);
	}
	
	public void hold(byte[] b, int start, int end)
	{
		this.size = end - start;
		ensureBig();
		System.arraycopy(b, start, arr, 0, size);
	}	
	
	public void hold(DataInput stream, int len) throws IOException
	{
		this.size = len;
		ensureBig();
		stream.readFully(arr, 0, len);
	}
	
	public byte[] release()
	{
		byte[] n = new byte[size];
		System.arraycopy(arr, 0, n, 0, size);
		return n;
	}
	
	public byte[] raw()
	{
		return this.arr;
	}
	
	public void release(byte[] b)
	{
		System.arraycopy(arr, 0, b, 0, size);
	}
	
	public void release(byte[] b, int off)
	{
		System.arraycopy(arr, 0, b, off, size);
	}
	
	public void release(DataOutput stream) throws IOException
	{
		stream.write(arr, 0, size);
	}
	
	private void ensureBig()
	{
		if(size > arr.length)
			arr = new byte[size];
	}
	
	public void gc()
	{
		if(size < arr.length)
		{
			byte[] n = new byte[size];
			System.arraycopy(arr, 0, n, 0, size);
			arr = n;
		}
	}
		
}
