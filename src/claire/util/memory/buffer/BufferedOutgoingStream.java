package claire.util.memory.buffer;

import java.io.IOException;

import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class BufferedOutgoingStream 
	   implements IOutgoingStream {

	private final IOutgoingStream os;
	private final byte[] buffer;
	
	private int pos = 0;
	
	public BufferedOutgoingStream(IOutgoingStream os, byte[] buffer)
	{
		this.os = os;
		this.buffer = buffer;
	}
	
	private void doIt(int incoming) throws IOException
	{
		if(pos + incoming > buffer.length) {
			os.writeBytes(buffer, 0, pos);
			pos = 0;
		}
	}
	
	public void flush() throws IOException
	{
		os.writeBytes(buffer, 0, pos);
		pos = 0;
	}
	
	public void close() throws IOException	
	{
		if(pos > 0)
			this.flush();
		os.close();
	}

	public void writeBool(boolean data) throws IOException
	{
		doIt(1);
		buffer[pos] = (byte) (data ? 1 : 0);
	}

	public void writeByte(byte data) throws IOException
	{
		doIt(1);
		buffer[pos] = data;
	}

	public void writeShort(short data) throws IOException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeChar(char data) throws IOException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeInt(int data) throws IOException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeLong(long data) throws IOException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeBools(boolean[] bools, int off, int len)
			throws IOException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeNibbles(byte[] nibbles, int off, int len)
			throws IOException
	{
		// TODO Auto-generated method stub
		
	}

	public void writeBytes(byte[] bytes, int off, int len) throws IOException
	{
		int rem = buffer.length - pos;
		if(rem > len) {
			System.arraycopy(bytes, off, buffer, pos, len);
			pos += len;
		} else {
			System.arraycopy(bytes, off, buffer, pos, rem);
			off += rem;
			len -= rem;
			while(len > buffer.length) {
				System.arraycopy(bytes, off, buffer, 0, buffer.length);
				off += buffer.length;
				len -= buffer.length;
			}
			System.arraycopy(bytes, off, buffer, pos, len);
			pos = len;
		}
	}

	@Override
	public void writeShorts(short[] shorts, int off, int len)
			throws IOException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeChars(char[] charss, int off, int len) throws IOException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeInts(int[] ints, int off, int len) throws IOException
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeLongs(long[] longs, int off, int len) throws IOException
	{
		// TODO Auto-generated method stub
		
	}

	public void rewind(long pos) throws IOException
	{
		this.rewind(pos);
	}

	public void skip(long pos) throws IOException
	{
		this.seek(pos);
	}

	public void seek(long pos) throws IOException
	{
		os.seek(pos);
	}

	public IIncomingStream getIncoming() throws UnsupportedOperationException, IOException
	{
		return os.getIncoming();
	}

}
