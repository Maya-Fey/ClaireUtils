package claire.util.memory.buffer;

import claire.util.memory.Bits;

public class PrimitiveAggregator {
	
	private final byte[] buffer = new byte[8];
	int pos = 0;
	
	public void addByte(byte b)
	{
		buffer[pos++] = b;
	}
	
	public void addBytes(byte[] bytes)
	{
		System.arraycopy(bytes, 0, buffer, pos, bytes.length);
	}
	
	public void addBytes(byte[] bytes, int start, int len)
	{
		System.arraycopy(bytes, start, buffer, pos, len);
	}
	
	public short getShort()
	{
		return Bits.shortFromBytes(buffer, 0);
	}
	
	public char getChar()
	{
		return Bits.charFromBytes(buffer, 0);
	}

	public int getInt()
	{
		return Bits.intFromBytes(buffer, 0);
	}

	public long getLong()
	{
		return Bits.longFromBytes(buffer, 0);
	}

}
