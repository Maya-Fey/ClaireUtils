package claire.util.memory.buffer;

import java.io.IOException;

import claire.util.encoding.CString;
import claire.util.memory.Bits;
import claire.util.standards.io.IIncomingStream;

public class ByteArrayIncomingStream 
	   implements IIncomingStream {

	private final byte[] array;
	
	private int pos = 0;
	
	public ByteArrayIncomingStream(byte[] data)
	{
		this.array = data;
	}
	
	public byte[] getArr()
	{
		return array;
	}
	
	public ByteArrayIncomingStream(byte[] data, int start)
	{
		this.array = data;
		pos = start;
	}
	
	public void close()
	{
		this.pos = array.length;
	}

	public byte readByte()
	{
		return array[pos++];
	}

	public short readShort() 
	{
		try {
			return Bits.shortFromBytes(array, pos);
		} finally {
			pos += 2;
		}
	}

	public char readChar() 
	{
		try {
			return Bits.charFromBytes(array, pos);
		} finally {
			pos += 2;
		}
	}

	public int readInt() 
	{
		try {
			return Bits.intFromBytes(array, pos);
		} finally {
			pos += 4;
		}
	}

	public long readLong() 
	{
		try {
			return Bits.longFromBytes(array, pos);
		} finally {
			pos += 8;
		}
	}

	public CString readText(int length) 
	{
		try {
			return new CString(this.readChars(length));
		} catch (IOException e) { return null; }
	}

	public void readBytes(byte[] out, int off, int bytes) 
	{
		System.arraycopy(array, pos, out, off, bytes);
		pos += bytes;
	}

	public void readShorts(short[] shorts, int off, int amt) 
	{
		Bits.bytesToShorts(array, pos, shorts, off, amt);
		pos += amt << 1;
	}

	public void readChars(char[] chars, int off, int amt) 
	{
		Bits.bytesToChars(array, pos, chars, off, amt);
		pos += amt << 1;
	}

	public void readInts(int[] ints, int off, int amt) 
	{
		Bits.bytesToInts(array, pos, ints, off, amt);
		pos += amt << 2;
	}

	public void readLongs(long[] longs, int off, int amt) 
	{
		Bits.bytesToLongs(array, pos, longs, off, amt);
		pos += amt << 3;
	}

	public void rewind(long len) 
	{
		pos -= len;
	}

	public void skip(long pos) 
	{
		this.pos += pos;
	}
	
	public void seek(long pos) 
	{
		this.pos = (int) pos;
	}
	
	public long available() 
	{
		return array.length - pos;
	}

	public boolean readBool() throws IOException
	{
		return array[pos++] == 1;
	}

	public void readBools(boolean[] out, int off, int amt) throws IOException
	{
		while(amt-- > 0) 
			out[off++] = array[pos++] == 1;
	}

	public void readNibbles(byte[] out, int off, int amt) throws IOException
	{
		while(amt > 1) {
			byte b = array[pos++];
			out[off++] = (byte) ((b & 0xFF) >>> 4);
			out[off++] = (byte)  (b & 0x0F)       ;
			amt -= 2;
		}
	}

}
