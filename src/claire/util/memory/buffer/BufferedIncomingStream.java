package claire.util.memory.buffer;

import java.io.IOException;

import claire.util.standards.io.IIncomingStream;

public class BufferedIncomingStream
	   implements IIncomingStream {
	
	private final IIncomingStream is;
	private final byte[] buffer;
	
	private int pos;
	
	public BufferedIncomingStream(IIncomingStream is, byte[] buffer)
	{
		this.is = is;
		this.buffer = buffer;
	}

	public void close() throws IOException
	{
		is.close();
	}

	private final void ensure(int amt) throws IOException
	{
		int len = buffer.length - pos;
		if(len > amt) {
			System.arraycopy(buffer, pos, buffer, 0, len);
			is.readBytes(buffer, len, pos);
			pos = 0;
		}
	}
	
	private final void next() throws IOException
	{
		if(pos == buffer.length)
			is.readBytes(buffer);
	}
	
	public boolean readBool() throws IOException
	{
		next();
		return buffer[pos++] == 1;
	}

	public byte readByte() throws IOException
	{
		next();
		return buffer[pos++];
	}

	public short readShort() throws IOException
	{
		ensure(2);
		return (short) (((buffer[pos++] & 0x00FF)) |
			             (buffer[pos++] & 0x00FF) << 8);
	}

	public char readChar() throws IOException
	{
		ensure(2);
		return (char) (((buffer[pos++] & 0x00FF)) |
			            (buffer[pos++] & 0x00FF) << 8);
	}

	public int readInt() throws IOException
	{
		ensure(4);
		return (int) (((buffer[pos++] & 0x00FF))       |
			          ((buffer[pos++] & 0x00FF) << 8 ) |
			          ((buffer[pos++] & 0x00FF) << 16) |
			          ((buffer[pos++] & 0x00FF) << 24));
	}

	public long readLong() throws IOException
	{
		ensure(8);
		return (long) (((buffer[pos++] & 0x00FF))       |
			           ((buffer[pos++] & 0x00FF) << 8 ) |
			           ((buffer[pos++] & 0x00FF) << 16) |
			           ((buffer[pos++] & 0x00FF) << 24) |
			           ((buffer[pos++] & 0x00FF) << 32) |
			           ((buffer[pos++] & 0x00FF) << 40) |
			           ((buffer[pos++] & 0x00FF) << 48) |
			           ((buffer[pos++] & 0x00FF) << 56));
	}

	public void readBools(boolean[] out, int off, int amt) throws IOException
	{
		// TODO Auto-generated method stub
		
	}

	public void readNibbles(byte[] out, int off, int amt) throws IOException
	{
		// TODO Auto-generated method stub
		
	}

	public void readBytes(byte[] out, int off, int bytes) throws IOException
	{
		// TODO Auto-generated method stub
		
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
		is.rewind(len);
	}

	public void skip(long pos) throws IOException
	{
		is.skip(pos);
	}

	public void seek(long pos) throws IOException
	{
		is.seek(pos);
	}

	public long available() throws IOException
	{
		return is.available();
	}

}
