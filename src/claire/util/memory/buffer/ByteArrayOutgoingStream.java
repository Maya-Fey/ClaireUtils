package claire.util.memory.buffer;

import java.io.IOException;

import claire.util.encoding.CString;
import claire.util.memory.Bits;
import claire.util.standards.io.IIncomingStream;
import claire.util.standards.io.IOutgoingStream;

public class ByteArrayOutgoingStream 
	   implements IOutgoingStream {
		   
	private final byte[] array;
	
	private int pos = 0;
	
	public ByteArrayOutgoingStream(byte[] data)
	{
		this.array = data;
	}
	
	public void close() {}
	
	public void writeByte(byte data) throws IOException
	{
		array[pos] = data;
		pos++;
	}

	public void writeShort(short data) throws IOException
	{
		Bits.shortToBytes(data, array, pos);
		pos += 2;
	}
	
	public void writeChar(char data) throws IOException
	{
		Bits.charToBytes(data, array, pos);
		pos += 2;
	}

	public void writeInt(int data) throws IOException
	{
		Bits.intToBytes(data, array, pos);
		pos += 4;
	}

	public void writeLong(long data) throws IOException
	{
		Bits.longToBytes(data, array, pos);
		pos += 8;
	}
	
	public void writeString(CString s) throws IOException
	{
		Bits.charsToBytes(s.toCharArray(), 0, array, pos);
		pos += s.length();
	}
	
	public void writeBytes(byte[] bytes, int off, int len) throws IOException
	{
		System.arraycopy(bytes, off, array, pos, len);
		pos += len;
	}
	
	public void writeShorts(short[] shorts, int off, int len) throws IOException
	{
		Bits.shortsToBytes(shorts, off, array, pos);
		pos += len << 1;
	}
	
	public void writeChars(char[] chars, int off, int len) throws IOException
	{
		Bits.charsToBytes(chars, off, array, pos);
		pos += len << 1;
	}

	public void writeInts(int[] ints, int off, int len) throws IOException
	{
		Bits.intsToBytes(ints, off, array, pos);
		pos += len << 2;
	}

	public void writeLongs(long[] longs, int off, int len) throws IOException
	{
		Bits.longsToBytes(longs, off, array, pos);
		pos += len << 3;		
	}

	public void rewind(long pos) throws IOException
	{
		this.pos -= pos;
	}

	public void skip(long pos) throws IOException
	{
		this.pos -= pos;
	}

	public void seek(long pos) throws IOException
	{
		this.pos = (int) pos;
	}

	public IIncomingStream getIncoming() throws UnsupportedOperationException, IOException
	{
		return new ByteArrayIncomingStream(this.array);
	}

	public void writeBool(boolean data) throws IOException
	{
		array[pos++] = (byte) (data ? 1 : 0);
	}

	public void writeBools(boolean[] bools, int off, int len) throws IOException
	{
		while(len-- > 0) 
			array[pos++] = (byte) (bools[off++] ? 1 : 0);
	}

	public void writeNibbles(byte[] nibbles, int off, int len) throws IOException
	{
		while(len > 1) {
			array[pos++] = (byte) (((nibbles[off++] & 0xF) << 4) |
								    (nibbles[off++] & 0xF));
			len -= 2;
		}
	}

}
