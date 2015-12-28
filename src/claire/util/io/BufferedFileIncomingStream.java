package claire.util.io;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import claire.util.memory.Bits;
import claire.util.standards.io.IIncomingStream;

public class BufferedFileIncomingStream implements IIncomingStream {

	private final FileInputStream file;
	
	private final byte[] buffer;
	private int pos = 0, max;
	private final int size;
	
	public BufferedFileIncomingStream(File file) throws IOException
	{
		this.file = new FileInputStream(file);
		this.buffer = new byte[4096];
		this.size = 4096;
		this.next();
	}
	
	public BufferedFileIncomingStream(File file, int size) throws IOException
	{
		this.file = new FileInputStream(file);
		this.buffer = new byte[size];
		this.size = size;
		this.next();
	}
	
	public BufferedFileIncomingStream(File file, byte[] buffer) throws IOException
	{
		this.file = new FileInputStream(file);
		this.buffer = buffer;
		this.size = buffer.length;
		this.next();
	}
	
	public void close() throws IOException
	{
		file.close();
	}

	public byte readByte() throws IOException
	{
		if(pos != max)
			return buffer[pos++];
		else
			next();
		return buffer[pos++];
	}

	public short readShort() throws IOException
	{
		ensureAvailable(2);
		pos += 2;
		return Bits.shortFromBytes(buffer, pos);
	}

	public char readChar() throws IOException
	{
		ensureAvailable(2);
		pos += 2;
		return Bits.charFromBytes(buffer, pos);
	}
	
	public int readInt() throws IOException
	{
		ensureAvailable(4);
		pos += 4;
		return Bits.intFromBytes(buffer, pos);
	}

	public long readLong() throws IOException
	{
		ensureAvailable(8);
		pos += 8;
		return Bits.longFromBytes(buffer, pos);
	}

	public void readBytes(byte[] out, int off, int bytes) throws IOException
	{
		int next;
		while(bytes > 0)
		{
			next = available(bytes);
			System.arraycopy(buffer, pos, out, off, next);
			pos += next;
			bytes -= next;
		}
	}

	public void readShorts(short[] shorts, int off, int amt) throws IOException
	{
		// TODO Auto-generated method stub

	}

	public void readChars(char[] chars, int off, int amt) throws IOException
	{
		// TODO Auto-generated method stub

	}

	public void readInts(int[] ints, int off, int amt) throws IOException
	{
		// TODO Auto-generated method stub

	}

	public void readLongs(long[] longs, int off, int amt) throws IOException
	{
		// TODO Auto-generated method stub

	}

	public void rewind(long len) throws IOException
	{
		file.skip(len ^ 0x8000000000000000L);
	}

	public void skip(long pos) throws IOException
	{
		file.skip(pos);
	}
	
	public void seek(long pos)
	{
		throw new java.lang.UnsupportedOperationException();
	}

	public long available() throws IOException
	{
		return file.available();
	}
	
	private void ensureAvailable(int amt) throws IOException
	{
		if(amt + pos > max)
			next();
	}
	
	private int available(int bytes) throws EOFException
	{
		if(bytes - pos > max)
			return max;
		return bytes;
	}
	
	private void next() throws IOException
	{
		int len = size - pos;
		System.arraycopy(buffer, pos, buffer, 0, len);
		this.max = file.read(buffer, len, size - len);
		if(this.max == 0)
			throw new java.io.EOFException();
		this.pos = 0;
	}

}
